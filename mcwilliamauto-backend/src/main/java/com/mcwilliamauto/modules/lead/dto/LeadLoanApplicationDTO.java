package com.mcwilliamauto.modules.lead.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class LeadLoanApplicationDTO {
    private Long vehicleId;

    // ---------- Personal Information ----------
    private String salutation;
    private String gender;
    @NotBlank private String firstName;
    @NotBlank private String lastName;
    @NotBlank @Email private String email;
    @NotBlank private String phone;
    @NotBlank private String maritalStatus;
    @NotNull private LocalDate dateOfBirth;
    /** 明文传输，后端加密后存入 sinEnc（示例中简化为直接存字符串，生产环境请接入真实加密方案） */
    private String sin;

    // ---------- Current Address ----------
    @NotBlank private String address;
    @NotBlank private String city;
    @NotBlank private String province;
    @NotBlank private String postalCode;
    @NotNull private Integer addressDurationYears;
    @NotNull private Integer addressDurationMonths;

    // ---------- Home Rent/Mortgage Information ----------
    @NotBlank private String homeStatus;
    @NotNull private BigDecimal monthlyHousingPayment;

    // ---------- Current Employment ----------
    @NotBlank private String employmentType;
    @NotBlank private String employerName;
    @NotBlank private String occupation;
    @NotBlank private String employmentAddress;
    @NotBlank private String employmentCity;
    @NotBlank private String employmentProvince;
    @NotBlank private String employmentPostalCode;
    @NotBlank private String employmentPhone;
    /** 前端传入的税前收入，后端加密后存入 monthlyIncomeEnc（示例中简化为直接存字符串，生产环境请接入真实加密方案） */
    @NotNull private BigDecimal grossIncome;
    @NotNull private Integer employmentDurationYears;
    @NotNull private Integer employmentDurationMonths;

    // ---------- Previous Employment ----------
    private String previousEmployer;
    private String previousEmployerPhone;
    private Integer previousEmploymentDurationYears;
    private Integer previousEmploymentDurationMonths;

    // ---------- Other Information ----------
    private String previousBankruptcy;
    private String previousRepossession;
    private String cosignerAvailable;
    private String creditRating;

    // ---------- Desired Vehicle ----------
    private String desiredMake;
    private String desiredModel;
    private Integer desiredYear;
    private String desiredVin;

    // ---------- Trade In ----------
    private String tradeMake;
    private String tradeModel;
    private Integer tradeYear;
    private Integer tradeKm;

    private BigDecimal downPayment;
    private String message;
}
