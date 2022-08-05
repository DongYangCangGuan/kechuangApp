package com.cloud.servicemanage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.commonsmng.entity.appletEntity.Aboutus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AboutusMapper extends BaseMapper {
    Aboutus getAboutus();

    int insertAboutus(@Param("aboutus") Aboutus aboutus);
}
