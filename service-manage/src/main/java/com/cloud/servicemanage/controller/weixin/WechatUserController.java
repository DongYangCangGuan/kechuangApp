package com.cloud.servicemanage.controller.weixin;

import com.cloud.servicemanage.entity.weixin.UserAccessTokenModel;
import com.cloud.servicemanage.service.weixin.WechatUserService;
import com.cloud.servicemanage.utils.weixin.json.JsonResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Author:cat
 * @Description 获取用户信息控制器类
 * @Date: 2018-04-08  18:06
 * @Modified By:
 */
@RestController
@RequestMapping("/wechatUser")
@Api ( "WechatUserController RESTful Service API" )
public class WechatUserController {

    private static Logger logger = LoggerFactory.getLogger(WechatUserController.class);

    @Autowired
    private WechatUserService wechatUserService;




    /**
     * 获取用户openId
     * HTTP 方式：GET
     * API 路径：/wechatUser
     * 方法名：getWechatOpenId
     * 方法返回类型：String
     * 方法异常：WechatException
     * @Author: cat
     * @Date: 2018/04/08
     */
    @RequestMapping(value = "", method = RequestMethod.GET, produces = "application/xml; charset=UTF-8")
    public String getWechatOpenId(@ApiParam(value = "code", required = true) @RequestParam(value = "code", required = true) String code) {
        logger.info("code:"+code);

        try {
            String openId = "";
            UserAccessTokenModel userAccessTokenModel = null;

            userAccessTokenModel = wechatUserService.GetAccessToken(code);

            if (userAccessTokenModel != null){
                openId = userAccessTokenModel.getOpenid();
            }
            logger.info("openId:"+openId);
            return new JsonResponseData(true, "", 200, null, openId).toString();
        } catch (RuntimeException e) {
            logger.error("controller:WechatUserController. function:getWechatOpenId.. error:" + e.getMessage());
            logger.error("error:" + e.getMessage());
            return new JsonResponseData(false, e.getMessage(), 201, null, null).toString();
        }
    }


}
