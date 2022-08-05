package com.cloud.commonsmng.activiti;

import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.activiti.common.ActivitiRestful;
import com.cloud.commonsmng.activiti.model.ActivitiUser;
import com.cloud.commonsmng.activiti.model.ReqType;
import com.cloud.commonsmng.activiti.model.RequestModel;
import com.cloud.commonsmng.activiti.model.ResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * activiti任务操作工具类
 */
public class ActivitiTaskUtil {
    public static final Logger logger = LoggerFactory.getLogger(ActivitiProcessUtil.class);

    /**
     * 获取下一任务节点信息
     *
     * @param processInstanceId
     * @return
     */
    public static ResponseModel getNextTask(String processInstanceId) {
        ResponseModel result = new ResponseModel(ResponseModel.RESP_SUCCCODE, ResponseModel.RESP_SUCCMSG);

        try {
            RequestModel requestModel = new RequestModel(
                    ReqType.REQJSONOBJ,
                    processInstanceId,
                    new ActivitiUser("admin", "admin")
            );
            result = ActivitiRestful.post("/task/all-tasks", requestModel);

            logger.info(result.toString());
        } catch (Exception e) {
            result.setRespCode(ResponseModel.RESP_ERRCODE);
            result.setRespMsg(ResponseModel.RESP_ERRMSG);

            logger.error("ActivitiTaskUtil getNextTask error:{}", e);
        }

        return result;
    }

    /**
     * 查询指定用户下的任务项总数
     *
     * @param userObj
     * @param user
     * @return
     */
    public static ResponseModel taskCountQueryByUser(JSONObject userObj, ActivitiUser user) {
        ResponseModel result = new ResponseModel(ResponseModel.RESP_SUCCCODE, ResponseModel.RESP_SUCCMSG);

        try {
            RequestModel requestModel = new RequestModel(
                    ReqType.REQJSONOBJ,
                    userObj,
                    user
            );
            result = ActivitiRestful.post("/task/tasksCount", requestModel);

            logger.info(result.toString());
        } catch (Exception e) {
            result.setRespCode(ResponseModel.RESP_ERRCODE);
            result.setRespMsg(ResponseModel.RESP_ERRMSG);

            logger.error("ActivitiTaskUtil taskQueryByUser error:{}", e);
        }

        return result;
    }


        /**
         * 查询指定用户下的任务项
         *
         * @param userObj
         * @return
         */
    public static ResponseModel taskQueryByUser(JSONObject userObj, ActivitiUser user) {
        ResponseModel result = new ResponseModel(ResponseModel.RESP_SUCCCODE, ResponseModel.RESP_SUCCMSG);

        try {
            RequestModel requestModel = new RequestModel(
                    ReqType.REQJSONOBJ,
                    userObj,
                    user
            );
            result = ActivitiRestful.post("/task/tasks", requestModel);

            logger.info(result.toString());
        } catch (Exception e) {
            result.setRespCode(ResponseModel.RESP_ERRCODE);
            result.setRespMsg(ResponseModel.RESP_ERRMSG);

            logger.error("ActivitiTaskUtil taskQueryByUser error:{}", e);
        }

        return result;
    }

    /**
     * 用户签收任务
     *
     * @param taskObj
     * @param user
     * @return
     */
    public static ResponseModel taskClaim(JSONObject taskObj, ActivitiUser user) {
        ResponseModel result = new ResponseModel(ResponseModel.RESP_SUCCCODE, ResponseModel.RESP_SUCCMSG);

        try {
            RequestModel requestModel = new RequestModel(
                    ReqType.REQJSONOBJ,
                    taskObj,
                    user
            );
            result = ActivitiRestful.post("/task/claim", requestModel);

            logger.info(result.toString());
        } catch (Exception e) {
            result.setRespCode(ResponseModel.RESP_ERRCODE);
            result.setRespMsg(ResponseModel.RESP_ERRMSG);

            logger.error("ActivitiTaskUtil taskClaim error:{}", e);
        }

        return result;
    }

    /**
     * 用户完成任务
     *
     * @param taskObj
     * @param user
     * @return
     */
    public static ResponseModel taskComplete(JSONObject taskObj, ActivitiUser user) {
        ResponseModel result = new ResponseModel(ResponseModel.RESP_SUCCCODE, ResponseModel.RESP_SUCCMSG);

        try {
            RequestModel requestModel = new RequestModel(
                    ReqType.REQJSONOBJ,
                    taskObj,
                    user
            );
            result = ActivitiRestful.post("/task/complete", requestModel);

            logger.info(result.toString());
        } catch (Exception e) {
            result.setRespCode(ResponseModel.RESP_ERRCODE);
            result.setRespMsg(ResponseModel.RESP_ERRMSG);

            logger.error("ActivitiTaskUtil taskComplete error:{}", e);
        }

        return result;
    }
}
