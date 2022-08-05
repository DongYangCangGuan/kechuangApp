package com.cloud.servicewechat.entity.weixin;

import java.util.List;

/**
 * @Author:cat
 * @Description 模板消息model
 * @Date: 2018-04-12  10:00
 * @Modified By:
 */
public class TemplateMessageModel {

	private String accessToken;//微信token
	private String openId;//推送用户的openId
	private String url;//模板消息跳转链接
	private String templateId;//模板消息ID
	private String first;//模板消息first
	private List<String> keywords;//模板消息keyword集合
	private String remark;//模板消息remark

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
