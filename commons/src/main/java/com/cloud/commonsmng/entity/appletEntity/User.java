package com.cloud.commonsmng.entity.appletEntity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cloud.commonsmng.entity.BaseUserInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)  //注解的作用就是将其父类属性不进行比较
@Accessors(chain = true)  //@Accessors用于配置getter和setter方法的生成结果
@TableName("user")
@ApiModel(value = "User对象",description = "用户表") //在实体类上边使用，标记类时swagger的解析类。
public class User extends BaseUserInfo {

    private static final long serialVersionUID = 1L;


    public String getUserId() {
        this.userId = super.getId();
        return this.userId;
    }


    public void setUserId(String userId) {
        this.userId = userId;
        this.setId(userId);
    }

    @ApiModelProperty(value = "对应统一认证系统的唯一用户id")
    @TableField(value="userId",exist = false)
    private String userId;

    @ApiModelProperty(value = "用户名")
    @TableField("userName")
    private String userName;

    @ApiModelProperty(value = "密码")
    @TableField("passWord")
    private String passWord;

    @ApiModelProperty(value = "对应微信openId")
    @TableField("openId")
    private String openId;


    @ApiModelProperty(value = "昵称")
    @TableField("nickName")
    private String nickName;


    @ApiModelProperty(value = "真实姓名")
    @TableField("realName")
    private String realName;


    @ApiModelProperty(value = "性别")
    @TableField("gender")
    private Integer gender;


    @ApiModelProperty(value = "出生日期")
    @TableField("birthdate")
    private Date birthdate;


    @ApiModelProperty(value = "身份证")
    @TableField("identityCard")
    private String identityCard;


    @ApiModelProperty(value = "头像")
    @TableField("headImg")
    private String headImg;

    @ApiModelProperty(value = "手机号")
    @TableField("phone")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    @TableField("email")
    private String email;

    @ApiModelProperty(value = "企业名称")
    @TableField("enterpriseName")
    private String enterpriseName;

    @ApiModelProperty(value = "联系地址")
    @TableField("contactAddress")
    private String contactAddress;

    @ApiModelProperty(value = "角色身份(0-pc端行内人员  1-小程序端客户)")
    @TableField("uRole")
    private String uRole;

    @ApiModelProperty(value = "认证标记（0：未认证，1：审核中，2.审核失败，3： 已认证）")
    @TableField("certificationMark")
    private String certificationMark;

    @ApiModelProperty(value = "是否删除")
    @TableField("delFlag")
    private String delFlag;

    @ApiModelProperty(value = "用户关联的会员信息")
    @TableField(value = "member",exist = false)
    private Member member;

    @ApiModelProperty(value = "用户关联的会员信息")
    @TableField(value = "memberUser",exist = false)
    private MemberUser memberUser;

    @ApiModelProperty(value = "是否使用")
    @TableField("isused")
    private String isused;

    @ApiModelProperty(value = "用户职位")
    @TableField("job")
    private String job;

    @ApiModelProperty(value = "会员表Id")
    @TableField("memberId")
    private String memberId;

    @ApiModelProperty(value = "用户类型")
    @TableField("userType")
    private String userType;


    @ApiModelProperty(value = "名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "名称")
    @TableField("loginname")
    private String loginname;

    @ApiModelProperty(value = "名称")
    @TableField("mobile")
    private String mobile;

    @ApiModelProperty(value = "名称")
    @TableField("pic")
    private String pic;

    @ApiModelProperty(value = "名称")
    @TableField("sex")
    private String sex;

    @ApiModelProperty(value = "名称")
    @TableField("birthday")
    private String birthday;

    @ApiModelProperty(value = "微信unionid")
    @TableField("unionid")
    private String unionid;
    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String getPassWord() {
        return passWord;
    }

    @Override
    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    @Override
    public String getNickName() {
        return nickName;
    }

    @Override
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public Integer getGender() {
        return gender;
    }

    @Override
    public void setGender(Integer gender) {
        this.gender = gender;
    }

    @Override
    public String getHeadImg() {
        return headImg;
    }

    @Override
    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }


}
