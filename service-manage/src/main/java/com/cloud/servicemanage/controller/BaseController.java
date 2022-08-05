package com.cloud.servicemanage.controller;


import com.cloud.commonsmng.entity.Loginfo;
import com.cloud.commonsmng.kafkaUtils.KafkaMessager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {

    //实例化日志对象，引用jar包org.slf4j.Logger
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

//    @Autowired
//    KafkaMessager kafkaMessager;

    @Autowired
    RedisTemplate redisTemplate;


    protected String getRedisValue(String key) {
        String result = "";
        try {
            result = (String) redisTemplate.opsForValue().get(key);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return result;
    }

    protected void setRedisValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }


//    //获取用户信息
//    protected Users getUserInfo(HttpServletRequest request, HttpServletResponse response) {
//        Users user = new Users();
//        try {
//            String token = request.getHeader("token");
//
//
//        } catch (Exception ex) {
//
//        }
//        return user;
//    }


//    protected void SendKafka(Loginfo logs, boolean logflag) throws Exception {
//
//        String classname = new Exception().getStackTrace()[1].getClassName(); //获取调用者的类名
//
//        String method_name = new Exception().getStackTrace()[1].getMethodName(); //获取调用者的方法名
//
//        logger.info(classname);
//        logger.info(method_name);
//
//
//
//
//    }

}
