package com.cloud.servicemanage.service;

import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.entity.appletEntity.*;
import com.cloud.commonsmng.entity.appletEntity.Dictionary;
import com.cloud.commonsmng.factory.BaseService;
import com.cloud.servicemanage.common.ConstantUtil;
import com.cloud.servicemanage.common.PageUtil;
import com.cloud.servicemanage.common.PageVo;
import com.cloud.servicemanage.mapper.ProductMapper;
import com.cloud.servicemanage.mapper.StatisticsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service("statistics")
public class StatisticsService extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(StatisticsService.class);

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");

    private static SimpleDateFormat daySdf = new SimpleDateFormat("yyyy-MM-dd");

    private static DecimalFormat df = new DecimalFormat("0.00%");

    @Autowired
    private StatisticsMapper statisticsMapper;

    @Override
    public Map<String, Object> exec(String method, String param) {
        Map<String, Object> obj = new HashMap<>();
        switch (method) {
            case ConstantUtil.GET_ACTIVE_USER:
                obj = getActiveUser(param);
                break;
            case ConstantUtil.GET_NEW_USER:
                obj = getNewUser(param);
                break;
            case ConstantUtil.ACTIVE_TREND:
                obj = activeTrend(param);
                break;
            case ConstantUtil.DAY_AVG_TIME:
                obj = dayAvgTime(param);
                break;
            case ConstantUtil.LOGIN_TIMES:
                obj = loginTimes(param);
                break;
            case ConstantUtil.EACH_AVG_TIME:
                obj = eachAvgTime(param);
                break;
            case ConstantUtil.GET_TIMES:
                obj = getTimes(param);
                break;
            case ConstantUtil.GET_STATISTICS:
                obj = getStatistics(param);
                break;
            case ConstantUtil.GET_PUBLISH:
                obj = getPublish(param);
                break;
            case ConstantUtil.GET_PERSONNEL_DIS:
                obj = getPersonnelDis(param);
                break;
            case ConstantUtil.GET_PRODUCT_DIS:
                obj = getProductDis(param);
                break;
            default:
                break;
        }
        return obj;
    }

    /**
     * 获取活跃用户
     * @param param
     * @return
     */
    private Map<String, Object> getActiveUser(String param){
        Map<String, Object> result = new HashMap<>();
        try {
            String  dayNumber = JSONObject.parseObject(param).getString("dayNumber");
            int dayNum = Integer.parseInt("-"+dayNumber);
            Calendar cal =Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH,dayNum);
            String agoDate =  daySdf.format(cal.getTime());
            String now = daySdf.format(new Date());
            int number =  statisticsMapper.getActiveUser(agoDate,now);

            result.put("beginDate",agoDate);
            result.put("nowDate",now);
            result.put("number",number);
        } catch (Exception e) {
            e.printStackTrace();
            Constants.getErrMsg(result,Constants.RESULT_NO_DATA);
        }
        return result;
    }

    /**
     * 获取新用户
     * @param param
     * @return
     */
    private Map<String, Object> getNewUser(String param){
        Map<String, Object> result = new HashMap<>();
        try {
            String  dayNumber = JSONObject.parseObject(param).getString("dayNumber");
            int dayNum = Integer.parseInt("-"+dayNumber);
            Calendar cal =Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH,dayNum);
            String agoDate =  daySdf.format(cal.getTime());
            String now = daySdf.format(new Date());
            int number =  statisticsMapper.getNewUser(agoDate,now);

            result.put("beginDate",agoDate);
            result.put("nowDate",now);
            result.put("number",number);
        } catch (Exception e) {
            e.printStackTrace();
            Constants.getErrMsg(result,Constants.RESULT_NO_DATA);
        }
        return result;
    }

    /**
     * 获取用户趋势
     * @param param
     * @return
     */
    private Map<String, Object> activeTrend(String param){
        Map<String, Object> result = new HashMap<>();
        try {
            List<PointEntity> thisList=  statisticsMapper.activeTrend("1");
            if(thisList ==null || thisList.size()<=0){

            }
            List<PointEntity> lastList=  statisticsMapper.activeTrend("2");
            List<PointEntity> lastYearList=  statisticsMapper.activeTrend("3");

            int max = 0;//最大
            int count =0;//总数
            int lastCount =0;//上个月
            int lastYearCount =0;//去年这个月

            for (PointEntity p : thisList) {
                if(p.getCount()>=max){
                    max = p.getCount();
                }
                count = count +p.getCount();
            }

            for (PointEntity p : lastList) {
                lastCount = lastCount +p.getCount();
            }

            for (PointEntity p : lastYearList) {
                lastYearCount = lastYearCount +p.getCount();
            }

            BigDecimal one = new BigDecimal(1);
            BigDecimal  average = new BigDecimal(count).divide(new BigDecimal(thisList.size()),BigDecimal.ROUND_UP);
            if(0==lastCount){
                result.put("chain",df.format(new BigDecimal(count)));
            }else{
                BigDecimal  chain =   (new BigDecimal(count).divide(new BigDecimal(lastCount))).subtract(one);
                result.put("chain",df.format(chain));
            }

            if(0==lastYearCount){
                result.put("chain",df.format(new BigDecimal(count)));
            }else{
                BigDecimal  yoy =   (new BigDecimal(count).divide(new BigDecimal(lastYearCount))).subtract(one);
                result.put("yearOnYear",df.format(yoy));
            }
            result.put("average",average);
            result.put("dataList",thisList);
            result.put("count",count);
            result.put("max",max);

        } catch (Exception e) {
            e.printStackTrace();
            Constants.getErrMsg(result,Constants.RESULT_NO_DATA);
        }
        return result;
    }


    /**
     * 获取每日平均时长
     * @param param
     * @return
     */
    private Map<String, Object> dayAvgTime(String param){
        Map<String, Object> result = new HashMap<>();
        try {
            List<PointEntity> thisList=  statisticsMapper.dayAvgTime("1");
            if(thisList ==null || thisList.size()<=0){

            }
            List<PointEntity> lastList=  statisticsMapper.dayAvgTime("2");
            List<PointEntity> lastYearList=  statisticsMapper.dayAvgTime("3");

            int max = 0;//最大
            int count =0;//总数
            int lastCount =0;//上个月
            int lastYearCount =0;//去年这个月

            for (PointEntity p : thisList) {
                if(p.getCount()>=max){
                    max = p.getCount();
                }
                count = count +p.getCount();
            }

            for (PointEntity p : lastList) {
                lastCount = lastCount +p.getCount();
            }

            for (PointEntity p : lastYearList) {
                lastYearCount = lastYearCount +p.getCount();
            }

            BigDecimal one = new BigDecimal(1);
            BigDecimal  average = new BigDecimal(count).divide(new BigDecimal(thisList.size()),BigDecimal.ROUND_UP);
            if(0==lastCount){
                result.put("chain",df.format(new BigDecimal(count)));
            }else{
                BigDecimal  chain =   (new BigDecimal(count).divide(new BigDecimal(lastCount))).subtract(one);
                result.put("chain",df.format(chain));
            }

            if(0==lastYearCount){
                result.put("chain",df.format(new BigDecimal(count)));
            }else{
                BigDecimal  yoy =   (new BigDecimal(count).divide(new BigDecimal(lastYearCount))).subtract(one);
                result.put("yearOnYear",df.format(yoy));
            }
            result.put("average",average);
            result.put("dataList",thisList);
            result.put("count",count);
            result.put("max",max);

        } catch (Exception e) {
            e.printStackTrace();
            Constants.getErrMsg(result,Constants.RESULT_NO_DATA);
        }
        return result;
    }


    /**
     * 登录次数
     * @param param
     * @return
     */
    private Map<String, Object> loginTimes(String param){
        Map<String, Object> result = new HashMap<>();
        try {
            List<PointEntity> thisList=  statisticsMapper.loginTimes("1");
            if(thisList ==null || thisList.size()<=0){

            }
            List<PointEntity> lastList=  statisticsMapper.loginTimes("2");
            List<PointEntity> lastYearList=  statisticsMapper.loginTimes("3");

            int max = 0;//最大
            int count =0;//总数
            int lastCount =0;//上个月
            int lastYearCount =0;//去年这个月

            for (PointEntity p : thisList) {
                if(p.getCount()>=max){
                    max = p.getCount();
                }
                count = count +p.getCount();
            }

            for (PointEntity p : lastList) {
                lastCount = lastCount +p.getCount();
            }

            for (PointEntity p : lastYearList) {
                lastYearCount = lastYearCount +p.getCount();
            }

            BigDecimal one = new BigDecimal(1);
            BigDecimal  average = new BigDecimal(count).divide(new BigDecimal(thisList.size()),BigDecimal.ROUND_UP);
            if(0==lastCount){
                result.put("chain",df.format(new BigDecimal(count)));
            }else{
                BigDecimal  chain =   (new BigDecimal(count).divide(new BigDecimal(lastCount))).subtract(one);
                result.put("chain",df.format(chain));
            }

            if(0==lastYearCount){
                result.put("chain",df.format(new BigDecimal(count)));
            }else{
                BigDecimal  yoy =   (new BigDecimal(count).divide(new BigDecimal(lastYearCount))).subtract(one);
                result.put("yearOnYear",df.format(yoy));
            }

            result.put("average",average);
            result.put("dataList",thisList);
            result.put("count",count);
            result.put("max",max);

        } catch (Exception e) {
            e.printStackTrace();
            Constants.getErrMsg(result,Constants.RESULT_NO_DATA);
        }
        return result;
    }


    /**
     * 平均单次使用时长
     * @param param
     * @return
     */
    private Map<String, Object> eachAvgTime(String param){
        Map<String, Object> result = new HashMap<>();
        try {
            List<PointEntity> thisList=  statisticsMapper.eachAvgTime("1");
            if(thisList ==null || thisList.size()<=0){

            }
            List<PointEntity> lastList=  statisticsMapper.eachAvgTime("2");
            List<PointEntity> lastYearList=  statisticsMapper.eachAvgTime("3");

            int max = 0;//最大
            int count =0;//总数
            int lastCount =0;//上个月
            int lastYearCount =0;//去年这个月

            for (PointEntity p : thisList) {
                if(p.getCount()>=max){
                    max = p.getCount();
                }
                count = count +p.getCount();
            }

            for (PointEntity p : lastList) {
                lastCount = lastCount +p.getCount();
            }

            for (PointEntity p : lastYearList) {
                lastYearCount = lastYearCount +p.getCount();
            }

            BigDecimal one = new BigDecimal(1);
            //当月平均值
            BigDecimal  average = new BigDecimal(count).divide(new BigDecimal(thisList.size()),BigDecimal.ROUND_UP);
            if(null==lastList || lastList.size()<=0){
                result.put("chain",df.format(new BigDecimal(count)));
            }else{
                //上月平均值
                BigDecimal  chainAverage = new BigDecimal(count).divide(new BigDecimal(lastList.size()),BigDecimal.ROUND_UP);
                BigDecimal  chain =   (average.divide(chainAverage)).subtract(one);
                result.put("chain",df.format(chain));
            }

            if(null==lastYearList || lastYearList.size()<=0){
                result.put("chain",df.format(new BigDecimal(count)));
            }else{
                //上年当月平均值
                BigDecimal  yoyAverage = new BigDecimal(count).divide(new BigDecimal(lastYearList.size()),BigDecimal.ROUND_UP);
                BigDecimal  yoy =   (average.divide(yoyAverage)).subtract(one);
                result.put("yearOnYear",df.format(yoy));
            }

            result.put("average",average);
            result.put("dataList",thisList);
            result.put("count",count);
            result.put("max",max);

        } catch (Exception e) {
            e.printStackTrace();
            Constants.getErrMsg(result,Constants.RESULT_NO_DATA);
        }
        return result;
    }


    /**
     * 获取活跃用户
     * @param param
     * @return
     */
    private Map<String, Object> getTimes(String param){
        Map<String, Object> result = new HashMap<>();
        String  type = JSONObject.parseObject(param).getString("type");
        try {
            int number =  statisticsMapper.getLoginTimes(type);
            result.put("number",number);
        } catch (Exception e) {
            e.printStackTrace();
            Constants.getErrMsg(result,Constants.RESULT_NO_DATA);
        }
        return result;
    }

    /**
     * 获取月度统计
     * @param param
     * @return
     */
    private Map<String, Object> getStatistics(String param){
        Map<String, Object> result = new HashMap<>();
        try {
            int readNum =  statisticsMapper.getReadStatistics();
            int collectNum =  statisticsMapper.getCollectNum();
            result.put("readNum",readNum);
            result.put("collectNum",collectNum);
        } catch (Exception e) {
            e.printStackTrace();
            Constants.getErrMsg(result,Constants.RESULT_NO_DATA);
        }
        return result;
    }

    /**
     * 获取产品发布统计
     * @param param
     * @return
     */
    private Map<String, Object> getPublish(String param){
        Map<String, Object> result = new HashMap<>();
        try {
            List<PointEntity> publish =  statisticsMapper.getPublish();
            result.put("publish",publish);
        } catch (Exception e) {
            e.printStackTrace();
            Constants.getErrMsg(result,Constants.RESULT_NO_DATA);
        }
        return result;
    }


    /**
     * 获取最近用户统计
     * @param param
     * @return
     */
    private Map<String, Object> getPersonnelDis(String param){
        Map<String, Object> result = new HashMap<>();
        try {
            List<PointEntity> publish =  statisticsMapper.getPersonnelDistribution();
            if(null == publish || publish.size()<=0){
                return result;
            }
            for (PointEntity p : publish) {
                if(null != p.getAddress() && "黑龙".equals(p.getAddress()) ){
                    p.setAddress("黑龙江");
                }
                if(null != p.getAddress() && "内蒙".equals(p.getAddress()) ){
                    p.setAddress("内蒙古");
                }
            }
            result.put("distribution",publish);
        } catch (Exception e) {
            e.printStackTrace();
            Constants.getErrMsg(result,Constants.RESULT_NO_DATA);
        }
        return result;
    }


    /**
     * 获取最近产品统计
     * @param param
     * @return
     */
    private Map<String, Object> getProductDis(String param){
        Map<String, Object> result = new HashMap<>();
        try {
            String  type = JSONObject.parseObject(param).getString("type");
            String child =  statisticsMapper.getChildList(type);
            if(child.startsWith(",")){
                child = child.substring(1);
            }
            String[] childs =  child.split(",");
            List<String> childList = Stream.of(childs).collect(Collectors.toList());

            List<PointEntity>  list =  statisticsMapper.getProductDistribution(childList);

            result.put("product",list);
        } catch (Exception e) {
            e.printStackTrace();
            Constants.getErrMsg(result,Constants.RESULT_NO_DATA);
        }
        return result;
    }

}
