package com.cloud.serviceauthorize.service;

import com.cloud.serviceauthorize.entity.BaseUser;
import com.cloud.serviceauthorize.entity.UserData;
import com.cloud.serviceauthorize.mapper.AppUserMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppUserService {

    @Autowired
    AppUserMapper mapper;
    //登录
    public BaseUser getAppUserInfo(String userid, String pwd) {

        //得到用户信息
        BaseUser users = mapper.getUserInfo(userid, pwd);

        return users;
    }

    public BaseUser getUserByUserID(@Param("USERID") String USERID){
        return mapper.getUserByUserID(USERID);
    }
    public Integer InsertUserData(UserData userData){
        return mapper.InsertUserData(userData);
    }
    public Integer updateDataUser(UserData userData){
        return mapper.updateDataUser(userData);
    }

    public Integer getUserData(@Param("USERID") String USERID){
        return  mapper.getUserData(USERID);
    }



}
