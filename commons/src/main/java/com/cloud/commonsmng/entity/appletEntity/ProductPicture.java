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
@TableName("product_picture")
@ApiModel(value = "product_picture对象", description = "产品图片表")
public class ProductPicture extends BaseEntity {
    @ApiModelProperty(value = "产品图片base64")
    @TableField("pic")
    private String pic;

    @ApiModelProperty(value = "是否删除")
    @TableField("delFlag")
    private Integer delFlag;
}
