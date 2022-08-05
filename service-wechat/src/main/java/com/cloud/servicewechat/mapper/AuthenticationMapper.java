package com.cloud.servicewechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.commonsmng.entity.appletEntity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;

@Mapper
public interface AuthenticationMapper extends BaseMapper<Authentication> {

    //用户认证，添加认证信息
    int addAuthenticationInfo(@Param("user") User user);
}
