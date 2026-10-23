package com.mcwilliamauto.modules.finance.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("finance_setting")
public class FinanceSetting {
    @TableId(type = IdType.AUTO)
    private Long id;
    private BigDecimal defaultInterestRate;
    private Integer minTermMonths;
    private Integer maxTermMonths;
    private Integer defaultTermMonths;
    private BigDecimal minDownPaymentPct;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
