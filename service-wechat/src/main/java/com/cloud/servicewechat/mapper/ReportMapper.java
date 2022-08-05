package com.cloud.servicewechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.commonsmng.entity.appletEntity.Report;
import com.cloud.servicewechat.common.PageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReportMapper extends BaseMapper<Report> {
    //根据报告类型查询全部报告列表信息
    List<Report> getReportList(@Param("pageVo") PageVo pageVo, @Param("userId") String userId, @Param("enterpriseCode") String enterpriseCode);

    //查询出可以用来展示的报告总数量
    Integer getPageTotal(@Param("reportkind") String reportkind);

    //根据kind查询字典表中的name以及pic，小程序端展示用
    List<Map<String, String>> getNameByKind();

    //增加阅读量
    Integer addArticleviews(@Param("id") String id);

    //查询报告的路径和相关属性信息
    Report getReportPathAndProperty(@Param("id") String id);

    //根据报告属性查询编号
    List<String> getIdByReportProperty(@Param("report") Report report);

    //获取会员下的报告属性
    List<String> getIdByMemberId(@Param("memberId") String id);

    //查询用户的会员编号
    String getMemberIdByUserId(@Param("userId") String id);

    //根据会员账号查询会员编号
    String getMemberIdByEnterpriseCode(@Param("enterpriseCode") String enterpriseCode);

    //根据用户id获取用户消息
    Integer getUserNotesNoReadNumber(@Param("userId") String id);
}
