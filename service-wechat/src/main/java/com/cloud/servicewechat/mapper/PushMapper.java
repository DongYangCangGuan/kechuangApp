package com.cloud.servicewechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.commonsmng.entity.appletEntity.WeixinInfo;

import com.cloud.servicewechat.common.PageVo;
import com.cloud.servicewechat.entity.PushEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PushMapper extends BaseMapper<PushEntity> {

    int addPush(PushEntity push);

    int modifyPush(PushEntity push);

    List<PushEntity> getPushList(@Param("pageVo") PageVo pageVo);

    int getPushCount(@Param("pageVo") PageVo pageVo);

    PushEntity getPushById(@Param("id") String id);

    int insertweixinIfo(@Param("openIds") List<String> openIds);

    void insertUnionid(@Param("list") List<WeixinInfo> list);

}
