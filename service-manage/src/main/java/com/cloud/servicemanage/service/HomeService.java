package com.cloud.servicemanage.service;

import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.factory.BaseService;
import com.cloud.servicemanage.common.ConstantUtil;
import com.cloud.servicemanage.mapper.HomeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("Home")
public class HomeService extends BaseService {

    @Autowired
    private HomeMapper homeMapper;

    @Override
    public Map<String, Object> exec(String method, String param) {
        Map<String, Object> obj = new HashMap<>();
        switch (method) {
            case ConstantUtil.GET_OPERATIONAL_DATA: //getOperationalData
                obj = getOperationalData(param);
                break;
            case ConstantUtil.SELECT_USER_AND_MEMBER_MONTH_OR_DAY_NUM_BY_DATE: //selectUserAndMemberMonthOrDayNumByDate
                obj = selectUserAndMemberMonthOrDayNumByDate(param);
                break;
            default:
                break;
        }
        return obj;
    }

    //获取运营数据
    private Map<String, Object> getOperationalData(String param) {
        Map<String, Object> result = new HashMap<>();

        Integer reportReadNum = homeMapper.selectReportReadNum(); //查询报告阅读次数
        Integer userNum = homeMapper.selectUserNum();//查询用户数
        Integer memberUnit = homeMapper.selectMemberUnit();//查询会员单位数
        Integer monthAddReport = homeMapper.selectMonthAddReport();//查询本月新增报告数
        Integer reportNum = homeMapper.selectReportNum(); //查询报告总数

        Map<String, Integer> map = new HashMap<>();
        map.put("reportReadNum", reportReadNum);
        map.put("userNum", userNum);
        map.put("memberUnit", memberUnit);
        map.put("monthAddReport", monthAddReport);
        map.put("reportNum", reportNum);

        Constants.getSuccMsg(result, map);
        return result;
    }

    //根据时间查询每个月后每天的用户数和会员单位
    private Map<String, Object> selectUserAndMemberMonthOrDayNumByDate(String param) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Date> map = new HashMap<>();
        Map<String, List<Map>> listMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(param);
        map.put("startTime", jsonObject.getDate("startTime"));
        map.put("endTime", jsonObject.getDate("endTime"));
        String according = jsonObject.getString("according");//筛选
        if (according != null && "year".equals(according)) {//判断按年查月
            //根据开始时间和截止时间按年查询每月用户数
            List<Map> userMonthNumByYear = homeMapper.selectUserMonthNumByYear(map);
            //根据开始时间和截止时间按年查询每月会员单位
            List<Map> memberMonthNumByYear = homeMapper.selectMemberMonthNumByYear(map);

            listMap.put("userPillarCountList", userMonthNumByYear);
            listMap.put("memberPillarCountList", memberMonthNumByYear);
        } else { //否则按周查天
            //根据开始时间和截止时间按周查询每天用户数
            List<Map> userDayNumByWeek = homeMapper.selectUserDayNumByWeek(map);
            //根据开始时间和截止时间按周查询每天会员单位
            List<Map> memberDayNumByWeek = homeMapper.selectMemberDayNumByWeek(map);

            listMap.put("userPillarCountList", userDayNumByWeek);
            listMap.put("memberPillarCountList", memberDayNumByWeek);
        }
        Constants.getSuccMsg(result, listMap);
        return result;
    }

    //查询报告阅读次数
    private Map<String, Object> selectReportReadNum(String param) {
        Map<String, Object> result = new HashMap<>();
        Integer reportReadNum = homeMapper.selectReportReadNum();
        Constants.getSuccMsg(result, reportReadNum);
        return result;
    }

    //查询用户数
    private Map<String, Object> selectUserNum(String param) {
        Map<String, Object> result = new HashMap<>();
        Integer userNum = homeMapper.selectUserNum();
        Constants.getSuccMsg(result, userNum);
        return result;
    }

    //查询会员单位数
    private Map<String, Object> selectMemberUnit(String param) {
        Map<String, Object> result = new HashMap<>();
        Integer memberUnit = homeMapper.selectMemberUnit();
        Constants.getSuccMsg(result, memberUnit);
        return result;
    }

    //查询本月新增报告数
    private Map<String, Object> selectMonthAddReport(String param) {
        Map<String, Object> result = new HashMap<>();
        Integer monthAddReport = homeMapper.selectMonthAddReport();
        Constants.getSuccMsg(result, monthAddReport);
        return result;
    }

    //查询报告总数
    private Map<String, Object> selectReportNum(String param) {
        Map<String, Object> result = new HashMap<>();
        Integer reportNum = homeMapper.selectReportNum();
        Constants.getSuccMsg(result, reportNum);
        return result;
    }
}
