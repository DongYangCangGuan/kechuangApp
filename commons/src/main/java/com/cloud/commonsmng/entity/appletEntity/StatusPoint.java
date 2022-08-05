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
@TableName("statusPoint")
@ApiModel(value = "statusPoint对象", description = "登录/退出埋点信息表")
public class StatusPoint extends BaseEntity {

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

    @ApiModelProperty(value = "openid")
    @TableField("openid")
    private String openid;

    @ApiModelProperty(value = "埋点类型，是登录埋点还是退出埋点")
    @TableField("type")
    private String type;

}
