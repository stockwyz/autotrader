package com.mcwilliamauto.modules.appointment.controller;

import com.mcwilliamauto.common.Result;
import com.mcwilliamauto.modules.appointment.dto.AppointmentDTO;
import com.mcwilliamauto.modules.appointment.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/** 前台预约接口：查询某天可约时间段、提交预约，公开无需登录 */
@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping("/slots")
    public Result<?> slots(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return Result.success(appointmentService.slots(date));
    }

    /** 邮件里按钮点开的页面：取预约摘要 */
    @GetMapping("/response/{token}")
    public Result<?> responseInfo(@PathVariable String token) {
        return Result.success(appointmentService.getByToken(token));
    }

    /** 客户在页面上点"确认/取消"后提交 (action=confirm|cancel)。必须是 POST，防止邮箱安全扫描器预取链接时误触发 */
    @PostMapping("/response/{token}")
    public Result<?> respond(@PathVariable String token, @RequestParam String action) {
        return Result.success(appointmentService.respond(token, action));
    }

    @PostMapping
    public Result<?> submit(@Valid @RequestBody AppointmentDTO dto) {
        String apptNo = appointmentService.submit(dto);
        return Result.success(java.util.Map.of("apptNo", apptNo));
    }
}
