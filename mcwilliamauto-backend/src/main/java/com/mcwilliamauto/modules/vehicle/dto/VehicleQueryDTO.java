package com.mcwilliamauto.modules.vehicle.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class VehicleQueryDTO {
    private Long makeId;
    private Long modelId;
    private Integer minYear;
    private Integer maxYear;
    private Long transmissionId;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Integer minKm;
    private Integer maxKm;
    private Long bodyStyleId;
    private Long exteriorColorId;
    private Long engineId;
    private Long fuelTypeId;
    private Boolean sold;
    private Boolean featured;
    private String keyword;

    private String sortBy = "createdAt"; // make/model/year/price/mileage/bodyStyle
    private String sortOrder = "desc";   // asc/desc

    private Long pageNum = 1L;
    private Long pageSize = 12L;
}
