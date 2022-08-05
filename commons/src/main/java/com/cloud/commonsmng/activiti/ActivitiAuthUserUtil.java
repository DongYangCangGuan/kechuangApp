package com.cloud.commonsmng.activiti;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.activiti.common.ActivitiRestful;
import com.cloud.commonsmng.activiti.model.ActivitiUser;
import com.cloud.commonsmng.activiti.model.ReqType;
import com.cloud.commonsmng.activiti.model.RequestModel;
import com.cloud.commonsmng.activiti.model.ResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * activiti用户操作工具类
 */
public class ActivitiAuthUserUtil {
    public static final Logger logger = LoggerFactory.getLogger(ActivitiDeployUtil.class);

    /**
     * 注册单个用户
     *
     * @param user
     * @return
     */
    public static ResponseModel registerUser(JSONObject user) {
        ResponseModel result = new ResponseModel(ResponseModel.RESP_SUCCCODE, ResponseModel.RESP_SUCCMSG);

        try {
            RequestModel requestModel = new RequestModel(
                    ReqType.REQJSONOBJ,
                    user,
                    new ActivitiUser("system", "system")
            );
            result = ActivitiRestful.post("/registerUser", requestModel);

            logger.info(result.toString());
        } catch (Exception e) {
            result.setRespCode(ResponseModel.RESP_ERRCODE);
            result.setRespMsg(ResponseModel.RESP_ERRMSG);

            logger.error("ActivitiAuthUser registerUsers error: {}", e);
        }

        return result;
    }

    /**
     * 批量注册用户
     *
     * @param userArrs  用户列表
     * @return
     */
    public static ResponseModel registerUsers(JSONArray userArrs) {
        ResponseModel result = new ResponseModel(ResponseModel.RESP_SUCCCODE, ResponseModel.RESP_SUCCMSG);

        try {
            RequestModel requestModel = new RequestModel(
                    ReqType.REQJSONARR,
                    userArrs,
                    new ActivitiUser("system", "system")
            );
            result = ActivitiRestful.post("/usergroup/auth", requestModel);

            logger.info(result.toString());
        } catch (Exception e) {
            result.setRespCode(ResponseModel.RESP_ERRCODE);
            result.setRespMsg(ResponseModel.RESP_ERRMSG);

            logger.error("ActivitiAuthUser registerUsers error: {}", e);
        }

        return result;
    }

    /**
     * 查询activiti已注册的用户信息
     *
     * @param userName  用户名
     * @return
     */
    public static ResponseModel queryUser(String userName) {
        ResponseModel result = new ResponseModel(ResponseModel.RESP_SUCCCODE, ResponseModel.RESP_SUCCMSG);

        try {
            RequestModel requestModel = new RequestModel(
                    ReqType.REQPARAM,
                    userName,
                    new ActivitiUser("system", "system")
            );
            result = ActivitiRestful.get("/usergroup/auth/query", requestModel);
        } catch (Exception e) {
            result.setRespCode(ResponseModel.RESP_ERRCODE);
            result.setRespMsg(ResponseModel.RESP_ERRMSG);

            logger.error("ActivitiAuthUser registerUsers error: {}", e);
        }

        return result;
    }
}
