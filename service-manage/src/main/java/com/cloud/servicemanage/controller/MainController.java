package com.cloud.servicemanage.controller;

import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.aop.OperationLogDetail;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.constants.util.snowflakeId.SnowflakeIdGenerator;
import com.cloud.commonsmng.factory.ExecuteFactory;
import com.cloud.commonsmng.security.AESUtil;
import com.cloud.commonsmng.security.JwtUtil;
import com.cloud.commonsmng.entity.BaseUserInfo;
import com.cloud.commonsmng.userConfig.GetUserInfo;
import com.cloud.servicemanage.common.DataProcessing;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class MainController {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    GetUserInfo getUserInfo;

    @Autowired
    AESUtil aesUtil;

    @Value("${SnowFlakeIdGenerator.workerId}")
    private long workerId;

    @Value("${SnowFlakeIdGenerator.datacenterId}")
    private long datacenterId;

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @RequestMapping(value = "/manage/manage/api", method = RequestMethod.POST)
    //@RequestMapping(value = "/manage/api", method = RequestMethod.POST)
    @OperationLogDetail(detail = "基线系统主入口", type = "PC")
    public String excute(@RequestBody String params, ServletRequest request) throws Exception {
        logger.info(String.format("请求入参加密前: [%s]", params));
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> map = new HashMap<>();
        String decodeParam = DataProcessing.urlDecode(params);//将数据进行转义处理
        String param1 = aesUtil.desEncryt(decodeParam, AESUtil.MANAGE_KEY);//解密

        if (param1 == null || "".equals(param1)) {
            result.put("code", "201");
            result.put("msg", "参数错误，请重新请求");
            String resultEncrypt = aesUtil.aesEncryptToBytes(result, AESUtil.MANAGE_KEY);
            return resultEncrypt;
        }
        logger.info(String.format("请求入参加密后: [%s]", param1));

        //获取用户信息
        HttpServletRequest r = (HttpServletRequest) request;
        String token = r.getHeader("token");
        logger.info(String.format("请求传入的token: [%s]", token));
        Map<String, String> userinfo = jwtUtil.verifyToken(token);
        String userid = userinfo.get("userid");
        String username = userinfo.get("username");
        String redis_Custom_key = userid + username;
        logger.info(String.format("userinfo[%s]", userinfo));
        BaseUserInfo usr = getUserInfo.getUser(Constants.REDIS_KEY, redis_Custom_key);
        logger.info(String.format("登录的用户信息: [%s]", usr));
        //获取用户信息
        logger.info(String.format("usr[%s]", usr));

        if (usr == null) {
            result.put("code", "401");
            result.put("msg", "未登录，请重新登录");
            String resultEncrypt = aesUtil.aesEncryptToBytes(result, AESUtil.MANAGE_KEY);
            return resultEncrypt;
        }

        JSONObject json = JSONObject.parseObject(param1);
        String className = StringUtils.trimToEmpty(json.getString("className"));
        String method = StringUtils.trimToEmpty(json.getString("method"));
        String param = StringUtils.trimToEmpty(json.getString("param"));
        if ("QuestionAnswer".equals(className) || "SearchFile".equals(className) || "Access".equals(className)) {
            result.put("code", 200);
            result.put("message", "succ");
            result.put("dataList", null);
            String resultEncrypt = aesUtil.aesEncryptToBytes(result, AESUtil.MANAGE_KEY);
            return resultEncrypt;
        }

        result = ExecuteFactory.getExecuteService(className, usr, new SnowflakeIdGenerator(workerId, datacenterId)).exec(method, param);
        logger.info(String.format("未加密前从后端获取的数据: [%s]", result));
        String resultEncrypt = aesUtil.aesEncryptToBytes(result, AESUtil.MANAGE_KEY);
        return resultEncrypt;
    }


    @RequestMapping(value = {"/manage/manage/ReportDownLoad"}, method = RequestMethod.POST, produces = "application/x-msdownload;charset=utf-8")
    @OperationLogDetail(detail = "下载文件的入口", type = "PC")
    public String ReportDownLoad(@RequestBody String filePath, HttpServletResponse response) throws IOException {
        logger.info("下载路径地址:" + filePath);
        FileInputStream inputStream = null;
        try{
            inputStream = new FileInputStream(filePath);
            String suffix = filePath.substring(filePath.lastIndexOf(".") + 1);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳

            String fileName = date + "." + suffix;

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1){
                os.write(buffer,0,length);
            }
            byte[] data=os.toByteArray();
            response.resetBuffer();
            response.setHeader("content-Disposition","attachment");
            response.addHeader("fileName", URLEncoder.encode(fileName,"UTF-8"));
            response.addHeader("Contern-Length",""+data.length);
//            response.setContentType("application/octet-stream; charset=UTF-8");
            response.setContentType("application/x-msdownload; charset=UTF-8");
            IOUtils.write(data,response.getOutputStream());
        }
        catch (IOException e)
        {
            logger.error("下载接口错误信息:" + e.toString());
        }
        finally
        {
            inputStream.close();
        }
        return "succ";
    }
}
