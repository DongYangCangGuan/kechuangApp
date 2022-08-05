package com.cloud.servicemanage.mapper;

import com.cloud.servicemanage.common.PageVo;
import com.cloud.servicemanage.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface StaffMapper {


    List<StaffInfo> getStaffById(Map<String, Object> searchMap);

    Integer getStaffSum(@Param("departmentid") String departmentid,@Param("isused") String isused,@Param("switch1") Boolean switch1);

    List<StaffInfo> selectByName(@Param("name") String name,@Param("departmentid") String departmentid);

//    Integer insertStaff(@Param("StaffInfo") StaffInfo staff,@Param("userId") String userId,@Param("list") List list);
    Integer insertStaff(@Param("StaffInfo") StaffInfo staff);

    Integer updateStaff(@Param("StaffInfo") StaffInfo staff);

    Integer deleteStaff(@Param("isused")String isused,@Param("id")String id);

    List<Role> selectRole();

    List<UserEducation> selectUserEdu(@Param("id") String id);

    List<UserDepart> selectUserDepart(@Param("id") String id);

    List<Dictionary> selectEducationOrType(@Param("kind")String kind);

    boolean isStaffIdExisted(@Param("id") String id, @Param("empid") String empid);
    boolean isStaffLoginNameExisted(@Param("id") String id, @Param("loginname") String loginname);

    List<String> selectUserRoleId (@Param("id") String id);

    List<String> selectUserRoleName (@Param("id") String id);

//    Integer deleteRole(@Param("id") String id);


    Integer insertFileImport(@Param("fileImport") FileImport fileImport);

    //Integer insertFileImportDetail(List<Map> exceldata,String bacthid);

    //回显数据 分页
    List<FileImportDetail> showStaffData(PageVo pageVo);

    //总记录数
    Integer getStaffDataTotal(PageVo pageVo);

    //校验临时表数据
    void checkFileImportDetail(@Param("batchid") String batchid);

    //判断errorMsg是否为空
    Integer isEmptyErrorMsg(@Param("batchid")String batchid);

    //查看错误信息
    List<FileImportDetail> selectErrorMsg(@Param("batchid")String batchid);

    //导入user和userrolerelation表
    Integer insertUserData(@Param("batchid") String batchid,@Param("fileImportDetailList") List<FileImportDetail> fileImportDetailList);

    //根据用户id查询用户密码
    String selectPwdByUserId(String userId);

    //修改密码
    Integer updatePassword(@Param("userId")String useId,@Param("password")String password);
}