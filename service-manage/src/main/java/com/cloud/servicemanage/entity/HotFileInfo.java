package com.cloud.servicemanage.entity;

public class HotFileInfo {
    private String Id;
    private Integer FileNum;
    private String Status;
    private Integer SftpfileNum;
    private String FilePath;
    private String SftpFilePath;
    private String FileName;
    private String CreateBy;
    private String VersionInfo;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public Integer getFileNum() {
        return FileNum;
    }

    public void setFileNum(Integer fileNum) {
        FileNum = fileNum;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public Integer getSftpfileNum() {
        return SftpfileNum;
    }

    public void setSftpfileNum(Integer sftpfileNum) {
        SftpfileNum = sftpfileNum;
    }

    public String getFilePath() {
        return FilePath;
    }

    public void setFilePath(String filePath) {
        FilePath = filePath;
    }

    public String getSftpFilePath() {
        return SftpFilePath;
    }

    public void setSftpFilePath(String sftpFilePath) {
        SftpFilePath = sftpFilePath;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getCreateBy() {
        return CreateBy;
    }

    public void setCreateBy(String createBy) {
        CreateBy = createBy;
    }

    public String getVersionInfo() {
        return VersionInfo;
    }

    public void setVersionInfo(String versionInfo) {
        VersionInfo = versionInfo;
    }

}

