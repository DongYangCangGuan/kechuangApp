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
@TableName("Customize_question")
@ApiModel(value = "CustomizeQuestion对象", description = "私人定制问题表")
public class CustomizeQuestion extends BaseEntity {

    @ApiModelProperty(value = "用户id")
    @TableField("userId")
    private String userId;

    @ApiModelProperty(value = "私人定制问题")
    @TableField("questionDescription")
    private String questionDescription;

    @ApiModelProperty(value = "是否删除(1-存在  2-删除)")
    @TableField(value = "delFlag")
    private byte delFlag;

    @ApiModelProperty(value = "序号")
    @TableField(value = "sequen")
    private int sequen;

    @ApiModelProperty(value = "发布标志（1已发布，0未发布")
    @TableField(value = "releaseFlag")
    private byte releaseFlag;

    @ApiModelProperty(value = "修改者名")
    @TableField(value = "modifierName", exist = false)
    private String modifierName;

    @ApiModelProperty(value = "创建者名")
    @TableField(value = "creatorName", exist = false)
    private String creatorName;
}
