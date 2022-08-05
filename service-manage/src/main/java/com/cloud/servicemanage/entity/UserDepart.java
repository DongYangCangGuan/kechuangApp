package com.cloud.servicemanage.entity;

public class UserDepart {
    private String id;
    private String departmentid;
    private String entrytime;
    private String transferdpt;
    private String transfertime;
    private String quittime;
    private String retiretime;
    private String userstatus;
    private String userstatusname;
    private String userid;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private String name;

    public String getDepartmentname() {
        return departmentname;
    }

    public void setDepartmentname(String departmentname) {
        this.departmentname = departmentname;
    }

    private String departmentname;

    public String getId() {
        return id;
    }

    public String getDepartmentid() {
        return departmentid;
    }

    public String getEntrytime() {
        return entrytime;
    }

    public String getTransferdpt() {
        return transferdpt;
    }

    public String getTransfertime() {
        return transfertime;
    }

    public String getQuittime() {
        return quittime;
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

    public void setTransferdpt(String transferdpt) {
        this.transferdpt = transferdpt;
    }

    public void setTransfertime(String transfertime) {
        this.transfertime = transfertime;
    }

    public void setQuittime(String quittime) {
        this.quittime = quittime;
    }

    public void setRetiretime(String retiretime) {
        this.retiretime = retiretime;
    }

    public void setUserstatus(String userstatus) {
        this.userstatus = userstatus;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getRetiretime() {
        return retiretime;
    }

    public String getUserstatus() {
        return userstatus;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserstatusname(String userstatusname) {
        this.userstatusname = userstatusname;
    }

    public String getUserstatusname() {
        return userstatusname;
    }
}
