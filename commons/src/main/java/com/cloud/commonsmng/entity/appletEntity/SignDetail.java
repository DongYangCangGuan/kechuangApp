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
@TableName("SignDetail")
@ApiModel(value = "模板填报返回表", description = "模板填报返回表")
public class SignDetail extends BaseEntity {


    @ApiModelProperty(value = "报名表ID")
    @TableField("signId")
    private String signId;

    @ApiModelProperty(value = "模板id")
    @TableField("templateId")
    private String templateId;

    @ApiModelProperty(value = "模板")
    @TableField("templateName")
    private String templateName;

    @ApiModelProperty(value = "回答")
    @TableField("answer")
    private String answer;

    @ApiModelProperty(value = "是否使用")
    @TableField("isused")
    private String isused;

    @ApiModelProperty(value = "删除标志")
    @TableField("delFlag")
    private String delFlag;

    @ApiModelProperty(value = "排序")
    @TableField("seq")
    private String seq;

    @ApiModelProperty(value = "模板类型")
    @TableField("templateType")
    private String templateType;

}
