package com.cloud.servicewechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.commonsmng.entity.appletEntity.Slider;
import com.cloud.commonsmng.entity.appletEntity.StatusPoint;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface StatusPointMapper extends BaseMapper<StatusPoint> {

//    //保存用户登录或者是退出的埋点信息
//    int addStatusPoint(@Param("statusPonit")StatusPoint statusPoint);
}
