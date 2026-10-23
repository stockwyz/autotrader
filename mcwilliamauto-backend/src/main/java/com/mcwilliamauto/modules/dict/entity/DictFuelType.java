package com.mcwilliamauto.modules.dict.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("dict_fuel_type")
public class DictFuelType {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
}
