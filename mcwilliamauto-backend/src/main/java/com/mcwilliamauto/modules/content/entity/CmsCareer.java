package com.mcwilliamauto.modules.content.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("cms_career")
public class CmsCareer {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String department;
    private String location;
    private String jobType;
    private String description;
    private Integer status;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
