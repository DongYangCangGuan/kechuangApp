package com.cloud.commonsmng.entity.appletEntity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cloud.commonsmng.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("report")
@ApiModel(value = "Report对象",description = "报告表")
public class Report extends BaseEntity {

    @ApiModelProperty(value = "报告代码")
    @TableField("code")
    private String code;

    @ApiModelProperty(value = "报告标题")
    @TableField("title")
    private String title;

    @ApiModelProperty(value = "报告图片路径")
    @TableField("pic")
    private String pic;

    @ApiModelProperty(value = "报告介绍")
    @TableField("description")
    private String description;

    @ApiModelProperty(value = "收藏人数")
    @TableField("favorite")
    private String favorite;

    @ApiModelProperty(value = "报告pdf路径")
    @TableField("url")
    private String url;

    @ApiModelProperty(value = "报告点赞次数")
    @TableField("thumbUpNumber")
    private Integer thumbUpNumber;

    @ApiModelProperty(value = "报告阅读次数")
    @TableField("articleviews")
    private Integer articleviews;

    @ApiModelProperty(value = "收藏标志")
    @TableField(value = "collectFlag", exist = false)
    private Integer collectFlag;

    @ApiModelProperty(value = "文件语音")
    @TableField(value = "voice", exist = false)
    private String voice;

    @ApiModelProperty(value = "报告表关联属性表")
    @TableField(value = "reportKind", exist = false)
    private ReportKind reportKind;

    @ApiModelProperty(value = "是否删除(1-存在  2-删除)")
    //exist 表示该属性不是表的字段，在新增、修改时 mybatis-plus 会排除该属性
    @TableField(value = "delFlag")
    private Integer delFlag;

    @ApiModelProperty(value = "行业列表")
    @TableField(value = "industryList", exist = false)
    private List<String> industryList;

    @ApiModelProperty(value = "主题列表")
    @TableField(value = "themeList", exist = false)
    private List<String> themeList;

    @ApiModelProperty(value = "报告所属区域列表")
    @TableField(value = "areaList", exist = false)
    private List<String> areaList;

    @ApiModelProperty(value = "分类列表")
    @TableField(value = "kindIdList", exist = false)
    private List<String> kindIdList;

    @ApiModelProperty(value = "标签列表")
    @TableField(value = "labelList", exist = false)
    private List<String> labelList;

    @ApiModelProperty(value = "宏观经济主题列表")
    @TableField(value = "economyList", exist = false)
    private List<String> economyList;

    @ApiModelProperty(value = "专题报告主题列表")
    @TableField(value = "specialList", exist = false)
    private List<String> specialList;

    @ApiModelProperty(value = "访问状态")
    @TableField(value = "visitStatus",exist = false)
    private String visitStatus;//200-可访问  500-不可访问 203-不可访问（未登录） 204-不可访问（无充值） 205-不可访问（无订阅）
}
