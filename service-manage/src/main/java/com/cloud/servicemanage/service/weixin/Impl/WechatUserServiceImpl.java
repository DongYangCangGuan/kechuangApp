package com.cloud.servicemanage.service.weixin.Impl;

import com.alibaba.fastjson.JSONObject;
import com.cloud.servicemanage.entity.weixin.UserAccessTokenModel;
import com.cloud.servicemanage.service.weixin.WechatUserService;
import com.cloud.servicemanage.thread.AccessTokenThread;
import com.cloud.servicemanage.utils.weixin.WeixinUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@SuppressWarnings("SpringJavaAutowiringInspection")
@Service("UserService")
public class WechatUserServiceImpl implements WechatUserService {

    private static Logger log = LoggerFactory.getLogger(WechatUserServiceImpl.class);

    // 换取网页授权access_token（POST）
    public static String getAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";

    /**
     * 换取网页授权access_token
     * @param code
     * @return 授权接口凭证
     */
    @Override
    public UserAccessTokenModel GetAccessToken(String code) {
        UserAccessTokenModel userAccessTokenModel = null;

        // 拼装换取网页授权的url
        String url = getAccessTokenUrl.replace("APPID", AccessTokenThread.appId).replace("SECRET", AccessTokenThread.appSecret).replace("CODE", code);

        JSONObject jsonObject = WeixinUtil.httpRequest(url, "GET", null);
        // 如果请求成功
        if (null != jsonObject) {
            try {
                userAccessTokenModel = new UserAccessTokenModel();
                userAccessTokenModel.setToken(jsonObject.getString("access_token"));
                userAccessTokenModel.setExpiresIn(jsonObject.getInteger("expires_in"));
                userAccessTokenModel.setOpenid(jsonObject.getString("openid"));
            } catch (Exception e) {
                userAccessTokenModel = null;
                // 获取token失败
                log.error("获取UserAccessToken失败 errcode:{} errmsg:{}", jsonObject.getInteger("errcode"), jsonObject.getString("errmsg"));
            }
        }
        return userAccessTokenModel;
    }



}
