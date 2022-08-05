package com.cloud.commonsmng.aop;

public class ResultVo<T> {

    public ResultVo(Integer code,String msg){

        this.code=code;
        this.msg=msg;
    }

    public ResultVo(Integer code,String msg,T data){

        this.code=code;
        this.msg=msg;
        this.data=data;

    }

    /*
    * 1,请求成功*/
    public static <T> ResultVo getSuccess(String msg){
        return new ResultVo(200,msg);
    }

    /*
     * 1,请求成功*/
    public static <T> ResultVo getSuccess(String msg,T data){
        return new ResultVo(200,msg,data);
    }


    /*
     * 0,请求失败
     * */
    public static <T> ResultVo getFailed(String msg){
        return new ResultVo(404,msg);
    }

    /*
     * 0,请求失败*/
    public static <T> ResultVo getFailed(String msg,T data){
        return new ResultVo(404,msg,data);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    private Integer code;
    private String msg;
    private T data;
}
