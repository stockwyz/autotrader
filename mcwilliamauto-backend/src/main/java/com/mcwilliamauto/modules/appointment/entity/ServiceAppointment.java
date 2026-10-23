package com.mcwilliamauto.modules.appointment.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/** 维修/养护预约。status: 0=新预约 1=已确认 2=已完成 3=已取消(取消后释放时间段) */
@Data
@TableName("service_appointment")
public class ServiceAppointment {
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 对客户展示的预约单号，如 AP-20261013-7K3Q，唯一 */
    private String apptNo;
    /** REPAIR / MAINTENANCE / PHR */
    private String serviceType;
    private LocalDate apptDate;
    private LocalTime apptTime;
    private String detail;
    private String customerName;
    private String phone;
    private String email;
    private String vehicleInfo;
    private Integer status;
    /** 邮件里确认/取消按钮用的随机令牌 */
    private String confirmToken;
    /** 客户在邮件里的操作：0=未操作 1=客户点了确认 2=客户点了取消(不影响 status，需管理员人工确认) */
    private Integer customerResponse;
    private LocalDateTime respondedAt;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
