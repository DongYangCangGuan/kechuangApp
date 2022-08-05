package com.cloud.serviceauthorize.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.commonsmng.entity.BaseUserInfo;
import com.cloud.commonsmng.entity.appletEntity.Member;
import com.cloud.serviceauthorize.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AppletLoginMapper extends BaseMapper<User> {

    User selectByAccountAndPwd(@Param("account") String account, @Param("pwd") String pwd);

    //根据openid查询用户信息，判断用户是否存在
    //   selectByOpenId
     User getSelectByOpenId(@Param("openId") String openid);

     int insertUser(@Param("user") User user);
     int addunionid(@Param("user") User user);
}
