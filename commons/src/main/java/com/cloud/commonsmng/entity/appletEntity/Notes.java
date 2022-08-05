package com.cloud.commonsmng.entity.appletEntity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cloud.commonsmng.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 消息表
 * author: tjs
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("notes")
@ApiModel(value = "notes对象",description = "消息表")
public class Notes extends BaseEntity {

    @ApiModelProperty(value = "发布主题")
    @TableField("title")
    private String title;

    @ApiModelProperty(value = "发布内容")
    @TableField("content")
    private String content;

    @ApiModelProperty(value = "发布类型（all：全局消息；user：用户消息）")
    @TableField("taskType")
    private String taskType;

    @ApiModelProperty(value = "是否反馈（0：不需要反馈；1：需要反馈）")
    @TableField("isFeedBack")
    private Integer isFeedBack;

    @ApiModelProperty(value = "是否删除（0：删除，1：存在）")
    @TableField("delFlag")
    private Boolean delFlag;

    @ApiModelProperty(value = "状态（0：待审核，1：已发布）")
    @TableField("status")
    private String status;

    @ApiModelProperty(value = "消息通知方式（1微信通知，2邮件通知 3都通知，0系统内部通知）")
    @TableField("flag")
    private Integer flag;


    @ApiModelProperty(value = "消息类型名称")
    @TableField(value = "notesTaskTypeName",exist = false)
    private String notesTaskTypeName;

    @ApiModelProperty(value = "修改者用户名")
    @TableField(value = "modifyusername",exist = false)
    private String modifyusername;

    @ApiModelProperty(value="已读次数")
    @TableField(value = "readNum",exist = false)
    private Integer readNum;

    @ApiModelProperty(value="需要阅读数")
    @TableField(value = "totalNum",exist = false)
    private Integer totalNum;

    @ApiModelProperty(value = "接收信息的用户")
    @TableField(value = "userIds",exist = false)
    private List<String> userIds;

    @ApiModelProperty(value = "信息详情")
    @TableField(value = "notesDetailList",exist = false)
    private List<NotesDetail> notesDetailList;

    @ApiModelProperty(value = "阅读状态")
    @TableField("read_status")
    private String readStatus;

    @ApiModelProperty(value = "阅读状态")
    @TableField("time")
    private String time;
    @ApiModelProperty(value = "接收人姓名")
    @TableField(value = "userName",exist = false)
    private String userName;
    @ApiModelProperty(value = "公众号消息通知模版id")
    @TableField(value = "templateId",exist = false)
    private String templateId;

    @ApiModelProperty(value = "备注")
    @TableField(value = "remark",exist = false)
    private String remark;

}
