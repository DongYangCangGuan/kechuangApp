package com.cloud.servicewechat.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.commonsmng.entity.appletEntity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 查询用户信息(包含会员信息)
     User getUser(@Param("userid") String userId);

    // 修改用户信息
     int upDateUser(@Param("user") User user);

     //仅查询会员信息
    User getUserInMemberInfo(@Param("enterpriseCode") String enterpriseCode);

     int addURole(@Param("id")String userId);

     int addUserDetail(@Param("user") User user);

    User getuRolebyId(@Param("id")String id);

    List<String> getUserListByRole(@Param("role")String role);

    //统计用户数
    Integer getCountByUser(@Param("id") String id,@Param("code") String code);
    int updateUserCherk(@Param("id")String id);

    String  getRole(@Param("id") String id,@Param("code") String code);

}
