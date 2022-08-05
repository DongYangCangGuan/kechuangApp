package com.cloud.servicewechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.commonsmng.entity.appletEntity.ActivityEntity;
import com.cloud.commonsmng.entity.appletEntity.ActivityTemplate;
import com.cloud.commonsmng.entity.appletEntity.SignDetail;
import com.cloud.commonsmng.entity.appletEntity.SignEntity;
import com.cloud.servicewechat.common.PageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ActivityMapper extends BaseMapper {

    Integer  countSignList (@Param("pageVo") PageVo pageVo);

    List<ActivityEntity> getSignList(@Param("pageVo") PageVo pageVo);

    SignEntity getSignDetail (@Param("activityEventId") String  activityEventId , @Param("userId") String userId);

    List<SignDetail> getSignTemAs(@Param("id") String id);


}
