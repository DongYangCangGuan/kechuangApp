package com.cloud.servicewechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.commonsmng.entity.appletEntity.Slider;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SliderMapper extends BaseMapper<Slider> {

    //查询全部slider
    List<Slider> getSlider();

}
