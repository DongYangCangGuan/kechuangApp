package com.cloud.servicemanage.entity.weixin;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author:cat
 * @Description 文本消息model
 * @Date: 2018-04-12  10:00
 * @Modified By:
 */
public class TextMessageRespModel extends BaseMessageRespModel {

    public enum TextMessageRespModelMapper implements RowMapper<TextMessageRespModel> {
        INSTANCE;
        @Override
        public TextMessageRespModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            TextMessageRespModel textMessageRespModel = new TextMessageRespModel();
            textMessageRespModel.setContent(rs.getString("content"));

            return textMessageRespModel;
        }
    }

    public Integer getFuncFlag() {
      return FuncFlag;
    }

    public void setFuncFlag(Integer funcFlag) {
      FuncFlag = funcFlag;
    }

    private Integer FuncFlag;
    private String Content;// 回复的消息内容

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        this.Content = content;
    }
}
