package com.cloud.servicemanage.entity;

import java.io.Serializable;

public class userEdu implements Serializable {
    private String id;
    private String education;
    private String educationname;
    private String major;
    private String starttime;
    private String endtime;
    private String userid;
    private String status;

    public void setEducationname(String educationname) {
        this.educationname = educationname;
    }

    public String getEducationname() {
        return educationname;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getEducation() {
        return education;
    }

    public String getMajor() {
        return major;
    }

    public String getStarttime() {
        return starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public String getUserid() {
        return userid;
    }

    public String getStatus() {
        return status;
    }
}
