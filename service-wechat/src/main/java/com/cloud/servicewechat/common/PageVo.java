package com.cloud.servicewechat.common;
import java.util.List;

/**
 *@author fyy
 */
public class PageVo<T> {
    private PageUtil page;
    private String userId;



    private String uRole;
    private List<T> dataList;
    private  T searchdata;


    public T getSearchdata() {
        return searchdata;
    }

    public void setSearchdata(T searchdata) {
        this.searchdata = searchdata;
    }

    public PageUtil getPage() {
        return page;
    }

    public void setPage(PageUtil page) {
        this.page = page;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    public String getUserId() { return userId; }

    public void setUserId(String userId) { this.userId = userId; }

    public String getuRole() { return uRole; }

    public void setuRole(String uRole) {this.uRole = uRole;}
}
