package com.cloud.servicemanage.service.activitiRest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.activiti.ActivitiAuthUserUtil;
import com.cloud.commonsmng.activiti.ActivitiProcessUtil;
import com.cloud.commonsmng.activiti.ActivitiTaskUtil;
import com.cloud.commonsmng.activiti.model.ActivitiUser;
import com.cloud.commonsmng.activiti.model.ResponseModel;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.factory.BaseService;
import com.cloud.servicemanage.service.activitiRest.model.WorkFlowModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("activiti-workflow")
public class ActivitiWorkFlow extends BaseService {
    public Map<String, Object> exec(String method, String param) {
        Map<String, Object> obj = new HashMap<String, Object>();
        switch (method) {
            case "startProcess":
                obj = startProcess(param);
                break;
            case "getMyTask":
                obj = getMyTask(param);
                break;
            case "doProcess":
                obj = doProcess(param);
                break;
            case "stopProcess":
                obj = stopProcess(param);
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
    private Map<String, Object> startProcess(String param) {
        Map<String, Object> result = new HashMap<>();

        // 解析前端json
        // 参数：
        // processDefinitionKey、startUserId、startUserRoleId、variables(各级审批人设置)
        WorkFlowModel workFlowModel = JSONObject.parseObject(param, WorkFlowModel.class);

        // 1、注册用户
        ActivitiUser activitiUser = new ActivitiUser(workFlowModel.getStartUserId(), workFlowModel.getStartUserId(), workFlowModel.getStartUserRoleId());
        List<ActivitiUser> registerUserList = new ArrayList<ActivitiUser>();
        registerUserList.add(activitiUser);
        ActivitiAuthUserUtil.registerUsers(JSONArray.parseArray(JSON.toJSONString(registerUserList)));
        // 2、启动流程
        // 解析前端json
        Map<String, Object> processJsonMap = new HashMap<String, Object>();
        processJsonMap.put("processInstanceName", workFlowModel.getProcessDefinitionKey());
        processJsonMap.put("processDefinitionKey", workFlowModel.getProcessDefinitionKey());
        processJsonMap.put("variables", workFlowModel.getVariables());

        // activiti-springsecurity验证
        processJsonMap.put("userName", activitiUser.getName());
        processJsonMap.put("userGroup", activitiUser.getGroup());
        ResponseModel responseModel = ActivitiProcessUtil.processStart(JSONObject.parseObject(JSON.toJSONString(processJsonMap)), activitiUser);
        String processInstanceId = String.valueOf(JSONObject.parseObject(responseModel.getRestBodyString(), Map.class).get("id"));
        // 3、获取下一任务节点Id
        responseModel = ActivitiTaskUtil.getNextTask(processInstanceId);
        String nextTaskId = JSONObject.parseObject(responseModel.getRestBodyString()).getString("nextTaskId");

        // 设置返回值
        workFlowModel.setProcessInstanceId(processInstanceId);
        workFlowModel.setNextTaskId(nextTaskId);
        Constants.getSuccMsg(result, workFlowModel);

        return result;
    }

    /**
     * 获取用户代办
     *
     * @param param
     * @return
     */
    private Map<String, Object> getMyTask(String param) {
        Map<String, Object> result = new HashMap<>();

        return result;
    }

    /**
     * 处理任务
     *
     * @param param
     * @return
     */
    private Map<String, Object> doProcess(String param) {
        Map<String, Object> result = new HashMap<>();

        return result;
    }

    /**
     * 终止流程
     *
     * @param param
     * @return
     */
    private Map<String, Object> stopProcess(String param) {
        Map<String, Object> result = new HashMap<>();

        return result;
    }
}
