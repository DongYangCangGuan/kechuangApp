package com.cloud.servicemanage.service.activitiRest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.activiti.ActivitiDeployUtil;
import com.cloud.commonsmng.activiti.ActivitiUploadBPMNUtil;
import com.cloud.commonsmng.activiti.model.ResponseModel;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.factory.BaseService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("activiti-deploy")
public class TestActivitiDeploy extends BaseService {
    public Map<String, Object> exec(String method, String param) {
        Map<String, Object> obj = new HashMap<String, Object>();
        switch (method) {
            case "search":
                obj = getProcessDefinitions(param);
                break;
            case "deploy":
                obj = deploy(param);
                break;
            case "undeploy":
                obj = undeploy(param);
                break;
            case "delete":
                obj = delete(param);
                break;
            default:
                break;
        }

        return obj;
    }

    /**
     * 查询已部署的流程文件
     *
     * @param param
     * @return
     */
    private Map<String, Object> getProcessDefinitions(String param) {
        Map<String, Object> result = new HashMap<>();

        ResponseModel responseModel = ActivitiDeployUtil.getProcessDefinitions();
        if (responseModel.isSuccess()) {
            // 拼装返回结果
            Constants.getSuccMsg(result, JSONArray.parseArray(responseModel.getRestBodyString()));
        } else {
            Constants.getErrMsg(result, Constants.RESULT_NO_DATA);
        }

        return result;
    }

    /**
     * 部署流程文件
     *
     * @param param
     * @return
     */
    private Map<String, Object> deploy(String param) {
        Map<String, Object> result = new HashMap<>();

        // 解析前端json
        JSONObject jsonObject = JSONObject.parseObject(param);
        String fileName = jsonObject.getString("fileName");

        ResponseModel responseModel = null;
        if (fileName.contains(".zip")) {    // 部署zip文件
            responseModel = ActivitiDeployUtil.deployProcessZipFile(fileName);
        } else if (fileName.contains(".bpmn20.xml") || fileName.contains(".bpmn")) {    // 部署bpmn文件
            responseModel = ActivitiDeployUtil.deployProcessBpmnFile(fileName);
        }

        if (responseModel.isSuccess()) {
            // 拼装返回结果
            Constants.getSuccMsg(result, JSONObject.parseObject(responseModel.getRestBodyString()));
        } else {
            Constants.getErrMsg(result, Constants.RESULT_NO_DATA);
        }

        return result;
    }

    /**
     * 取消部署
     *
     * @param param
     * @return
     */
    private Map<String, Object> undeploy(String param) {
        Map<String, Object> result = new HashMap<>();

        // 解析前端json
        JSONObject jsonObject = JSONObject.parseObject(param);
        String processDefinitionId = jsonObject.getString("processDefinitionId");

        ResponseModel responseModel = ActivitiDeployUtil.deleteDeployProcess(processDefinitionId);
        if (responseModel.isSuccess()) {
            Constants.getSuccMsg(result, JSONObject.parseObject(responseModel.getRestBodyString()));
        } else {
            Constants.getErrMsg(result, Constants.RESULT_NO_DATA);
        }

        return result;
    }

    /**
     * 删除文件
     *
     * @param param
     * @return
     */
    private Map<String, Object> delete(String param) {
        Map<String, Object> result = new HashMap<>();

        // 解析前端json
        JSONObject jsonObject = JSONObject.parseObject(param);
        String fileName = jsonObject.getString("fileName");

        ResponseModel responseModel = ActivitiUploadBPMNUtil.deleteBpmnFile(fileName);
        if (responseModel.isSuccess()) {
            Constants.getSuccMsg(result, JSONObject.parseObject(responseModel.getRestBodyString()));
        } else {
            Constants.getErrMsg(result, Constants.RESULT_NO_DATA);
        }

        return result;
    }
}
