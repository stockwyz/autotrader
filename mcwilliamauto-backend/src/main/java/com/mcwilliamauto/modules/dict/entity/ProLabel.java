package com.mcwilliamauto.modules.dict.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 促销标签(Pro-Label)：可复用的车辆促销图标(比如"Featured"/"Manager's Special"这种带图案的角标)，
 * 图片全局共用(存在 data/img/label 下，不跟某台具体车绑定)，size/angle/posX/posY 控制这个图标
 * 叠加在车辆图片上的缩放比例、旋转角度、和位置(百分比坐标，方便适配首页/搜索结果/详情页里
 * 不同尺寸但相同宽高比的图片容器)。
 */
@Data
@TableName("pro_label")
public class ProLabel {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String imageUrl;
    /** 缩放百分比：宽/高分开控制，支持横纵独立拉伸(不再强制等比缩放)。100=基准大小 */
    private Integer widthPercent;
    private Integer heightPercent;
    /** 旋转角度，0为起始(不旋转)，顺时针为正 */
    private Integer angle;
    /** 标签中心点X坐标，相对图片容器宽度的百分比 0-100 */
    private java.math.BigDecimal posX;
    /** 标签中心点Y坐标，相对图片容器高度的百分比 0-100 */
    private java.math.BigDecimal posY;
    private Integer sortNo;
    private Integer status;
}
