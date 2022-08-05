package com.cloud.servicemanage.mapper;

import com.cloud.commonsmng.entity.BaseRole;
import com.cloud.servicemanage.common.PageVo;
import com.cloud.servicemanage.entity.BaseUsers;
import com.cloud.servicemanage.entity.Users;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface RoleUserMapper {

    //tjs modify xml中不存在该方法
    //List<BaseUsers> getBaseUserInfo(@Param("userid") String USERID, @Param("pwd") String PWD);

    //Integer updateUserPwd(@Param("userid") String USERID,@Param("newpwd") String NEWPWD);
    //tjs modify 2021-06-17

    int getPageTotal(PageVo pageVo);

    int delRoleforUser(@Param("roleId") String  roleId,@Param("userId") String  userId);

    List<Users> getUsers(PageVo pageVo);

    //List<BaseRole> getUserRoleList(@Param("userId") String userId);

}