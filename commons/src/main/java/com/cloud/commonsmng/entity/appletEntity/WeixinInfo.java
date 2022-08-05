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
@TableName("weixin_info")
@ApiModel(value = "微信信息", description = "微信信息")
public class WeixinInfo extends BaseEntity {


    @ApiModelProperty(value = "openId")
    @TableField("openId")
    private String openId;

    @ApiModelProperty(value = "unionid")
    @TableField("unionid")
    private String unionid;

    @ApiModelProperty(value = "delFlag")
    @TableField("delFlag")
    private int delFlag;



}
