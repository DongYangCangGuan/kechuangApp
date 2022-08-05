package com.cloud.servicemanage.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.commonsmng.entity.appletEntity.Slider;
import com.cloud.servicemanage.common.PageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SliderMapper extends BaseMapper<Slider> {
    //分页获取轮播图信息
    List<Slider> getSliderPageVo(@Param("pageVo")PageVo pageVo);

    //查询轮播图总数
    Integer getPageTotal(@Param("pageVo") PageVo pageVo);

    //根据编号查询轮播图信息
    Slider getSliderById(@Param("id")String id);

    //新增轮播图信息-->
    Integer insertSlider(@Param("slider") Slider slider);

    //修改轮播图信息
    Integer updateSlider(@Param("slider") Slider slider);

    //删除轮播图信息
    Integer deleteSlider(@Param("id") String id);

    //判断优先级是否存在
    Integer checkSliderByIndex(@Param("index") Integer index);

}
