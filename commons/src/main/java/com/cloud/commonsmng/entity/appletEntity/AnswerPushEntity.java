package com.cloud.commonsmng.entity.appletEntity;

public class AnswerPushEntity {

    private String id;
    private String userId;//用户
    private String isPush;//删除标志
    private String questionBelong;//问题归属
    private String creatorId;//创建者
    private String createTime;//创建时间
    private String modifyTime;//修改时间
    private String modifierId;//修改者


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIsPush() {
        return isPush;
    }

    public void setIsPush(String isPush) {
        this.isPush = isPush;
    }

    public String getQuestionBelong() {
        return questionBelong;
    }

    public void setQuestionBelong(String questionBelong) {
        this.questionBelong = questionBelong;
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
}

