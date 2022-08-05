package com.cloud.servicewechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.commonsmng.entity.appletEntity.Point;
import com.cloud.commonsmng.entity.appletEntity.PointEntity;
import com.cloud.servicewechat.common.PageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface PointMapper extends BaseMapper<Point>{

    // 更新经纬度和地理位置
    int updateLocationAndAddress(@Param("point") Point point);

    Integer getPageTotal(@Param("pageVo") PageVo pageVo);

    // 根据用户id查询全部历史记录
    List<Point> getPointList(@Param("pageVo") PageVo pageVo);

    // 添加埋点信息
    int addPoint(@Param("pe") PointEntity pe);

     Point getPointDetail (@Param("productId") String  productId, @Param("userId") String userId);

}
