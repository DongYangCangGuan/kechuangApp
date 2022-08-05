package com.cloud.servicemanage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.commonsmng.entity.appletEntity.*;
import com.cloud.servicemanage.common.PageVo;
import com.cloud.servicemanage.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ActivityMapper extends BaseMapper {

    List<Activity> selectActivityList(Map<String, Object> map);

    List<Sign> selectSignList(String id);

    boolean insertActivity(@Param("activity") ActivityEntity activity);

    boolean insertActivityTemplate(@Param("activityTemplate") ActivityTemplate activityTemplate);


    boolean updateActivity(@Param("activity") ActivityEntity activity);

    boolean updateActivityTemplate(@Param("activityTemplate") ActivityTemplate activityTemplate);



    List<Questionnaire> selectQuestionnaireList(Map<String, Object> map);

    List<Question> selectQuestionList(String questionname);

    List<MyQuestion> selectMyQuestionList(String id);

    boolean insertQuestion(Map<String, Object> map);

    boolean updateQuestion(QuestionEntity  question);

    boolean updateMyQuestion (@Param("myquestion") QuestionOptionsEntity myquestion);

    boolean insertMyQuestion(Map<String, Object> map1);

    boolean deleteMyQuestion(String id);

    boolean deleteQuestion(String questionname);

    boolean deleteActivityId(String id);

    boolean updateActivityId(Map<String, Object> map);

    boolean deleteQuest(String id);

    boolean deleteMy(String id);

    boolean deleteMyquest(String id);

    boolean updateQuest(Map<String, Object> map);

    boolean updateMyQuest(Map<String, Object> map1);

    Integer  countActivityList (@Param("pageVo") PageVo pageVo);

    List<ActivityEntity> getActivityList(@Param("pageVo") PageVo pageVo);

    Integer countSignList (@Param("pageVo") PageVo pageVo);

    List<SignEntity>  getSignList(@Param("pageVo") PageVo pageVo);

    List<SignDetail> getSignTemAs(@Param("id") String id);

    Map<String,Object> SelectActivityFile(@Param("id") String id);

    Questionnaire getQuestionMsg(@Param("questionBelong") String questionBelong);

    //列表查询问卷填写公司 统计接口
    Integer  countQesMemberList(@Param("pageVo") PageVo pageVo);
    //列表接口
    List<User> getQesMemberList(@Param("pageVo") PageVo pageVo);

    //根据用户id获取公司信息
    Member getMemberByUserId(@Param("id") String id);

    //获取问卷
    List<QuestionEntity> getUserAnswer(@Param("userId") String userId,@Param("questionBelong") String questionBelong);

    //获取问卷回答
    String getUserAnswerByQuestionId(@Param("id") String id,@Param("userId") String userId);

    //获取问卷回答
    List<QuestionOptionsEntity>  getUserAnswerDetail(@Param("id") String id,@Param("ls") List<String> ls);
}
