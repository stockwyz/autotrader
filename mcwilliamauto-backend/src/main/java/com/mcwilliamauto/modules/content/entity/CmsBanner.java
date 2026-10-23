package com.mcwilliamauto.modules.content.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("cms_banner")
public class CmsBanner {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String imageUrl;
    private String title;
    private String linkUrl;
    private Integer sortNo;
    private Integer status;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
