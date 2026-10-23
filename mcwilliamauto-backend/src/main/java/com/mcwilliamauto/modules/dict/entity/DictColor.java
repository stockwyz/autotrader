package com.mcwilliamauto.modules.dict.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("dict_color")
public class DictColor {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String hexCode;
}
