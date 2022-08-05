package com.cloud.servicewechat.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailUtils {
    //实例化日志对象，引用jar包org.slf4j.Logger
    private static final Logger logger = LoggerFactory.getLogger(MailUtils.class);
    private MailUtils(){}
    /**
     * 发送邮件
     * @param email 收件人的邮箱地址
     * @param subject 邮件主题
     * @param emailMsg 邮件内容
     * @param sendemail 发件人邮箱
     * @param password 邮箱授权码
     */
    public static void sendMail(String email, String subject, String emailMsg, String sendemail, String password, String hostname) {
        //logger.info("---------------------发送3------------------------");
       // logger.info(sendemail);
       // logger.info(password);
       // logger.info(hostname);
        // 1.[连接发件服务器]创建一个程序与发件人的 发送邮件服务器会话对象 Session
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "SMTP");//邮件发送协议
       // props.setProperty("mail.host", "smtp.126.com");//邮件发送服务器的地址（如QQ邮箱的发件服务器地址SMTP服务器: smtp.qq.com）
       // props.setProperty("mail.host","smtp.qiye.ssic-capital.com"); //shkechuang@ssic-capital.com
        props.setProperty("mail.host",hostname);
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port","465");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.localhost", "127.0.0.1");
        props.put("mail.smtp.connectiontimeout", "30000");
        props.put("mail.smtp.timeout", "30000");
        props.setProperty("mail.smtp.auth", "true");//指定验证为true
        String username = sendemail.substring(0, sendemail.indexOf("@"));

        // 创建验证器
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
//发件人的用户名（不带后缀的，如QQ邮箱的@qq.com不用写）和授权码(这里一般不使用密码，为避免密码泄露，用授权码代替密码登录第三方邮件客户端)
//授权码：用于登录第三方邮件客户端的专用密码。 第三方邮件客户端：如这个java程序。username"QQ邮箱地址不带@qq.com"
                return new PasswordAuthentication(sendemail, password);
            }
        };
        Session session = Session.getInstance(props, auth);
       // logger.info("---------------------发送4------------------------");
       // logger.info(email);
        try {

// 2.[创建一封邮件]创建一个Message，它相当于是邮件内容
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sendemail)); // 设置发送者的邮箱地址
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(email)); // 设置发送方式与接收者
            message.setSubject(subject);//邮件主题
            message.setContent(emailMsg, "text/html;charset=utf-8");//设置邮件的内容
            logger.info("---------------------发送5------------------------");
// 3.[发送邮件]创建 Transport用于将邮件发送
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            logger.error("senfEmail----",e.getMessage());
        }
        logger.info("发送邮件："+email);
    }

    public static void yanzheng(String sendemail, String password, String hostname){
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setUsername(sendemail);
        javaMailSender.setPassword(password);
        javaMailSender.setHost(hostname);
        javaMailSender.setPort(25);
        javaMailSender.setProtocol("smtp");
        javaMailSender.setDefaultEncoding("UTF-8");
        String username = sendemail.substring(0, sendemail.indexOf("@"));
        Properties mailProperties = new Properties();
        // 发送服务器需要身份验证,要采用指定用户名密码的方式去认证
        mailProperties.put("mail.smtp.auth", true);
        mailProperties.put("mail.smtp.starttls.enable", true);
        // 添加连接对象到邮件对象中
        javaMailSender.setJavaMailProperties(mailProperties);
        try {
            // 得到默认的对话对象
            Authenticator a = new Authenticator() {
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(sendemail, password);
                }
            };
            Session session = Session.getDefaultInstance(mailProperties, a);
            session.setDebug(true);
            // 创建MimeMessage实例对象
            MimeMessage message = new MimeMessage(session);
            javaMailSender.send(message);
        } catch (MailAuthenticationException e) {
            logger.warn("------------------邮件用户名或密码不正确--------------------");
            e.printStackTrace();

        } catch (NullPointerException e) {
            logger.warn("----------------------邮件验证通过--------------------");
        }
    }
}
