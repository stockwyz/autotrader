package com.mcwilliamauto.modules.appointment.controller;

import com.mcwilliamauto.common.Result;
import com.mcwilliamauto.modules.appointment.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/** 后台预约管理，需登录 */
@RestController
@RequestMapping("/api/admin/appointments")
@RequiredArgsConstructor
public class AdminAppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping
    public Result<?> page(@RequestParam(defaultValue = "1") Long pageNum,
                          @RequestParam(defaultValue = "10") Long pageSize,
                          @RequestParam(required = false) Integer status,
                          @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return Result.success(appointmentService.page(pageNum, pageSize, status, date));
    }

    @PatchMapping("/{id}/status")
    public Result<?> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        appointmentService.updateStatus(id, status);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        appointmentService.delete(id);
        return Result.success();
    }
}
