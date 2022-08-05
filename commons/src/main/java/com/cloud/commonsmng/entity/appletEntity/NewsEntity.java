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
@TableName("aboutus")
@ApiModel(value = "新闻对象",description = "新闻表")
public class NewsEntity extends BaseEntity {

    @ApiModelProperty(value = "标题")
    @TableField("title")
    private String title;

    @ApiModelProperty(value = "描述")
    @TableField("describe")
    private String describe;


    @ApiModelProperty(value = "新闻图片")
    @TableField("newsPic")
    private String newsPic;


    @ApiModelProperty(value = "描述")
    @TableField("newsLink")
    private String newsLink;


    @ApiModelProperty(value = "描述")
    @TableField("newsTime")
    private String newsTime;


    @ApiModelProperty(value = "描述")
    @TableField("remark")
    private String remark;

}
