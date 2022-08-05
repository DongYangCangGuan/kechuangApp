package com.cloud.servicemanage.entity;

import java.util.List;

public class FileImport {
    private String id;
    private String filepath;
    private String batchid;
    private String creatorid;
    private String createtime;
    private String modifierid;
    private String modifytime;
    private List<FileImportDetail> fileImportDetailList;

    public List<FileImportDetail> getFileImportDetailList() {
        return fileImportDetailList;
    }

    public void setFileImportDetailList(List<FileImportDetail> fileImportDetailList) {
        this.fileImportDetailList = fileImportDetailList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getBatchid() {
        return batchid;
    }

    public void setBatchid(String batchid) {
        this.batchid = batchid;
    }

    public String getCreatorid() {
        return creatorid;
    }

    public void setCreatorid(String creatorid) {
        this.creatorid = creatorid;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getModifierid() {
        return modifierid;
    }

    public void setModifierid(String modifierid) {
        this.modifierid = modifierid;
    }

    public String getModifytime() {
        return modifytime;
    }

    public void setModifytime(String modifytime) {
        this.modifytime = modifytime;
    }
}
