package com.mcwilliamauto.modules.lead.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LeadTradeInDTO {
    @NotBlank private String firstName;
    @NotBlank private String lastName;
    @NotBlank @Email private String email;
    @NotBlank private String phone;
    private Integer tradeYear;
    @NotBlank private String tradeMake;
    @NotBlank private String tradeModel;
    private String tradeTrim;
    private Integer tradeKm;
    private String tradeBodyStyle;
    private String tradeTransmission;
    private String tradeDriveline;
    private String tradeFuelType;
    private String tradeColor;
    private String tradeCondition;
    private String tradeVin;
    private String conditionNote;
    private String message;
}
