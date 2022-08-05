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
@TableName("report_type")
@ApiModel(value = "ReportType对象",description = "报告分类表")
public class ReportType extends BaseEntity {

    @ApiModelProperty(value = "报告代码")
    @TableField("code")
    private String code;

    @ApiModelProperty(value = "报告分类名称")
    @TableField("reportTypeName")
    private String reportTypeName;

    @ApiModelProperty(value = "报告封面图片")
    @TableField("pic")
    private String pic;

    @ApiModelProperty(value = "当前封面类型是否可用")
    @TableField("delFlag")
    private Integer delFlag;
}
