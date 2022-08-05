package com.cloud.servicewechat.service;

import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.entity.appletEntity.CustomizeAnswer;
import com.cloud.commonsmng.factory.BaseService;
import com.cloud.servicewechat.common.ConstantUtil;
import com.cloud.servicewechat.common.PageVo;
import com.cloud.servicewechat.mapper.CustomizeAnswerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("CustomizeAnswer")
public class CustomizeAnswerService extends BaseService {

    @Autowired
    private CustomizeAnswerMapper customizeAnswerMapper;

    @Override
    public Map<String, Object> exec(String method, String param) {

        Map<String, Object> obj = new HashMap<>();
        switch (method) {
            case ConstantUtil.GET_CUSTOMIZE_ANSWER_PAGE_VO_BY_QUESTION_ID: //getCustomizeAnswerPageVoByQuestionId
                obj = getCustomizeAnswerPageVoByQuestionId(param);
                break;
            case ConstantUtil.ADD_CUSTOMIZE_ANSWER:
                obj = addCustomizeAnswer(param);
                break;
            default:
                break;
        }
        return obj;
    }

    //根据问题编号查询问题的回答信息
    private Map<String, Object> getCustomizeAnswerPageVoByQuestionId(String param) {
        Map<String, Object> result = new HashMap<>();
        PageVo pageVo = JSONObject.parseObject(param, PageVo.class);
        List<CustomizeAnswer> customizeAnswerList = customizeAnswerMapper.getCustomizeAnswerPageVoByQuestionId(pageVo);
        Constants.getSuccMsg(result, customizeAnswerList);
        return result;
    }

    //提交问题的答案
    private Map<String, Object> addCustomizeAnswer(String param) {
        Map<String, Object> result = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(param);
        List<JSONObject> list = jsonObject.getObject("answerlist", List.class);
        List<CustomizeAnswer> answerList = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (JSONObject jo : list) {
                CustomizeAnswer answer = new CustomizeAnswer();
                super.insertBaseInfo(answer);
                answer.setAnswer(jo.getString("answer"));
                answer.setQuestionId(jo.getString("questionId"));
                answerList.add(answer);
            }
        }
        int i = customizeAnswerMapper.addCustomizeAnswer(answerList);
        Constants.getSuccMsg(result, i > 0);
        return result;
    }

}
