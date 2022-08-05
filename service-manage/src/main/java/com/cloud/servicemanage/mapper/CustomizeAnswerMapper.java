package com.cloud.servicemanage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.commonsmng.entity.appletEntity.CustomizeAnswer;
import com.cloud.servicemanage.common.PageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CustomizeAnswerMapper extends BaseMapper<CustomizeAnswer> {
    //获取总条数
    int getPageTotal(@Param("pageVo") PageVo pageVo);

    // 获取已回复总页数
    int getRepliedPageTotal(@Param("pageVo") PageVo pageVo);

    // 获取未回复总页数
    int getUnRepliedPageTotal(@Param("pageVo") PageVo pageVo);

    //获取分页区间的回答
    List<CustomizeAnswer> getCustomizeAnswerPageVo(@Param("pageVo") PageVo pageVo);

    //获取分页区间的已回复回答
    List<CustomizeAnswer> getRepliedCustomizeAnswerPageVo(@Param("pageVo") PageVo pageVo);

    //获取分页区间的未回复回答
    List<CustomizeAnswer> getUnRepliedCustomizeAnswerPageVo(@Param("pageVo") PageVo pageVo);

    //根据id获取回答
    CustomizeAnswer getCustomizeAnswerDetailById(String id);

    // 回复
    int replyCustomizeAnswer(@Param("customizeAnswer") CustomizeAnswer customizeAnswer);
}
