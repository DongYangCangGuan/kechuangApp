package com.cloud.commonsmng.entity.appletEntity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cloud.commonsmng.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("track")
@ApiModel(value = "track对象", description = "足迹表")
public class Track extends BaseEntity {
    @ApiModelProperty(value = "用户id")
    @TableField("userId")
    private String userId;

    @ApiModelProperty(value = "报告id")
    @TableField("reportId")
    private String reportId;

    @ApiModelProperty(value = "关联报告表")
    @TableField(value = "report", exist = false)
    private Report report;

    @ApiModelProperty(value = "关联报告属性表")
    @TableField(value = "reportKind", exist = false)
    private ReportKind reportKind;

}