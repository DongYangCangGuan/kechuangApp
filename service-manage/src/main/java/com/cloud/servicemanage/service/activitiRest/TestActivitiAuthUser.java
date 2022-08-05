package com.cloud.servicemanage.service.activitiRest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.activiti.ActivitiAuthUserUtil;
import com.cloud.commonsmng.activiti.model.ActivitiUser;
import com.cloud.commonsmng.activiti.model.ResponseModel;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.factory.BaseService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("activiti-user")
public class TestActivitiAuthUser extends BaseService {

    public Map<String, Object> exec(String method, String param) {
        Map<String, Object> obj = new HashMap<String, Object>();
        switch (method) {
            case "registerUser":
                obj = registerUser(param);
                break;
            case "queryUser":
                obj = queryUser(param);
                break;
            default:
                break;
        }

        return obj;
    }

    /**
     * activiti用户注册
     *
     * @param param
     * @return
     */
    private Map<String, Object> registerUser(String param) {
        Map<String, Object> result = new HashMap<>();

        // 解析前端json
        JSONObject jsonObject = JSONObject.parseObject(param);
        String userName = jsonObject.getString("userId");
        String userGroup = jsonObject.getString("userRoles");


        // 注册当前登录用户
        List<ActivitiUser> registerUserList = new ArrayList<ActivitiUser>();
        registerUserList.add(new ActivitiUser(userName, userName, userGroup));

        ResponseModel responseModel = ActivitiAuthUserUtil.registerUsers(
                JSONArray.parseArray(JSON.toJSONString(registerUserList))
        );
        if (responseModel.isSuccess()) {
            Constants.getSuccMsg(result, Constants.RESULT_NO_DATA);
        } else {
            Constants.getErrMsg(result, Constants.RESULT_NO_DATA);
        }

        return result;
    }

    /**
     * 查询activiti用户信息
     *
     * @param param
     * @return
     */
    private Map<String, Object> queryUser(String param) {
        Map<String, Object> result = new HashMap<>();

        // 解析前端json
        JSONObject jsonObject = JSONObject.parseObject(param);
        String userName = jsonObject.getString("userId");

        ResponseModel responseModel = ActivitiAuthUserUtil.queryUser(userName);
        if (responseModel.isSuccess()) {
            Constants.getSuccMsg(result, JSONObject.parseObject(responseModel.getRestBodyString()));
        } else {
            Constants.getErrMsg(result, Constants.RESULT_NO_DATA);
        }

        return result;
    }
}
