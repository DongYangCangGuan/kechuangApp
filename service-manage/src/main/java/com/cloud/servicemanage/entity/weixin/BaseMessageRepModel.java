package com.cloud.servicemanage.entity.weixin;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author:cat
 * @Description 消息基类（普通用户 -> 公众帐号）
 * @Date: 2018-04-12  10:00
 * @Modified By:
 */
public class BaseMessageRepModel {

    public enum BaseMessageRepModelMapper implements RowMapper<BaseMessageRepModel> {
        INSTANCE;
        @Override
        public BaseMessageRepModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            BaseMessageRepModel baseMessageRepModel = new BaseMessageRepModel();
            baseMessageRepModel.setToUserName(rs.getString("toUserName"));
            baseMessageRepModel.setFromUserName(rs.getString("fromUserName"));
            baseMessageRepModel.setCreateTime(rs.getLong("createTime"));
            baseMessageRepModel.setMsgType(rs.getString("msgType"));
            baseMessageRepModel.setMsgId(rs.getLong("msgId"));

            return baseMessageRepModel;
        }
    }

    private String toUserName;// 开发者微信号
    private String fromUserName;// 发送方帐号（一个OpenID）
    private long createTime;// 消息创建时间 （整型）
    private String msgType;// 消息类型（text/image/location/link）
    private long msgId;// 消息id，64位整型

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }
}
