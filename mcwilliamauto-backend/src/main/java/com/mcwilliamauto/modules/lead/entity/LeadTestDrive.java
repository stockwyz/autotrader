package com.mcwilliamauto.modules.lead.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("lead_test_drive")
public class LeadTestDrive {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long vehicleId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String message;
    private LocalDateTime preferredTime;
    private Integer status;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
