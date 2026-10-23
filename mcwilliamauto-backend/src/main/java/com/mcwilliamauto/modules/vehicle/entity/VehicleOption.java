package com.mcwilliamauto.modules.vehicle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/** 出厂选装包分组：EXTERIOR / INTERIOR / MECHANICAL / SAFETY / ENTERTAINMENT */
@Data
@TableName("vehicle_option")
public class VehicleOption {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long vehicleId;
    private String optionGroup;
    private String content;
    private Integer sortNo;
}
