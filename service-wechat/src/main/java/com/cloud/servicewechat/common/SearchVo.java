package com.cloud.servicewechat.common;

/**
 * @program: applet
 * @description: 搜索框搜索相关研报列表信息
 * @author: lu.wh(lu.wenhua)
 * @create: 2021-05-17 16:30
 **/
public class SearchVo {

    private String searchValue;  //搜索框内容
    private PageVo pageVo;

    public SearchVo(String searchInput, PageVo pageVo) {
        this.searchValue = searchInput;
        this.pageVo = pageVo;
    }

    public String getSearchInput() {
        return searchValue;
    }

    public void setSearchInput(String searchInput) {
        this.searchValue = searchInput;
    }

    public PageVo getPageVo() {
        return pageVo;
    }

    public void setPageVo(PageVo pageVo) {
        this.pageVo = pageVo;
    }

}
