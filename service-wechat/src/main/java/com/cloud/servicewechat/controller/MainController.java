package com.cloud.servicewechat.controller;

import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.aop.OperationLogDetail;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.constants.util.snowflakeId.SnowflakeIdGenerator;
import com.cloud.commonsmng.factory.ExecuteFactory;
import com.cloud.commonsmng.security.AESUtil;
import com.cloud.commonsmng.security.JwtUtil;
import com.cloud.commonsmng.entity.BaseUserInfo;
import com.cloud.commonsmng.userConfig.GetUserInfo;
import com.cloud.servicewechat.common.DataProcessing;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet
        .http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class MainController {

    @Autowired
    AESUtil aesUtil;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    GetUserInfo getUserInfo;

    @Value("${SnowFlakeIdGenerator.workerId}")
    private long workerId;

    @Value("${SnowFlakeIdGenerator.datacenterId}")
    private long datacenterId;

    private String key = "abcdefgabcdefg12";

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @RequestMapping(value = "/wechat/mobile/api", method = RequestMethod.POST)
    //@RequestMapping(value = "/mobile/api", method = RequestMethod.POST)
    @OperationLogDetail(detail = "基线系统主入口", type = "MOBILE")
    public Map<String, Object> excute(@RequestBody String params, ServletRequest request) throws Exception {
        Map<String, Object> result = new HashMap<>();
//        String urlDecode = DataProcessing.urlDecode(params);
//        String param1 = aesUtil.desEncryt(urlDecode.substring(10, urlDecode.length() - 2), key);//解密
        //获取用户信息
        logger.info(String.format("解密后的参数值: [%s]", params));
        HttpServletRequest r = (HttpServletRequest) request;
        String token = r.getHeader("token");
        BaseUserInfo usr = null;
        if (token != null && !"".equals(token)) {
            logger.info(String.format("请求传入的token: [%s]", token));
            Map<String, String> userinfo = jwtUtil.verifyToken(token);
            String logininfo = userinfo.get("logininfokey");
            logger.info(String.format("token中logininfo: [%s]", logininfo));

            usr = getUserInfo.getUser(Constants.REDIS_KEY, logininfo);
            logger.info(String.format("登录的用户信息: [%s],[%s]", usr.toString(), usr));
        }

        JSONObject json = JSONObject.parseObject(params);

        String className = StringUtils.trimToEmpty(json.getString("className"));
        String method = StringUtils.trimToEmpty(json.getString("method"));
        String param = StringUtils.trimToEmpty(json.getString("param"));
        result = ExecuteFactory.getExecuteService(className, usr, new SnowflakeIdGenerator(workerId, datacenterId)).exec(method, param);
        //String resultEncrypt = aesUtil.aesEncryptToBytes(result, key);
        return result;
    }
}
