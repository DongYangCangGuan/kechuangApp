package com.cloud.servicemanage.mapper;

import com.cloud.servicemanage.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface MenuMapper {
    List<Menu> getMenuList();
    List<Menu> getMenuListById(@Param("ID") String ID);
    int modifyMenuInfo(Menu menu);
    int modifyMenuTree(@Param("id") String ID, @Param("parentid") String PARENTID);
    int insertMenu(Menu menu);

    List<Menu> getMenuInfoById(@Param("ID") String ID);

    //@Param("userId") String userId
    List<Menu> querySecurityMenuTreeList(@Param("userId") String userId);

    //selectMenuByParentId
    List<Menu> selectMenuByParentId(@Param("id") String id);


    List<Menu> selectMenuByRoleId(@Param("roleid") String ID);


    int insertMenuByRoleId(@Param("roleid") String Roleid,@Param("list") List<Menu> list);

    boolean isMenuCodeExisted(@Param("id") String id, @Param("code") String code);

}
