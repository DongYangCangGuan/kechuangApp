package com.cloud.servicemanage.service.activitiRest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.activiti.ActivitiAuthUserUtil;
import com.cloud.commonsmng.activiti.ActivitiTaskUtil;
import com.cloud.commonsmng.activiti.model.ActivitiUser;
import com.cloud.commonsmng.activiti.model.ResponseModel;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.factory.BaseService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("activiti-task")
public class TestActivitiTask extends BaseService {

    public Map<String, Object> exec(String method, String param) {
        Map<String, Object> obj = new HashMap<>();
        switch (method) {
            case "getUserTaskCount":
                obj = getUserTaskCount(param);
                break;
            case "getUserTask":
                obj = getUserTask(param);
                break;
            case "taskClaim":
                obj = taskClaim(param);
                break;
            case "taskComplete":
                obj = taskComplete(param);
                break;
            default:
                break;
        }

        return obj;
    }

    /**
     * 获取用户代办任务总数
     *
     * @param param
     * @return
     */
    private Map<String, Object> getUserTaskCount(String param) {
        Map<String, Object> result = new HashMap<>();

        // 解析前端返回json
        JSONObject jsonObject = JSONObject.parseObject(param);

        String userName = jsonObject.getString("userId");
        String userGroup = jsonObject.getString("roles");

        // 注册activiti用户
        ActivitiUser activitiUser = new ActivitiUser(userName, userName, userGroup);
        ResponseModel responseModel = ActivitiAuthUserUtil.registerUser(JSONObject.parseObject(JSON.toJSONString(activitiUser)));
        if (responseModel.isSuccess()) {
            // 查询该用户下的代办任务项总数
            Map<String, Object> jsonMap = new HashMap<String, Object>();
            jsonMap.put("userName", activitiUser.getName());
            jsonMap.put("userGroup", activitiUser.getGroup());

            responseModel = ActivitiTaskUtil.taskCountQueryByUser(JSONObject.parseObject(JSON.toJSONString(jsonMap)), activitiUser);
            if (responseModel.isSuccess()) {
                Constants.getSuccMsg(result, JSONObject.parseObject(responseModel.getRestBodyString()));
            } else {
                Constants.getErrMsg(result, Constants.RESULT_NO_DATA);
            }
        }

        return result;
    }

    /**
     * 获取用户任务（支持分页）
     *
     * @param param
     * @return
     */
    private Map<String, Object> getUserTask(String param) {
        Map<String, Object> result = new HashMap<>();

        // 解析前端返回json
        JSONObject jsonObject = JSONObject.parseObject(param);
        String userName = jsonObject.getString("userId");
        String userGroup = jsonObject.getString("roles");

        // 注册activiti用户
        ActivitiUser activitiUser = new ActivitiUser(userName, userName, userGroup);
        ResponseModel responseModel = ActivitiAuthUserUtil.registerUser(JSONObject.parseObject(JSON.toJSONString(activitiUser)));
        if (responseModel.isSuccess()) {
            // 查询该用户下的任务项
            Map<String, Object> jsonMap = new HashMap<String, Object>();
            jsonMap.put("userName", activitiUser.getName());
            jsonMap.put("userGroup", activitiUser.getGroup());
            jsonMap.put("startIndex", jsonObject.getString("startIndex"));
            jsonMap.put("maxItems", jsonObject.getString("maxItems"));

            responseModel = ActivitiTaskUtil.taskQueryByUser(JSONObject.parseObject(JSON.toJSONString(jsonMap)), activitiUser);
            if (responseModel.isSuccess()) {
                Constants.getSuccMsg(result, JSONObject.parseArray(responseModel.getRestBodyString()));
            } else {
                Constants.getErrMsg(result, Constants.RESULT_NO_DATA);
            }
        }

        return result;
    }

    /**
     * 用户签收任务
     *
     * @param param
     * @return
     */
    private Map<String, Object> taskClaim(String param) {
        Map<String, Object> result = new HashMap<>();

        // 解析前端返回json
        JSONObject jsonObject = JSONObject.parseObject(param);
        String userName = jsonObject.getString("userId");
        String userGroup = jsonObject.getString("roles");

        // 注册activiti用户
        ActivitiUser activitiUser = new ActivitiUser(userName, userName, userGroup);
        List<ActivitiUser> registerUserList = new ArrayList<ActivitiUser>();
        registerUserList.add(activitiUser);
        ResponseModel responseModel = ActivitiAuthUserUtil.registerUsers(JSONArray.parseArray(JSON.toJSONString(registerUserList)));
        if (responseModel.isSuccess()) {
            // 用户签收任务
            Map<String, Object> jsonMap = new HashMap<String, Object>();
            jsonMap.put("userName", activitiUser.getName());
            jsonMap.put("userGroup", activitiUser.getGroup());
            jsonMap.put("taskId", jsonObject.getString("taskId"));

            responseModel = ActivitiTaskUtil.taskClaim(JSONObject.parseObject(JSON.toJSONString(jsonMap)), activitiUser);
            if (responseModel.isSuccess()) {
                Constants.getSuccMsg(result, JSONObject.parse(responseModel.getRestBodyString()));
            } else {
                Constants.getErrMsg(result, Constants.RESULT_NO_DATA);
            }
        }

        return result;
    }

    /**
     * 用户完成任务
     *
     * @param param
     * @return
     */
    private Map<String, Object> taskComplete(String param) {
        Map<String, Object> result = new HashMap<>();

        // 解析前端返回json
        JSONObject jsonObject = JSONObject.parseObject(param);
        String userName = jsonObject.getString("userId");
        String userGroup = jsonObject.getString("roles");

        // 注册activiti用户
        ActivitiUser activitiUser = new ActivitiUser(userName, userName, userGroup);
        List<ActivitiUser> registerUserList = new ArrayList<ActivitiUser>();
        registerUserList.add(activitiUser);

        ResponseModel responseModel = ActivitiAuthUserUtil.registerUsers(JSONArray.parseArray(JSON.toJSONString(registerUserList)));
        if (responseModel.isSuccess()) {
            // 用户完成
            Map<String, Object> jsonMap = new HashMap<String, Object>();
            jsonMap.put("userName", activitiUser.getName());
            jsonMap.put("userGroup", activitiUser.getGroup());
            jsonMap.put("taskId", jsonObject.getString("taskId"));
            jsonMap.put("variables", (Map) JSONObject.parseObject(jsonObject.getString("variables")));  // 设置审批意见

            responseModel = ActivitiTaskUtil.taskComplete(JSONObject.parseObject(JSON.toJSONString(jsonMap)), activitiUser);
            if (responseModel.isSuccess()) {
                Constants.getSuccMsg(result, JSONObject.parse(responseModel.getRestBodyString()));
            } else {
                Constants.getErrMsg(result, Constants.RESULT_NO_DATA);
            }
        }

        return result;
    }
}
