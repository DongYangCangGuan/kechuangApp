package com.cloud.serviceauthorize.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ： lijun
 * @date ：Created in 2019/3/27 11:29 PM
 * @description：
 * @modified By：
 * @version: $
 */

public class Menu {

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String path;

    private String component;

    private  boolean hidden;

    private  Meta meta=new Meta();

    private  String name;


    public List<Menu> getChildren() {
        return children;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }

    private List<Menu> children=new ArrayList<>();

}
