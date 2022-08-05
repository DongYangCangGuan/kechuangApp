package com.cloud.commonsmng.entity.appletEntity;

import com.cloud.commonsmng.entity.BaseEntity;

import java.util.List;

public class ActivityEntity extends BaseEntity {

    private String id;
    private String realName;
    private String signId;
    private String code;//活动代码
    private String title;//活动标题
    private String pic;//活动图片路径
    private String description;//活动介绍
    private String url;//活动的pdf路径
    private Integer signTolNum;//活动的pdf路径

    public Integer getSignTolNum() {
        return signTolNum;
    }

    public void setSignTolNum(Integer signTolNum) {
        this.signTolNum = signTolNum;
    }

    private  List<SignEntity> signEntity;

    public String getIsused() {
        return isused;
    }

    public void setIsused(String isused) {
        this.isused = isused;
    }

    private String name;//活动的pdf路径
    private Integer thumbUpNumber;//活动点赞次数
    private Integer articleviews;//活动收藏次数
    private String artStartTime;//活动开始时间
    private String artEndTime;//活动结束时间
    private String isused;//是否有效 1-是 0-否
    private String delflag;

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    private String activityId;
    private Integer delFlag;//是否删除 1-是 0-否
    private SignEntity sign;
    private String template;
    private String seq;
    private String  activityTemId;
    private  List<ActivityTemplate> activityTemplate;

    public String getDelflag() {
        return delflag;
    }

    public void setDelflag(String delflag) {
        this.delflag = delflag;
    }

    public List<ActivityTemplate> getActivityTemplate() {
        return activityTemplate;
    }

    public void setActivityTemplate(List<ActivityTemplate> activityTemplate) {
        this.activityTemplate = activityTemplate;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public List<SignEntity> getSignEntity() {
        return signEntity;
    }

    public void setSignEntity(List<SignEntity> signEntity) {
        this.signEntity = signEntity;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getActivityTemId() {
        return activityTemId;
    }

    public void setActivityTemId(String activityTemId) {
        this.activityTemId = activityTemId;
    }

    private  List<ActivityTemplate>  acList;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getThumbUpNumber() {
        return thumbUpNumber;
    }

    public void setThumbUpNumber(Integer thumbUpNumber) {
        this.thumbUpNumber = thumbUpNumber;
    }

    public Integer getArticleviews() {
        return articleviews;
    }

    public void setArticleviews(Integer articleviews) {
        this.articleviews = articleviews;
    }

    public String getArtStartTime() {
        return artStartTime;
    }

    public void setArtStartTime(String artStartTime) {
        this.artStartTime = artStartTime;
    }

    public String getArtEndTime() {
        return artEndTime;
    }

    public void setArtEndTime(String artEndTime) {
        this.artEndTime = artEndTime;
    }



    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public List<ActivityTemplate> getAcList() {
        return acList;
    }

    public void setAcList(List<ActivityTemplate> acList) {
        this.acList = acList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SignEntity getSign() {
        return sign;
    }

    public void setSign(SignEntity sign) {
        this.sign = sign;
    }

    public String getSignId() {
        return signId;
    }

    public void setSignId(String signId) {
        this.signId = signId;
    }
}

