package com.cloud.servicemanage.thread;

import com.cloud.servicemanage.entity.weixin.AccessTokenModel;
import com.cloud.servicemanage.utils.weixin.WeixinUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * @Author:cat
 * @Description 定时获取微信access_token的线程
 * @Description 在WechatMpDemoApplication中注解@EnableScheduling，在程序启动时就开启定时任务。
 * @Description 每7200秒执行一次
 * @Date: 2018-04-12  10:00
 * @Modified By:
 */
@Component
public class AccessTokenThread {
    private static Logger log = LoggerFactory.getLogger(AccessTokenThread.class);

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * appId
     */
    public static String appId;
    /**
     * appSecret
     */
    public static String appSecret;

    @Value("${wx.mp.configs.appId}")
    public void setAppId(String appId) {
        AccessTokenThread.appId = appId;
    }
    @Value("${wx.mp.configs.secret}")
    public void setAppSecret(String appSecret) {
        AccessTokenThread.appSecret = appSecret;
    }

    /**
     * 第三方用户唯一凭证
     */
    private static AccessTokenModel accessToken = null;

    @Scheduled(fixedDelay = 2*3600*1000)
    //7200秒执行一次
    public void getToken() {
        accessToken = WeixinUtil.getAccessToken(appId, appSecret);
        if (accessToken != null) {
            /**把微信token保存在数据库*/
            int iRet = 0;
            try {
                /**把微信token保存在数据库操作
                 *
                 * */
              redisTemplate.opsForValue().set("access_token",accessToken.getToken());
              iRet=1;
            } catch (DataAccessException e) {
                iRet = 0;
                throw new RuntimeException("保存微信token到数据库失败");
            }
            if (iRet > 0) {
                log.info("微信token获取成功，accessToken:" + accessToken.getToken());
            } else {
                log.error("微信token获取失败");
            }
        }else {
            log.error("微信token获取失败");
        }
    }
}
