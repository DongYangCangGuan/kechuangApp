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
@TableName("reportkind")
@ApiModel(value = "ReportDetail对象",description = "报告详情表")
public class ReportKind extends BaseEntity {

    @ApiModelProperty(value = "报告id")
    @TableField(value = "reportId")
    private String reportId;

    @ApiModelProperty(value = "报告分类")
    @TableField("kindId")
    private String kindId;

    @ApiModelProperty(value = "报告专题")
    @TableField("theme")
    private String theme;

    @ApiModelProperty(value = "报告行业")
    @TableField("industry")
    private String industry;

    @ApiModelProperty(value = "报告区域")
    @TableField("area")
    private String area;

    @ApiModelProperty(value = "报告标签")
    @TableField("label")
    private String label;

    @ApiModelProperty(value = "宏观经济主题")
    @TableField("economy")
    private String economy;

    @ApiModelProperty(value = "专题报告主题")
    @TableField("special")
    private String special;

    @ApiModelProperty(value = "报告是否有效(1-有效 0-无效)")
    @TableField("delFlag")
    private Integer delFlag;
}
