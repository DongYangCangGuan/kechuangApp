package com.cloud.serviceauthorize.mapper;

import com.cloud.commonsmng.entity.BaseUserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UsersMapper {

    //正常登录调用
    BaseUserInfo getUsersByLoginName(@Param("userName") String userName, @Param("passWord") String passWord);

    BaseUserInfo getUsersByID(@Param("id") String id);

    BaseUserInfo getUserInfo(@Param("id") String id);

}
