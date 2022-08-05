package com.cloud.serviceauthorize.service;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.cloud.commonsmng.constants.util.snowflakeId.SnowflakeIdGenerator;
import com.cloud.serviceauthorize.entity.User;
import com.cloud.serviceauthorize.entity.UserInfo;
import com.cloud.serviceauthorize.mapper.AppletLoginMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class AppletLoginService {

    @Autowired
    AppletLoginMapper appletLoginMapper;

    @Value("${SnowFlakeIdGenerator.workerId}")
    private long workerId;

    @Value("${SnowFlakeIdGenerator.datacenterId}")
    private long datacenterId;

    private SnowflakeIdGenerator snowflakeIdGenerator;

    //使用账号密码登录
    public User selectByAccountAndPwd(@Param("account") String account, @Param("pwd") String pwd) {
        return appletLoginMapper.selectByAccountAndPwd(account, pwd);
    }

    //使用openid作为参数查询数据库中数据
    public User selectByOpenId(@Param("openid") String openid) {
        return appletLoginMapper.getSelectByOpenId(openid);
    }

    //添加一个用户
    public User insertUser(String openId, UserInfo userInfo, String unionid) {
        User user = new User();
        snowflakeIdGenerator = new SnowflakeIdGenerator(this.workerId, this.datacenterId);
        String idWorker = SnowflakeIdGenerator.getIdWorker(snowflakeIdGenerator);
        user.setId(idWorker);
        user.setUserName(IdWorker.getIdStr());//默认用户名
        user.setOpenId(openId);
        user.setNickName(userInfo.getNickName());
        user.setGender(userInfo.getGender());
        user.setHeadImg(userInfo.getAvatarUrl());
        user.setURole(Integer.valueOf(0));//小程序端添加默认为小程序端客户
        user.setCreatorId(user.getId());
        user.setCreateTime(new Date());
        user.setModifierId(user.getId());
        user.setModifyTime(new Date());
        user.setUnionid(unionid);
        int result = appletLoginMapper.insertUser(user);
        return result > 0 ? user : null;
    }

    public int addunionid(User user){
        int result = appletLoginMapper.addunionid(user);
        return result;
    }
}
