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
@TableName("point")
@ApiModel(value = "point对象", description = "埋点表")
public class PointEntity extends BaseEntity {

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
    private String dateTime;

    @ApiModelProperty(value = " 访问页面id")
    @TableField(value = "pageId", exist = false)
    private String pageId;

    @ApiModelProperty(value = "访问页面类型 ac活动 pd产品 news新闻")
    @TableField("type")
    private String type;

    @ApiModelProperty(value = "时长")
    @TableField("timeLong")
    private int timeLong;

    @ApiModelProperty(value = "用户id")
    @TableField("userId")
    private String userId;

    @ApiModelProperty(value = "报告id")
    @TableField("reportId")
    private String reportId;

    @ApiModelProperty(value = "统计数目")
    @TableField(value = "count")
    private int count;

    @ApiModelProperty(value = "名称")
    @TableField(value = "name")
    private String name;


}
