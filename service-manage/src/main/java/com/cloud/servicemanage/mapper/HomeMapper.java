package com.cloud.servicemanage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface HomeMapper extends BaseMapper {

    //查询报告阅读次数
    Integer selectReportReadNum();

    //查询用户数
    Integer selectUserNum();

    //查询会员单位数
    Integer selectMemberUnit();

    //查询本月新增报告数
    Integer selectMonthAddReport();

    //查询报告总数
    Integer selectReportNum();

    //根据开始时间和截止时间按年查询每月用户数
    List<Map> selectUserMonthNumByYear(@Param("map") Map<String,Date> map);

    //根据开始时间和截止时间按年查询每月会员单位
    List<Map> selectMemberMonthNumByYear(@Param("map") Map<String,Date> map);

    //根据开始时间和截止时间按周查询每天用户数
    List<Map> selectUserDayNumByWeek(@Param("map") Map<String,Date> map);

    //根据开始时间和截止时间按周查询每天会员单位
    List<Map> selectMemberDayNumByWeek(@Param("map") Map<String,Date> map);
}
