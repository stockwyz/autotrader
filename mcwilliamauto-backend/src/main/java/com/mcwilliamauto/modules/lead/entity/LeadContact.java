package com.mcwilliamauto.modules.lead.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("lead_contact")
public class LeadContact {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String type; // CONTACT / TEXT_US
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String message;
    private Integer status;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
