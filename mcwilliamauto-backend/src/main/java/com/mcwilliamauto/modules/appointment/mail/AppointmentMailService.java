package com.mcwilliamauto.modules.appointment.mail;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mcwilliamauto.modules.appointment.entity.ServiceAppointment;
import com.mcwilliamauto.modules.appointment.mail.AppointmentEmailTemplates.Appt;
import com.mcwilliamauto.modules.appointment.mail.AppointmentEmailTemplates.Dealer;
import com.mcwilliamauto.modules.appointment.mail.AppointmentEmailTemplates.Mail;
import com.mcwilliamauto.modules.dealer.entity.DealerInfo;
import com.mcwilliamauto.modules.dealer.service.DealerInfoService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

/**
 * 预约相关邮件的发送。所有发送都在后台线程里异步执行，失败只记日志，
 * 绝不影响预约提交 / 后台改状态本身。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentMailService {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("EEEE, MMM d, yyyy", Locale.ENGLISH);
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);

    private final ObjectProvider<JavaMailSender> mailSenderProvider;
    private final DealerInfoService dealerInfoService;

    @Value("${app.mail.enabled:false}")
    private boolean enabled;

    @Value("${app.mail.from:}")
    private String from;

    @Value("${spring.mail.username:}")
    private String smtpUser;

    @Value("${app.site-url:http://localhost:5173}")
    private String siteUrl;

    /** 客户提交预约后：给客户发"请确认"邮件(带确认/取消按钮) */
    public void sendCustomerReceived(ServiceAppointment a) {
        if (!StringUtils.hasText(a.getEmail())) return;
        DealerInfo info = dealer();
        String base = site();
        String confirmUrl = base + "/appointment/response/" + a.getConfirmToken() + "?action=confirm";
        String cancelUrl = base + "/appointment/response/" + a.getConfirmToken() + "?action=cancel";
        Mail m = AppointmentEmailTemplates.customerReceived(toDealer(info), toAppt(a), confirmUrl, cancelUrl);
        sendAsync(a.getEmail(), m, "customer-received #" + a.getId());
    }

    /** 客户在邮件里点了确认/取消：通知店里(dealer_info.email) */
    public void sendDealerResponse(ServiceAppointment a, boolean confirmed) {
        DealerInfo info = dealer();
        if (info == null || !StringUtils.hasText(info.getEmail())) {
            log.warn("[mail] dealer_info.email is empty, skip dealer notice for appointment #{}", a.getId());
            return;
        }
        Mail m = AppointmentEmailTemplates.dealerResponse(toDealer(info), toAppt(a), confirmed, site() + "/admin/appointments");
        sendAsync(info.getEmail(), m, "dealer-response #" + a.getId());
    }

    /** 管理员人工确认改状态后：通知客户 (status 1=确认 2=完成 3=取消，其他状态不发信) */
    public void sendCustomerStatus(ServiceAppointment a, int status) {
        if (status < 1 || status > 3 || !StringUtils.hasText(a.getEmail())) return;
        Mail m = AppointmentEmailTemplates.customerStatus(toDealer(dealer()), toAppt(a), status, site() + "/appointment");
        sendAsync(a.getEmail(), m, "customer-status-" + status + " #" + a.getId());
    }

    // ---------------------------------------------------------------

    private void sendAsync(String to, Mail mail, String tag) {
        if (!enabled) {
            log.info("[mail] disabled (app.mail.enabled=false). Would send '{}' to {} [{}]", mail.subject(), to, tag);
            return;
        }
        JavaMailSender sender = mailSenderProvider.getIfAvailable();
        if (sender == null) {
            log.warn("[mail] JavaMailSender is not configured (spring.mail.host), skip [{}]", tag);
            return;
        }
        CompletableFuture.runAsync(() -> {
            try {
                MimeMessage msg = sender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(msg, false, "UTF-8");
                String sender0 = StringUtils.hasText(from) ? from : smtpUser;
                if (StringUtils.hasText(sender0)) helper.setFrom(sender0);
                helper.setTo(to);
                helper.setSubject(mail.subject());
                helper.setText(mail.html(), true);
                sender.send(msg);
                log.info("[mail] sent '{}' to {} [{}]", mail.subject(), to, tag);
            } catch (Exception e) {
                log.error("[mail] failed to send [{}] to {}: {}", tag, to, e.getMessage(), e);
            }
        });
    }

    private DealerInfo dealer() {
        return dealerInfoService.list(Wrappers.<DealerInfo>lambdaQuery().last("limit 1"))
                .stream().findFirst().orElse(null);
    }

    private String site() {
        return siteUrl.endsWith("/") ? siteUrl.substring(0, siteUrl.length() - 1) : siteUrl;
    }

    private Dealer toDealer(DealerInfo i) {
        String name = i != null && StringUtils.hasText(i.getName()) ? i.getName() : "Our Dealership";
        String code = i != null ? i.getThemeCode() : null;
        // 与前台 Website Style 的 A~H 主色保持一致
        String color = switch (code == null ? "A" : code) {
            case "B" -> "#2F6B4F";
            case "C" -> "#1F7A8C";
            case "D" -> "#9A3B3B";
            case "E" -> "#4A57A8";
            case "F" -> "#8A6420";
            case "G" -> "#6E4B8E";
            case "H" -> "#475569";
            default -> "#2D6CB0";
        };
        return new Dealer(name, i == null ? null : i.getAddress(), i == null ? null : i.getPhone(), color);
    }

    private Appt toAppt(ServiceAppointment a) {
        LocalDate d = a.getApptDate();
        LocalTime t = a.getApptTime();
        return new Appt(a.getApptNo(), typeName(a.getServiceType()),
                d == null ? "" : d.format(DATE_FMT),
                t == null ? "" : t.format(TIME_FMT),
                a.getDetail(), a.getCustomerName(), a.getPhone(), a.getEmail(), a.getVehicleInfo());
    }

    public static String typeName(String type) {
        if (type == null) return "";
        return switch (type) {
            case "REPAIR" -> "Repair";
            case "MAINTENANCE" -> "Maintenance";
            case "PHR" -> "PHR";
            default -> type;
        };
    }
}
