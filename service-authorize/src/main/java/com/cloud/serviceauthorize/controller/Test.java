package com.cloud.serviceauthorize.controller;

import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.aop.OperationLogDetail;
import com.cloud.commonsmng.entity.BaseUserInfo;
import com.cloud.commonsmng.security.JwtUtil;
import com.cloud.serviceauthorize.service.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@RestController
public class Test {
    //实例化日志对象，引用jar包org.slf4j.Logger
    private static final Logger logger = LoggerFactory.getLogger(Test.class);

    @Autowired
    private UsersService service;

    @Autowired
    private JwtUtil jwtUtil;

    //日志打印规范示例
    @RequestMapping("/hello")
    @OperationLogDetail(detail="测试描述")
    public Map<String,Object> hello() throws Exception {
        Map<String,Object> results=new HashMap<>();
        service.getUserInfo("DD","DD");
            //日志打印示例：日志严禁使用System.out.println()，严禁使用多个字符串相加：String+Sting+String
            logger.info("hello");//1，直接打印单个参数日志示例
            logger.info("日志输出：{}", "hello");//2，日志打印单个参数key:value示例
            logger.info("日志输出1：{},日志输出2：{}", "hello-1", "hello-2");//3，日志打印多个参数：key1:value1，key2:value2示例；（严禁使用多个字符串相加）
            results.put("code",200);
            results.put("mag","success");
            results.put("data","测试service-authorize接口成功");
            return results;
    }

}
