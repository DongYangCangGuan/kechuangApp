package com.cloud.serviceauthorize.controller;

import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.aop.OperationLogDetail;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.security.AESUtil;
import com.cloud.commonsmng.security.JwtUtil;
import com.cloud.commonsmng.security.RSAUtil;
import com.cloud.commonsmng.entity.BaseUserInfo;
import com.cloud.commonsmng.sessionConfig.DataStore;
import com.cloud.serviceauthorize.common.DataProcessing;
import com.cloud.serviceauthorize.service.UsersService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    DataStore dataStore;

    @Autowired
    UsersService service;

    @Autowired
    RSAUtil rsaUtil;

    @Autowired
    AESUtil aesUtil;

    private String key = "asdsdfs112323242";//aes 加密

    private long loginDatagramValidityTime = 5 * 60 * 1000;//登录报文有效期为5分钟

    //    @RequestMapping(value = "manage/auth/login", method = RequestMethod.POST)
    @RequestMapping(value = "/auth/login", method = RequestMethod.POST)
    //public String login(@RequestBody String params, HttpServletRequest request) throws Exception {
    public String login(@RequestBody String params) throws Exception {
        logger.info(String.format("登录请求报文: [%s]", params));
        String decodeParam = DataProcessing.urlDecode(params);
        String param1 = aesUtil.desEncryt(decodeParam, key);

        Map<String, Object> result = new HashMap<>();
        if (param1 == null) {
            result.put("code", 203);
            result.put("msg", "succ");
            result.put("data", "输入参数不正确，无法解析");

        } else {

            JSONObject json = JSONObject.parseObject(param1);
            // JSONObject json = JSONObject.parseObject(params);

            //UserData userData=new UserData();

            String loginname = StringUtils.trimToEmpty(json.getString("loginname"));

            String pwd = StringUtils.trimToEmpty(json.getString("password"));

            long datagramTimestamp = Long.valueOf(StringUtils.trimToEmpty(json.getString("date")));//报文中的时间戳
            long nowTimestamp = new Date().getTime();//当前系统的时间戳

            if ((nowTimestamp - datagramTimestamp) > loginDatagramValidityTime) {
                result.put("code", 203);
                result.put("msg", "succ");
                result.put("data", "该报文已失效, 不可继续使用!");
            } else {
                BaseUserInfo user = null;

//        if(id!=null &&  !id.isEmpty()){
//            user= service.getUserInfoById(id);
//            for(Role r:user.getListRole()) {
//                user.getRoles().add(r.getCODE());
//
//            }
//            Menu m=new Menu();
//            m.setPath("/");
//            m.setComponent(null);
//            Meta a=new Meta();
//            m.setMeta(a);
//            a.setIcon("home_menu");
//            a.setTitle("主页");
//            Menu m2=new Menu();
//            m.getChildren().add(m2);
//            m2.setPath("dashboard");
//            m2.setComponent("@/views/logMonitor/appMonitor/index");
//            m2.setName("Dashboard");
//            m2.getMeta().setTitle("监控首页");
//            m2.getMeta().setIcon("home_menu");
//            m2=new Menu();
//            m.getChildren().add(m2);
//            m2.setPath("home");
//            m2.setComponent("@/views/home/index");
//            m2.setName("home");
//            m2.getMeta().setTitle("系统主页");
//            m2.getMeta().setIcon("home_menu");
//
//            user.getMenus().add(m);
//
//    }
//        else{
                user = service.getUserInfo(loginname, pwd);
                //  user.getRoles().add("admin");
//        }

                if (user != null) {
                    //token 写入response header
                    String token = JwtUtil.CreateToken(user.getId(), user.getUserName());
                    user.setToken(token);
                    String redis_Custom_key = user.getId() + user.getUserName();
                    //dataStore.setData(request, Constants.REDIS_KEY + redis_Custom_key, (BaseUserInfo) user);
                    dataStore.setData(null, Constants.REDIS_KEY + redis_Custom_key, (BaseUserInfo) user);
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
        }

        String resultJson = JSONObject.toJSONString(result);
        //logger.info("将返回结果转成json后的值：[%s]", resultJson);
        //String key = "asdsdfs112323242";
        String resultEncrypt = aesUtil.aesEncryptToBytes(resultJson, key);
        return resultEncrypt;
    }


    //生成密钥对
    @RequestMapping(value = "/auth/login/getkey", method = RequestMethod.POST)
    @OperationLogDetail(detail = "生成密钥对")
    public String getPublic(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("msg", "succ");
        Map<Integer, String> map = new HashMap<Integer, String>();
        map = rsaUtil.genKeyPair();

        dataStore.setData(request, Constants.REDIS_KEY_PRIVATEKEY, map.get(0));
        //redisTemplate.opsForValue().set(Constants.REDIS_KEY_PRIVATEKEY, map.get(0));

        //私钥存到redis
        result.put("info", map.get(1));
        String resultJson = JSONObject.toJSONString(result);
        String resultEncrypt = aesUtil.aesEncryptToBytes(resultJson, key);
        return resultEncrypt;
    }


    //生成密钥对
    @RequestMapping(value = "/auth/login/getaeskey", method = RequestMethod.POST)
    @OperationLogDetail(detail = "拿取对称加密的秘钥")
    public String decryptByPrivate(@RequestBody String params, HttpServletRequest request) throws Exception {
        JSONObject json = JSONObject.parseObject(params);
        //UserData userData=new UserData();

        String word = StringUtils.trimToEmpty(json.getString("aesKey"));

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("msg", "success");


        String key = (String) dataStore.getData(request, Constants.REDIS_KEY_PRIVATEKEY);

        String decrytString = rsaUtil.decryptByPrivate(word, key);
        dataStore.setData(request, Constants.REDIS_KEY_AESKEY, decrytString);

        result.put("info", decrytString);
        String resultJson = JSONObject.toJSONString(result);
        String resultEncrypt = aesUtil.aesEncryptToBytes(resultJson, key);
        return resultEncrypt;
    }


    @RequestMapping(value = "/auth/register", method = RequestMethod.POST)
    public Map<String, Object> register() {
        return null;
    }
}
