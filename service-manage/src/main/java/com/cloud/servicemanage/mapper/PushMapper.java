package com.cloud.servicemanage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.commonsmng.entity.appletEntity.PushDetail;
import com.cloud.commonsmng.entity.appletEntity.User;
import com.cloud.commonsmng.entity.appletEntity.WeixinInfo;
import com.cloud.servicemanage.common.PageVo;
import com.cloud.servicemanage.entity.PushEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;

@Mapper
public interface PushMapper extends BaseMapper<PushEntity> {

    int addPush(@Param("push") PushEntity push);

    int modifyPush(@Param("push") PushEntity push);

    List<PushEntity> getPushList(@Param("pageVo") PageVo pageVo);

    int getPushCount(@Param("pageVo") PageVo pageVo);

    PushEntity getPushById(@Param("id")String id);

    int insertweixinIfo(@Param("openIds")List<String> openIds);

    void insertUnionid(@Param("list")List<WeixinInfo> list);

    List<String> getUserIds(@Param("sendtype")String sendtype);

    List<User> getUserInfoList(@Param("pageVo") PageVo pageVo);

    int getUserInfoCount(@Param("pageVo") PageVo pageVo);

    List<String> getpushDatailopenIds(@Param("pushId")String pushId);

    int updatePushDetail(@Param("pushId")String pushId, @Param("push") PushEntity push);

    List<PushDetail> getPushDetail(@Param("id")String id);
}
