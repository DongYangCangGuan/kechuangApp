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
 * activiti流程部署操作工具类
 */
public class ActivitiDeployUtil {
    public static final Logger logger = LoggerFactory.getLogger(ActivitiDeployUtil.class);

    /**
     * 查询已部署的流程
     *
     * @return
     */
    public static ResponseModel getProcessDefinitions() {
        ResponseModel result = new ResponseModel(ResponseModel.RESP_SUCCCODE, ResponseModel.RESP_SUCCMSG);

        try {
            RequestModel requestModel = new RequestModel(
                    ReqType.REQJSONOBJ,
                    new JSONObject(),
                    new ActivitiUser("system", "system")
            );
            result = ActivitiRestful.post("/process-definitions", requestModel);

            logger.info(result.toString());
        } catch (Exception e) {
            result.setRespCode(ResponseModel.RESP_ERRCODE);
            result.setRespMsg(ResponseModel.RESP_ERRMSG);

            logger.error("ActivitiDeployProcess getProcessDefinitions error: {}", e);
        }

        return result;
    }

    /**
     * 部署bpmn文件
     *
     * @param bpmnFiles 文件名可为多个“，”隔开
     * @return
     */
    public static ResponseModel deployProcessBpmnFile(String bpmnFiles) {
        ResponseModel result = new ResponseModel(ResponseModel.RESP_SUCCCODE, ResponseModel.RESP_SUCCMSG);

        try {
            RequestModel requestModel = new RequestModel(
                    ReqType.REQPARAM,
                    bpmnFiles,
                    new ActivitiUser("system", "system")
            );
            result = ActivitiRestful.post("/deploy-process-bpmn", requestModel);

            logger.info(result.toString());
        } catch (Exception e) {
            result.setRespCode(ResponseModel.RESP_ERRCODE);
            result.setRespMsg(ResponseModel.RESP_ERRMSG);

            logger.error("ActivitiDeployProcess deployProcessBpmnFile error: {}", e);
        }

        return result;
    }

    /**
     * 部署zip文件
     *
     * @param zipFile
     * @return
     */
    public static ResponseModel deployProcessZipFile(String zipFile) {
        ResponseModel result = new ResponseModel(ResponseModel.RESP_SUCCCODE, ResponseModel.RESP_SUCCMSG);

        try {
            RequestModel requestModel = new RequestModel(
                    ReqType.REQPARAM,
                    zipFile,
                    new ActivitiUser("system", "system")
            );
            result = ActivitiRestful.post("/deploy-process-zip", requestModel);

            logger.info(result.toString());
        } catch (Exception e) {
            result.setRespCode(ResponseModel.RESP_ERRCODE);
            result.setRespMsg(ResponseModel.RESP_ERRMSG);

            logger.error("ActivitiDeployProcess deployProcessZipFile error: {}", e);
        }

        return result;
    }

    /**
     * 删除流程部署
     *
     * @param processDefinitionId
     * @return
     */
    public static ResponseModel deleteDeployProcess(String processDefinitionId) {
        ResponseModel result = new ResponseModel(ResponseModel.RESP_SUCCCODE, ResponseModel.RESP_SUCCMSG);

        try {
            RequestModel requestModel = new RequestModel(
                    ReqType.REQPARAM,
                    processDefinitionId,
                    new ActivitiUser("system", "system")
            );
            result = ActivitiRestful.delete("/delete-deploy-process", requestModel);

            logger.info(result.toString());
        } catch (Exception e) {
            result.setRespCode(ResponseModel.RESP_ERRCODE);
            result.setRespMsg(ResponseModel.RESP_ERRMSG);

            logger.error("ActivitiDeployProcess deleteDeployProcess error: {}", e);
        }

        return result;
    }
}
