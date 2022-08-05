package com.cloud.servicemanage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.commonsmng.entity.appletEntity.PointEntity;
import com.cloud.commonsmng.entity.appletEntity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 *
 */
@Mapper
public interface StatisticsMapper extends BaseMapper<Product> {

    //获取活跃用户
    Integer getActiveUser(@Param("agoDate") String agoDate,@Param("now") String now);

    //获取活跃用户
    Integer getNewUser(@Param("agoDate") String agoDate,@Param("now") String now);

    //获取用户趋势
    List<PointEntity> activeTrend(@Param("flag") String flag);

    //获取平均单日使用时长
    List<PointEntity> dayAvgTime(@Param("flag") String flag);

    //获取登录次数
    List<PointEntity> loginTimes(@Param("flag") String flag);

    //获取平均每次
    List<PointEntity> eachAvgTime(@Param("flag") String flag);

    //获取获取埋点次数
    Integer getLoginTimes(@Param("type") String type);

    //获取当月阅读量
    Integer getReadStatistics();

    //获取活跃用户
    Integer getCollectNum();

    //获取产品发布数量
    List<PointEntity> getPublish();

    //获取活跃用户
    List<PointEntity> getPersonnelDistribution();

     //获取活跃用户
     String getChildList(@Param("type") String type);

    //获取当月各产品点击数目
    List<PointEntity> getProductDistribution(@Param("list") List<String> list);
}
