package com.cloud.servicewechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.commonsmng.entity.appletEntity.*;
import com.cloud.servicewechat.common.PageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HomeMapper extends BaseMapper{

    List<BannerEntity> getBanner();

    List<NewsEntity> getNews(@Param("pageVo") PageVo pageVo);

    int getNewsCount(@Param("pageVo") PageVo pageVo);

    int addCollect(@Param("userId")String userId, @Param("productId")String productId);

    int deleteCollect(@Param("userId")String userId, @Param("productId")String productId);

    List<ActivityEntity> getActivity(@Param("act")ActivityEntity act);

    List<ActivityTemplate> getActivityTemplate( @Param("id")String id);

    int  signUp(SignEntity signEntity);

    String  getSignUp(SignEntity signEntity);

    int  deleteSign(String id);

    int  signUpAppendix(@Param("list") List<SignAppendixEntity> list, @Param("uid")String uid);

    int  signDetail(@Param("list") List<SignDetail> list, @Param("uid")String uid);

    List<String> getMemberName(@Param("userId")String userId);

    String  getAcTemplateBySeq(@Param("actId")String actId,@Param("seq")String seq);
}
