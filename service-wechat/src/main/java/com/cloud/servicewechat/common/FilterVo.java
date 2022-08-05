package com.cloud.servicewechat.common;

/**
 * @program: applet
 * @description: 用于筛选的实现
 * @author: chen.cq(chen.caoqing)
 * @create: 2021-05-17 15:24
 **/
public class FilterVo {

    private int start;  //筛选的开始时间
    private int end;    //筛选的结束时间
    private PageVo pageVo;

    public FilterVo(int start, int end, PageVo pageVo) {
        this.start = start;
        this.end = end;
        this.pageVo = pageVo;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public PageVo getPageVo() {
        return pageVo;
    }

    public void setPageVo(PageVo pageVo) {
        this.pageVo = pageVo;
    }
}
