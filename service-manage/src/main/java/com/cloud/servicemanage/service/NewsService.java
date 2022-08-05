package com.cloud.servicemanage.service;

import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.entity.BaseUserInfo;
import com.cloud.commonsmng.entity.appletEntity.Dictionary;
import com.cloud.commonsmng.entity.appletEntity.NewsEntity;
import com.cloud.commonsmng.entity.appletEntity.User;
import com.cloud.commonsmng.factory.BaseService;
import com.cloud.servicemanage.common.ConstantUtil;
import com.cloud.servicemanage.common.PageUtil;
import com.cloud.servicemanage.common.PageVo;
import com.cloud.servicemanage.mapper.MemberMapper;
import com.cloud.servicemanage.mapper.NewsMapper;
import com.cloud.servicemanage.mapper.UsersMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("newsMange")
public class NewsService extends BaseService {


    @Autowired
    NewsMapper newsMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Override
    public Map<String, Object> exec(String method, String param) {
        Map<String, Object> obj = new HashMap<>();
        switch (method) {
            case ConstantUtil.GET_NEW_LIST: //
                obj = getNewsList(param);
                break;
            case ConstantUtil.INSERT_NEW: //
                obj = insertNews(param);
                break;
            case ConstantUtil.UPDATE_NEW: //
                obj = updateNews(param);
                break;
            case ConstantUtil.DELETE_NEW: //
                obj = deleteNews(param);
                break;
            default:
                break;
        }
        return obj;
    }


    /**
     * 获取
     *
     */
    private Map<String,Object> getNewsList(String param){
        Map<String, Object> result = new HashMap<>();
        try {
            PageVo pagevo = JSONObject.parseObject(param,PageVo.class);
            PageUtil page = pagevo.getPage();//获取前端的页面分页信息
            int pageTotal = newsMapper.getUserCount(pagevo);
            List<NewsEntity> list = newsMapper.getUserList(pagevo);
            PageVo<NewsEntity> pageVo = new PageVo<>();
            pageVo.setPage(new PageUtil(page.getPageIndex(), page.getPageSize(), pageTotal));
            pageVo.setDataList(list);
            Constants.getSuccMsg(result, pageVo);
        }catch (Exception e) {
            e.printStackTrace();
            Constants.getErrMsg(result, Constants.RESULT_NO_DATA);
        }

        return result;
    }

    /**
     * 新增
     *
     */
    private Map<String,Object> insertNews(String param){
        Map<String, Object> result = new HashMap<>();
        try {
            NewsEntity news = JSONObject.parseObject(param,NewsEntity.class);
            //获取图片服务器
           /* List<Dictionary> ds = memberMapper.getMemberTypelist("pictureaddress");
            String address = ds.get(0).getName();
            String pic = news.getNewsPic();
            news.setNewsPic(address + pic);*/
            int count = newsMapper.insertUser(news);
            Constants.getSuccMsg(result, count>0);
        }catch (Exception e) {
            e.printStackTrace();
            Constants.getErrMsg(result, Constants.RESULT_NO_DATA);
        }

        return result;
    }


    /**
     * 删除
     *
     */
    private Map<String,Object> deleteNews(String param){
        Map<String, Object> result = new HashMap<>();
        try {
            NewsEntity news =  JSONObject.parseObject(param,NewsEntity.class);
            int count = newsMapper.deleteUser(news.getId());
            Constants.getSuccMsg(result, count>0);
        }catch (Exception e) {
            e.printStackTrace();
            Constants.getErrMsg(result, Constants.RESULT_NO_DATA);
        }

        return result;
    }

    /**
     * 修改
     *
     */
    private Map<String,Object> updateNews(String param){
        Map<String, Object> result = new HashMap<>();
        try {
            NewsEntity news = JSONObject.parseObject(param,NewsEntity.class);
            //获取图片服务器
/*            List<Dictionary> ds = memberMapper.getMemberTypelist("pictureaddress");
            String address = ds.get(0).getName();
            String pic = news.getNewsPic();
            news.setNewsPic(address + pic);*/
            int count = newsMapper.updateUser(news);
            Constants.getSuccMsg(result, count>0);
        }catch (Exception e) {
            e.printStackTrace();
            Constants.getErrMsg(result, Constants.RESULT_NO_DATA);
        }

        return result;
    }

}

