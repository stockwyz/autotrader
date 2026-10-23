package com.mcwilliamauto.modules.dealer.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("dealer_info")
public class DealerInfo {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String logoUrl;
    private String address;
    private String websiteUrl;
    private String phone;
    private String email;
    private String dealerPermitNo;
    private String facebookUrl;
    private String instagramUrl;
    private BigDecimal latitude;
    private BigDecimal longitude;
    /** 前台网站风格编码 A~H，为空按 A 处理 */
    private String themeCode;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
