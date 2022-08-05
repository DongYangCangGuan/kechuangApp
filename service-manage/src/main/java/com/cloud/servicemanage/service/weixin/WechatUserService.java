package com.cloud.servicemanage.service.weixin;

import com.cloud.servicemanage.entity.weixin.UserAccessTokenModel;


/**
 * 获取用户信息Service
 * Created by cat on 2017-11-09.
 */
public interface WechatUserService {

    /**
     * 换取网页授权access_token
     * @param code
     * @return 授权接口凭证
     */
    UserAccessTokenModel GetAccessToken(String code);
}
