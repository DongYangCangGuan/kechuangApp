package com.cloud.servicewechat.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cloud.commonsmng.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("push")
public class PushEntity extends BaseEntity {

  private String pushMessageId;
  private String templateType;
  private String title;
  private String keywords1;
  private String keywords2;
  private String keywords3;
  private String keywords4;
  private String keywords5;
  private String remark;
  private String url;
  private Integer status;//发布状态 0未发布，1已发布
  private Integer delFlag;//0删除，1未删除
  private String appId;//小程序id //wxce8aacafe36942d1
}
