package com.cloud.commonsmng.activiti.model;

public class ActivitiUser {
    private String name;
    private String password;
    private String group;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    /**
     * 构造函数，用户restful调用basic auth
     *
     * @param userName
     * @param password
     */
    public ActivitiUser(String userName, String password) {
        this.name = userName;
        this.password = password;
    }

    /**
     * 构造函数，用户activiti流程引擎注册用户
     *
     * @param userName
     * @param password
     * @param roles
     */
    public ActivitiUser(String userName, String password, String roles) {
        this.name = userName;
        this.password = password;
        this.group = roles;
    }
}
