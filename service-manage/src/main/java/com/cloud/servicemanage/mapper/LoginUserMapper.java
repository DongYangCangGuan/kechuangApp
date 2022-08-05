package com.cloud.servicemanage.mapper;

import com.cloud.commonsmng.entity.BaseUserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LoginUserMapper {
    //正常登录调用
    BaseUserInfo getUsersByLoginName(@Param("userName") String userName, @Param("passWord") String passWord);

    BaseUserInfo getUsersByID(@Param("id") String id);
}
