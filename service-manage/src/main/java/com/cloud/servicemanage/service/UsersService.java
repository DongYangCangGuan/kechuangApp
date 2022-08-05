package com.cloud.servicemanage.service;

import com.cloud.commonsmng.entity.BaseUserInfo;
import com.cloud.commonsmng.userConfig.IBaseUserService;
import com.cloud.servicemanage.mapper.UsersMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersService implements IBaseUserService {

    @Autowired
    UsersMapper mapper;

    public BaseUserInfo getUserInfo(@Param("LOGINNAME") String LOGINNAME, @Param("PASSWORD") String PASSWORD) {
        BaseUserInfo usr = mapper.getUsersByLoginName(LOGINNAME, PASSWORD);
        return usr;
    }

    //getUserInfoById
    public BaseUserInfo getUserInfoById(@Param("ID") String id) {
        BaseUserInfo usr = mapper.getUsersByID(id);
        return usr;
    }

    @Override
    public BaseUserInfo getUserInfo(String ID) {
        BaseUserInfo usr = mapper.getUserInfo(ID);
        return usr;
    }
}
