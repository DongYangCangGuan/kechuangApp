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
@TableName("slideshow")
@ApiModel(value = "slider对象",description = "轮播广告表")
public class Slider extends BaseEntity {

    @ApiModelProperty(value = "索引")
    @TableField("index")
    private Integer index;


    @ApiModelProperty(value = "图片base64")
    @TableField("pic")
    private String pic;


    @ApiModelProperty(value = "跳转路径")
    @TableField("url")
    private String url;

    @ApiModelProperty(value = "是否有效(1-有效，0-无效)")
    @TableField("isused")
    private Boolean isused;

    @ApiModelProperty(value = "修改者用户名")
    @TableField(value = "modifyusername",exist = false)
    private String modifyusername;

    @ApiModelProperty(value = "标题")
    @TableField(value = "title")
    private String title;

}
