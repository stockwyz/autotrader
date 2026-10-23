package com.mcwilliamauto.modules.vehicle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/** 分类配置：MECHANICAL / SAFETY / TECHNOLOGY / COMFORT / EXTERIOR / OFFROAD */
@Data
@TableName("vehicle_feature")
public class VehicleFeature {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long vehicleId;
    private String category;
    private String content;
    private Integer sortNo;
}
