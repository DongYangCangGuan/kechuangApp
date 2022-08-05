package com.cloud.commonsmng.activiti.model;

import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.activiti.util.EmptyUtil;

/**
 * restful请求实体类
 */
public class RequestModel {
	// 参数类型
	private ReqType reqType;
	// 请求JSONObject
	private JSONObject reqJsonObj;
	// 请求JSONArray
	private JSONArray reqJsonArr;
	// 请求String
	private String reqParam;
	// 请求Map
	private Map<String,Object> reqMap;
	// 用户auth
	private ActivitiUser authUsr;


	/**
	 * 初始化参数类型为ReqJsonObj
	 *
	 * @param reqType
	 * @param reqJsonObj
	 * @param user
	 */
	public RequestModel(ReqType reqType, JSONObject reqJsonObj, ActivitiUser user) {
		this.reqType = reqType;
		this.reqJsonObj = reqJsonObj;
		this.authUsr = user;
	}

	/**
	 * 初始化参数类型为ReqJsonArr
	 *
	 * @param reqType
	 * @param reqJsonArr
	 * @param user
	 */
	public RequestModel(ReqType reqType, JSONArray reqJsonArr, ActivitiUser user) {
		this.reqType = reqType;
		this.reqJsonArr = reqJsonArr;
		this.authUsr = user;
	}

	/**
	 * 初始化参数类型为ReqSParam
	 *
	 * @param reqType
	 * @param reqParam
	 * @param user
	 */
	public RequestModel(ReqType reqType, String reqParam, ActivitiUser user) {
		this.reqType = reqType;
		this.reqParam = reqParam;
		this.authUsr = user;
	}

	/**
	 * 初始化参数类型为ReqMap
	 *
	 * @param reqType
	 * @param reqMap
	 * @param user
	 */
	public RequestModel(ReqType reqType, Map<String, Object> reqMap, ActivitiUser user) {
		super();
		this.reqType = reqType;
		this.reqMap = reqMap;
		this.authUsr = user;
	}

	public ReqType getReqType() {
		return reqType;
	}

	public void setReqType(ReqType reqType) {
		this.reqType = reqType;
	}

	public JSONObject getReqJsonObj() {
		return reqJsonObj;
	}

	public void setReqJsonObj(JSONObject reqJsonObj) {
		this.reqJsonObj = reqJsonObj;
	}

	public JSONArray getReqJsonArr() {
		return reqJsonArr;
	}

	public void setReqJsonArr(JSONArray reqJsonArr) {
		this.reqJsonArr = reqJsonArr;
	}

	public String getReqParam() {
		return reqParam;
	}

	public void setReqParam(String reqParam) {
		this.reqParam = reqParam;
	}

	public Map<String, Object> getReqMap() {
		return reqMap;
	}

	public void setReqMap(Map<String, Object> reqMap) {
		this.reqMap = reqMap;
	}

	public ActivitiUser getAuthUsr() {
		return authUsr;
	}

	public void setAuthUsr(ActivitiUser authUsr) {
		this.authUsr = authUsr;
	}

	/**
	 * 获取请求参数
	 * com.alibaba.fastjson.JSONObject-->cn.hutool.json.JSONObject
	 *
	 * @return
	 */
	public Object getReqObj() {
		Object obj = new Object();

		switch (this.reqType) {
			case REQJSONOBJ:
				if (this.reqJsonObj != null)
					obj = new cn.hutool.json.JSONObject(this.reqJsonObj.toString());
				break;
			case REQJSONARR:
				if (this.reqJsonArr != null)
					obj = new cn.hutool.json.JSONArray(this.reqJsonArr.toString());
				break;
			case REQPARAM:
				if(EmptyUtil.isNotEmptyObject(this.reqParam))
					obj = this.reqParam;
				break;
			case REQMAP:
				if(EmptyUtil.isNotEmptyMap(this.reqMap))
					obj = this.reqMap;
				break;
			default:
				break;
		}

		return obj;
	}

	@Override
	public String toString() {
		return "RequestModel [reqType=" + reqType + ", reqJsonObj=" + reqJsonObj + ", reqJsonArr=" + reqJsonArr
				+ ", reqParam=" + reqParam + ", reqMap=" + reqMap + "]";
	}
}
