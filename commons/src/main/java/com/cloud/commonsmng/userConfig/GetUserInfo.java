package com.cloud.commonsmng.userConfig;

import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.entity.BaseUserInfo;
import com.cloud.commonsmng.entity.appletEntity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


@Service
public class GetUserInfo {

    @Autowired
    RedisTemplate redisTemplate;

    //@Autowired(required = false)
    //IBaseUserService baseUserService;

    public BaseUserInfo getUser(String system, String userid) {
        BaseUserInfo user = null;
        if (redisTemplate.hasKey(system + userid)) {
            user = JSONObject.parseObject(redisTemplate.opsForValue().get(system + userid).toString(), User.class);
        }
        return user;
    }


}
