package com.cloud.servicemanage.entity;

import java.io.Serializable;

public class userDpt implements Serializable {
    private String id;
    private String departmentid;
    private String departmentname;
    private String entrytime;
    private String userstatus;
    private String userstatusname;
    private String userid;
    private String status;

    public void setDepartmentname(String departmentname) {
        this.departmentname = departmentname;
    }

    public void setUserstatusname(String userstatusname) {
        this.userstatusname = userstatusname;
    }

    public String getDepartmentname() {
        return departmentname;
    }

    public String getUserstatusname() {
        return userstatusname;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDepartmentid(String departmentid) {
        this.departmentid = departmentid;
    }

    public void setEntrytime(String entrytime) {
        this.entrytime = entrytime;
    }

    public void setUserstatus(String userstatus) {
        this.userstatus = userstatus;
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

    public String getDepartmentid() {
        return departmentid;
    }

    public String getEntrytime() {
        return entrytime;
    }

    public String getUserstatus() {
        return userstatus;
    }

    public String getUserid() {
        return userid;
    }

    public String getStatus() {
        return status;
    }
}
