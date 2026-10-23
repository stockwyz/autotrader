package com.mcwilliamauto.modules.lead.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class LeadCarFinderDTO {
    @NotBlank private String firstName;
    @NotBlank private String lastName;
    @NotBlank @Email private String email;
    @NotBlank private String phone;
    @NotBlank private String desiredMake;
    @NotBlank private String desiredModel;
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
    @NotNull private BigDecimal budgetMax;
    private String message;
}
