package com.cloud.servicemanage.mapper;

import com.cloud.servicemanage.common.PageVo;
import com.cloud.servicemanage.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMapper {
    //查询总页数
    public int getPageTotal(PageVo pageVo);

    //分页

    //查询角色基本信息
     List<Role> getRole(PageVo pageVo);

    //新增角色
    int addRole(Role role);

    //修改角色
    int updateRole(Role role);

    //删除角色
    int deleteRole(String id);

    int checkRoleCode(@Param("id") String id,@Param("code")  String code);
    int checkRoleName(@Param("id") String id,@Param("name")  String name);
//    判断该角色下是否有人员
    int isRoleUsed(@Param("code") String code);

}
