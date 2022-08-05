package com.cloud.servicewechat.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendMailThread extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(SendMailThread.class);

    private String email;
    private String subject;
    private String emailMsg;
    private String sendemail;
    private String password;
    private String hostname;


    /**
     *
     * 发送邮件
     * @param email 收件人的邮箱地址
     * @param subject 邮件主题
     * @param emailMsg 邮件内容
     * @param sendemail 发件人邮箱
     * @param password 邮箱授权码
     */
    public SendMailThread(String email, String subject, String emailMsg, String sendemail, String password, String hostname){
        this.email = email;
        this.subject = subject;
        this.emailMsg = emailMsg;
        this.sendemail = sendemail;
        this.password = password;
        this.hostname = hostname;
    }

    @Override
    public void run() {
        this.sendEmails();
    }

    public void sendEmails(){
        MailUtils.sendMail(email,subject,emailMsg,sendemail,password,hostname);
        //MailUtils.yanzheng(sendemail,password,hostname);
    }
}
