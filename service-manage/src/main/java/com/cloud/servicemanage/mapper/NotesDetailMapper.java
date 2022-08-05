package com.cloud.servicemanage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.commonsmng.entity.appletEntity.NotesDetail;
import com.cloud.servicemanage.common.PageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NotesDetailMapper extends BaseMapper<NotesDetail> {

    //查询总条数
    int getPageTotal(@Param("pageVo") PageVo pageVo);

    //查询详细的消息信息（包含用户信息）
    List<NotesDetail> selectNotesDetailByNotesId(@Param("pageVo") PageVo pageVo);
}
