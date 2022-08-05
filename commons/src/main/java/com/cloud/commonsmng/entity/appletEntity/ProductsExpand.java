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
@TableName("product_expand")
@ApiModel(value = "cooperation_mode对象", description = "合作模式信息表")
public class ProductsExpand extends BaseEntity {
    @ApiModelProperty(value = "产品id")
    @TableField("productId")
    private String productId;

    @ApiModelProperty(value = "字段名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "字段内容")
    @TableField("content")
    private String content;

    @ApiModelProperty(value = "状态")
    @TableField("status")
    private String status;

}
