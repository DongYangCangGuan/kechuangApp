package com.cloud.commonsmng.entity.appletEntity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cloud.commonsmng.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ActivityTemplate")
@ApiModel(value = "活动模板表", description = "活动模板表")
public class ActivityTemplate extends BaseEntity {

    @ApiModelProperty(value = "活动表ID")
    @TableField("activityId")
    private String activityId;


    @ApiModelProperty(value = "活动表ID")
    @TableField("activityTemId")
    private String activityTemId;


    @ApiModelProperty(value = "模板名称")
    @TableField("template")
    private String template;

    @ApiModelProperty(value = "排序")
    @TableField("seq")
    private String seq;

    @ApiModelProperty(value = "是否使用")
    @TableField("isused")
    private String isused;

    @ApiModelProperty(value = "删除标志")
    @TableField("delFlag")
    private String delFlag;

    @ApiModelProperty(value = "删除标志")
    @TableField("templateType")
    private String templateType;


}
