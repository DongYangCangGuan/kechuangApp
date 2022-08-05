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
@TableName("product")
@ApiModel(value = "cooperation_mode对象", description = "合作模式信息表")
public class CooperationMode extends BaseEntity {
    @ApiModelProperty(value = "产品id")
    @TableField("productId")
    private String productId;
    @ApiModelProperty(value = "合作模式名称")
    @TableField("name")
    private String name;
    @ApiModelProperty(value = "合作模式简介")
    @TableField("cmIntroduction")
    private String cmIntroduction;
    @ApiModelProperty(value = "合作模式图片地址")
    @TableField("cmPic")
    private String cmPic;
}
