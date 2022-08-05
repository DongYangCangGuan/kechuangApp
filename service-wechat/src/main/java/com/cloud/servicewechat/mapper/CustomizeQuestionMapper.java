package com.cloud.servicewechat.mapper;

import com.cloud.commonsmng.entity.appletEntity.CustomizeQuestion;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CustomizeQuestionMapper {

    // 获取所有问题
    List<CustomizeQuestion> getQuestionList();
}
