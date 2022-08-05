package com.cloud.commonsmng.entity.appletEntity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Authentication",description = "认证")
public class Authentication {

    @ApiModelProperty(value = "gp对象")
    @TableField("user")
    private User user;


    @ApiModelProperty(value = "创业公司对象")
    @TableField("member")
    private Member member;

}
