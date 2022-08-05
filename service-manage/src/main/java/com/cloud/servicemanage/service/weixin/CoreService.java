package com.cloud.servicemanage.service.weixin;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author:cat
 * @Description 微信控制服务类
 * @Date: 2018-04-12  10:01
 * @Modified By:
 */
public interface CoreService {

    /**
     * @Author:cat
     * @Description 处理微信发来的请求（包括事件的推送）
     * @params:
     * @Date:2018-04-12 14:03
     * @Return:
     * @Modified By:
     */
    String processRequest(HttpServletRequest request);
}
