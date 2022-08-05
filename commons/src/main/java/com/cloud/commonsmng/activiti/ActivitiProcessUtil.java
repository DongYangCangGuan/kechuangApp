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
 * activiti流程操作工具类
 */
public class ActivitiProcessUtil {
    public static final Logger logger = LoggerFactory.getLogger(ActivitiProcessUtil.class);

    /**
     * 启动流程
     *
     * @param processObj
     * @param user
     * @return
     */
    public static ResponseModel processStart(JSONObject processObj, ActivitiUser user){
        ResponseModel result = new ResponseModel(ResponseModel.RESP_SUCCCODE, ResponseModel.RESP_SUCCMSG);

        try {
            RequestModel requestModel = new RequestModel(
                    ReqType.REQJSONOBJ,
                    processObj,
                    user
            );
            result = ActivitiRestful.post("/process/start", requestModel);

            logger.info(result.toString());
        } catch (Exception e) {
            result.setRespCode(ResponseModel.RESP_ERRCODE);
            result.setRespMsg(ResponseModel.RESP_ERRMSG);

            logger.error("ActivitiProcessUtil processStart error:{}", e);
        }

        return result;
    }

    /**
     * 删除流程
     *
     * @param processObj
     * @param user
     * @return
     */
    public static ResponseModel processDelete(JSONObject processObj, ActivitiUser user){
        ResponseModel result = new ResponseModel(ResponseModel.RESP_SUCCCODE, ResponseModel.RESP_SUCCMSG);

        try {
            RequestModel requestModel = new RequestModel(
                    ReqType.REQJSONOBJ,
                    processObj,
                    user
            );
            result = ActivitiRestful.post("/process/delete", requestModel);

            logger.info(result.toString());
        } catch (Exception e) {
            result.setRespCode(ResponseModel.RESP_ERRCODE);
            result.setRespMsg(ResponseModel.RESP_ERRMSG);

            logger.error("ActivitiProcessUtil processDelete error:{}", e);
        }
        return result;
    }

    /**
     * 设置流程参数
     *
     * @param processObj
     * @param user
     * @return
     */
    public static ResponseModel processSetVariables(JSONObject processObj, ActivitiUser user) {
        ResponseModel result = new ResponseModel(ResponseModel.RESP_SUCCCODE, ResponseModel.RESP_SUCCMSG);

        try {
            RequestModel requestModel = new RequestModel(
                    ReqType.REQJSONOBJ,
                    processObj,
                    user
            );
            result = ActivitiRestful.post("/process/set-variables", requestModel);

            logger.info(result.toString());
        } catch (Exception e) {
            result.setRespCode(ResponseModel.RESP_ERRCODE);
            result.setRespMsg(ResponseModel.RESP_ERRMSG);

            logger.error("ActivitiProcessUtil processSetVariables error:{}", e);
        }

        return result;
    }
}
