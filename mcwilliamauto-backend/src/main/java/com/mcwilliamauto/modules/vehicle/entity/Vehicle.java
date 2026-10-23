package com.mcwilliamauto.modules.vehicle.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("vehicle")
public class Vehicle {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String stockNo;
    private String vin;
    private String title;
    private String slug;

    private Long makeId;
    private Long modelId;
    private Integer year;
    private Long bodyStyleId;
    private Long transmissionId;
    private Long engineId;
    private String engineSize;
    private Long fuelTypeId;
    private String driveline;
    private Long exteriorColorId;
    private Long interiorColorId;
    private Integer doors;
    private Integer passengers;

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
    private Integer status;

    /** CARFAX报告PDF相对路径，如 "{stockNo}/carfax.pdf"，跟车辆图片/视频存储在同一个以stockNo命名的目录下 */
    private String carfaxPdfUrl;
    private String videoUrl;
    private String dealerPermitNo;

    private Integer viewCount;
    private Integer sortNo;

    private Long createdBy;
    private Long updatedBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    private LocalDateTime soldAt;

    @TableLogic
    private Integer deleted;
}
