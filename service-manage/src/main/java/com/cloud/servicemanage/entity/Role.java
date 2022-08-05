package com.cloud.servicemanage.entity;

import com.cloud.commonsmng.entity.BaseRole;

public class Role extends BaseRole {

    private String id;
    private String code;
    private String name;
    private String creatorid;
    private String createtime;
    private String modifierid;
    private String modifytime;
    private boolean isused;
    private String username;
    private String area;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean getIsused() {
        return isused;
    }

    public void setIsused(boolean isused) {
        this.isused = isused;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatorid() {
        return creatorid;
    }

    public void setCreatorid(String creatorId) {
        this.creatorid = creatorId;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createTime) {
        this.createtime = createTime;
    }

    public String getModifierid() {
        return modifierid;
    }

    public void setModifierid(String modifierId) {
        this.modifierid = modifierId;
    }

    public String getModifytime() {
        return modifytime;
    }

    public void setModifytime(String modifyTime) {
        this.modifytime = modifytime;
    }

}
