package com.mcwilliamauto.modules.appointment.service;

import com.mcwilliamauto.common.PageResult;
import com.mcwilliamauto.modules.appointment.dto.AppointmentDTO;

import java.time.LocalDate;
import java.util.Map;

public interface AppointmentService {
    /** 某一天的可约时间段(按营业时间生成，已满/已过的标记为不可用) */
    Map<String, Object> slots(LocalDate date);

    /** 前台提交预约，服务端会再次校验日期、营业时间、时间段是否仍可用 */
    /** @return 新生成的预约单号 */
    String submit(AppointmentDTO dto);

    /** 邮件里的确认/取消按钮点开的页面用：按令牌取预约摘要 */
    Map<String, Object> getByToken(String token);

    /** 客户在邮件页面上点了确认/取消(action=confirm|cancel)，只记录客户的意愿并通知店里，不改 status */
    Map<String, Object> respond(String token, String action);

    PageResult<?> page(Long pageNum, Long pageSize, Integer status, LocalDate date);

    void updateStatus(Long id, Integer status);

    void delete(Long id);
}
