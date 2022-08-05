package com.cloud.servicemanage.entity.weixin;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author:cat
 * @Description 消息基类（公众帐号 -> 普通用户）
 * @Date: 2018-04-12  10:00
 * @Modified By:
 */
public class BaseMessageRespModel {

    public enum BaseMessageRespModelMapper implements RowMapper<BaseMessageRespModel> {
        INSTANCE;
        @Override
        public BaseMessageRespModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            BaseMessageRespModel baseMessageRespModel = new BaseMessageRespModel();
            baseMessageRespModel.setToUserName(rs.getString("toUserName"));
            baseMessageRespModel.setFromUserName(rs.getString("fromUserName"));
            baseMessageRespModel.setCreateTime(rs.getLong("createTime"));
            baseMessageRespModel.setMsgType(rs.getString("msgType"));
            //baseMessageRespModel.setFuncFlag(rs.getInt("funcFlag"));

            return baseMessageRespModel;
        }
    }


    private String ToUserName;// 接收方帐号（收到的OpenID）
    private String FromUserName;// 开发者微信号
    private long CreateTime;// 消息创建时间 （整型）
    private String MsgType;// 消息类型（text/music/news）
    //private int funcFlag;// 位0x0001被标志时，星标刚收到的消息

    public String getToUserName() {
        return ToUserName;
    }

    public void setToUserName(String toUserName) {
        this.ToUserName = toUserName;
    }

    public String getFromUserName() {
        return FromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.FromUserName = fromUserName;
    }

    public long getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(long createTime) {
        this.CreateTime = createTime;
    }

    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String msgType) {
        this.MsgType = msgType;
    }

//    public int getFuncFlag() {
//        return funcFlag;
//    }
//
//    public void setFuncFlag(int funcFlag) {
//        this.funcFlag = funcFlag;
//    }
}
