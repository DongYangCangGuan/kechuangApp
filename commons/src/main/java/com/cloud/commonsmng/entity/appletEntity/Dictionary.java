package com.cloud.commonsmng.entity.appletEntity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cloud.commonsmng.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * 字典表
 * author: tjs
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("Dictionary")
@ApiModel(value = "Dictionary对象", description = "字典表")
public class Dictionary extends BaseEntity {


    @ApiModelProperty(value = "代码")
    @TableField("code")
    private String code;

    @ApiModelProperty(value = "名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "类型")
    @TableField("kind")
    private String kind;

    @ApiModelProperty(value = "父级编号")
    @TableField("parentId")
    private String parentId;

    @ApiModelProperty(value = "创建者")
    @TableField("creatorid")
    private String creatorid;

    @ApiModelProperty(value = "创建时间")
    @TableField("createtime")
    private String createtime;

    @ApiModelProperty(value = "修改者")
    @TableField("modifierid")
    private String modifierid;

    @ApiModelProperty(value = "修改时间")
    @TableField("modifytime")
    private String modifytime;

    @ApiModelProperty(value = "图片资源")
    @TableField("pic")
    private String pic;

    @ApiModelProperty(value = "图片路径")
    @TableField("picurl")
    private String picurl;

    @ApiModelProperty(value = "是否有效（0-失效，1-有效）")
    @TableField("isused")
    private String isused;

    @ApiModelProperty(value = "索引")
    @TableField("index")
    private Integer index;

    @ApiModelProperty(value = "价格")
    @TableField("price")
    private BigDecimal price;

    @ApiModelProperty(value = "数值")
    @TableField("value")
    private Integer value;

    @ApiModelProperty(value = "参数")
    @TableField("parameter")
    private String parameter;

    @ApiModelProperty(value = "类型")
    @TableField("type")
    private String type;

    @ApiModelProperty(value = "修改名称")
    @TableField(value = "modifyname", exist = false)
    private String modifyname;

    @ApiModelProperty(value = "类型编号")
    @TableField(value = "kindCode", exist = false)
    private String kindCode;

    @ApiModelProperty(value = "子节点")
    @TableField(value = "children", exist = false)
    List<Dictionary> children;
}
