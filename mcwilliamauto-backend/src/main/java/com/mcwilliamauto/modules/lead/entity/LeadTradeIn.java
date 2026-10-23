package com.mcwilliamauto.modules.lead.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("lead_trade_in")
public class LeadTradeIn {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Integer tradeYear;
    private String tradeMake;
    private String tradeModel;
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
    private Integer status;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
