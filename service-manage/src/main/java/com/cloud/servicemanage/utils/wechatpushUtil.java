package com.cloud.servicemanage.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cloud.servicemanage.entity.PushEntity;
import com.cloud.servicemanage.utils.weixin.WeixinUtil;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class wechatpushUtil {
    public static String getALLUsersURL="https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN";
    public static String getUserInfo = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
    public static String customUrl="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";


    private static final Logger logger = LoggerFactory.getLogger(wechatpushUtil.class);
    //获取所有关注的人openId
    public static List<String> GetOpenIdList(String nextopenid, String access_token)
    {
        //if (string.IsNullOrEmpty(nextopenid))
        //    nextopenid = "";
        String url = getALLUsersURL.replace("ACCESS_TOKEN", access_token);
        JSONObject jsonObject = WeixinUtil.httpRequest(url, "POST", null);
        String data=jsonObject.getString("data");
        JSONObject obj= JSON.parseObject(data);
        List<String> list= new ArrayList<>();
        try {
            list = obj.getObject("openid",List.class);
        }catch (Exception e){
            System.out.println("关注人员获取失败：" + e.getMessage());
            e.printStackTrace();
        }

        return list;

    }
    //获取用户信息unionid
    public static String getUnionId(String openId, String access_token){
        String url = getUserInfo.replace("ACCESS_TOKEN", access_token).replace("OPENID", openId);
        JSONObject jsonObject = WeixinUtil.httpRequest(url, "POST", null);
        String unionid = jsonObject.getString("unionid");
        return unionid;
    }

    /**
     * 2、发送模版消息
     * openId     用户Id
     * templateId 模板Id
     * data       模板参数
     * @param
     */
    public static int sendMsg(String appId,String secret,PushEntity pushEntity, String templateId, List<String> openIds){
        int result = 0;
        WxMpInMemoryConfigStorage wxStorage = new WxMpInMemoryConfigStorage();
        wxStorage.setAppId(appId);
        wxStorage.setSecret(secret);
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxStorage);
        List<WxMpTemplateData> data = new ArrayList<>();
        //审核结果模版
        if(templateId.equals("2WYFptTPrbIExm97CLGZ-iaveFA19WQGxwi5q7yyeRI")){
            data = Arrays.asList(
                    new WxMpTemplateData("first", pushEntity.getTitle()),
                    new WxMpTemplateData("keyword1", pushEntity.getKeywords4()),  //姓名
                    new WxMpTemplateData("keyword2", pushEntity.getKeywords2()),
                    new WxMpTemplateData("keyword3", pushEntity.getKeywords3()),
                    new WxMpTemplateData("remark", pushEntity.getKeywords1())
            );
        }else if(templateId.equals("Slxjlr_FPAdcwrIFuR4KIUoA53KggalBxkS2ICbQUik")){


            data = Arrays.asList(
                    new WxMpTemplateData("first", pushEntity.getTitle()),
                    new WxMpTemplateData("Content1", pushEntity.getKeywords1()),
                    new WxMpTemplateData("Good", pushEntity.getKeywords2()),
                    new WxMpTemplateData("expDate", pushEntity.getKeywords3()),
                    new WxMpTemplateData("name", pushEntity.getKeywords4()),
                    new WxMpTemplateData("menu", pushEntity.getKeywords5()),
                    new WxMpTemplateData("remark", pushEntity.getRemark())
            );
        }else{
            return result;
        }

        //2,推送消息
   /* WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
            .toUser("ooBG35m7H8nNJLe9Ouur9jnj5vfg")//要推送的用户openid
            .templateId(templateId)//模版id
            .url(pushEntity.getUrl())//点击模版消息要访问的网址
            .data(data)
            .build();*/
        // TreeMap<String, String> miniprograms = new TreeMap<String, String>();
        WxMpTemplateMessage.MiniProgram miniprograms = new WxMpTemplateMessage.MiniProgram();
        miniprograms.setAppid(pushEntity.getAppId());
        //miniprograms.setAppid("wx46b5365b0ba7022e");
        miniprograms.setPath(pushEntity.getUrl());// 注意，这里是支持传参的！！！
        //----------------------------------------------------------------------
        // openIds.clear();
        // openIds.set(0,"ooBG35mkxHtkUI2p8SangD_gSSlc");

        if(openIds!=null && openIds.size()>0){
            for(int i=0;i<openIds.size();i++){
                //System.out.println("---------------------------------------"+openIds.get(i));

                WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                        .toUser(openIds.get(i))//要推送的用户openid
                        .templateId(templateId)//模版id
                        //.url(pushEntity.getUrl())//点击模版消息要访问的网址
                        .miniProgram(miniprograms)
                        .data(data)
                        .build();
                try {
                    String msg = wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
                    System.out.println("推送成功：" + msg);
                    result=1;
                } catch (Exception e) {
                    System.out.println("推送失败：" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
    public static int sendMsg2(PushEntity pushEntity, String templateId, List<String> openIds,String accessToken){
        int result = 0;

        String url = customUrl.replace("ACCESS_TOKEN", accessToken);
        //String openid="ooBG35m7H8nNJLe9Ouur9jnj5vfg";
        String title=pushEntity.getTitle();
        String description ="标题："+pushEntity.getKeywords1()+"\n"+"时间："+pushEntity.getKeywords2()+"\n"+"内容：" +
                pushEntity.getKeywords3() ;
        String pathUrl=pushEntity.getUrl();


        for (int i = 0; i < openIds.size(); i++ ){
            String openid = openIds.get(i);
        String respMessage="{\n" +
                "    \"touser\":\""+openid+"\",\n" +
                "    \"msgtype\":\"news\",\n" +
                "    \"news\":{\n" +
                "        \"articles\": [\n" +
                "         {\n" +
                "             \"title\":\""+title+"\",\n" +
                "             \"description\":\""+description+"\",\n" +
                "             \"url\":\""+pathUrl+"\",\n" +
                "             \"picurl\":\""+pathUrl+"\"\n" +
                "         }\n" +
                "         ]\n" +
                "    }\n" +
                "}";

            try {
                // 模板Id
                JSONObject jsonObject = WeixinUtil.httpRequest(url, "POST", respMessage);
                logger.info("推送-----------------------------：" + openid);
                System.out.println("推送-----------------------------：" + openid);
                result=1;
            } catch (Exception e) {
                System.out.println("推送失败：" + e.getMessage());
                e.printStackTrace();
            }


        }

        return result;
    }
}
