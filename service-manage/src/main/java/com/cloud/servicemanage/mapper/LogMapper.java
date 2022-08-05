package com.cloud.servicemanage.mapper;

import com.cloud.commonsmng.entity.Loginfo;
import com.cloud.servicemanage.common.PageVo;
import com.cloud.servicemanage.entity.LogHourData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LogMapper {
    List<Loginfo> getLogInfo(PageVo pageVo);
    
    List<LogHourData> getLogHour(@Param("actionType")String actionType);

    List<LogHourData> getLogWeek(@Param("actionType")String actionType);

    List<LogHourData> getLogMonth(@Param("actionType")String actionType);

    int getLogHourCount(@Param("actionType")String actionType);

    int getLogWeekCount(@Param("actionType")String actionType);

    int getLogMonthCount(@Param("actionType")String actionType);

    int getLogDateCount(@Param("actionType") String actionType,@Param("startDate") String startDate,@Param("endDate") String endDate);

    List<LogHourData> getLogDate(@Param("actionType") String actionType, @Param("startDate") String startDate,@Param("endDate") String endDate);

    int getPageTotal(PageVo pageVo);
}
