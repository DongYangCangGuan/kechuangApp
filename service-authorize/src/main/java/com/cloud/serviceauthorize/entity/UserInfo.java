package com.cloud.serviceauthorize.entity;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/*
* 用户信息类，信息来源是前段传递过来的
* */
@Data
public class UserInfo {
    @ApiModelProperty(value = "国家")
    private String country;

    @ApiModelProperty(value = "省份")
    private String province;

    @ApiModelProperty(value = "城市")
    private String city;

    @ApiModelProperty(value = "性别")
    private Integer gender;

    @ApiModelProperty(value = "头像url")
    private String avatarUrl;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "语言")
    private String language;
}
