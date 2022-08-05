package com.cloud.servicewechat.service;

import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.entity.appletEntity.CustomizeQuestion;
import com.cloud.commonsmng.factory.BaseService;
import com.cloud.servicewechat.common.ConstantUtil;
import com.cloud.servicewechat.mapper.CustomizeQuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("customizeQuestion")
public class CustomizeQuestionService extends BaseService {

    @Autowired
    private CustomizeQuestionMapper customizeQuestionMapper;

    @Override
    public Map<String, Object> exec(String method, String param) {

        Map<String, Object> obj = new HashMap<>();
        switch (method) {
            case ConstantUtil.GET_QUESTION_LIST:
                obj = getQuestionList();
                break;
            default:
                break;
        }
        return obj;
    }

    // 获取所有问题
    private Map<String, Object> getQuestionList() {
        Map<String,Object> result = new HashMap<>();

        List<CustomizeQuestion> customizeQuestionList = customizeQuestionMapper.getQuestionList();

        Constants.getSuccMsg(result,customizeQuestionList);

        return result;
    }
}
