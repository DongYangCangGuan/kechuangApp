package com.cloud.servicemanage.entity.weixin;

public class weChartUserInfo {
    private String equestUrl;//请求地址
    private String requestMethod; //请求方式
    private String outputStr; //返回参数

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEquestUrl() {
        return equestUrl;
    }

    public void setEquestUrl(String equestUrl) {
        this.equestUrl = equestUrl;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getOutputStr() {
        return outputStr;
    }

    public void setOutputStr(String outputStr) {
        this.outputStr = outputStr;
    }
}
