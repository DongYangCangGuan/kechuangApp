package com.cloud.servicemanage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.commonsmng.entity.appletEntity.CustomizeQuestion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CustomizeQuestionMapper extends BaseMapper<CustomizeQuestion> {

    // 获取所有问题
    List<CustomizeQuestion> getCustomizeQuestionList();

    // 删除问题
    int deleteCustomizeQuestion(@Param("customizeQuestion") CustomizeQuestion customizeQuestion);

    // 插入问题
    int insertCustomizeQuestion(@Param("customizeQuestion") CustomizeQuestion customizeQuestion);

    // 根据id获取问题
    CustomizeQuestion getCustomizeQuestionById(@Param("id") String id);

    // 修改问题
    int updateCustomizeQuestion(@Param("customizeQuestion") CustomizeQuestion customizeQuestion);

    // 根据id查看详细问题
    CustomizeQuestion getCustomizeQuestionDetailById(@Param("id") String id);

    // 发布问题
    int releaseCustomizeQuestion(@Param("customizeQuestion") CustomizeQuestion customizeQuestion);
}
