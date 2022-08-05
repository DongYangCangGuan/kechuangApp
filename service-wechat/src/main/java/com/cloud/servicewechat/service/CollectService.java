package com.cloud.servicewechat.service;

import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.entity.appletEntity.ActivityEntity;
import com.cloud.commonsmng.entity.appletEntity.Product;
import com.cloud.commonsmng.entity.appletEntity.User;
import com.cloud.commonsmng.factory.BaseService;
import com.cloud.commonsmng.entity.appletEntity.Collect;
import com.cloud.servicewechat.common.ConstantUtil;
import com.cloud.servicewechat.common.PageUtil;
import com.cloud.servicewechat.common.PageVo;
import com.cloud.servicewechat.mapper.CollectMapper;
import com.cloud.servicewechat.mapper.ReportMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("collect")
public class CollectService extends BaseService {

    @Autowired
    private CollectMapper collectMapper;

    @Autowired
    private ReportMapper reportMapper;

    //实例化日志对象，引用jar包org.slf4j.Logger
    private static final Logger logger = LoggerFactory.getLogger(CollectService.class);

    @Override
    public Map<String, Object> exec(String method, String param) {
        Map<String, Object> obj = new HashMap<>();
        switch (method) {
            case ConstantUtil.GET_COLLECT:
                obj = getCollect(param);
                break;
            case ConstantUtil.GET_COLLECT_DETAIL:
                obj = getCollectDetail(param);
                break;
            default:
                break;
        }
        return obj;
    }


    private Map<String, Object> getCollect(String param) {
        Map<String, Object> result = new HashMap<>();
        PageVo pageVo = JSONObject.parseObject(param, PageVo.class);//把前端传来的参数分装成分页类
        PageUtil pageUtil = pageVo.getPage();
        pageVo.setUserId(this.getUserInfo().getId());
        int totalNum = collectMapper.getPageTotal(pageVo);
        if (totalNum > 0) {
            List<Collect> collect = collectMapper.getCollectList(pageVo);
            PageVo<Collect> pageVo1 = new PageVo<>();
            pageVo1.setPage(new PageUtil(pageUtil.getPageIndex(), pageUtil.getPageSize(), totalNum));
            pageVo1.setDataList(collect);
            Constants.getSuccMsg(result, pageVo1);
        }
        return result;
    }


    private Map<String,Object>getCollectDetail( String param){
        Map<String,Object> result  = new HashMap<>();
        JSONObject json=JSONObject.parseObject(param);
        String productId =json.getString("productId");
        String userId =super.getUserInfo().getId();
        Collect c=collectMapper.getCollectDetail(productId,userId);
        Constants.getSuccMsg(result,c);
        return result;
    }

}
