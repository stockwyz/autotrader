package com.mcwilliamauto.modules.appointment.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mcwilliamauto.common.BusinessException;
import com.mcwilliamauto.common.PageResult;
import com.mcwilliamauto.modules.appointment.dto.AppointmentDTO;
import com.mcwilliamauto.modules.appointment.entity.ServiceAppointment;
import com.mcwilliamauto.modules.appointment.mail.AppointmentMailService;
import com.mcwilliamauto.modules.appointment.mapper.ServiceAppointmentMapper;
import com.mcwilliamauto.modules.dealer.entity.DealerBusinessHours;
import com.mcwilliamauto.modules.dealer.service.DealerBusinessHoursService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private static final DateTimeFormatter HHMM = DateTimeFormatter.ofPattern("HH:mm");

    private final ServiceAppointmentMapper appointmentMapper;
    private final DealerBusinessHoursService businessHoursService;
    private final AppointmentMailService mailService;

    /** 每个时间段的时长(分钟) */
    @Value("${app.appointment.slot-minutes:30}")
    private int slotMinutes;

    /** 同一个时间段最多可接几个预约(几个工位/技师) */
    @Value("${app.appointment.capacity:1}")
    private int capacity;

    /** 今天的预约至少要提前多少分钟 */
    @Value("${app.appointment.min-lead-minutes:60}")
    private int minLeadMinutes;

    /** 最多可提前预约多少天 */
    @Value("${app.appointment.max-advance-days:60}")
    private int maxAdvanceDays;

    @Override
    public Map<String, Object> slots(LocalDate date) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("date", date.toString());

        DealerBusinessHours hours = hoursOf(date);
        boolean open = hours != null && dayBookable(date);
        result.put("open", open);
        if (hours != null) {
            result.put("openTime", hours.getOpenTime() == null ? null : hours.getOpenTime().format(HHMM));
            result.put("closeTime", hours.getCloseTime() == null ? null : hours.getCloseTime().format(HHMM));
        }
        List<Map<String, Object>> list = new ArrayList<>();
        if (open) {
            Map<LocalTime, Long> booked = bookedCount(date);
            LocalDateTime earliest = LocalDateTime.now().plusMinutes(minLeadMinutes);
            for (LocalTime t : generateSlots(hours)) {
                boolean full = booked.getOrDefault(t, 0L) >= capacity;
                boolean past = LocalDateTime.of(date, t).isBefore(earliest);
                Map<String, Object> s = new LinkedHashMap<>();
                s.put("time", t.format(HHMM));
                s.put("available", !full && !past);
                list.add(s);
            }
        }
        result.put("slots", list);
        return result;
    }

    @Override
    public synchronized String submit(AppointmentDTO dto) {
        LocalDate date = dto.getApptDate();
        if (!dayBookable(date)) {
            throw new BusinessException("This date is not available for booking");
        }
        DealerBusinessHours hours = hoursOf(date);
        if (hours == null) {
            throw new BusinessException("We are closed on the selected day");
        }
        LocalTime time = dto.getApptTime().withSecond(0).withNano(0);
        if (!generateSlots(hours).contains(time)) {
            throw new BusinessException("The selected time is outside our business hours");
        }
        if (LocalDateTime.of(date, time).isBefore(LocalDateTime.now().plusMinutes(minLeadMinutes))) {
            throw new BusinessException("The selected time is no longer available");
        }
        if (bookedCount(date).getOrDefault(time, 0L) >= capacity) {
            throw new BusinessException("This time slot has just been taken, please choose another one");
        }

        ServiceAppointment entity = new ServiceAppointment();
        entity.setServiceType(dto.getServiceType());
        entity.setApptDate(date);
        entity.setApptTime(time);
        entity.setDetail(dto.getDetail());
        entity.setCustomerName(dto.getCustomerName().trim());
        entity.setPhone(dto.getPhone().trim());
        entity.setEmail(StringUtils.hasText(dto.getEmail()) ? dto.getEmail().trim() : null);
        entity.setVehicleInfo(dto.getVehicleInfo());
        entity.setStatus(0);
        entity.setCustomerResponse(0);
        entity.setApptNo(generateApptNo());
        entity.setConfirmToken(UUID.randomUUID().toString().replace("-", ""));
        appointmentMapper.insert(entity);

        // 异步给客户发"请确认"邮件，发送失败不影响预约本身
        mailService.sendCustomerReceived(entity);
        return entity.getApptNo();
    }

    @Override
    public Map<String, Object> getByToken(String token) {
        ServiceAppointment a = findByToken(token);
        return summary(a);
    }

    @Override
    public synchronized Map<String, Object> respond(String token, String action) {
        ServiceAppointment a = findByToken(token);
        int resp;
        if ("confirm".equalsIgnoreCase(action)) resp = 1;
        else if ("cancel".equalsIgnoreCase(action)) resp = 2;
        else throw new BusinessException("Invalid action");

        // 管理员已经人工处理完(完成/取消)的预约，客户不能再改
        if (a.getStatus() != null && (a.getStatus() == 2 || a.getStatus() == 3)) {
            throw new BusinessException("This appointment has already been " + (a.getStatus() == 2 ? "completed" : "cancelled") + ".");
        }
        if (a.getApptDate() != null && a.getApptDate().isBefore(LocalDate.now())) {
            throw new BusinessException("This appointment date has already passed.");
        }
        // 重复点同一个按钮：直接返回当前状态，不重复发邮件
        if (a.getCustomerResponse() != null && a.getCustomerResponse() == resp) {
            return summary(a);
        }
        ServiceAppointment upd = new ServiceAppointment();
        upd.setId(a.getId());
        upd.setCustomerResponse(resp);
        upd.setRespondedAt(LocalDateTime.now());
        appointmentMapper.updateById(upd);
        a.setCustomerResponse(resp);

        // 通知店里；只记录客户意愿，status 仍由管理员在后台人工确认后才变
        mailService.sendDealerResponse(a, resp == 1);
        return summary(a);
    }

    /** 预约单号字符集：去掉容易混淆的 0/O、1/I/L */
    private static final String NO_CHARS = "ABCDEFGHJKMNPQRSTUVWXYZ23456789";
    private static final java.security.SecureRandom RANDOM = new java.security.SecureRandom();

    /** 形如 AP-20261013-7K3Q：AP-提交日期-4位随机码；重复则重新生成 */
    private String generateApptNo() {
        String day = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        for (int i = 0; i < 10; i++) {
            StringBuilder sb = new StringBuilder("AP-").append(day).append('-');
            for (int j = 0; j < 4; j++) sb.append(NO_CHARS.charAt(RANDOM.nextInt(NO_CHARS.length())));
            String no = sb.toString();
            Long exists = appointmentMapper.selectCount(Wrappers.<ServiceAppointment>lambdaQuery().eq(ServiceAppointment::getApptNo, no));
            if (exists == null || exists == 0) return no;
        }
        throw new BusinessException("Could not generate an appointment number, please try again");
    }

    private ServiceAppointment findByToken(String token) {
        if (!StringUtils.hasText(token) || token.length() > 64) throw new BusinessException("Invalid or expired link");
        ServiceAppointment a = appointmentMapper.selectOne(Wrappers.<ServiceAppointment>lambdaQuery()
                .eq(ServiceAppointment::getConfirmToken, token).last("limit 1"));
        if (a == null) throw new BusinessException("Invalid or expired link");
        return a;
    }

    /** 给公开页面返回的摘要：只含展示需要的字段，不带手机/邮箱/令牌 */
    private Map<String, Object> summary(ServiceAppointment a) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("apptNo", a.getApptNo());
        m.put("serviceType", a.getServiceType());
        m.put("apptDate", a.getApptDate() == null ? null : a.getApptDate().toString());
        m.put("apptTime", a.getApptTime() == null ? null : a.getApptTime().format(HHMM));
        m.put("customerName", a.getCustomerName());
        m.put("detail", a.getDetail());
        m.put("status", a.getStatus());
        m.put("customerResponse", a.getCustomerResponse());
        return m;
    }

    @Override
    public PageResult<?> page(Long pageNum, Long pageSize, Integer status, LocalDate date) {
        Page<ServiceAppointment> page = appointmentMapper.selectPage(new Page<>(pageNum, pageSize),
                Wrappers.<ServiceAppointment>lambdaQuery()
                        .eq(status != null, ServiceAppointment::getStatus, status)
                        .eq(date != null, ServiceAppointment::getApptDate, date)
                        .orderByDesc(ServiceAppointment::getApptDate)
                        .orderByDesc(ServiceAppointment::getApptTime));
        return new PageResult<>(page.getTotal(), page.getCurrent(), page.getSize(), page.getRecords());
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        if (status == null || status < 0 || status > 3) {
            throw new BusinessException("Invalid status");
        }
        ServiceAppointment old = appointmentMapper.selectById(id);
        if (old == null) throw new BusinessException("Appointment not found");
        if (old.getStatus() != null && old.getStatus().equals(status)) return; // 没变化，不重复发信

        ServiceAppointment upd = new ServiceAppointment();
        upd.setId(id);
        upd.setStatus(status);
        appointmentMapper.updateById(upd);

        // 管理员人工确认后，才给客户发 确认/完成/取消 邮件
        old.setStatus(status);
        mailService.sendCustomerStatus(old, status);
    }

    @Override
    public void delete(Long id) {
        appointmentMapper.deleteById(id);
    }

    // ---------------------------------------------------------------

    /** 今天起、未来 maxAdvanceDays 天内才允许预约 */
    private boolean dayBookable(LocalDate date) {
        LocalDate today = LocalDate.now();
        return !date.isBefore(today) && !date.isAfter(today.plusDays(maxAdvanceDays));
    }

    /** 当天营业时间；没配置、或开关门时间为空(休息日)都返回 null */
    private DealerBusinessHours hoursOf(LocalDate date) {
        int dow = date.getDayOfWeek().getValue(); // 1=周一 ... 7=周日，与 dealer_business_hours 一致
        DealerBusinessHours h = businessHoursService.getOne(Wrappers.<DealerBusinessHours>lambdaQuery()
                .eq(DealerBusinessHours::getDayOfWeek, dow).last("limit 1"));
        if (h == null || h.getOpenTime() == null || h.getCloseTime() == null) return null;
        if (!h.getCloseTime().isAfter(h.getOpenTime())) return null;
        return h;
    }

    /** 从开门起每隔 slotMinutes 一个时间段，最后一个时间段要在关门前结束 */
    private List<LocalTime> generateSlots(DealerBusinessHours h) {
        List<LocalTime> list = new ArrayList<>();
        LocalTime t = h.getOpenTime().withSecond(0).withNano(0);
        LocalTime close = h.getCloseTime();
        while (!t.plusMinutes(slotMinutes).isAfter(close) && t.plusMinutes(slotMinutes).isAfter(t)) {
            list.add(t);
            t = t.plusMinutes(slotMinutes);
        }
        return list;
    }

    /** 某天各时间段已占用的预约数(已取消的不占用) */
    private Map<LocalTime, Long> bookedCount(LocalDate date) {
        return appointmentMapper.selectList(Wrappers.<ServiceAppointment>lambdaQuery()
                        .eq(ServiceAppointment::getApptDate, date)
                        .ne(ServiceAppointment::getStatus, 3))
                .stream()
                .collect(Collectors.groupingBy(a -> a.getApptTime().withSecond(0).withNano(0), Collectors.counting()));
    }
}
