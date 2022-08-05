package com.cloud.servicemanage.service.activitiRest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.activiti.ActivitiAuthUserUtil;
import com.cloud.commonsmng.activiti.ActivitiProcessUtil;
import com.cloud.commonsmng.activiti.model.ActivitiUser;
import com.cloud.commonsmng.activiti.model.ResponseModel;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.factory.BaseService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("activiti-process")
public class TestActivitiProcess extends BaseService {

    public Map<String, Object> exec(String method, String param) {
        Map<String, Object> obj = new HashMap<>();
        switch (method) {
            case "processStart":
                obj = processStart(param);
                break;
            case "processSetVariable":
                obj = processSetVariable(param);
                break;
            case "processDelete":
                obj = processDelete(param);
                break;
            default:
                    break;
        }

        return obj;
    }

    /**
     * 启动流程
     *
     * @param param
     * @return
     */
    private Map<String, Object> processStart(String param) {
        Map<String, Object> result = new HashMap<>();

        // 解析前端json
        JSONObject jsonObject = JSONObject.parseObject(param);
        String userName = jsonObject.getString("userId");
        String userGroup = jsonObject.getString("userRoles");

        // 注册当前登录用户作为流程启动人
        ActivitiUser activitiUser = new ActivitiUser(userName, userName, userGroup);
        List<ActivitiUser> registerUserList = new ArrayList<ActivitiUser>();
        registerUserList.add(activitiUser);
        ResponseModel responseModel = ActivitiAuthUserUtil.registerUsers(JSONArray.parseArray(JSON.toJSONString(registerUserList)));
        if (responseModel.isSuccess()) {
            // 2、使用当前登录用户启动流程
            // 解析前端json
            Map<String, Object> jsonMap = new HashMap<String, Object>() ;

            jsonMap.put("processInstanceName", jsonObject.getString("processInstanceName"));
            jsonMap.put("processDefinitionKey", jsonObject.getString("processDefinitionKey"));
            jsonMap.put("variables", (Map) JSONObject.parseObject(jsonObject.getString("variables")));

            // activiti-springsecurity验证
            jsonMap.put("userName", activitiUser.getName());
            jsonMap.put("userGroup", activitiUser.getGroup());

            responseModel = ActivitiProcessUtil.processStart(JSONObject.parseObject(JSON.toJSONString(jsonMap)), activitiUser);
            if (responseModel.isSuccess()) {
                Constants.getSuccMsg(result, JSONObject.parseObject(responseModel.getRestBodyString()));
            } else {
                Constants.getErrMsg(result, Constants.RESULT_NO_DATA);
            }
        } else {
            Constants.getErrMsg(result, Constants.RESULT_NO_DATA);
        }

        return result;
    }

    /**
     * 删除流程
     *
     * @param param
     * @return
     */
    private Map<String, Object> processDelete(String param) {
        Map<String, Object> result = new HashMap<>();

        // 1、注册当前登录用户作为流程删除人
        // 解析前端json
        JSONObject jsonObject = JSONObject.parseObject(param);
        String userName = jsonObject.getString("userId");
        String userGroup = jsonObject.getString("userRoles");

        ActivitiUser activitiUser = new ActivitiUser(userName, userName, userGroup);
        List<ActivitiUser> registerUserList = new ArrayList<ActivitiUser>();
        registerUserList.add(activitiUser);

        ResponseModel responseModel = ActivitiAuthUserUtil.registerUsers(JSONArray.parseArray(JSON.toJSONString(registerUserList)));
        if (responseModel.isSuccess()) {
            // 2、使用当前登录用户删除流程
            // 解析前端json
            Map<String, Object> jsonMap = new HashMap<String, Object>();

            jsonMap.put("processInstanceId", jsonObject.getString("processInstanceId"));    // 流程实例Id
            jsonMap.put("deleteReason", jsonObject.getString("deleteReason"));              // 流程删除原因

            // activiti-springsecurity验证
            jsonMap.put("userName", activitiUser.getName());
            jsonMap.put("userGroup", activitiUser.getGroup());
            responseModel = ActivitiProcessUtil.processDelete(JSONObject.parseObject(JSON.toJSONString(jsonMap)), activitiUser);
            if (responseModel.isSuccess()) {
                Constants.getSuccMsg(result, JSONObject.parseObject(responseModel.getRestBodyString()));
            } else {
                Constants.getErrMsg(result, Constants.RESULT_NO_DATA);
            }
        }

        return result;
    }

    /**
     * 设置流程参数（设置各级审批人）
     *
     * @param param
     * @return
     */
    private Map<String, Object> processSetVariable(String param) {
        Map<String, Object> result = new HashMap<>();

        // 1、注册当前登录用户作为流程删除人
        // 解析前端json
        JSONObject jsonObject = JSONObject.parseObject(param);
        String userName = jsonObject.getString("userId");
        String userGroup = jsonObject.getString("userRoles");

        ActivitiUser activitiUser = new ActivitiUser(userName, userName, userGroup);
        List<ActivitiUser> registerUserList = new ArrayList<ActivitiUser>() ;
        registerUserList.add(activitiUser);

        ResponseModel responseModel = ActivitiAuthUserUtil.registerUsers(JSONArray.parseArray(JSON.toJSONString(registerUserList)));
        if (responseModel.isSuccess()) {
            // 2、使用当前登录用户删除流程
            // 解析前端json
            Map<String, Object> jsonMap = new HashMap<String, Object>();
            jsonMap.put("processInstanceId", jsonObject.getString("processInstanceId"));// 流程实例Id
            HashMap<String, Object> Hash=new HashMap<String, Object>();

            Hash.put("applyUserId", userName);                                        // 设置流程启动人
            Hash.put("zhjbrUserList", jsonObject.getString("zhjbrUserList"));    // 设置"支行经办人"
            Hash.put("zhbgsUserList", jsonObject.getString("zhbgsUserList"));    // 设置"总行办公室"
            jsonMap.put("variables", Hash);

            // activiti-springsecurity验证
            jsonMap.put("userName", activitiUser.getName());
            jsonMap.put("userGroup", activitiUser.getGroup());
            responseModel = ActivitiProcessUtil.processSetVariables(JSONObject.parseObject(JSON.toJSONString(jsonMap)), activitiUser);
            if (responseModel.isSuccess()) {
                Constants.getSuccMsg(result, JSONObject.parseObject(responseModel.getRestBodyString()));
            } else {
                Constants.getErrMsg(result, Constants.RESULT_NO_DATA);
            }
        }


        return result;
    }
}
