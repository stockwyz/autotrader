package com.mcwilliamauto.modules.vehicle.dto;

import com.mcwilliamauto.modules.vehicle.entity.VehicleFeature;
import com.mcwilliamauto.modules.vehicle.entity.VehicleImage;
import com.mcwilliamauto.modules.vehicle.entity.VehicleOption;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class VehicleDetailVO {
    private Long id;
    private String stockNo;
    private String vin;
    private String title;
    private String slug;

    private String makeName;
    private String modelName;
    private Integer year;
    private String bodyStyleName;
    private String transmissionName;
    private String engineName;
    private String engineSize;
    private String fuelTypeName;
    private String driveline;
    private String exteriorColorName;
    private String interiorColorName;
    private Integer doors;
    private Integer passengers;

    /** 以下是原始外键ID，前台详情页用不上，专门给后台编辑表单的下拉框回显用 */
    private Long makeId;
    private Long modelId;
    private Long bodyStyleId;
    private Long transmissionId;
    private Long engineId;
    private Long fuelTypeId;
    private Long exteriorColorId;
    private Long interiorColorId;
    private Integer status;

    private Integer odometerKm;
    private String cityFuelConsumption;
    private String hwyFuelConsumption;

    private BigDecimal price;
    private BigDecimal biweeklyPayment;
    private Integer loanTermMonths;

    private String description;

    private Boolean isOneOwner;
    private Boolean isLowKm;
    private Boolean isFeatured;
    private Boolean isSold;

    private String carfaxPdfUrl;
    private String dealerPermitNo;
    private String videoUrl;

    private List<VehicleImage> images;
    private List<VehicleFeature> features;
    private List<VehicleOption> options;

    private Long prevVehicleId;
    private Long nextVehicleId;
}
