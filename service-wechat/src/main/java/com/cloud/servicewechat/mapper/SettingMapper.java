package com.cloud.servicewechat.mapper;

import com.cloud.commonsmng.entity.appletEntity.Setting;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SettingMapper {

    Integer insert(@Param("setting") Setting setting);

}
