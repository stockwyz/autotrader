package com.mcwilliamauto.modules.content.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("cms_testimonial")
public class CmsTestimonial {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String customerName;
    private Integer rating;
    private String content;
    private String source;
    private Integer status;
    private Integer sortNo;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
