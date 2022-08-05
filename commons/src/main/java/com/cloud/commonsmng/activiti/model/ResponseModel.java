package com.cloud.commonsmng.activiti.model;

import com.alibaba.fastjson.JSONObject;

/**
 * restful返回实体类
 *
 */
public class ResponseModel {
	public static final String RESP_SUCCCODE = "000000";
	public static final String RESP_SUCCMSG = "交易成功！";
	public static final String RESP_ERRCODE = "RESP00";
	public static final String RESP_ERRMSG = "系统错误，交易失败！";

	public static final String REST_CODE = "rest_code";
	public static final String REST_BODY = "rest_body";

	private String respCode;// 返回码
	private String respMsg;// 返回请求信息
	private JSONObject respBody;// 主体内容

	public ResponseModel(String respCode, String respMsg) {
		this.respCode = respCode;
		this.respMsg = respMsg;
	}

	public String getRespCode() {
		return respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	public String getRespMsg() {
		return respMsg;
	}

	public void setRespMsg(String respMsg) {
		this.respMsg = respMsg;
	}

	public JSONObject getRespBody() {
		return respBody;
	}

	public void setRespBody(JSONObject respBody) {
		this.respBody = respBody;
	}

	/**
	 * 判断restful交易是否成功
	 *
	 * @return
	 */
	public boolean isSuccess() {
		return (ResponseModel.RESP_SUCCCODE.equals(this.respCode)
				&& "200".equals(this.respBody.getString(ResponseModel.REST_CODE)));
	}

	/**
	 * 获取RESTful返回内容
	 *
	 * @return
	 */
	public String getRestBodyString() {
		return this.respBody.getString(ResponseModel.REST_BODY);
	}

	@Override
	public String toString() {
		return "ResponseModel [respCode=" + respCode + ", respMsg=" + respMsg + ", respBody=" + respBody + "]";
	}
}
