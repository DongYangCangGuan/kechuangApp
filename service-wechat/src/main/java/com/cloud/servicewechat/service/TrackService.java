package com.cloud.servicewechat.service;

import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.entity.appletEntity.Report;
import com.cloud.commonsmng.entity.appletEntity.Track;
import com.cloud.commonsmng.entity.appletEntity.User;
import com.cloud.commonsmng.factory.BaseService;
import com.cloud.servicewechat.common.ConstantUtil;
import com.cloud.servicewechat.common.PageUtil;
import com.cloud.servicewechat.common.PageVo;
import com.cloud.servicewechat.mapper.ReportMapper;
import com.cloud.servicewechat.mapper.TrackMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("track")
public class TrackService extends BaseService {

    @Autowired
    private TrackMapper trackMapper;

    @Autowired
    private ReportMapper reportMapper;

    //实例化日志对象，引用jar包org.slf4j.Logger
    private static final Logger logger = LoggerFactory.getLogger(NotesService.class);


    @Override
    public Map<String, Object> exec(String method, String param) {
        Map<String, Object> obj = new HashMap<>();
        switch (method) {
            case ConstantUtil.GET_TRACK:
                obj = getTrack(param);
                break;
            default:
                break;
        }
        return obj;
    }


    //查询出所有可以展示的报告列表信息
    private Map<String, Object> getTrack(String param) {
        Map<String, Object> result = new HashMap<>();
        PageVo pageVo = JSONObject.parseObject(param, PageVo.class);
        PageUtil pageUtil = pageVo.getPage();//获取前端的页面分页信息
        try {
            PageVo<Report> trackPageVo = new PageVo<>();
            Map<String, String> map = new HashMap<>();
            logger.info(String.format("获取历史传递参数: [%s]", param));
            if (pageVo.getSearchdata() != null) {
                map = (Map<String, String>) pageVo.getSearchdata();
            }
            if (super.getUserInfo() != null) {
                User user = (User) super.getUserInfo();
                if (user.getId() != null && !"".equals(user.getId())) {
                    map.put("userId", super.getUserInfo().getId());//获取当前用户编号
                } else if (user.getMember() != null && user.getMember().getEnterpriseCode() != null && !"".equals(user.getMember().getEnterpriseCode())) {
                    map.put("enterpriseCode", user.getMember().getEnterpriseCode());//获取当前企业编号
                }
            }
            pageVo.setSearchdata(map);
            List<Report> trackList = trackMapper.getTrackList(pageVo);
            List<Map<String, String>> getName = reportMapper.getNameByKind();
            for (Report track : trackList) {
                for (Map<String, String> item : getName) {
                    if (StringUtils.hasText(track.getReportKind().getIndustry())) {
                        List<String> industryList = Arrays.asList(track.getReportKind().getIndustry().split(","));
                        if (industryList != null && industryList.size() > 0) {
                            for (String s : industryList) {
                                if (s.equals(item.get("code"))) {
                                    track.getReportKind().setIndustry(track.getReportKind().getIndustry().replace(item.get("code"), item.get("name")));
                                }
                            }
                        }
                    }
//                    if (StringUtils.hasText(track.getReportKind().getLabel())) {
////                        if (track.getReportKind().getLabel().equals(item.get("code"))) {
////                            track.getReportKind().setLabel(track.getReportKind().getLabel().replace(item.get("code"), item.get("picurl")));
////                        }
//                    }
                    if (StringUtils.hasText(track.getReportKind().getKindId())) {
                        List<String> KindList = Arrays.asList(track.getReportKind().getKindId().split(","));
                        if (KindList != null && KindList.size() > 0) {
                            for (String s : KindList) {
                                if (s.equals(item.get("code"))) {
                                    track.getReportKind().setKindId(track.getReportKind().getKindId().replace(item.get("code"), item.get("name")));
                                }
                            }
                        }
                    }
                }
                if (StringUtils.hasText(track.getReportKind().getIndustry())) {
                    track.setIndustryList(Arrays.asList(track.getReportKind().getIndustry().split(",")));
                }
                if (StringUtils.hasText(track.getReportKind().getKindId())) {
                    track.setKindIdList(Arrays.asList(track.getReportKind().getKindId().split(",")));
                }

            }
            trackPageVo.setDataList(trackList);
            Constants.getSuccMsg(result, trackPageVo);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

}













