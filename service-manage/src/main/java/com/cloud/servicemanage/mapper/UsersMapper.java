package com.cloud.servicemanage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.commonsmng.entity.BaseUserInfo;
import com.cloud.commonsmng.entity.appletEntity.User;
import com.cloud.servicemanage.common.PageVo;
import com.cloud.servicemanage.entity.Users;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UsersMapper extends BaseMapper<Users> {

    //正常登录调用
    BaseUserInfo getUsersByLoginName(@Param("LOGINNAME") String LOGINNAME, @Param("PASSWORD") String PWD);


    BaseUserInfo getUsersByID(@Param("ID") String id);
    BaseUserInfo getUserInfo(@Param("ID") String ID);


    int  getUserCount(@Param("pageVo") PageVo pageVo);

    List<User>  getUserList(@Param("pageVo") PageVo pageVo);

    int  insertUser(User user);

    int  deleteUser(String id);

    int  updateUser(User user);

    String    getEnterpriseName(@Param("id") String id);
}
