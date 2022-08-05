package com.cloud.servicewechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.commonsmng.entity.appletEntity.Report;
import com.cloud.commonsmng.entity.appletEntity.Track;
import com.cloud.servicewechat.common.PageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TrackMapper extends BaseMapper<Track> {

        // 根据用户id查询全部历史记录
    List<Report> getTrackList(@Param("pageVo") PageVo pageVo);

    //查询出可以用来展示的报告总数量
    Integer getPageTotal(@Param("reportkind") String reportkind);

}
