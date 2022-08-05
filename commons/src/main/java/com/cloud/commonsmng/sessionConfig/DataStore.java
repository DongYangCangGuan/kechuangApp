package com.cloud.commonsmng.sessionConfig;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class DataStore {

    @Autowired
    RedisTemplate redisTemplate;

    @Value("redis.config")
    private String flag = "";

    @Autowired
    sessionConfig config;

    public void setData(HttpServletRequest request, String key, Object value) {
        try {
            if (!flag.equals("1")) {
                //存储信息到redis中，设置30分钟过期
                redisTemplate.opsForValue().set(key, JSONObject.toJSONString(value), 30,TimeUnit.DAYS);
            } else {
                config.setSession(request, key, JSONObject.toJSONString(value));
            }
        } catch (ServletException ex) {
        } catch (IOException e) {
        }
    }

    public Object getData(HttpServletRequest request, String key) {
        Object obj = new Object();
        try {
            if (flag.equals("1")) {
                //obj= redisTemplate.opsForValue().get(key);
            } else {
                obj = config.getSession(request, key);
            }
        } catch (ServletException ex) {

        } catch (IOException e) {

        }
        return obj;
    }

}
