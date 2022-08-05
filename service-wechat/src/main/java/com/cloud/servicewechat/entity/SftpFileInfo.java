package com.cloud.servicewechat.entity;

public class SftpFileInfo {
    private String Id;
    private String Permissions;
    private String FileNum;
    private String UserName;
    private String UserGroup;
    private String FileSize;
    private String Date;
    private String File;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getPermissions() {
        return Permissions;
    }

    public void setPermissions(String permissions) {
        Permissions = permissions;
    }

    public String getFileNum() {
        return FileNum;
    }

    public void setFileNum(String fileNum) {
        FileNum = fileNum;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserGroup() {
        return UserGroup;
    }

    public void setUserGroup(String userGroup) {
        UserGroup = userGroup;
    }

    public String getFileSize() {
        return FileSize;
    }

    public void setFileSize(String fileSize) {
        FileSize = fileSize;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getFile() {
        return File;
    }

    public void setFile(String file) {
        File = file;
    }
}

