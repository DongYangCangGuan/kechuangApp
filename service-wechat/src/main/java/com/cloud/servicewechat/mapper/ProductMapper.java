package com.cloud.servicewechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.cloud.commonsmng.entity.appletEntity.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper extends BaseMapper{
    int deleteAnswers(@Param("userId") String userId ,@Param("questionBelong") String questionBelong);

    Aboutus getAboutus();

    int insertAnswers(@Param("answerEntity") AnswerEntity answerEntity);

    List<AnswerEntity> getAnswers(@Param("userId") String userId);

    List<QuestionEntity> getQuestionnaire(@Param("type") String type);

    List<QuestionOptionsEntity> getOpinion(String id);

    List<Product> getProductByProSeq(@Param("seq") String seq,@Param("type") String type,@Param("id") String id,@Param("sort") String sort);

    List<Product> getHotProduct(@Param("type") String type);

    String getAnswersMsg(@Param("questionId") String questionId,@Param("answer") String answer);

    int getProductReadTimes(@Param("productId") String productId);

    String getUserordinal(@Param("userId") String userId);

    int checkProduct(@Param("productId") String productId,@Param("userId") String userId);

    //根据kind和parentId获取dictionary
    List<Dictionary> getProductTypes(@Param("kind")String kind, @Param("parentId")String parentId);

    Product getContactUs(@Param("id") String id);

    int setTime(@Param("userId") String userId ,@Param("questionBelong") String questionBelong);

    String getQuestionBelong(@Param("id") String id);

    int setAnswerPush(@Param("userId") String userId ,@Param("questionBelong") String questionBelong);

    //获取配置list
    List<ProductsExpand> getProductExpandList(@Param("id") String id);

    //添加咨询信息
    int addConsultingInfo(@Param("consultingInfo")ConsultingInfo info);
}
