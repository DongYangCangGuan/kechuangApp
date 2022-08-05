package com.cloud.servicemanage.service;

import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.factory.BaseService;
import com.cloud.commonsmng.entity.appletEntity.CustomizeQuestion;
import com.cloud.servicemanage.common.ConstantUtil;
import com.cloud.servicemanage.mapper.CustomizeQuestionMapper;
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
            case ConstantUtil.GET_CUSTOMIZE_QUESTION_LIST:
                obj = getCustomizeQuestionList();
                break;
            case ConstantUtil.ADD_CUSTOMIZE_QUESTION:
                obj = addCustomizeQuestion(param);
                break;
            case ConstantUtil.DELETE_CUSTOMIZE_QUESTION:
                obj = deleteCustomizeQuestion(param);
                break;
            case ConstantUtil.GET_CUSTOMIZE_QUESTION_BY_ID:
                obj = getCustomizeQuestionById(param);
                break;
            case ConstantUtil.UPDATE_CUSTOMIZE_QUESTION:
                obj = updateCustomizeQuestion(param);
                break;
            case ConstantUtil.GET_CUSTOMIZE_QUESTION_DETAIL_BY_ID:
                obj = getCustomizeQuestionDetailById(param);
                break;
            case ConstantUtil.RELEASE_CUSTOMIZE_QUESTION:
                obj = releaseCustomizeQuestion(param);
                break;
            default:
                break;
        }
        return obj;
    }

    // 发布
    private Map<String, Object> releaseCustomizeQuestion(String param) {
        Map<String, Object> result = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(param);

        String id = jsonObject.getString("id");
        CustomizeQuestion customizeQuestion = new CustomizeQuestion();
        customizeQuestion.setId(id);

        int delResult = customizeQuestionMapper.releaseCustomizeQuestion(customizeQuestion);

        Constants.getSuccMsg(result, delResult > 0);
        return result;

    }

    // 删除
    private Map<String, Object> deleteCustomizeQuestion(String param) {
        Map<String, Object> result = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(param);

        String id = jsonObject.getString("id");
        CustomizeQuestion customizeQuestion = new CustomizeQuestion();
        customizeQuestion.setId(id);

        int delResult = customizeQuestionMapper.deleteCustomizeQuestion(customizeQuestion);

        Constants.getSuccMsg(result, delResult > 0);
        return result;
    }

    //根据id查询
    private Map<String, Object> getCustomizeQuestionById(String param) {
        Map<String, Object> result = new HashMap<>();

        //获取前端传递的id
        String id = JSONObject.parseObject(param).getString("id");

        //根据id查询
        CustomizeQuestion customizeQuestion = customizeQuestionMapper.getCustomizeQuestionById(id);

        Constants.getSuccMsg(result, customizeQuestion);
        return result;
    }

    //修改
    private Map<String, Object> updateCustomizeQuestion(String param) {
        Map<String, Object> result = new HashMap<>();
        //转化json格式数据为Notes对象
        CustomizeQuestion customizeQuestion = JSONObject.parseObject(param, CustomizeQuestion.class);
        customizeQuestion.setModifierId(super.getUserInfo().getId());
        int updateNotes = customizeQuestionMapper.updateCustomizeQuestion(customizeQuestion);
        Constants.getSuccMsg(result, updateNotes > 0);
        return result;
    }

    // 添加
    private Map<String, Object> addCustomizeQuestion(String param) {
        Map<String, Object> result = new HashMap<>();
        CustomizeQuestion customizeQuestion = JSONObject.parseObject(param, CustomizeQuestion.class);

        customizeQuestion.setId(this.getIdWorker());
        customizeQuestion.setCreatorId(super.getUserInfo().getId());
        int insert = customizeQuestionMapper.insertCustomizeQuestion(customizeQuestion);
        Constants.getSuccMsg(result, insert > 0);
        return result;
    }

    //获取列表
    private Map<String, Object> getCustomizeQuestionList() {
        Map<String, Object> result = new HashMap<>();
        List<CustomizeQuestion> customizeQuestionList = customizeQuestionMapper.getCustomizeQuestionList();
        Constants.getSuccMsg(result, customizeQuestionList);
        return result;
    }

    // 查看详细
    private Map<String, Object> getCustomizeQuestionDetailById(String param) {
        Map<String, Object> result = new HashMap<>();

        //获取前端传递的id
        String id = JSONObject.parseObject(param).getString("id");

        //根据id查询
        CustomizeQuestion customizeQuestion = customizeQuestionMapper.getCustomizeQuestionDetailById(id);

        Constants.getSuccMsg(result, customizeQuestion);
        return result;
    }


}
