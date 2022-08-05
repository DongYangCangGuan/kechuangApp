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
 * 咨询信息表
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("consultingInfo")
@ApiModel(value = "ConsultingInfo", description = "咨询信息表")
public class ConsultingInfo extends BaseEntity {

    @ApiModelProperty(value = "用户Id")
    @TableField("userId")
    private String userId;

    @ApiModelProperty(value = "客户经理")
    @TableField("accountmanager")
    private String accountmanager;

    @ApiModelProperty(value = "联系方式")
    @TableField("telephone")
    private String telephone;

    @ApiModelProperty(value = "邮箱")
    @TableField("email")
    private String email;

    @ApiModelProperty(value = "用户姓名关联user表")
    @TableField(value = "realName",exist = false)
    private String realName;

    @ApiModelProperty(value = "用户联系方式")
    @TableField(value = "phone",exist = false)
    private String phone;

    @ApiModelProperty(value = "公司Id")
    @TableField(value = "memberId",exist = false)
    private String memberId;

    @ApiModelProperty(value = "公司名称")
    @TableField(value = "memberName",exist = false)
    private String memberName;

}
