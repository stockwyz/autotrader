package com.mcwilliamauto.modules.vehicle.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class VehicleListVO {
    private Long id;
    private String title;
    private String slug;
    private String stockNo;
    private String vin;
    private Integer year;
    private String makeName;
    private String modelName;
    private String bodyStyleName;
    private String transmissionName;
    private String engineName;
    private String fuelTypeName;
    private String driveline;
    private String exteriorColorName;
    private String interiorColorName;
    private Integer doors;
    private Integer odometerKm;
    private BigDecimal price;
    private BigDecimal biweeklyPayment;
    private Integer loanTermMonths;
    private String mainImageUrl;
    private String mainThumbUrl;
    private Boolean isOneOwner;
    private Boolean isLowKm;
    private Boolean isFeatured;
    private Boolean isSold;
}
