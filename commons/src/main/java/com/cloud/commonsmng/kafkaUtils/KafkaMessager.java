package com.cloud.commonsmng.kafkaUtils;

import com.alibaba.fastjson.JSONObject;
//import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *封装kafka消息字段及格式
 */
//@Component
public class KafkaMessager {
    private static final Logger  logger = LoggerFactory.getLogger(KafkaMessager.class);
    private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public KafkaMessager() {

    }

    private static final String KEY_DATE = "recievtime";//时间
    private static final String KEY_THREAD = "thread";//线程

    private static final String KEY_LEVEL = "log_level";//日志级别
    private static final String KEY_MSG = "type";//日志类型
    private static final String APPID = "moudle";//系统id
    private static final String LOGIN_NAME = "userid";//当前登陆人帐号
    private static final String METHOD_NAME = "function";//方法名称
    private static final String HTTP_URL = "client_ip";//调用接口的地址
    private static final String INPUT_PARAM = "inputparam";//入参
    private static final String OUTPUT_PARAM = "outputparam";//出参
    private static final String EXCEPTION = "errormsg";//异常信息
    private static final String LOG_OF_ACTION = "1";//日志类型：业务请求数据
    private static final String LOG_OF_LOGIN = "0";//日志类型：登录认证数据
   // private static final String TYPE="type";

    private static final String OTHER_PARAM="otherparam";
    private static final String RUNTIME="runtime";
    private static final String DESCRIB="describ";

    /**
     * 业务请求数据发送到kafka
     *
     * @param methodName 方法名称
     * @param appId       系统id
     * @param loginName   当前登陆人帐号
     * @param level     调用接口的地址
     * @param inputParam  入参
     * @param outputParam 出参
     * @param exception   异常信息

     * @param runtime   运行时间
     */
    public void sendCtrlMessage(String methodName, String appId, String loginName, String level, String inputParam,
                                String outputParam, String exception, String runtime,String otherparam,String describ,String type) {
        //调用发送
        send2kafka(methodName, appId, loginName, level, inputParam,
                outputParam, exception,  runtime, otherparam,describ,type);
    }

    /**
     * 登陆认证数据发送到kafka
     *
     * @param methodName  方法名
     * @param appId       系统id
     * @param loginName   当前登陆人帐号
     * @param httpURL     调用接口的地址
     * @param inputParam  入参
     * @param outputParam 出参
     * @param exception
     * @param isSucc      是否成功（无异常）
     * @param className   类全限定名
     */
//    public void sendLoginMessage(String methodName, String appId, String loginName, String httpURL, String inputParam,
//                                 String outputParam,String exception, boolean isSucc, String className) {
//
//        //调用发送
//        send2kafka(methodName, appId, loginName, httpURL, inputParam,
//                outputParam, exception, isSucc, className, KafkaMessager.LOG_OF_LOGIN);
//    }


    /**
     * 执行发送消息到kafka
     *
     * @param methodName
     * @param appId
     * @param loginName
     * @param level
     * @param inputParam
     * @param outputParam
     * @param exception

     * @param runtime
     * @param type
     */
    private void send2kafka(String methodName, String appId, String loginName, String level, String inputParam,
                            String outputParam, String exception, String runtime,String otherparam, String describ,String type) {
        JSONObject message = new JSONObject();

        try {
            //kafka开启时才执行发送动作
            if (KafkaConstants.IS_KAFKAACCESS) {

                String dateStr = format.format(new Date());
                // 本机ip
                String ip = "";
                try {
                    ip = InetAddress.getLocalHost().getHostAddress();
                } catch (UnknownHostException e1) {
                    logger.error("在消息发送kafka时，未获取本机ip");
                }
                String errorMsg = exception;

                message.put(METHOD_NAME, methodName);//方法名称
                message.put(APPID, appId);//系统标识
                message.put(LOGIN_NAME, loginName);//当前登陆人帐号
                message.put(HTTP_URL, ip);//请求地址
                message.put(INPUT_PARAM, inputParam);//入参
                message.put(OUTPUT_PARAM, outputParam);//出参
                message.put(EXCEPTION, errorMsg);//异常信息
                message.put(KEY_DATE, dateStr);
                message.put(KEY_THREAD, Thread.currentThread().getName());
                message.put(RUNTIME, runtime);
                message.put(OTHER_PARAM,otherparam);
                message.put(KEY_LEVEL, level);
                message.put(DESCRIB,describ);

                message.put(KEY_MSG, type);

                // 发送消息
                logger.info(kafkaTemplate.getDefaultTopic());
                kafkaTemplate.sendDefault(message.toJSONString());

                logger.info(message.toJSONString());
            }
        } catch (Exception e) {
            logger.error("kafka发送异常：{}",e.toString());
        } finally {
            message = null;
        }

    }

}