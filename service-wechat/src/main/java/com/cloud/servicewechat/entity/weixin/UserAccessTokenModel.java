package com.cloud.servicewechat.entity.weixin;

/**
 * @Author:cat
 * @Description 授权接口凭证
 * @Date: 2018-04-12  10:00
 * @Modified By:
 */
public class UserAccessTokenModel {
    // 获取到的凭证
    private String token;
    // 凭证有效时间，单位：秒
    private int expiresIn;
    //用户OpenId
    private String openid;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
