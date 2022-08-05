package com.cloud.commonsmng.entity.appletEntity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cloud.commonsmng.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 会员详情表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("member_detail")
@ApiModel(value = "MemberDetail对象",description = "会员详情表")
public class MemberDetail extends BaseEntity {
    @ApiModelProperty(value = "用户编号(外键，关联user表)")
    @TableField("userId")
    private String userId;

    @ApiModelProperty(value = "会员编号(外键，关联member表)")
    @TableField("memberId")
    private String memberId;

    @ApiModelProperty(value = "是否删除(1-存在 0-删除)")
    @TableField("delFlag")
    private Integer delFlag;

    @ApiModelProperty(value = "企业子账号Id")
    @TableField("membersonid")
    private String membersonid;

    @ApiModelProperty(value = "审批状态,1已审批，0未审批，2拒绝")
    @TableField("approvalstatus")
    private String approvalstatus;

    @ApiModelProperty(value = "用户类型")
    @TableField(value = "userType")
    private String userType;

    @ApiModelProperty(value = "会员类型,关联member表")
    @TableField(value = "memberType", exist = false)
    private String memberType;

    @ApiModelProperty(value = "会员类型名称,关联字典表")
    @TableField(value = "typeName", exist = false)
    private String typeName;

    @ApiModelProperty(value = "会员名称，关联member表")
    @TableField(value = "enterpriseName", exist = false)
    private String enterpriseName;

    @ApiModelProperty(value = "联系人,关联member表")
    @TableField(value = "contact", exist = false)
    private String contact;

    @ApiModelProperty(value = "联系电话,关联member表")
    @TableField(value = "phone", exist = false)
    private String phone;

    @ApiModelProperty(value = "邮箱,关联member表")
    @TableField(value = "email", exist = false)
    private String email;

    @ApiModelProperty(value = "真实姓名，关联user表")
    @TableField(value = "realName", exist = false)
    private String realName;

    @ApiModelProperty(value = "用户姓名，关联user表")
    @TableField(value = "userName", exist = false)
    private String userName;

    @ApiModelProperty(value = "时间")
    @TableField(value = "time", exist = false)
    private String time;

    @ApiModelProperty(value = "会员，关联member表")
    @TableField(value = "member", exist = false)
    private Member member;
}
