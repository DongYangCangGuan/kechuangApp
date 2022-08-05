package com.cloud.serviceauthorize.entity;

import com.cloud.commonsmng.entity.BaseRole;

import java.util.List;

public class BaseUser {

    private String userid;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRooturl() {
        return rooturl;
    }

    public void setRooturl(String rooturl) {
        this.rooturl = rooturl;
    }



    public List<BaseRole> getListrole() {
        return listrole;
    }

    public void setListrole(List<BaseRole> listrole) {
        this.listrole = listrole;
    }

    private List<BaseRole> listrole;
    private String username;
    private String rooturl;

    public String getImg_URL() {
        return img_URL;
    }

    public void setImg_URL(String img_URL) {
        this.img_URL = img_URL;
    }

    private String img_URL;
}
