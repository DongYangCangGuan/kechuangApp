package com.cloud.servicewechat.service;

import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.entity.appletEntity.Dictionary;
import com.cloud.commonsmng.factory.BaseService;
import com.cloud.servicewechat.common.ConstantUtil;
import com.cloud.servicewechat.mapper.DictionaryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("dictionary")
public class DictionaryService extends BaseService {
    @Autowired
    private DictionaryMapper dictionaryMapper;

    //实例化日志对象，引用jar包org.slf4j.Logger
    private static final Logger logger = LoggerFactory.getLogger(NotesService.class);

    @Override
    public Map<String, Object> exec(String method, String param) {
        Map<String,Object> obj = new HashMap<>();
        switch (method){
            case ConstantUtil.SELECT_BY_KIND:
                obj = selectByKind(param);
            case ConstantUtil.SELECT_BY_PARENT:
                obj = selectByParent(param);
            default:
                break;
        }
        return obj;
    }


    //根据kind查询code和name
    private Map<String, Object> selectByKind(String param) {
        Map<String, Object> result = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(param);
        String kind = jsonObject.getString("kind");

        try {
            List<Dictionary> dictionaryList = dictionaryMapper.selectByKind(kind);
            if (dictionaryList!= null) {
                result.put("code", 200);
                result.put("msg", "succ");
                result.put("data", dictionaryList);
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            logger.error(e.getMessage());
        }
        return result;
    }

    //根据Parent查询
    private Map<String, Object> selectByParent(String param) {
        Map<String, Object> result = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(param);
        String parentId = jsonObject.getString("parentId");
        String kind = jsonObject.getString("kind");

        try {
            List<Dictionary> dictionaryList = dictionaryMapper.selectByParent(parentId,kind);
            if (dictionaryList!= null) {
                result.put("code", 200);
                result.put("msg", "succ");
                result.put("data", dictionaryList);
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            logger.error(e.getMessage());
        }
        return result;
    }

}
