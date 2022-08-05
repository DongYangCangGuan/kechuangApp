package com.cloud.servicewechat.entity.weixin;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @Author:cat
 * @Description 多条图文消息model
 * @Date: 2018-04-12  10:00
 * @Modified By:
 */
public class NewsMessageRespModel extends BaseMessageRespModel {

    public enum NewsMessageRespModelMapper implements RowMapper<NewsMessageRespModel> {
        INSTANCE;
        @Override
        public NewsMessageRespModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            NewsMessageRespModel newsMessageRespModel = new NewsMessageRespModel();
            newsMessageRespModel.setArticleCount(rs.getInt("articleCount"));

            return newsMessageRespModel;
        }
    }

    private int ArticleCount;// 图文消息个数，限制为10条以内

  public List<ArticleRespModel> getArticles() {
    return Articles;
  }

  public void setArticles(List<ArticleRespModel> articles) {
    Articles = articles;
  }

  private List<ArticleRespModel> Articles;// 多条图文消息信息，默认第一个item为大图


    public int getArticleCount() {
        return ArticleCount;
    }

    public void setArticleCount(int articleCount) {
        this.ArticleCount = articleCount;
    }
//
//    public List<ArticleRespModel> getArticleRespModelList() {
//        return Articles;
//    }
//
//    public void setArticleRespModelList(List<ArticleRespModel> articleRespModelList) {
//        this.Articles = articleRespModelList;
//    }
}
