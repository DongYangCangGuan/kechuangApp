package com.cloud.commonsmng.entity.appletEntity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cloud.commonsmng.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("member_detail")
@ApiModel(value = "审批", description = "审批")
public class Approve extends BaseEntity {


    @ApiModelProperty(value = "用户Id")
    @TableField("memberId")
    private String memberId;

    @ApiModelProperty(value = "问题Id")
    @TableField("userId")
    private String userId;

    @ApiModelProperty(value = "回答内容")
    @TableField("membersonid")
    private String membersonid;

    @ApiModelProperty(value = "公司Id")
    @TableField("approvalstatus")
    private String approvalstatus;

    @ApiModelProperty(value = "删除标志")
    @TableField("delFlag")
    private String delFlag;

}
