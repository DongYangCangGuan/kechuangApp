package com.cloud.commonsmng.entity.appletEntity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cloud.commonsmng.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 消息明细表
 * author: tjs
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("notes_detail")
@ApiModel(value = "NotesDetail对象", description = "消息明细表")
public class NotesDetail extends BaseEntity {

    @ApiModelProperty(value = "消息主题")
    @TableField("notesId")
    private String notesId;

    @ApiModelProperty(value = "阅读状态（0未读；1已读）")
    @TableField("status")
    private String status;

    @ApiModelProperty(value = "用户编号")
    @TableField("userId")
    private String userId;

    @ApiModelProperty(value = "关联的消息信息")
    @TableField(value = "notes", exist = false)
    private Notes notes;

    @ApiModelProperty(value = "消息相关联的用户信息")
    @TableField(value = "userInfo", exist = false)
    private User userInfo;



}
