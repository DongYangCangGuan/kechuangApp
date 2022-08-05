package com.cloud.servicewechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.commonsmng.entity.appletEntity.Collect;
import com.cloud.servicewechat.common.PageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CollectMapper extends BaseMapper<Collect> {

    // 根据用户id查询全部收藏信息
    List<Collect> getCollectList(@Param("pageVo") PageVo pageVo);

    //查询出可以用来展示的报告总数量
    Integer getPageTotal(@Param("pageVo") PageVo pageVo);

    Integer addCollect(@Param("collect") Collect collect);

    Integer delCollect(@Param("collect") Collect collect);

    Integer updateCollect(@Param("collect") Collect collect);

    Integer updateFavorite(@Param("reportId") String reportId);

     Collect getCollectDetail (@Param("productId") String  productId , @Param("userId") String userId);
}
