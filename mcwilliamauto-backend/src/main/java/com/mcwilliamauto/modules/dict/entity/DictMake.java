package com.mcwilliamauto.modules.dict.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("dict_make")
public class DictMake {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String logoUrl;
    private Integer sortNo;
    private Integer status;
}
