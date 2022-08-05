package com.cloud.servicemanage.entity;

import com.cloud.commonsmng.entity.appletEntity.User;
import java.util.List;

public class Department extends BaseEntity {
    private String code;
    private String name;
    private String address;
    private String parentid;
    private String parentname;
    private boolean isused;
    private String treeabout;
    private String level;
    private String creatorname;
    private String modifiername;

    List<Department> childrens;

    List<User> children;

    public List<Department> getChildrens() {
        return childrens;
    }

    public void setChildrens(List<Department> childrens) {
        this.childrens = childrens;
    }

    public List<User> getChildren() {
        return children;
    }

    public void setChildren(List<User> children) {
        this.children = children;
    }

    public void setCreatorname(String creatorname) {
        this.creatorname = creatorname;
    }

    public void setModifiername(String modifiername) {
        this.modifiername = modifiername;
    }

    public String getCreatorname() {
        return creatorname;
    }

    public String getModifiername() {
        return modifiername;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public String getParentname() {
        return parentname;
    }

    public void setParentname(String parentname) {
        this.parentname = parentname;
    }

    public boolean isIsused() {
        return isused;
    }

    public void setIsused(boolean isused) {
        this.isused = isused;
    }

    public String getTreeabout() {
        return treeabout;
    }

    public void setTreeabout(String treeabout) {
        this.treeabout = treeabout;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
