package com.cloud.servicewechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.commonsmng.entity.appletEntity.*;
import com.cloud.servicewechat.common.PageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface AnswerMapper extends BaseMapper<Answer> {

    Integer countAnswerList (@Param("pageVo") PageVo pageVo);

    List<Dictionary> getAnswerList(@Param("pageVo")PageVo pageVo);

    List<Answer> getAnswerDetail(@Param("kindId")String kindId, @Param("userId") String userId ,@Param("createTime") String createTime);

    List<AnswerPushEntity> getAllPush();

    String getQuestionName(@Param("questionBelong")String questionBelong);

    Integer updateIspush (@Param("id") String id);

    //
    List<QuestionnaireEntity> selectQuestionnaireList(Map<String, Object> map);

    QuestionnaireEntity getQuestionMsg(@Param("questionBelong") String questionBelong);

    List<QuestionEntity> selectQuestionList(String questionname);

    List<QuestionOptionsEntity> selectMyQuestionList(String id);

    //列表查询问卷填写公司 统计接口
    Integer  countQesMemberList(@Param("pageVo") PageVo pageVo);
    //列表接口
    List<User> getQesMemberList(@Param("pageVo") PageVo pageVo);
    //根据用户id获取公司信息
    Member getMemberByUserId(@Param("id") String id);

    //列表接口
    List<QuestionEntity> getUserAnswerQuestion(@Param("userId") String userId,@Param("questionBelong") String questionBelong);

    //列表接口
    List<QuestionOptionsEntity> getUserAllAnswers(@Param("questionId") String questionId,@Param("answers") List<String> answers);
}
