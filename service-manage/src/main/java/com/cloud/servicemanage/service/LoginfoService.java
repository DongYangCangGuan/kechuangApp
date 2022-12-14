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
        String type = jsonObject.getString("type");   //type 0:?????????1????????? 2?????????  3:?????????
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
        logger.info("????????????type:" + type);
        if ("0".equals(type)) {
            login = logMapper.getLogHourCount("0");
            each = logMapper.getLogHourCount("1");
            error = logMapper.getLogHourCount("2");
            redis = logMapper.getLogHourCount("3");
            //????????????actiontype 0
            //????????? ???actiontype 1
            //????????????log_level error
            //redis????????????type 1  type ???0??????app???redis??????

        } else if ("1".equals(type)) {
            login = logMapper.getLogWeekCount("0");
            each = logMapper.getLogWeekCount("1");
            error = logMapper.getLogWeekCount("2");
            redis = logMapper.getLogWeekCount("3");
            //??????
        } else if ("2".equals(type)) {
            //??????
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
        map.put("msg", "????????????");
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
        String type = jsonObject.getString("type");   //type 0:?????????1????????? 2????????? 3:?????????
        String actionType = jsonObject.getString("dataType"); //dataType  0:???????????????  1?????????????????? 2?????????????????? 3.redis?????????
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
        logger.info("????????????actionType:" + actionType + ",type:" + type);
        //??????
        if ("0".equals(type)) {
            //????????????actiontype 0
            //????????? ???actiontype 1
            //????????????log_level error
            //redis????????????type 1  type ???0??????app???redis??????
            log = logMapper.getLogHour(actionType);
        } else if ("1".equals(type)) {
            //??????
            log = logMapper.getLogWeek(actionType);

        } else if ("2".equals(type)) {
            log = logMapper.getLogMonth(actionType);
        } else if ("3".equals(type)) {
            log = logMapper.getLogDate(actionType, startDate, endDate);
        }
        map.put("resultData", log);
        map.put("msg", "????????????");
        map.put("code", 200);
        map.put("count", log.size());
        return map;
    }

    private Map<String, Object> getLogInfo(String param) {
        Map<String, Object> map = new HashMap<>();
        PageVo pageVo = JSONObject.parseObject(param, PageVo.class);   //????????????????????????????????????object??????

        PageUtil pageUtil = pageVo.getPage();//?????????????????????????????????
        int totalNum = logMapper.getPageTotal(pageVo);
        if (totalNum > 0) {
            List<Loginfo> logInfoList = logMapper.getLogInfo(pageVo);
            //????????????
            //???????????????pagevo????????????datalist??????????????????????????????
            map.put("resultData", logInfoList);
            map.put("msg", "????????????");
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
        System.out.println("UTC??????: " + date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + 8);

        String date1 = DateUtils.formatDate(calendar.getTime(), "yyyy-MM-dd").toString();
        //calendar.getTime() ????????????Date????????????????????????calendar.getTimeInMillis()???????????????


        return date1;
    }
}
