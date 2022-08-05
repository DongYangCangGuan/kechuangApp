package com.cloud.servicewechat.service;


import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.entity.appletEntity.*;
import com.cloud.commonsmng.factory.BaseService;
import com.cloud.servicewechat.common.ConstantUtil;
import com.cloud.servicewechat.common.PageUtil;
import com.cloud.servicewechat.common.PageVo;
import com.cloud.servicewechat.mapper.HomeMapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service("home")
public class HomeService extends BaseService {

    @Autowired
    private HomeMapper homeMapper;

    //实例化日志对象，引用jar包org.slf4j.Logger
    private static final Logger logger = LoggerFactory.getLogger(HomeService.class);

    @Override
    public Map<String, Object> exec(String method, String param) {
        Map<String, Object> obj = new HashMap<>();
        switch (method) {
            case ConstantUtil.GET_BANNER:
                obj = getBanner(param);
                break;
            case ConstantUtil.GET_NEWS:
                obj = getNews(param);
                break;
            case ConstantUtil.ADD_COLLECT:
                obj = addCollect(param);
                break;
            case ConstantUtil.DEL_COLLECT:
                obj = deleteCollect(param);
                break;
            case ConstantUtil.GET_ACTIVITY:
                obj = getActivity(param);
                break;
            case ConstantUtil.SIGN_UP:
                obj = signUp(param);
                break;
            case ConstantUtil.GET_MEMBER_NAME:
                obj = getMemberName(param);
                break;
            default:
                break;
        }
        return obj;
    }

    //获取首页banner
    private Map<String, Object> getBanner(String param) {
        Map<String, Object> result = new HashMap<>();
        List<BannerEntity>  list = homeMapper.getBanner();
        Constants.getSuccMsg(result, list);
        logger.info(String.format("banner图查询结果", result));
        return result;
    }

    //获取首页新闻
    private Map<String, Object> getNews(String param) {
        Map<String, Object> result = new HashMap<>();
        PageVo pageVo = JSONObject.parseObject(param, PageVo.class);//把前端传来的参数分装成分页类
        PageUtil pageUtil = pageVo.getPage();
        int totalNum = homeMapper.getNewsCount(pageVo);
        if (totalNum > 0) {
            List<NewsEntity> answer = homeMapper.getNews(pageVo);
            PageVo<NewsEntity> pageVo1 = new PageVo<>();
            pageVo1.setPage(new PageUtil(pageUtil.getPageIndex(), pageUtil.getPageSize(), totalNum));
            pageVo1.setDataList(answer);
            Constants.getSuccMsg(result, pageVo1);
        }
        logger.info(String.format("新闻查询结果", result));
        return result;
    }

    //新闻添加收藏
    private Map<String, Object> addCollect(String param) {
        Map<String, Object> result = new HashMap<>();
        JSONObject json = JSONObject.parseObject(param);
        String userId = json.getString("userId");
        String productId = json.getString("productId");
        homeMapper.addCollect(userId,productId);
        Constants.getSuccMsg(result, null);
        logger.info(String.format("新闻收藏成功", result));
        return result;
    }

    //删除收藏
    private Map<String, Object> deleteCollect(String param) {
        Map<String, Object> result = new HashMap<>();
        JSONObject json = JSONObject.parseObject(param);
        String userId = json.getString("userId");
        String productId = json.getString("productId");
        homeMapper.deleteCollect(userId,productId);
        Constants.getSuccMsg(result, null);
        logger.info(String.format("产品取消收藏成功", result));
        return result;
    }

    //获取首页活动
    private Map<String, Object> getActivity(String param) {
        Map<String, Object> result = new HashMap<>();
        ActivityEntity act = JSONObject.parseObject(param,ActivityEntity.class);
        List<ActivityEntity>  list = homeMapper.getActivity(act);
        list.forEach((ActivityEntity a) -> {
           List<ActivityTemplate> atList =  homeMapper.getActivityTemplate(a.getId());
           a.setAcList(atList);
        });
        Constants.getSuccMsg(result, list);
        logger.info(String.format("活动信息查询", result));
        return result;
    }

    //根据用户ID获取公司
    private Map<String, Object> getMemberName(String param) {
        Map<String, Object> result = new HashMap<>();
        JSONObject json = JSONObject.parseObject(param);
        String userId = json.getString("userId");
        List<String>  list = homeMapper.getMemberName(userId);
        Constants.getSuccMsg(result, list);
        logger.info(String.format("活动信息查询", result));
        return result;
    }


    //首页活动报名
    private Map<String, Object> signUp(String param) {
        Map<String, Object> result = new HashMap<>();
        SignEntity se = JSONObject.parseObject(param, SignEntity.class);
        List<SignAppendixEntity> lse = se.getSignAppendixList();
        String uid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        se.setId(uid);

        String id = homeMapper.getSignUp(se);
        if(StringUtils.isNotBlank(id)){
            homeMapper.deleteSign(id);
        }
        homeMapper.signUp(se);
        if(null!=lse && lse.size()>=0){
            homeMapper.signUpAppendix(lse,uid);
        }

        List<SignDetail> lsd = se.getSdList();
        if(null!=lse && lse.size()>=0){
            lsd.forEach((SignDetail s) -> {
                s.setTemplateId(homeMapper.getAcTemplateBySeq(se.getActivityEventId(),s.getSeq()));
            });
            homeMapper.signDetail(lsd,uid);
        }
        Constants.getSuccMsg(result,null);
        logger.info("活动报名成功");
        return result;
    }


}
