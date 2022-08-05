package com.cloud.servicemanage.entity;
import com.cloud.servicemanage.common.PageUtil;

/**
 *@author fyy
 */
public class RequestParamPageVo<T> {
    private T requestParam;
    private PageUtil pageUtil;

    public T getRequestParam() {
        return requestParam;
    }

    public void setRequestParam(T requestParam) {
        this.requestParam = requestParam;
    }

    public PageUtil getPageUtil() {
        return pageUtil;
    }

    public void setPageUtil(PageUtil pageUtil) {
        this.pageUtil = pageUtil;
    }
}
