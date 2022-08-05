package com.cloud.commonsmng.entity.appletEntity;


import com.baomidou.mybatisplus.annotation.FieldFill;
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
@TableName("product")
@ApiModel(value = "product对象", description = "产品表")
public class Product extends BaseEntity {

    @ApiModelProperty(value = "产品图片id,关联产品图片表")
    @TableField("img")
    private String img;

    @ApiModelProperty(value = "产品名称")
    @TableField("productName")
    private String productName;

    @ApiModelProperty(value = "产品简介")
    @TableField("introduction")
    private String introduction;

    //合作方
    @ApiModelProperty(value = "合作方，部门ID，关联部门表")
    @TableField("departmentId")
    private String departmentId;
    @ApiModelProperty(value = "合作方，部门名称")
    @TableField(value = "departmentName" , exist = false)
    private String departmentName;


    @ApiModelProperty(value = "贷款金额")
    @TableField("loanAmount")
    private String loanAmount;
    @ApiModelProperty(value = "期限")
    @TableField("timeLimit")
    private String timeLimit;
    @ApiModelProperty(value = "利率")
    @TableField("interestRate")
    private String interestRate;
    @ApiModelProperty(value = "融资额度")
    @TableField("financingAmount")
    private String financingAmount;
    @ApiModelProperty(value = "还款节奏")
    @TableField("repaymentRhythm")
    private String repaymentRhythm;
    @ApiModelProperty(value = "综合成本")
    @TableField("overallCosts")
    private String overallCosts;
    @ApiModelProperty(value = "融物标的")
    @TableField("meltingObject")
    private String meltingObject;
    @ApiModelProperty(value = "增信结构")
    @TableField("ces")
    private String ces;
    @ApiModelProperty(value = "使用场景")
    @TableField("useScenes1")
    private String useScenes1;
    @ApiModelProperty(value = "使用场景")
    @TableField("useScenes2")
    private String useScenes2;
    @ApiModelProperty(value = "使用场景")
    @TableField("useScenes3")
    private String useScenes3;
    @ApiModelProperty(value = "使用场景")
    @TableField("useScenes4")
    private String useScenes4;
    @ApiModelProperty(value = "使用场景")
    @TableField("useScenes5")
    private String useScenes5;
    @ApiModelProperty(value = "使用场景")
    @TableField("useScenes6")
    private String useScenes6;
    @ApiModelProperty(value = "服务对象")
    @TableField("serviceObject1")
    private String serviceObject1;
    @ApiModelProperty(value = "服务对象")
    @TableField("serviceObject2")
    private String serviceObject2;
    @ApiModelProperty(value = "服务对象")
    @TableField("serviceObject3")
    private String serviceObject3;
    @ApiModelProperty(value = "服务对象")
    @TableField("serviceObject4")
    private String serviceObject4;
    @ApiModelProperty(value = "服务对象")
    @TableField("serviceObject5")
    private String serviceObject5;
    @ApiModelProperty(value = "服务对象")
    @TableField("serviceObject6")
    private String serviceObject6;
    @ApiModelProperty(value = "产品类型,关联字典表")
    @TableField("productType")
    private String productType;
    @ApiModelProperty(value = "产品类型名称,字典表")
    @TableField(value = "TypeName",exist = false)
    private String TypeName;
    @ApiModelProperty(value = "产品状态（0无效，1有效,2发布）")
    @TableField("status")
    private Integer status;

    //关联表
    @ApiModelProperty(value = "产品合作模式")
    @TableField("CooperationModes")
    private List<CooperationMode> CooperationModes;

    @ApiModelProperty(value = "产品图片base64")
    @TableField(value = "pic",exist = false)
    private String pic;

    //product表中还未加入该字段
    @ApiModelProperty(value = "产归属品")
    @TableField("ascription")
    private String ascription;

    @ApiModelProperty(value = "次数")
    @TableField("times")
    private String times;

    @ApiModelProperty(value = "产品序号")
    @TableField("proSeq")
    private String proSeq;


    @ApiModelProperty(value = "产品特点")
    @TableField("productFeatures")
    private String productFeatures;

    @ApiModelProperty(value = "修改者")
    @TableField(value = "modifierName")
    private String modifierName;

    @ApiModelProperty(value = "案例描述")
    @TableField("caseDescription")
    private String caseDescription;

    @ApiModelProperty(value = "案例pdf存放路径")
    @TableField("casePath")
    private String casePath;

    @ApiModelProperty(value = "产品标签，使用,隔开")
    @TableField("label")
    private String label;

    @ApiModelProperty(value = "联系人")
    @TableField("linkMan")
    private String linkMan;

    @ApiModelProperty(value = "联系方式")
    @TableField("linkPhone")
    private String linkPhone;

    @ApiModelProperty(value = "联系邮箱")
    @TableField("linkEmail")
    private String linkEmail;

    @ApiModelProperty(value = "准入条件")
    @TableField("access")
    private String access;

    @ApiModelProperty(value = "是否收藏")
    @TableField("isCollect")
    private String isCollect;

    @ApiModelProperty(value = "产品亮点")
    @TableField("highLight")
    private String highLight;

    @ApiModelProperty(value = "案例")
    @TableField("example")
    private String example;


    @ApiModelProperty(value = "拓展字段")
    @TableField("productsExpandList")
    private List<ProductsExpand>  productsExpandList;

}
