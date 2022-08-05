package com.cloud.servicemanage.entity;

public class HotLoad {

    private String  ID;
    private String SftpIP;
    private String SftpPort;
    private String OrgID;
    private String Environment;
    private String System;
    private String SftpPath;
    private String LoginName;
    private String Password;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getSftpIP() {
        return SftpIP;
    }

    public void setSftpIP(String sftpIP) {
        SftpIP = sftpIP;
    }

    public String getSftpPort() {
        return SftpPort;
    }

    public void setSftpPort(String sftpPort) {
        SftpPort = sftpPort;
    }

    public String getOrgID() {
        return OrgID;
    }

    public void setOrgID(String orgID) {
        OrgID = orgID;
    }

    public String getEnvironment() {
        return Environment;
    }

    public void setEnvironment(String environment) {
        Environment = environment;
    }

    public String getSystem() {
        return System;
    }

    public void setSystem(String system) {
        System = system;
    }

    public String getSftpPath() {
        return SftpPath;
    }

    public void setSftpPath(String sftpPath) {
        SftpPath = sftpPath;
    }

    public String getLoginName() {
        return LoginName;
    }

    public void setLoginName(String loginName) {
        LoginName = loginName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }


}
