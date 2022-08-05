package com.cloud.commonsmng.entity.appletEntity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cloud.commonsmng.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("actionPoint")
@ApiModel(value = "actionPoint对象", description = "行为埋点信息表")
public class ActionPoint extends BaseEntity {
    @ApiModelProperty(value = "点击、播放开始/结束，订，踩，评论等")
    @TableField("consume")
    private String consume;

    @ApiModelProperty(value = "openid")
    @TableField("openid")
    private String openid;

    @ApiModelProperty(value = "页面id")
    @TableField("pageId")
    private String pageId;

    @ApiModelProperty(value = "来源")
    @TableField("source")
    private String source;

    @ApiModelProperty(value = "携带参数")
    @TableField("withParameters")
    private String withParameters;


    @ApiModelProperty(value = "时长")
    @TableField("timeLong")
    private BigDecimal timeLong;

    @ApiModelProperty(value = "报告Id")
    @TableField("reportId")
    private String reportId;

    @ApiModelProperty(value = "企业编号")
    @TableField("enterpriseCode")
    private String enterpriseCode;


    @ApiModelProperty(value = "userId")
    @TableField("userId")
    private String userId;

    @ApiModelProperty(value = "关联报告表")
    @TableField(value = "report", exist = false)
    private Report report;

    @ApiModelProperty(value = "关联报告属性表")
    @TableField(value = "reportKind", exist = false)
    private ReportKind reportKind;
}
