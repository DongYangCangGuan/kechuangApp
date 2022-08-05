package com.cloud.serviceauthorize.controller;

import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.aop.OperationLogDetail;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.kafkaUtils.KafkaMessager;
import com.cloud.commonsmng.redisConfig.RedisTemplateConfig;
import com.cloud.commonsmng.security.JwtUtil;
import com.cloud.commonsmng.sessionConfig.DataStore;
import com.cloud.serviceauthorize.entity.BaseUser;
import com.cloud.serviceauthorize.entity.UserData;
import com.cloud.serviceauthorize.service.AppUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//@RestController
public class AppLoginController {
    //实例化日志对象，引用jar包org.slf4j.Logger
    @Autowired
    KafkaMessager kafkaMessager;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    AppUserService users;

    @Autowired
    DataStore dataStore;


    @RequestMapping(value = "/auth/applogin", method = RequestMethod.POST)
    public Map<String, Object> login(@RequestBody String params, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> result = new HashMap<>();
        JSONObject json = JSONObject.parseObject(params);
        UserData userData = new UserData();

        String loginname = StringUtils.trimToEmpty(json.getString("loginname"));
        String token1 = json.containsKey("token") ? json.getString("token") : "";
        String pwd = StringUtils.trimToEmpty(json.getString("pwd"));
        String device = StringUtils.trimToEmpty(json.getString("device"));
        String appVersion = StringUtils.trimToEmpty(json.getString("appVersion"));
        String deviceid = StringUtils.trimToEmpty(json.getString("deviceid"));
        String longitude = StringUtils.trimToEmpty(json.getString("longitude"));
        String latitude = StringUtils.trimToEmpty(json.getString("latitude"));
        String mobileType = StringUtils.trimToEmpty(json.getString("mobileType"));
        String versionCode = StringUtils.trimToEmpty(json.getString("versionCode"));

        userData.setUserid(loginname);
        userData.setDevice_id(deviceid);
        userData.setDevice(device);
        userData.setVersion(appVersion);
        userData.setPosition("{'longitude':" + longitude + ",'latitude':" + latitude + "}");
        userData.setPhonemodel(mobileType);
        userData.setDeviceversion(versionCode);
        System.out.println("userData--->" + loginname);

        BaseUser user;
        //手势登录
        if (!"".equals(token1) && null != token1) {
            user = users.getUserByUserID(loginname);
        } else {
            //正常登录
            user = users.getAppUserInfo(loginname, pwd);
        }

        if (user != null) {


            //更新设备信息到用户扩展表中
            //1.判断用户是否存在
            Integer count = users.getUserData(loginname);
            if (userData.getDevice_id().equals("") || userData.getDevice_id() == null) {
                userData.setDevice_id("11111111");
                //用户存在更新
                if (count == 1) {
                    users.updateDataUser(userData);
                } else {
                    //新增
                    users.InsertUserData(userData);
                }
            }
            //token 写入response header
            String token = JwtUtil.CreateToken(loginname, "");
            response.addHeader("token", token);

            result.put("code", 200);
            result.put("msg", "succ");
            result.put("data", user);
            dataStore.setData(request, Constants.REDIS_KEY + loginname, user);
            // redisTemplate.opsForValue().set(Constants.CurrentUser,loginname);
        } else {
            result.put("code", 201);
            result.put("msg", "succ");
            result.put("data", "无此用户");
        }

        return result;
    }

}
