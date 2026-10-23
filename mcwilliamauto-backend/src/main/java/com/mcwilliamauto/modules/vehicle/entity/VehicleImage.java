package com.mcwilliamauto.modules.vehicle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("vehicle_image")
public class VehicleImage {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long vehicleId;
    private String imageUrl;
    private String thumbUrl;
    private Boolean isMain;
    private Integer sortNo;
}
