package com.cloud.servicemanage.web;

import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.security.AESUtil;
import com.cloud.commonsmng.security.JwtUtil;
import com.cloud.servicemanage.entity.ResultInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;

@WebFilter(urlPatterns = {"/manage/manage/api", "/manage/api/file/*","/manage/manage/ReportDownLoad"}, filterName = "tokenAuthorFilter")
public class TokenAuthorFilter implements Filter {

    @Autowired
    AESUtil aesUtil;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    RedisTemplate redisTemplate;

    //实例化日志对象，引用jar包org.slf4j.Logger
    private static final Logger logger = LoggerFactory.getLogger(TokenAuthorFilter.class);

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse rep = (HttpServletResponse) response;

        //设置允许跨域的配置
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        String token = req.getHeader("token");//header方式
        ResultInfo resultInfo = new ResultInfo();
        boolean isFilter = false;
        String uri = req.getRequestURI();

        //登录放行
        if (uri.contains("login")) {
            chain.doFilter(request, response);
            resultInfo.setSuccess(true);
        } else {
            if (null == token || token.isEmpty()) {
                resultInfo.setSuccess(false);
                resultInfo.setCode("401");
                resultInfo.setMsg("token没有认证通过!原因为：客户端请求参数中无token信息");
            } else {
                try {
                    //token 验证不通过
                    if (!jwtUtil.verify(token)) {
                        resultInfo.setSuccess(false);
                        resultInfo.setCode("401");
//                        resultInfo.setMsg("token没有认证通过!原因为：客户端请求中认证的token信息无效，请查看申请流程中的正确token信息");
                    } else {
                        Map<String, String> userinfo = jwtUtil.verifyToken(token);
                        String userid = userinfo.get("userid");
                        String username = userinfo.get("username");
                        if (!redisTemplate.hasKey(Constants.REDIS_KEY + userid + username)) {
                            resultInfo.setSuccess(false);
                            resultInfo.setCode("401");
                            resultInfo.setMsg("token没有认证通过!原因为：客户端请求中认证的token信息已失效");
                        } else {
                            isFilter = true;
                            resultInfo.setSuccess(true);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error(e.getMessage());
                }
            }

            if (!resultInfo.isSuccess()) {
                rep.setStatus(HttpServletResponse.SC_OK);
                PrintWriter writer = null;
                OutputStreamWriter osw = null;
                try {
                    osw = new OutputStreamWriter(rep.getOutputStream(), "UTF-8");
                    writer = new PrintWriter(osw, true);
                    String encryptStr = aesUtil.aesEncryptToBytes(JSONObject.toJSONString(resultInfo), AESUtil.MANAGE_KEY);
                    writer.write(encryptStr);
                    writer.flush();
                    writer.close();
                    osw.close();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    logger.error(e.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error(e.getMessage());
                } finally {
                    if (null != writer) {
                        writer.close();
                    }
                    if (null != osw) {
                        osw.close();
                    }
                }
                return;
            }

            if (isFilter) {
                chain.doFilter(request, response);
            }
        }

    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {

    }
}

