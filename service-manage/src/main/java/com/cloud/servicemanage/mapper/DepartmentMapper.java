package com.cloud.servicemanage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.servicemanage.entity.Department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {
    List<Department> getDeptInfoList();

    Department getDeptInfoByDeptId(@Param("id") String id);

    List<Department> getSubDeptsByDeptId(@Param("parentid") String parentId, @Param("isused") String isused);

    boolean isDeptCodeExisted(@Param("id") String id, @Param("code") String code);

    int addDeptInfo(Department dept);

    int updateDeptInfo(Department dept);

    List<Department> getDeptTreeByParentId(@Param("id") String parentid);
}
