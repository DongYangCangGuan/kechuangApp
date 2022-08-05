package com.cloud.commonsmng.entity.appletEntity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cloud.commonsmng.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 会员表
 * author: tjs
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("member")
@ApiModel(value = "Member对象", description = "会员表")
public class Member extends BaseEntity{

    //GP  创业公司：项目法定名称   LP:LP全称
    @ApiModelProperty(value = "企业名称")
    @TableField("enterpriseName")
    private String enterpriseName;

    @ApiModelProperty(value = "企业账号")
    @TableField("enterpriseCode")
    private String enterpriseCode;

    //GP:机构简称  创业公司：项目简称   LP:LP简称
    @ApiModelProperty(value = "简称")
    @TableField("abbreviation")
    private String abbreviation;

    @ApiModelProperty(value = "密码")
    @TableField("pwd")
    private String pwd;

    @ApiModelProperty(value = "主要合伙人,联系人")
    @TableField("contact")
    private String contact;

    @ApiModelProperty(value = "联系电话")
    @TableField("phone")
    private String phone;

    //GP:总部所在省份城市  企业:项目经营地
    @ApiModelProperty(value = "地址")
    @TableField("address")
    private String address;

    //创业公司：项目注册地
    @ApiModelProperty(value = "注册地址")
    @TableField("address1")
    private String address1;


    @ApiModelProperty(value = "邮箱")
    @TableField("email")
    private String email;


    @ApiModelProperty(value = "客户经理（外键，关联user表）")
    @TableField("accountManager")
    private String accountManager;

    @ApiModelProperty(value = "备注")
    @TableField("remark")
    private String remark;

    @ApiModelProperty(value = "是否删除")
    @TableField("delFlag")
    private Integer delFlag;

    @ApiModelProperty(value = "客户经理的名称")
    @TableField(value = "accountManagerName", exist = false)
    private String accountManagerName;


    @ApiModelProperty(value = "会员类型(关联字典表，code)")
    @TableField(value = "memberType")
    private String memberType;

    @ApiModelProperty(value = "会员类型名称")
    @TableField(value = "typeName", exist = false)
    private String typeName;


    @ApiModelProperty(value = "联系人职位")
    @TableField(value = "job")
    private String job;

    //GP
    @ApiModelProperty(value = "上海是否有办公室,0没有，1有")
    @TableField(value = "SHOffice")
    private Integer SHOffice;

    //GP(上级为科创) 科创投资基金    创业公司（上级为GP） 投资基金   上级单位   parentId
    @ApiModelProperty(value = "投资基金,上级单位ID")
    @TableField(value = "investmentFund")
    private String investmentFund;

    //GP(上级为科创) 科创投资基金    创业公司（上级为GP） 投资基金   上级单位
    @ApiModelProperty(value = "投资基金,上级单位")
    @TableField(value = "investmentFundName")
    private String investmentFundName;

    //GP
    @ApiModelProperty(value = "关注行业，关联字典表")
    @TableField(value = "attentionIndustry", exist = false)
    private String attentionIndustry;

    //GP
    @ApiModelProperty(value = "关注行业，关联字典表")
    @TableField(value = "attentionIndustryId")
    private String attentionIndustryId;

    //GP
    @ApiModelProperty(value = "关注阶段，关联字典表")
    @TableField(value = "attentionStage", exist = false)
    private String attentionStage;
    //GP
    @ApiModelProperty(value = "关注阶段，关联字典表")
    @TableField(value = "attentionStageId")
    private String attentionStageId;

    //GP
    @ApiModelProperty(value = "投后项目经理 ,手写")
    @TableField(value = "investmentManager")
    private String investmentManager;

    //创业公司
    @ApiModelProperty(value = "科创行业分类,关联字典表")
    @TableField(value = "industryClassification")
    private String industryClassification;
    //创业公司
    @ApiModelProperty(value = "科创行业分类,关联字典表")
    @TableField(value = "industryClassificationId")
    private String industryClassificationId;

    //创业公司
    @ApiModelProperty(value = "科创直投分类,关联字典表")
    @TableField(value = "directInvestmentClassification", exist = false)
    private String directInvestmentClassification;

    //创业公司
    @ApiModelProperty(value = "科创直投分类,关联字典表")
    @TableField(value = "directInvestmentClassificationId")
    private String directInvestmentClassificationId;

    //创业公司
    @ApiModelProperty(value = "犀牛标签")
    @TableField(value = "rhinocerosLabel", exist = false)
    private String rhinocerosLabel;

    //创业公司
    @ApiModelProperty(value = "犀牛标签,关联字典表")
    @TableField(value = "rhinocerosLabelId")
    private String rhinocerosLabelId;
  /*  //创业公司
    @ApiModelProperty(value = "犀牛标签1,关联字典表")
    @TableField(value = "rhinocerosLabel1", exist = false)
    private String rhinocerosLabel1;
    //创业公司
    @ApiModelProperty(value = "犀牛标签1,关联字典表")
    @TableField(value = "rhinocerosLabelId1")
    private String rhinocerosLabelId1;
    //创业公司
    @ApiModelProperty(value = "犀牛标签2，关联字典表")
    @TableField(value = "rhinocerosLabel2", exist = false)
    private String rhinocerosLabel2;
    //创业公司
    @ApiModelProperty(value = "犀牛标签2，关联字典表")
    @TableField(value = "rhinocerosLabelId2")
    private String rhinocerosLabelId2;

    //创业公司
    @ApiModelProperty(value = "犀牛标签3，关联字典表")
    @TableField(value = "rhinocerosLabel3", exist = false)
    private String rhinocerosLabel3;
    //创业公司
    @ApiModelProperty(value = "犀牛标签3，关联字典表")
    @TableField(value = "rhinocerosLabelId3")
    private String rhinocerosLabelId3;
    //创业公司
    @ApiModelProperty(value = "犀牛标签4，关联字典表")
    @TableField(value = "rhinocerosLabel4", exist = false)
    private String rhinocerosLabel4;
    //创业公司
    @ApiModelProperty(value = "犀牛标签4，关联字典表")
    @TableField(value = "rhinocerosLabelId4")
    private String rhinocerosLabelId4;
    //创业公司
    @ApiModelProperty(value = "犀牛标签5，关联字典表")
    @TableField(value = "rhinocerosLabel5", exist = false)
    private String rhinocerosLabel5;
    //创业公司
    @ApiModelProperty(value = "犀牛标签5，关联字典表")
    @TableField(value = "rhinocerosLabelId5")
    private String rhinocerosLabelId5;
    //创业公司
    @ApiModelProperty(value = "犀牛标签6，关联字典表")
    @TableField(value = "rhinocerosLabel6", exist = false)
    private String rhinocerosLabel6;
    //创业公司
    @ApiModelProperty(value = "犀牛标签6，关联字典表")
    @TableField(value = "rhinocerosLabelId6")
    private String rhinocerosLabelId6;*/


    //创业公司
    @ApiModelProperty(value = "首次投资时间")
    @TableField(value = "firstInvestmentTime")
    private String firstInvestmentTime;

    //创业公司
    @ApiModelProperty(value = "首次投资阶段，字典表")
    @TableField(value = "firstInvestmentStage", exist = false)
    private String firstInvestmentStage;
    //创业公司
    @ApiModelProperty(value = "首次投资阶段，字典表")
    @TableField(value = "firstInvestmentStageId")
    private String firstInvestmentStageId;

    //创业公司
    @ApiModelProperty(value = "是否领投，0否，1是")
    @TableField(value = "leadInvestment")
    private Integer leadInvestment;

    //lp
    @ApiModelProperty(value = "参与科创基金,关联字典表")
    @TableField(value = "participationFund", exist = false)
    private String participationFund;

    //lp
    @ApiModelProperty(value = "参与科创基金,关联字典表")
    @TableField(value = "participationFundId")
    private String participationFundId;

    //lp
    @ApiModelProperty(value = "1:导入，0：输入")
    @TableField(value = "flag")
    private String flag;

    //供应商
    @ApiModelProperty(value = "提供产品版块")
    @TableField(value = "productSection")
    private String productSection;
    //供应商
    @ApiModelProperty(value = "提供产品名称")
    @TableField(value = "productName")
    private String productName;

    //创业公司
    @ApiModelProperty(value = "注册省份")
    @TableField(value = "registrationProvince")
    private String registrationProvince;

    public Object[] getBasicInformation(){
        Object[] m = {this.getId(),this.enterpriseCode,this.enterpriseName,this.typeName,this.contact,
                this.phone,this.address,this.email};

        return m;
    }

    public Object[] getGPInformation(){
        String sh = "";
        if(this.SHOffice!=null&&!this.SHOffice.equals("")){
            if(this.SHOffice==0){
                sh = "无";
            }else if(this.SHOffice==1){
                sh = "有";
            }
        }
        Object[] m = {this.getId(),this.enterpriseCode,this.enterpriseName,this.typeName,this.contact,
                this.phone,this.email,this.pwd,this.abbreviation,this.investmentFund,this.address,sh,this.attentionIndustry,
                this.attentionStage,this.investmentManager};

        return m;
    }

    public Object[] getkechuangInformation(){
        Object[] m = {this.getId(),this.enterpriseCode,this.enterpriseName,this.typeName,this.contact,
                this.phone,this.address,this.email,this.pwd};

        return m;
    }

    public Object[] getcygsInformation(){
        String lead = "";
        if(this.leadInvestment!=null&&!this.leadInvestment.equals("")){
            if(this.leadInvestment==0){
                lead = "否";
            }else if(this.leadInvestment == 1){
                lead = "是";
            }
        }
        Object[] m = {this.getId(),this.enterpriseCode,this.enterpriseName,this.typeName,this.contact,
                this.phone,this.email,this.pwd,this.abbreviation,this.investmentFund,this.industryClassification,
                this.directInvestmentClassification,this.rhinocerosLabel,this.address1,this.address,this.investmentManager,this.firstInvestmentStage,lead,
                this.firstInvestmentTime};

        return m;
    }
    public Object[] getlpInformation(){
        Object[] m = {this.getId(),this.enterpriseCode,this.enterpriseName,this.abbreviation,this.typeName,this.contact,
                this.job,this.phone,this.email,this.pwd,this.participationFund};

        return m;
    }

}
