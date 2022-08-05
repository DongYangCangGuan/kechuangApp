package com.cloud.commonsmng.entity;

public class Loginfo {

    public Loginfo(){

    }
    private String ID;
    //操作用户
    private String USERID;
    //日志记录时间
    private String RECIVETIME;
    //日志属于哪一模块
    private String MODULE;
    //日志级别
    private String LOG_LEVEL;
    //调用方法
    private String FUNCTION;
    //入参
    private String INPUTPARAM;
    //出参
    private String OUTPUTPARAM;

    public String getCLASS() {
        return CLASS;
    }

    public void setCLASS(String CLASS) {
        this.CLASS = CLASS;
    }

    private String CLASS;
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }

    public String getRECIVETIME() {
        return RECIVETIME;
    }

    public void setRECIVETIME(String RECIVETIME) {
        this.RECIVETIME = RECIVETIME;
    }

    public String getMODULE() {
        return MODULE;
    }

    public void setMODULE(String MODULE) {
        this.MODULE = MODULE;
    }

    public String getLOG_LEVEL() {
        return LOG_LEVEL;
    }

    public void setLOG_LEVEL(String LOG_LEVEL) {
        this.LOG_LEVEL = LOG_LEVEL;
    }

    public String getFUNCTION() {
        return FUNCTION;
    }

    public void setFUNCTION(String FUNCTION) {
        this.FUNCTION = FUNCTION;
    }

    public String getINPUTPARAM() {
        return INPUTPARAM;
    }

    public void setINPUTPARAM(String INPUTPARAM) {
        this.INPUTPARAM = INPUTPARAM;
    }

    public String getOUTPUTPARAM() {
        return OUTPUTPARAM;
    }

    public void setOUTPUTPARAM(String OUTPUTPARAM) {
        this.OUTPUTPARAM = OUTPUTPARAM;
    }

    public String getERRORMSG() {
        return ERRORMSG;
    }

    public void setERRORMSG(String ERRORMSG) {
        this.ERRORMSG = ERRORMSG;
    }

    public String getCLIENT_IP() {
        return CLIENT_IP;
    }

    public void setCLIENT_IP(String CLIENT_IP) {
        this.CLIENT_IP = CLIENT_IP;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getACTIONTYPE() {
        return ACTIONTYPE;
    }

    public void setACTIONTYPE(String ACTIONTYPE) {
        this.ACTIONTYPE = ACTIONTYPE;
    }

    public String getTHREAD() {
        return THREAD;
    }

    public void setTHREAD(String THREAD) {
        this.THREAD = THREAD;
    }


    public String getRUNTIME() {
        return RUNTIME;
    }

    public void setRUNTIME(String RUNTIME) {
        this.RUNTIME = RUNTIME;
    }

    public String getOTHERPARAM() {
        return OTHERPARAM;
    }

    public void setOTHERPARAM(String OTHERPARAM) {
        this.OTHERPARAM = OTHERPARAM;
    }

    public String getJARNAME() {
        return JARNAME;
    }

    public void setJARNAME(String JARNAME) {
        this.JARNAME = JARNAME;
    }

    private String RUNTIME;

    private String OTHERPARAM;

    //JAR包名
    private String JARNAME;
    //错误信息
    private String ERRORMSG;
    //服务器IP
    private String CLIENT_IP;
    //类型 redis app、pc、redis
    private String TYPE;
    //操作类型 0，登录 1，调用
    private String ACTIONTYPE;
    //线程
    private String THREAD;

    public String getDESCRIB() {
        return DESCRIB;
    }

    public void setDESCRIB(String DESCRIB) {
        this.DESCRIB = DESCRIB;
    }

    private String DESCRIB;


    private String startDate;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    private String endDate;

    private int page;

    private int pageSize;

}
