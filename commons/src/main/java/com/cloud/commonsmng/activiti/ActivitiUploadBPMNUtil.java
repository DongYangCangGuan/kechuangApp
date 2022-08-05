package com.cloud.commonsmng.activiti;

import com.cloud.commonsmng.activiti.common.ActivitiRestful;
import com.cloud.commonsmng.activiti.model.ActivitiUser;
import com.cloud.commonsmng.activiti.model.ReqType;
import com.cloud.commonsmng.activiti.model.RequestModel;
import com.cloud.commonsmng.activiti.model.ResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * activiti文件上传操作工具类
 */
public class ActivitiUploadBPMNUtil {
    public static final Logger logger = LoggerFactory.getLogger(ActivitiUploadBPMNUtil.class);

    /**
     * 上传单个文件
     *
     * @param BpmnFileMap 需要上传的文件
     * @return
     */
    public static ResponseModel uploadBpmnFile(Map<String, Object> BpmnFileMap) {
        ResponseModel result = new ResponseModel(ResponseModel.RESP_SUCCCODE, ResponseModel.RESP_SUCCMSG);

        try {
            RequestModel requestModel = new RequestModel(
                    ReqType.REQMAP,
                    BpmnFileMap,
                    new ActivitiUser("system", "system")
            );
            result = ActivitiRestful.post("/upload", requestModel);

            logger.info(result.toString());
        } catch (Exception e) {
            result.setRespCode(ResponseModel.RESP_ERRCODE);
            result.setRespMsg(ResponseModel.RESP_ERRMSG);

            logger.error("ActivitiBpmnFile uploadBpmnFile error: {}", e);
        }

        return result;
    }

    /**
     * 删除文件
     *
     * @param fileNames 需要删除的文件名
     * @return
     */
    public static ResponseModel deleteBpmnFile(String fileNames) {
        ResponseModel result = new ResponseModel(ResponseModel.RESP_SUCCCODE, ResponseModel.RESP_SUCCMSG);

        try {
            RequestModel requestModel = new RequestModel(
                    ReqType.REQPARAM,
                    fileNames,
                    new ActivitiUser("system", "system")
            );
            result = ActivitiRestful.delete("/delete-file", requestModel);

            logger.info(result.toString());
        } catch (Exception e) {
            result.setRespCode(ResponseModel.RESP_ERRCODE);
            result.setRespMsg(ResponseModel.RESP_ERRMSG);

            logger.error("ActivitiBpmnFile deleteBpmnFiles error: {}", e);
        }

        return result;
    }
}
