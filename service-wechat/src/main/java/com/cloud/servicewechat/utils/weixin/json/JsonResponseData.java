package com.cloud.servicewechat.utils.weixin.json;


import com.alibaba.fastjson.JSONObject;

/**
 * Created by psl on 2018-04-03.
 */
public class JsonResponseData {
  private static JSONObject gson=new JSONObject();

    //空构造方法
    public JsonResponseData(){}

    //构造方法
    public JsonResponseData(Boolean ret,String msg,int status, String message, Object data){
        this.ret = ret;
        this.msg = msg;
        this.code = status;
        this.message = message;
        this.data = data==null?new Object():data;
    }


    //是否成功
    private Boolean ret = false;

    //返回信息
    private String msg = null;

    //返回状态
    private int code = -100;

    //返回状态对应消息
    private String message = null;

    //返回数据
    private Object data = null;

    //重写toString方法
    public String toString(){
        return gson.toJSONString(this);
    }
}
