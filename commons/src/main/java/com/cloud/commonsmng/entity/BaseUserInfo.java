package com.cloud.commonsmng.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "BaseUserInfo对象",description = "父级用户信息")
public class BaseUserInfo extends BaseEntity implements Serializable {
    @ApiModelProperty(value = "编号")
    private String id;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "密码")
    private String passWord;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "头像")
    private String headImg;

    @ApiModelProperty(value = "性别")
    private Integer gender;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "机构编号")
    private String departmentId;

    @ApiModelProperty(value = "")
    private String empid;

    @ApiModelProperty(value = "")
    private String deptCode;

    @ApiModelProperty(value = "机构名称")
    private String deptName;

    @ApiModelProperty(value = "用户角色集")
    private List<BaseRole> listrole;

    @ApiModelProperty(value = "token")
    private String token;
}
