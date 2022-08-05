package com.cloud.servicemanage.utils;

import com.cloud.commonsmng.entity.appletEntity.Notes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendWechatThread extends Thread  {
    private static final Logger logger = LoggerFactory.getLogger(SendWechatThread.class);
    private sendWechat sendwechat;
    private Notes notes;



    public SendWechatThread(sendWechat sendwechat,Notes notes){
        this.sendwechat = sendwechat;
        this.notes = notes;
    }

    @Override
    public void run() {
        this.sendwechatgzh();
    }

    public void sendwechatgzh(){
        sendwechat.sendweixin(notes);
        //MailUtils.yanzheng(sendemail,password,hostname);
    }
}
