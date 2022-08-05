package com.cloud.serviceauthorize.controller;

import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.aop.OperationLogDetail;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.entity.BaseUserInfo;
import com.cloud.commonsmng.security.AESUtil;
import com.cloud.commonsmng.security.JwtUtil;
import com.cloud.commonsmng.sessionConfig.DataStore;
import com.cloud.serviceauthorize.entity.Login;
import com.cloud.serviceauthorize.entity.OpenIdJson;
import com.cloud.serviceauthorize.entity.User;
import com.cloud.serviceauthorize.entity.UserInfo;
import com.cloud.serviceauthorize.service.AppletLoginService;
import com.cloud.serviceauthorize.common.HttpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AppletLoginController {

    private static final Logger logger = LoggerFactory.getLogger(AppletLoginController.class);

    @Autowired
    AESUtil aesUtil;

    @Autowired
    AppletLoginService appletLoginService;

    @Autowired
    DataStore dataStore;

    @Autowired
    RedisTemplate redisTemplate;

    @Value("${access.wechat.server}")
    String wechatUrl;

    @RequestMapping(value = "/applet/login", method = RequestMethod.POST)
    @OperationLogDetail(detail = "基线系统主入口", type = "PC")
    public Map<String, Object> userLogin(@RequestBody String params, HttpServletRequest request) throws IOException {
        params = URLDecoder.decode(params, "UTF-8");
        if (params.endsWith("=")) {
            params = params.substring(0, params.length() - 1);
        }
        if (params.contains(" ")) {
            params = params.replace(" ", "+");
        }

        //解密
//        String key = "abcdefgabcdefg12";
//        String param1 = aesUtil.desEncryt(params.substring(10, params.length() - 2), key);
        JSONObject jsonObject = JSONObject.parseObject(params);
        String param1 = jsonObject.getString("param");
        logger.info("param1 is {}", param1);
        Login loginInfo = JSONObject.parseObject(param1, Login.class);
        logger.info("loginInfo is {}", loginInfo);
        String code = StringUtils.trimToEmpty(loginInfo.getCode());
        String appId = StringUtils.trimToEmpty(loginInfo.getAppId());
        String secret = StringUtils.trimToEmpty(loginInfo.getSecret());

        String users = StringUtils.trimToEmpty(loginInfo.getUserInfo());
        UserInfo userInfo = JSONObject.parseObject(users, UserInfo.class);

        Map<String, Object> returnResult = new HashMap<String, Object>();
        String result = "";
        try {//请求微信服务器，用code换取openid。HttpUtil是工具类，后面会给出实现，Configure类是小程序配置信息，后面会给出代码

           // String url = wechatUrl + appId + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";
            String url = wechatUrl + appId + "&secret=" + "8375579631b183bb2d568d4efc353331" + "&js_code=" + code + "&grant_type=authorization_code";
            logger.info("url is {}", url);
            result = HttpUtil.doGet(url, null);

            ObjectMapper mapper = new ObjectMapper();
            OpenIdJson openIdJson = mapper.readValue(result, OpenIdJson.class);

            //根据openid去数据库中查询有无此用户
            User user = appletLoginService.selectByOpenId(openIdJson.getOpenid());
            if (user == null) {
                String unionid = openIdJson.getUnionid();
                //当userList为空时说明数据库中没有当前用户，将当前用户保存到用表中
                User insertUser = appletLoginService.insertUser(openIdJson.getOpenid(), userInfo, unionid);//执行添加操作
                if (insertUser != null) {
                    user = insertUser;
                } else {
                    Constants.getErrMsg(returnResult, "添加失败，稍后重试！");
                    return returnResult;
                }
            }
            if(user.getUnionid()==null ||user.getUnionid().equals("")){
                user.setUnionid(openIdJson.getUnionid());
                appletLoginService.addunionid(user);
            }
            String logininfokey = user.getId() + user.getOpenId();
            String token = JwtUtil.CreateWXToken(logininfokey);
            user.setToken(token);

            dataStore.setData(request, Constants.REDIS_KEY + logininfokey, (BaseUserInfo) user);
            Constants.getSuccMsg(returnResult, user);
            return returnResult;
        } catch (Exception e) {
            e.printStackTrace();
            returnResult.put("code", 500);
            returnResult.put("msg", "输入参数不正确，无法解析");
            returnResult.put("data", "输入参数不正确，无法解析");
        }
        return returnResult;
    }

    @RequestMapping(value = "/applet/accountLogin", method = RequestMethod.POST)
    public Map<String, Object> accountLogin(@RequestBody String params, HttpServletRequest request) throws IOException {
        params = URLDecoder.decode(params, "UTF-8");
        if (params.endsWith("=")) {
            params = params.substring(0, params.length() - 1);
        }
        if (params.contains(" ")) {
            params = params.replace(" ", "+");
        }

        //解密
        String key = "abcdefgabcdefg12";
        String param1 = aesUtil.desEncryt(params.substring(10, params.length() - 2), key);
        Map<String, Object> result = new HashMap<>();
        if (param1 == null) {
            result.put("code", 203);
            result.put("msg", "输入参数不正确，无法解析");
            result.put("data", "输入参数不正确，无法解析");
        } else {
            JSONObject json = JSONObject.parseObject(param1);
            String account = StringUtils.trimToEmpty(json.getString("account"));
            String pwd = StringUtils.trimToEmpty(json.getString("pwd"));

            //根据openid去数据库中查询有无此用户
            User user = appletLoginService.selectByAccountAndPwd(account, pwd);

            if (user != null) {
                String logininfokey = user.getMember().getEnterpriseCode() + user.getMember().getEnterpriseName();
                String token = JwtUtil.CreateWXToken(logininfokey);
                user.setToken(token);

                dataStore.setData(request, Constants.REDIS_KEY + logininfokey, (BaseUserInfo) user);

                result.put("code", 200);
                result.put("msg", "succ");
                result.put("data", user);
            } else {
                result.put("code", 201);
                result.put("msg", "用户名或者密码错误");
                result.put("message", "用户名或者密码错误");
                result.put("data", "用户名或者密码错误");
            }
        }

        return result;
    }
}
