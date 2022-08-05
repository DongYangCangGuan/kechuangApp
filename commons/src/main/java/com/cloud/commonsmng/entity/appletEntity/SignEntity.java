package com.cloud.commonsmng.entity.appletEntity;

import java.util.List;

public class SignEntity {

    private String id;
    private String signId;
    private String  uName;
    private String activityEventId;//活动事件Id
    private String memberId;//公司
    private String enterpriseName;
    private String realName;

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    private String phone;//联系方式
    private String name;//联系人
    private String userId;//联系人
    private String remark;//报名备注
    private String creatorId;//创建者
    private String createTime;//创建时间
    private String modifyTime;//修改时间
    private String modifierId;//修改者
    private Integer readymade;//是否有现成产品
    private Integer customization;//是否可定制
    private String isused;//修改者
    private  ActivityEntity act;
    private  List<ActivityTemplate> activityTemplate;

    public List<ActivityTemplate> getActivityTemplate() {
        return activityTemplate;
    }

    public void setActivityTemplate(List<ActivityTemplate> activityTemplate) {
        this.activityTemplate = activityTemplate;
    }

    private List<SignDetail> sdList;

    private List<SignAppendixEntity> SignAppendixList;//是否可定制

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActivityEventId() {
        return activityEventId;
    }

    public void setActivityEventId(String activityEventId) {
        this.activityEventId = activityEventId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getModifierId() {
        return modifierId;
    }

    public void setModifierId(String modifierId) {
        this.modifierId = modifierId;
    }

    public Integer getReadymade() {
        return readymade;
    }

    public void setReadymade(Integer readymade) {
        this.readymade = readymade;
    }

    public Integer getCustomization() {
        return customization;
    }

    public void setCustomization(Integer customization) {
        this.customization = customization;
    }

    public List<SignAppendixEntity> getSignAppendixList() {
        return SignAppendixList;
    }

    public void setSignAppendixList(List<SignAppendixEntity> signAppendixList) {
        SignAppendixList = signAppendixList;
    }

    public String getIsused() {
        return isused;
    }

    public void setIsused(String isused) {
        this.isused = isused;
    }

    public ActivityEntity getAct() {
        return act;
    }

    public void setAct(ActivityEntity act) {
        this.act = act;
    }

    public List<SignDetail> getSdList() {
        return sdList;
    }

    public void setSdList(List<SignDetail> sdList) {
        this.sdList = sdList;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getSignId() {
        return signId;
    }

    public void setSignId(String signId) {
        this.signId = signId;
    }


}

