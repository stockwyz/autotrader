package com.mcwilliamauto.modules.dict.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("dict_body_style")
public class DictBodyStyle {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    /** 车身类型示例图片相对路径，如 bodystyle/logo-1735821423891.jpg */
    private String photoUrl;
    private Integer sortNo;
    private Integer status;
}
