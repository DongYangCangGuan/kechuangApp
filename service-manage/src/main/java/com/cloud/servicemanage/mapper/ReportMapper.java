package com.cloud.servicemanage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.commonsmng.entity.appletEntity.Report;
import com.cloud.servicemanage.common.PageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 报告类的Mapper
 */
@Mapper
public interface ReportMapper extends BaseMapper<Report> {

    //查询总页数
    int getPageTotal(@Param("pageVo") PageVo pageVo);

    //查询所有报告信息
    List<Report> getReportPageVo(@Param("pageVo") PageVo pageVo);

    //根据编号查询报告信息
    Report getReportById(@Param("code") String code);

    //根据id查询报告信息
    Report getReportInfoById(@Param("id") String id);

    //
    Report getNameFromDic(@Param("map") Map map);

    //新增
    Integer insertReport(@Param("report") Report report);

    //修改
    Integer updateReport(@Param("report") Report report);

    //删除
    Integer deleteReport(@Param("id") String id);

    //判断报告编号是否已经存在
    Integer checkReportCode(@Param("id") String id, @Param("code") String code);
}
