package com.mcwilliamauto.modules.dict.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("dict_model")
public class DictModel {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long makeId;
    private String name;
    private Integer sortNo;
    private Integer status;

    /** 非数据库字段，查询时从 dict_make 关联补充，用于前端展示品牌名/图标 */
    @TableField(exist = false)
    private String makeName;
    @TableField(exist = false)
    private String makeLogoUrl;
}
