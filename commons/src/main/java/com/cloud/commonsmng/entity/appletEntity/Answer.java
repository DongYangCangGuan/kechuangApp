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
import java.util.List;

@Data
    @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    @TableName("answer")
    @ApiModel(value = "问卷答案对象", description = "我的问卷")
    public class Answer extends BaseEntity {

        @ApiModelProperty(value = "用户Id")
        @TableField("userId")
        private String userId;

        @ApiModelProperty(value = "问题Id")
        @TableField("questionId")
        private String questionId;

        @ApiModelProperty(value = "回答内容")
        @TableField("answer")
        private String answer;

        @ApiModelProperty(value = "公司Id")
        @TableField("memberid")
        private String memberid;

        @ApiModelProperty(value = "删除标志")
        @TableField("delFlag")
        private String delFlag;

        @ApiModelProperty(value = "问题描述")
        @TableField("questionDescription")
        private String questionDescription;

        @ApiModelProperty(value = "删除标志")
        @TableField("content")
        private String content;

        @ApiModelProperty(value = "问题类型")
        @TableField("questionType")
         private String questionType;

        @ApiModelProperty(value = "回答")
        @TableField("questionOptionsList")
        private List<QuestionOptionsEntity> questionOptionsList;
    }
