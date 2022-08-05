package com.cloud.servicemanage.service.weixin.Impl;

import com.alibaba.fastjson.JSONObject;
import com.cloud.servicemanage.entity.weixin.TemplateMessageModel;
import com.cloud.servicemanage.service.weixin.TemplateService;
import com.cloud.servicemanage.utils.weixin.WeixinUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@SuppressWarnings("SpringJavaAutowiringInspection")
@Service("TemplateService")

public class TemplateServiceImpl implements TemplateService {

    private static Logger log = LoggerFactory.getLogger(TemplateServiceImpl.class);

    // 发送模版消息（POST）
    public static String template_url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";

    /**
     * 发送模版消息
     *
     * @param templateMessageModel 模板消息Model
     * @return 0表示成功，其他值表示失败
     */
    @Override
    public int SendTemplateMessage(TemplateMessageModel templateMessageModel) {
        int result = 0;

        //拼装json
        StringBuilder sb = new StringBuilder("{\"touser\":\""+ templateMessageModel.getOpenId() +"\",");
        sb.append("\"template_id\":\""+ templateMessageModel.getTemplateId() +"\",");
        if(templateMessageModel.getUrl() != null){
            sb.append("\"url\":\"" + templateMessageModel.getUrl() + "\",");
        }
        sb.append("\"data\":{");
        sb.append("\"first\": {\"value\":\""+ templateMessageModel.getFirst() +"\"},");
        for (int i = 0; i < templateMessageModel.getKeywords().size(); i++) {
            sb.append("\"keyword"+ (i+1) +"\": {\"value\":\""+ templateMessageModel.getKeywords().get(i) +"\"},");
        }
        sb.append("\"remark\": {\"value\":\"" + templateMessageModel.getRemark() + "\"}");
        sb.append("}}");

        String jsonTemplate =sb.toString();

        // 拼装发送模版消息的url
        String url = template_url.replace("ACCESS_TOKEN", templateMessageModel.getAccessToken());

        // 调用接口发送模版消息
        JSONObject jsonObject = WeixinUtil.httpRequest(url, "POST", jsonTemplate);

        if (null != jsonObject) {
            if (0 != jsonObject.getInteger("errcode")) {
                result = jsonObject.getInteger("errcode");

                throw new RuntimeException("发送模板消息失败 errcode:"+ result +" errmsg:"+ jsonObject.getString("errmsg") +"****"+jsonTemplate+"****");
            }
        }
        return result;
    }



}
