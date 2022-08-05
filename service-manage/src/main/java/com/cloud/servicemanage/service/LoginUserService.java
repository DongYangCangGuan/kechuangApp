package com.cloud.servicemanage.service;

import com.cloud.commonsmng.entity.BaseUserInfo;
import com.cloud.servicemanage.mapper.LoginUserMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginUserService {
    @Autowired
    LoginUserMapper mapper;

    public BaseUserInfo getUserInfo(@Param("userName") String userName, @Param("passWord") String passWord) {
        BaseUserInfo usr = mapper.getUsersByLoginName(userName, passWord);
        return usr;
    }

    //getUserInfoById
    public BaseUserInfo getUserInfoById(@Param("id") String id) {
        BaseUserInfo usr = mapper.getUsersByID(id);
        return usr;
    }

}
