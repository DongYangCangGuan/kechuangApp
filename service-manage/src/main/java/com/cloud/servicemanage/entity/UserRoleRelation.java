package com.cloud.servicemanage.entity;

public class UserRoleRelation {
    private String id;
    private String name;
    private String userid;
    private String roleid;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUserid() {
        return userid;
    }

    public String getRoleid() {
        return roleid;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }
}
