package com.mcwilliamauto.modules.content.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("cms_page")
public class CmsPage {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String code;
    private String title;
    private String content;
    private String seoTitle;
    private String seoKeywords;
    private String seoDescription;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
