package com.cloud.servicemanage.service;


import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.entity.appletEntity.Aboutus;
import com.cloud.commonsmng.factory.BaseService;
import com.cloud.servicemanage.common.ConstantUtil;
import com.cloud.servicemanage.mapper.AboutusMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("Aboutus")
public class AboutusService  extends BaseService {

    @Autowired
    private AboutusMapper aboutusMapper;

    @Override
    public Map<String, Object> exec(String method, String param) {
        Map<String, Object> obj = new HashMap<>();
        switch (method) {
            case ConstantUtil.GET_ABOUTUS: //getAboutus
                obj = getAboutus(param);
                break;
            case ConstantUtil.INSERT_ABOUTUS: //insertAboutus
                obj = insertAboutus(param);
                break;
            default:
                break;
        }
        return obj;
    }

    //查询关于我们
    private Map<String, Object> getAboutus(String param) {
        Map<String,Object> result = new HashMap<>();
        Aboutus aboutus = aboutusMapper.getAboutus();
        Constants.getSuccMsg(result,aboutus);
        return result;
    }

   //插入一条新的的关于我们的信息
    private Map<String, Object> insertAboutus(String param) {
            Map<String, Object> result = new HashMap<>();
        Aboutus aboutus = JSONObject.parseObject(param, Aboutus.class);
        super.insertBaseInfo(aboutus);

        int insert = aboutusMapper.insertAboutus(aboutus);
        Constants.getSuccMsg(result, insert > 0);
        return result;
    }

}
