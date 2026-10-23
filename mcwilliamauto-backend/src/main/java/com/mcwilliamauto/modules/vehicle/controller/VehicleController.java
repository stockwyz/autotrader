package com.mcwilliamauto.modules.vehicle.controller;

import com.mcwilliamauto.common.Result;
import com.mcwilliamauto.modules.vehicle.dto.VehicleQueryDTO;
import com.mcwilliamauto.modules.vehicle.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/** 前台公开接口：车辆列表、详情 */
@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @GetMapping
    public Result<?> list(VehicleQueryDTO dto) {
        return Result.success(vehicleService.pageList(dto));
    }

    @GetMapping("/{id}")
    public Result<?> detail(@PathVariable Long id) {
        return Result.success(vehicleService.getDetail(id));
    }
}
