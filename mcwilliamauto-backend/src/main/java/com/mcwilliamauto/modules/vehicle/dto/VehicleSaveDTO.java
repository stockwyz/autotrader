package com.mcwilliamauto.modules.vehicle.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class VehicleSaveDTO {
    private Long id; // 编辑时传入

    /** 新增时由服务端自动生成(年份+5位顺序码)，不接受前端传值；编辑时保持原值不变，故不做必填校验 */
    private String stockNo;
    @NotBlank(message = "VIN is required")
    @Size(max = 30, message = "VIN must be 30 characters or fewer")
    private String vin;
    @NotBlank(message = "Title is required")
    @Size(max = 150, message = "Title must be 150 characters or fewer")
    private String title;
    private String slug;

    @NotNull(message = "Make is required")
    private Long makeId;
    @NotNull(message = "Model is required")
    private Long modelId;
    @NotNull(message = "Year is required")
    private Integer year;
    private Long bodyStyleId;
    private Long transmissionId;
    private Long engineId;
    @Size(max = 20, message = "Engine size must be 20 characters or fewer")
    private String engineSize;
    private Long fuelTypeId;
    @Size(max = 20, message = "Driveline must be 20 characters or fewer")
    private String driveline;
    private Long exteriorColorId;
    private Long interiorColorId;
    private Integer doors;
    private Integer passengers;

    private Integer odometerKm;
    @Size(max = 20, message = "City fuel consumption must be 20 characters or fewer")
    private String cityFuelConsumption;
    @Size(max = 20, message = "Hwy fuel consumption must be 20 characters or fewer")
    private String hwyFuelConsumption;

    @NotNull(message = "Price is required")
    private BigDecimal price;
    private Integer loanTermMonths;

    private String description;

    private Boolean isOneOwner;
    private Boolean isLowKm;
    private Boolean isFeatured;
    private Boolean isSold;
    private Integer status;

    @Size(max = 30, message = "Dealer permit number must be 30 characters or fewer")
    private String dealerPermitNo;
    private String videoUrl;
    private String carfaxPdfUrl;

    /** 分类配置列表 */
    private List<FeatureItem> features;
    /** 出厂选装包列表 */
    private List<OptionItem> options;

    @Data
    public static class FeatureItem {
        private String category;
        private String content;
    }

    @Data
    public static class OptionItem {
        private String optionGroup;
        private String content;
    }
}
