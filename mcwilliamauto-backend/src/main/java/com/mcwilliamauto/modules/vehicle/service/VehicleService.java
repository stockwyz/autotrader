package com.mcwilliamauto.modules.vehicle.service;

import com.mcwilliamauto.common.PageResult;
import com.mcwilliamauto.modules.vehicle.dto.VehicleDetailVO;
import com.mcwilliamauto.modules.vehicle.dto.VehicleListVO;
import com.mcwilliamauto.modules.vehicle.dto.VehicleQueryDTO;
import com.mcwilliamauto.modules.vehicle.dto.VehicleSaveDTO;
import com.mcwilliamauto.modules.vehicle.entity.VehicleImage;

public interface VehicleService {
    PageResult<VehicleListVO> pageList(VehicleQueryDTO dto);
    VehicleDetailVO getDetail(Long id);
    Long saveVehicle(VehicleSaveDTO dto);
    void deleteVehicle(Long id);
    void updateStatus(Long id, String field, Boolean value);
    java.util.List<VehicleImage> attachImages(Long vehicleId, java.util.List<String[]> urlThumbPairs);
    String getStockNo(Long vehicleId);
    void deleteImage(Long imageId);
    void reorderImages(Long vehicleId, java.util.List<Long> orderedImageIds);
    void deleteVideo(Long vehicleId);
    void deleteCarfax(Long vehicleId);
    void deleteOldVideoFile(Long vehicleId);
}
