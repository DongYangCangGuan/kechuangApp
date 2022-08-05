package com.cloud.servicemanage.service;

import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.entity.Loginfo;
import com.cloud.commonsmng.factory.BaseService;
import com.cloud.servicemanage.common.ConstantUtil;
import com.cloud.servicemanage.common.PageUtil;
import com.cloud.servicemanage.common.PageVo;
import com.cloud.servicemanage.entity.LogHourData;
import com.cloud.servicemanage.mapper.LogMapper;
import org.apache.http.client.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("Loginfo")
public class LoginfoService extends BaseService {
    @Autowired
    public com.cloud.servicemanage.mapper.LoginfoMapper LoginfoMapper;
    @Autowired
    public LogMapper logMapper;
    private static final Logger logger = LoggerFactory.getLogger(LoginfoService.class);

    public Map<String, Object> exec(String method, String param) {

        // Users user=(Users) this.usr;

        Map<String, Object> obj = new HashMap<>();

        switch (method) {
            case ConstantUtil.GET_LOG_INFO_NUM:
                obj = getLoginfoNum(param);
                break;
            case ConstantUtil.GET_LOG_INFO_LIST:
                obj = getLoginfoList(param);
                break;
            case ConstantUtil.GET_LOG_INFO:
                obj = getLogInfo(param);
                break;
            case ConstantUtil.GET_LOG_CHARTS:
                obj = getLogCharts(param);
                break;
            case ConstantUtil.GET_LINE_CHART_COUNT:
                obj = getLineChartCount(param);
                break;
        }
        return obj;
    }

    private Map<String, Object> getLineChartCount(String param) {
        Map<String, Object> map = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(param);
        String type = jsonObject.getString("type");   //type 0:本日，1：本周 2：本月  3:时间段
        String startDate = jsonObject.getString("startDate");

        if (startDate != null && !startDate.equals("")) {

            startDate = UTCToCST(startDate, "yyyy-MM-dd") + " 00:00:00";

        }
        String endDate = jsonObject.getString("endDate");
        if (endDate != null && !endDate.equals("")) {
            endDate = UTCToCST(endDate, "yyyy-MM-dd") + " 23:59:59";
        }
        int login = 0;
        int each = 0;
        int error = 0;
        int redis = 0;
        logger.info("获取到的type:" + type);
        if ("0".equals(type)) {
            login = logMapper.getLogHourCount("0");
            each = logMapper.getLogHourCount("1");
            error = logMapper.getLogHourCount("2");
            redis = logMapper.getLogHourCount("3");
            //登陆量：actiontype 0
            //交互量 ：actiontype 1
            //出错量：log_level error
            //redis监控量：type 1  type 为0时是app的redis监控

        } else if ("1".equals(type)) {
            login = logMapper.getLogWeekCount("0");
            each = logMapper.getLogWeekCount("1");
            error = logMapper.getLogWeekCount("2");
            redis = logMapper.getLogWeekCount("3");
            //本周
        } else if ("2".equals(type)) {
            //本月
            login = logMapper.getLogMonthCount("0");
            each = logMapper.getLogMonthCount("1");
            error = logMapper.getLogMonthCount("2");
            redis = logMapper.getLogMonthCount("3");
        } else if ("3".equals(type)) {
            login = logMapper.getLogDateCount("0", startDate, endDate);
            each = logMapper.getLogDateCount("1", startDate, endDate);
            error = logMapper.getLogDateCount("2", startDate, endDate);
            redis = logMapper.getLogDateCount("3", startDate, endDate);
        }
        map.put("msg", "查询成功");
        map.put("code", 200);
        map.put("login", login);
        map.put("each", each);
        map.put("error", error);
        map.put("redis", redis);
        return map;
    }

    private Map<String, Object> getLogCharts(String param) {
        Map<String, Object> map = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(param);
        String type = jsonObject.getString("type");   //type 0:本日，1：本周 2：本月 3:时间段
        String actionType = jsonObject.getString("dataType"); //dataType  0:系统登陆量  1：系统交互量 2：系统出错量 3.redis监控量
        String startDate = jsonObject.getString("startDate");

        if (startDate != null && !startDate.equals("")) {
            startDate = UTCToCST(startDate, "yyyy-MM-dd") + " 00:00:00";
        }

        String endDate = jsonObject.getString("endDate");
        if (endDate != null && !endDate.equals("")) {
            endDate = UTCToCST(endDate, "yyyy-MM-dd") + " 23:59:59";
        }
        String sql = "";
        List<LogHourData> log = null;
        logger.info("获取到的actionType:" + actionType + ",type:" + type);
        //本日
        if ("0".equals(type)) {
            //登陆量：actiontype 0
            //交互量 ：actiontype 1
            //出错量：log_level error
            //redis监控量：type 1  type 为0时是app的redis监控
            log = logMapper.getLogHour(actionType);
        } else if ("1".equals(type)) {
            //本周
            log = logMapper.getLogWeek(actionType);

        } else if ("2".equals(type)) {
            log = logMapper.getLogMonth(actionType);
        } else if ("3".equals(type)) {
            log = logMapper.getLogDate(actionType, startDate, endDate);
        }
        map.put("resultData", log);
        map.put("msg", "查询成功");
        map.put("code", 200);
        map.put("count", log.size());
        return map;
    }

    private Map<String, Object> getLogInfo(String param) {
        Map<String, Object> map = new HashMap<>();
        PageVo pageVo = JSONObject.parseObject(param, PageVo.class);   //把前端传过来的数据转化为object类型

        PageUtil pageUtil = pageVo.getPage();//获取前端的页面分页信息
        int totalNum = logMapper.getPageTotal(pageVo);
        if (totalNum > 0) {
            List<Loginfo> logInfoList = logMapper.getLogInfo(pageVo);
            //返回参数
            //然后在使用pagevo类里面的datalist数组存放返回来的数组
            map.put("resultData", logInfoList);
            map.put("msg", "查询成功");
            map.put("code", 200);
            map.put("count", totalNum);
        }
        return map;
    }

    private Map<String, Object> getLoginfoNum(String param) {


        Map<String, Object> logNum = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(param);
        String type = jsonObject.getString("type");
        String actionType = jsonObject.getString("actionType");
        String logLevel = jsonObject.getString("logLevel");


        return logNum;
    }

    private Map<String, Object> getLoginfoList(String param) {
        Map<String, Object> loginfoListMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(param);
        String type = jsonObject.getString("type");
        String actionType = jsonObject.getString("actionType");
        String logLevel = jsonObject.getString("logLevel");

        List<Loginfo> loginfoList = LoginfoMapper.getTopNumInfo(actionType, logLevel);
        List<Loginfo> newLoginfoList = new ArrayList<>();
        for (int i = 0; i < loginfoList.size(); i++) {
            Loginfo info = loginfoList.get(i);
            newLoginfoList.add(info);

        }
        loginfoListMap.put("loginfoList", newLoginfoList);
        return loginfoListMap;
    }

    public static String UTCToCST(String UTCStr, String format) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            date = sdf.parse(UTCStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("UTC时间: " + date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + 8);

        String date1 = DateUtils.formatDate(calendar.getTime(), "yyyy-MM-dd").toString();
        //calendar.getTime() 返回的是Date类型，也可以使用calendar.getTimeInMillis()获取时间戳


        return date1;
    }
}
