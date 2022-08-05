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
@TableName("actionpoint")
@ApiModel(value = "point对象", description = "埋点表")
public class Point extends BaseEntity {

    @ApiModelProperty(value = "设备信息")
    @TableField("equipment")
    private String equipment;

    @ApiModelProperty(value = "APP应用版本号")
    @TableField("version")
    private String version;

    @ApiModelProperty(value = "地名")
    @TableField("address")
    private String address;

    @ApiModelProperty(value = "经纬度")
    @TableField("location")
    private String location;

    @ApiModelProperty(value = "页面消费数据")
    @TableField("consume")
    private String consume;

    @ApiModelProperty(value = "用户openid")
    @TableField("openid")
    private String openid;

    @ApiModelProperty(value = "日期")
    @TableField("dateTime")
    private Date dateTime;

    @ApiModelProperty(value = "埋点类型")
    @TableField("type")
    private String type;

    @ApiModelProperty(value = "时长")
    @TableField("timeLong")
    private int timeLong;

    @ApiModelProperty(value = "用户id")
    @TableField("userId")
    private String userId;

    @ApiModelProperty(value = "用户id")
    @TableField("productId")
    private String productId;

    @ApiModelProperty(value = "用户id")
    @TableField("productPicture")
    private ProductPicture productPicture;


    @ApiModelProperty(value = "关联产品表")
    @TableField(value = "product", exist = false)
    private Product product;



}
