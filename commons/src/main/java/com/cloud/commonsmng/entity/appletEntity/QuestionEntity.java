package com.cloud.commonsmng.entity.appletEntity;

import java.util.List;

public class QuestionEntity {

    private String id;
    private String questionDescription;//问题描述
    private String delFlag;//1存在 0删除
    private String sequen;//排序
    private String releaseFlag;//发布标志 1已发布 0未发布
    private String questionname;//问卷名
    private String questionType;//问题类型 1多选0单选
    private String answer;
    private String creatorId;//创建者
    private String createTime;//创建时间

    public List<QuestionOptionsEntity> getMyquestion() {
        return myquestion;
    }

    public void setMyquestion(List<QuestionOptionsEntity> myquestion) {
        this.myquestion = myquestion;
    }

    private String modifyTime;//修改时间
    private String modifierId;//修改者
    private List<QuestionOptionsEntity> myquestion;

    private List<QuestionOptionsEntity> questionOptionsList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionDescription() {
        return questionDescription;
    }

    public void setQuestionDescription(String questionDescription) {
        this.questionDescription = questionDescription;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getSequen() {
        return sequen;
    }

    public void setSequen(String sequen) {
        this.sequen = sequen;
    }

    public String getReleaseFlag() {
        return releaseFlag;
    }

    public void setReleaseFlag(String releaseFlag) {
        this.releaseFlag = releaseFlag;
    }

    public String getQuestionname() {
        return questionname;
    }

    public void setQuestionname(String questionname) {
        this.questionname = questionname;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
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

    public List<QuestionOptionsEntity> getQuestionOptionsList() {
        return questionOptionsList;
    }

    public void setQuestionOptionsList(List<QuestionOptionsEntity> questionOptionsList) {
        this.questionOptionsList = questionOptionsList; }

    public String getAnswer() { return answer; }

    public void setAnswer(String answer) { this.answer = answer; }
}

