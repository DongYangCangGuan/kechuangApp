package com.cloud.servicemanage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.commonsmng.entity.appletEntity.ConsultingInfo;
import com.cloud.servicemanage.common.PageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ConsultingMapper extends BaseMapper<ConsultingInfo> {
    int getConsultingPageTotal(@Param("pageVo") PageVo pageVo);

    List<ConsultingInfo> getConsultingPageVo(@Param("pageVo") PageVo pageVo);
}
