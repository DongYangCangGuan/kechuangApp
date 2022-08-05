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
@TableName("Customize_question")
@ApiModel(value = "CustomizeQuestion对象", description = "私人定制问题表")
public class CustomizeAnswer extends BaseEntity {

    @ApiModelProperty(value = "私人定制问题id")
    @TableField("questionId")
    private String questionId;

    @ApiModelProperty(value = "回答")
    @TableField("answer")
    private String answer;

    @ApiModelProperty(value = "是否删除(1-存在  2-删除)")
    @TableField(value = "delFlag")
    private int delFlag;

    @ApiModelProperty(value = "回复")
    @TableField(value = "reply")
    private String reply;

    @ApiModelProperty(value = "回复时间")
    @TableField(value = "replyTime")
    private String replyTime;

    @ApiModelProperty(value = "回复人Id")
    @TableField(value = "replyerId")
    private String replyerId;

    @ApiModelProperty(value = "回复者用户名")
    @TableField(value = "replyerName", exist = false)
    private String replyerName;

    @ApiModelProperty(value = "创建者用户名")
    @TableField(value = "creatorName", exist = false)
    private String creatorName;

    @ApiModelProperty(value = "问题描述")
    @TableField(value = "questionDescription",exist = false)
    private String questionDescription;

    @ApiModelProperty(value = "回答者用户信息")
    @TableField(value = "answerUser",exist = false)
    private User answerUser;

    @ApiModelProperty(value = "回复者用户信息")
    @TableField(value = "replyerUser",exist = false)
    private User replyerUser;
}
