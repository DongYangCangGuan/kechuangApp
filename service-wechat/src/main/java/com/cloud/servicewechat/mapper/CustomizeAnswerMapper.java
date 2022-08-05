package com.cloud.servicewechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.commonsmng.entity.appletEntity.CustomizeAnswer;
import com.cloud.servicewechat.common.PageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CustomizeAnswerMapper extends BaseMapper<CustomizeAnswer> {

    //根据问题编号查询问题的回答信息
    List<CustomizeAnswer> getCustomizeAnswerPageVoByQuestionId(@Param("pageVo") PageVo pageVo);

    //提交问题的答案
    int addCustomizeAnswer(@Param("answerList") List<CustomizeAnswer> customizeAnswer);
}
