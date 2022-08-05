package com.cloud.commonsmng.entity.appletEntity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cloud.commonsmng.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * 反馈表
 * author: xh
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("feedback")
@ApiModel(value = "feedback对象", description = "反馈表")
public class Feedback extends BaseEntity {

    @ApiModelProperty(value = "用户id")
    @TableField("userId")
    private String userId;

    @ApiModelProperty(value = "用户名")
    @TableField(value = "userName", exist = false)
    private String userName;

    @ApiModelProperty(value = "意见")
    @TableField("opinion")
    private String opinion;


    @ApiModelProperty(value = "是否删除(1-存在  2-删除)")
    //exist 表示该属性不是表的字段，在新增、修改时 mybatis-plus 会排除该属性
    @TableField(value = "delFlag")
    private Integer delFlag;

}
