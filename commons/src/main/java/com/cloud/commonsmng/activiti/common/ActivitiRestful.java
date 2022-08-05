package com.cloud.commonsmng.activiti.common;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.cloud.commonsmng.activiti.model.RequestModel;
import com.cloud.commonsmng.activiti.model.ResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


/**
 * activiti-restful的http请求工具类
 */
public class ActivitiRestful {
	public static final Logger logger = LoggerFactory.getLogger(ActivitiRestful.class);

	// service-workflow的地址
	public final static String URL = "http://localhost:8093/lightworkflow";

	// http请求参数
	private static final int TIMEOUT = 30000;// 超时
	private static final String CONTENTTYPE = "Content-type";
	private static final String CONTENTTYPEJSON = "application/json;charset=UTF-8";
	private static final String CHARUTF8 = "Utf-8";
	private static final char SPRIT = '/';

	/**
	 * 统一get请求
	 * 
	 * @param urlString	请求API
	 * @param requestModel	返回结果
	 * @return
	 */
	public static ResponseModel get(String urlString, RequestModel requestModel) {
		logger.info(urlString + "的get请求开始************************");

		ResponseModel respModel = new ResponseModel(ResponseModel.RESP_SUCCCODE, ResponseModel.RESP_SUCCMSG);
		com.alibaba.fastjson.JSONObject resultJson = new com.alibaba.fastjson.JSONObject();

		// 设置请求参数
		Object tempObj = requestModel.getReqObj();
		StringBuilder reqUrl = new StringBuilder(URL).append(urlString);

		if (tempObj instanceof JSONArray) {
			JSONArray jsonArr = (JSONArray) tempObj;
			for (int i = 0; i < jsonArr.size(); i++) {
				reqUrl.append(SPRIT);
				reqUrl.append((jsonArr.get(i)).toString());
			}
		} else if (tempObj instanceof String) {
			reqUrl.append(SPRIT);
			reqUrl.append((String) tempObj);
		}
		logger.info("urlString 请求地址：" + reqUrl.toString());

		// 远程API调用
		HttpResponse httpResponse = HttpUtil.createRequest(Method.GET, reqUrl.toString())
				.basicAuth(requestModel.getAuthUsr().getName(), requestModel.getAuthUsr().getPassword())
				.timeout(TIMEOUT)
				.execute()
				.charset(CHARUTF8);
		if (httpResponse.getStatus() != 200) {
			respModel.setRespCode(ResponseModel.RESP_ERRCODE);
			respModel.setRespMsg(ResponseModel.RESP_ERRMSG);
		}
		resultJson.put(ResponseModel.REST_CODE, httpResponse.getStatus());
		resultJson.put(ResponseModel.REST_BODY, httpResponse.body());
		respModel.setRespBody(resultJson);

		logger.info(urlString + "的get请求结束************************");
		return respModel;
	}

	/**
	 * 统一post请求
	 * 
	 * @param urlString	远程调用的API
	 * @param requestModel	远程调用的返回结果
	 * @return
	 */
	public static ResponseModel post(String urlString, RequestModel requestModel) {
		logger.info(urlString + "的post请求开始************************");

		ResponseModel respModel = new ResponseModel(ResponseModel.RESP_SUCCCODE, ResponseModel.RESP_SUCCMSG);
		com.alibaba.fastjson.JSONObject resultJson = new com.alibaba.fastjson.JSONObject();

		// 设置请求参数
		Object tempObj = requestModel.getReqObj();
		StringBuilder reqUrl = new StringBuilder(URL).append(urlString);
		HttpRequest httpRequest = null;

		if (tempObj instanceof JSONObject) {
			httpRequest = HttpUtil.createRequest(Method.POST, reqUrl.toString())
					.timeout(TIMEOUT)
					.header(CONTENTTYPE, CONTENTTYPEJSON)
					.body((JSONObject) tempObj);
		} else if (tempObj instanceof JSONArray) {
			httpRequest = HttpUtil.createRequest(Method.POST, reqUrl.toString())
					.timeout(TIMEOUT)
					.header(CONTENTTYPE, CONTENTTYPEJSON)
					.body((JSONArray) tempObj);
		} else if (tempObj instanceof String) {
			reqUrl.append(SPRIT);
			reqUrl.append((String) tempObj);
			httpRequest = HttpUtil.createRequest(Method.POST, reqUrl.toString())
					.timeout(TIMEOUT)
					.header(CONTENTTYPE, CONTENTTYPEJSON);
		} else if (tempObj instanceof Map) {
			httpRequest = HttpUtil.createRequest(Method.POST, reqUrl.toString())
					.form((Map) tempObj)
					.timeout(TIMEOUT);
		}
		// basic auth
		httpRequest.basicAuth(requestModel.getAuthUsr().getName(), requestModel.getAuthUsr().getPassword());

		// 远程API调用
		HttpResponse httpResponse = httpRequest.execute().charset(CHARUTF8);
		if (httpResponse.getStatus() != 200) {
			respModel.setRespCode(ResponseModel.RESP_ERRCODE);
			respModel.setRespMsg(ResponseModel.RESP_ERRMSG);
		}
		resultJson.put(ResponseModel.REST_CODE, httpResponse.getStatus());
		resultJson.put(ResponseModel.REST_BODY, httpResponse.body());
		respModel.setRespBody(resultJson);

		logger.info(urlString + "的post请求结束************************");
		return respModel;
	}

	/**
	 * 统一delete请求
	 *
	 * @param urlString	请求API
	 * @param requestModel	返回结果
	 * @return
	 */
	public static ResponseModel delete(String urlString, RequestModel requestModel) {
		logger.info(urlString + "的delete请求开始************************");

		ResponseModel respModel = new ResponseModel(ResponseModel.RESP_SUCCCODE, ResponseModel.RESP_SUCCMSG);
		com.alibaba.fastjson.JSONObject resultJson = new com.alibaba.fastjson.JSONObject();

		// 设置请求参数
		Object tempObj = requestModel.getReqObj();
		StringBuilder reqUrl = new StringBuilder(URL).append(urlString);

		if (tempObj instanceof JSONArray) {
			JSONArray jsonArr = (JSONArray) tempObj;
			for (int i = 0; i < jsonArr.size(); i++) {
				reqUrl.append(SPRIT);
				reqUrl.append((jsonArr.get(i)).toString());
			}
		} else if (tempObj instanceof String) {
			reqUrl.append(SPRIT);
			reqUrl.append((String) tempObj);
		}
		logger.info("urlString 请求地址：" + reqUrl.toString());

		// 远程API调用
		HttpResponse httpResponse = HttpUtil.createRequest(Method.DELETE, reqUrl.toString())
				.basicAuth(requestModel.getAuthUsr().getName(), requestModel.getAuthUsr().getPassword())
				.timeout(TIMEOUT)
				.execute()
				.charset(CHARUTF8);
		if (httpResponse.getStatus() != 200) {
			respModel.setRespCode(ResponseModel.RESP_ERRCODE);
			respModel.setRespMsg(ResponseModel.RESP_ERRMSG);
		}
		resultJson.put(ResponseModel.REST_CODE, httpResponse.getStatus());
		resultJson.put(ResponseModel.REST_BODY, httpResponse.body());
		respModel.setRespBody(resultJson);

		logger.info(urlString + "的delete请求结束************************");
		return respModel;
	}
}
