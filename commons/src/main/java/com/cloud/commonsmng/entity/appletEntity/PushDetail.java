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
@TableName("push_detail")
@ApiModel(value = "pushdetail对象", description = "推送人员表")
public class PushDetail extends BaseEntity {

    @ApiModelProperty(value = "推送Id")
    @TableField("pushId")
    private String pushId;

    @ApiModelProperty(value = "状态")
    @TableField("status")
    private String status;

    @ApiModelProperty(value = "openId编号")
    @TableField("openId")
    private String openId;
}
