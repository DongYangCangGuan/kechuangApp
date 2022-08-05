package com.cloud.serviceauthorize.service;

import com.cloud.commonsmng.entity.BaseUserInfo;
import com.cloud.commonsmng.userConfig.IBaseUserService;
import com.cloud.serviceauthorize.mapper.UsersMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersService implements IBaseUserService {

    @Autowired
    UsersMapper mapper;

    public BaseUserInfo getUserInfo(@Param("userName") String userName, @Param("passWord") String passWord) {
        BaseUserInfo usr = mapper.getUsersByLoginName(userName, passWord);
        return usr;
    }

    //getUserInfoById
    public BaseUserInfo getUserInfoById(@Param("id") String id) {
        BaseUserInfo usr = mapper.getUsersByID(id);
        return usr;
    }

    @Override
    public BaseUserInfo getUserInfo(String id) {
        BaseUserInfo usr = mapper.getUserInfo(id);
        return usr;
    }
}
