package com.cloud.servicemanage.mapper;
import com.cloud.commonsmng.entity.Loginfo;
import com.cloud.servicemanage.entity.LogHourData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LoginfoMapper {
//    Integer getLogInfoByType(@Param("type") String TYPE);
//    Integer getLogInfoByLevel(@Param("type") String TYPE, @Param("level") String LOG_LEVEL);
    Integer getLoginfo(@Param("actiontype") String type,@Param("log_level") String level);
    //获取每个小时的数量
    List<LogHourData> getLoginfoByhour(@Param("actiontype") String type, @Param("log_level") String level);

    List<Loginfo> getTopNumInfo(@Param("actiontype") String type, @Param("log_level") String level);


}
