package com.cloud.servicemanage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.commonsmng.entity.appletEntity.User;
import com.cloud.servicemanage.common.PageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户的mapper
 * author: tjs
 */
@Mapper
public interface UserInfoMapper extends BaseMapper<User> {

    List<User> getUserInfoPageVo(@Param("pageVo")PageVo pageVo);

    int getPageTotal(@Param("pageVo")PageVo pageVo);

    List<User> getUserInfoByCertificationMarkPageVo(@Param("pageVo") PageVo pageVo);

    int getUserInfoByCertificationMarkPageTotal(@Param("pageVo") PageVo pageVo);

    //分页查询小程序端的用户信息
    List<User> getUserByUroleEqOnePageVo(@Param("pageVo") PageVo pageVo);

    //查询分页时的小程序端用户数
    Integer getUserByUroleEqOnePageVoCount(@Param("pageVo") PageVo pageVo);

    //List<User> getUserInfoBatchById(@Param("userIds") List<String> userIds);

    //int getBatchPageTotal(@Param("userIds") List<String> userIds);

    //获取用户角色urole
    User getuRolebyId(@Param("id")String id);
    //根据urole获取会员
    List<String> getUserListByuRole(@Param("uRole")String urole);
    //获取所有人的email
    List<String> getUseremailAll();

    //获取所有通知的userid
    List<String> getUserIdList();

    //统计用户数
    Integer getCountByUser(@Param("id") String id,@Param("code") String code);
}
