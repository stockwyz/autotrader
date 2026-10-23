package com.mcwilliamauto.modules.lead.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("lead_loan_application")
public class LeadLoanApplication {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long vehicleId;

    // ---------- Personal Information ----------
    private String salutation;
    private String gender;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String maritalStatus;
    private LocalDate dateOfBirth;
    private String sinEnc;

    // ---------- Current Address ----------
    private String address;
    private String city;
    private String province;
    private String postalCode;
    private BigDecimal yearsAtAddress; // 历史遗留字段，已不再使用，新表单改用下面两个字段
    private Integer addressDurationYears;
    private Integer addressDurationMonths;

    // ---------- Home Rent/Mortgage Information ----------
    private String homeStatus;
    private BigDecimal monthlyHousingPayment;

    // ---------- Current Employment ----------
    private String employmentType;
    private String employerName;
    private String jobTitle; // 对应前端表单的 Occupation 字段
    private String employmentAddress;
    private String employmentCity;
    private String employmentProvince;
    private String employmentPostalCode;
    private String employmentPhone;
    private BigDecimal employmentYears; // 历史遗留字段，已不再使用，新表单改用下面两个字段
    private Integer employmentDurationYears;
    private Integer employmentDurationMonths;
    private String monthlyIncomeEnc; // 对应前端表单的 Gross Income 字段

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
    private Integer status;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
