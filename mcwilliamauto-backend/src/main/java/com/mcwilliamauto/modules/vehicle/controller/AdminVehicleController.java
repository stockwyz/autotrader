package com.mcwilliamauto.modules.vehicle.controller;

import com.mcwilliamauto.common.Result;
import com.mcwilliamauto.modules.vehicle.dto.VehicleQueryDTO;
import com.mcwilliamauto.modules.vehicle.dto.VehicleSaveDTO;
import com.mcwilliamauto.modules.vehicle.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/** 后台管理接口：需要登录(JWT) */
@RestController
@RequestMapping("/api/admin/vehicles")
@RequiredArgsConstructor
public class AdminVehicleController {

    private final VehicleService vehicleService;

    @GetMapping
    public Result<?> list(VehicleQueryDTO dto) {
        return Result.success(vehicleService.pageList(dto));
    }

    @GetMapping("/{id}")
    public Result<?> detail(@PathVariable Long id) {
        return Result.success(vehicleService.getDetail(id));
    }

    @PostMapping
    public Result<?> create(@Valid @RequestBody VehicleSaveDTO dto) {
        return Result.success(vehicleService.saveVehicle(dto));
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @Valid @RequestBody VehicleSaveDTO dto) {
        dto.setId(id);
        return Result.success(vehicleService.saveVehicle(dto));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return Result.success();
    }

    /** body: {"field":"sold|featured|status","value":true} */
    @PatchMapping("/{id}/status")
    public Result<?> updateStatus(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        String field = (String) body.get("field");
        Boolean value = (Boolean) body.get("value");
        vehicleService.updateStatus(id, field, value);
        return Result.success();
    }

    /** 删除某台车下的某一张图片(物理文件+数据库记录) */
    @DeleteMapping("/{vehicleId}/images/{imageId}")
    public Result<?> deleteImage(@PathVariable Long vehicleId, @PathVariable Long imageId) {
        vehicleService.deleteImage(imageId);
        return Result.success();
    }

    /** 保存已上传图片的排序（拖拽调整后调用），排在第一位的自动成为封面图 */
    @PutMapping("/{vehicleId}/images/reorder")
    public Result<?> reorderImages(@PathVariable Long vehicleId, @RequestBody List<Long> imageIds) {
        vehicleService.reorderImages(vehicleId, imageIds);
        return Result.success();
    }

    /** 删除车辆视频(物理文件+清空videoUrl字段) */
    @DeleteMapping("/{vehicleId}/video")
    public Result<?> deleteVideo(@PathVariable Long vehicleId) {
        vehicleService.deleteVideo(vehicleId);
        return Result.success();
    }

    /** 删除车辆CARFAX报告PDF(物理文件+清空carfaxPdfUrl字段) */
    @DeleteMapping("/{vehicleId}/carfax")
    public Result<?> deleteCarfax(@PathVariable Long vehicleId) {
        vehicleService.deleteCarfax(vehicleId);
        return Result.success();
    }
}
