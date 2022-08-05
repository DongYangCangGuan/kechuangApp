package com.cloud.servicemanage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.cloud.commonsmng.entity.appletEntity.Feedback;
import com.cloud.servicemanage.common.PageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FeedbackMapper extends BaseMapper {

    //查询总页数
    int getPageTotal(@Param("pageVo") PageVo pageVo);

    //获取建议列表
    List<Feedback> getFeedbackListPageVo(@Param("pageVo") PageVo pageVo);

    //查看建议
    Feedback getFeedbackDetailById(@Param("id") String id);

    //删除反馈建议
    int deleteFeedback(@Param("feedback") Feedback feedback);
}
