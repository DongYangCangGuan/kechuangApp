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
@TableName("member_user")
@ApiModel(value = "MemberUser对象",description = "会员详情表")
public class MemberUserExport extends BaseEntity {
    @ApiModelProperty(value = "用户编号(外键，关联user表)")
    @TableField("userId")
    private String userId;

    @ApiModelProperty(value = "公司编号(外键，关联member表)")
    @TableField("memberId")
    private String memberId;

    @ApiModelProperty(value = "用户类型")
    @TableField(value = "memberType")
    private String memberType;

    @ApiModelProperty(value = "审批状态,1已审批，0未审批，2拒绝")
    @TableField(value = "approvalstatus", exist = false)
    private String approvalstatus;



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

    @ApiModelProperty(value = "修改者")
    @TableField(value = "modifierName")
    private String modifierName;


    }


