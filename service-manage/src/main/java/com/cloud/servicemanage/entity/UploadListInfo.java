package com.cloud.servicemanage.entity;

public class UploadListInfo {
    private String FileName;
    private String VersionInfo;
    private String ZipFileName;

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getVersionInfo() {
        return VersionInfo;
    }

    public void setVersionInfo(String versionInfo) {
        VersionInfo = versionInfo;
    }

    public String getZipFileName() {
        return ZipFileName;
    }

    public void setZipFileName(String zipFileName) {
        ZipFileName = zipFileName;
    }
}
