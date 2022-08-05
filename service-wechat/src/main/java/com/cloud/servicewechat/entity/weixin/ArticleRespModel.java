package com.cloud.servicewechat.entity.weixin;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author:cat
 * @Description 图文model
 * @Date: 2018-04-12  10:00
 * @Modified By:
 */
public class ArticleRespModel {

    public enum ArticleRespModelMapper implements RowMapper<ArticleRespModel> {
        INSTANCE;
        @Override
        public ArticleRespModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            ArticleRespModel articleRespModel = new ArticleRespModel();
            articleRespModel.setTitle(rs.getString("title"));
            articleRespModel.setDescription(rs.getString("description"));
            articleRespModel.setPicUrl(rs.getString("picUrl"));
            articleRespModel.setUrl(rs.getString("url"));

            return articleRespModel;
        }
    }

    private String title;// 图文消息名称
    private String description;// 图文消息描述
    private String picUrl;// 图片链接，支持JPG、PNG格式，较好的效果为大图640*320，小图80*80，限制图片链接的域名需要与开发者填写的基本资料中的Url一致
    private String url;// 点击图文消息跳转链接

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
