package com.cloud.servicemanage.entity.weixin;

/**
 * @Author:cat
 * @Description 微信通用接口凭证
 * @Date: 2018-04-12  10:00
 * @Modified By:
 */
public class AccessTokenModel {
    // 获取到的凭证
    private String token;
    // 凭证有效时间，单位：秒
    private int expiresIn;

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
}
