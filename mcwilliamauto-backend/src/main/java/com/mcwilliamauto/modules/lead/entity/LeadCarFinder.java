package com.mcwilliamauto.modules.lead.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("lead_car_finder")
public class LeadCarFinder {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String desiredMake;
    private String desiredModel;
    private Integer minYear;
    private Integer maxYear;
    private String desiredTrim;
    private Integer desiredKm;
    private String desiredBodyStyle;
    private String desiredTransmission;
    private String desiredDriveline;
    private String desiredFuelType;
    private String desiredColor;
    private String desiredCondition;
    private BigDecimal budgetMin;
    private BigDecimal budgetMax;
    private String message;
    private Integer status;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
