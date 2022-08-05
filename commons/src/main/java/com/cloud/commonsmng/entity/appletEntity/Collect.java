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
 * 收藏表
 * author：xh
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("collect")
@ApiModel(value = "collect对象", description = "收藏表")
public class Collect extends BaseEntity {
    @ApiModelProperty(value = "用户id")
    @TableField("userId")
    private String userId;

    @ApiModelProperty(value = "报告id")
    @TableField("reportId")
    private String productId;

    @ApiModelProperty(value = "删除标记")
    @TableField("delFlag")
    private Integer delFlag;

    @ApiModelProperty(value = "关联报告表")
    @TableField(value = "news", exist = false)
    private Product product;

    @ApiModelProperty(value = "关联报告表")
    @TableField(value = "news", exist = false)
    private ProductPicture productPicture;




}
