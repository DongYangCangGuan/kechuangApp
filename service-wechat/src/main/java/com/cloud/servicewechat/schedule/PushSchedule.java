package com.cloud.servicewechat.schedule;

import com.cloud.commonsmng.entity.appletEntity.AnswerPushEntity;
import com.cloud.commonsmng.entity.appletEntity.Notes;
import com.cloud.commonsmng.entity.appletEntity.User;
import com.cloud.servicewechat.mapper.AnswerMapper;
import com.cloud.servicewechat.mapper.UserMapper;
import com.cloud.servicewechat.utils.SendWechatThread;
import com.cloud.servicewechat.utils.sendWechat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@EnableScheduling
public class PushSchedule {

    @Autowired
    private sendWechat sendwechat;

    @Autowired
    private AnswerMapper answerMapper;

    @Autowired
    private UserMapper userMapper;


    private static Logger logger = LoggerFactory.getLogger(PushSchedule.class);

    @Scheduled(cron = "0 0/5 * * * ? ")
    @Transactional(rollbackFor = Exception.class)
    public void getQuestionPush() {

        //查询所有带推送回答
        List<AnswerPushEntity> answerList = answerMapper.getAllPush();
        if(answerList==null && answerList.size()<=0){
            return ;
        }
        //获取推送用户
        List<String> userList= userMapper.getUserListByRole("answerPushPG");

        answerList.forEach((AnswerPushEntity a) -> {

            User u =  userMapper.getUser(a.getUserId());
           String questionName =  answerMapper.getQuestionName(a.getQuestionBelong());
            Notes n = new Notes();
            n.setTitle("您好,有用户提交了问卷,请至管理端查看");
            n.setContent(questionName);
            n.setUserName(a.getCreateTime());
            n.setUserIds(userList);
            n.setTemplateId("udS_q-BZ4PDMsuUGfMr1wRY6ajt--NG3-wxEAGKWSiQ");
            n.setRemark(u.getEnterpriseName()+"  "+u.getRealName()+"("+u.getPhone()+")填写了该问卷");
            Thread t =  new SendWechatThread(sendwechat,n);
            t.start();
            answerMapper.updateIspush(a.getId());
        });


        //推送问卷结束
        logger.info("推送问卷结束");

    }


}
