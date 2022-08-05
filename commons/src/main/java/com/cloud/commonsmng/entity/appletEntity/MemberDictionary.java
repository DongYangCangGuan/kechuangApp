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
 * 会员套餐报告属性关联表
 * author: tjs
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("member_dictionary")
@ApiModel(value = "MemberDictionary对象",description = "会员套餐报告属性关联表")
public class MemberDictionary extends BaseEntity {

    @ApiModelProperty(value = "会员编号（外键，关联Member表）")
    @TableField("memberId")
    private String memberId;

    @ApiModelProperty(value = "报告属性编号")
    @TableField("reportKind")
    private String reportKind;

    @ApiModelProperty(value = "报告属性的Kind值")
    @TableField("propertyKind")
    private String propertyKind;
}
