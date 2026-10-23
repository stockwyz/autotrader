package com.mcwilliamauto.modules.dealer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalTime;

@Data
@TableName("dealer_business_hours")
public class DealerBusinessHours {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Integer dayOfWeek; // 1=周一...7=周日
    private LocalTime openTime;
    private LocalTime closeTime;
}
