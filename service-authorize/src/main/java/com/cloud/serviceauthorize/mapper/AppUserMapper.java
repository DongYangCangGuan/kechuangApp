package com.cloud.serviceauthorize.mapper;

import com.cloud.serviceauthorize.entity.BaseUser;
import com.cloud.serviceauthorize.entity.UserData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AppUserMapper { //正常登录调用
    BaseUser getUserInfo(@Param("USERID") String USERID, @Param("PWD") String PWD);
    //手势登录调用
    BaseUser getUserByUserID(@Param("USERID") String USERID);
    //新增用户扩展表
    Integer InsertUserData(UserData userData);
    //更新用户扩展表
    Integer updateDataUser(UserData userData);
    //判断user_data 表中 用户是否存在
    Integer getUserData(@Param("USERID") String USERID);
}
