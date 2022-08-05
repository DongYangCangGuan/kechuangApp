package com.cloud.servicewechat.service;

import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.entity.appletEntity.*;
import com.cloud.commonsmng.entity.appletEntity.Dictionary;
import com.cloud.commonsmng.factory.BaseService;
import com.cloud.servicewechat.common.ConstantUtil;
import com.cloud.servicewechat.entity.PushEntity;
import com.cloud.servicewechat.mapper.*;
import com.cloud.servicewechat.utils.SendMailThread;
import com.cloud.servicewechat.utils.SendWechatThread;
import com.cloud.servicewechat.utils.sendWechat;
import com.cloud.servicewechat.utils.wechatpushUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service("authentication")
public class AuthenticationService extends BaseService {

    @Autowired
    AuthenticationMapper authenticationMapper;

    @Autowired
    MemberMapper memberMapper;

    @Autowired
    MemberUserMapper memberUserMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    private NotesMapper notesMapper;
    @Autowired
    private sendWechat sendwechat;

    //实例化日志对象，引用jar包org.slf4j.Logger
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    @Override
    public Map<String, Object> exec(String method, String param) {
        Map<String, Object> obj = new HashMap<>();
        switch (method) {
            case ConstantUtil.CHECK_MEMBER:
                obj = checkMember(param);
                break;
            case ConstantUtil.MEMBER_AUTHENTICATION:
                obj = memberAuthentication(param);
                break;
            case ConstantUtil.GET_MEMBERS: 
                obj = getMembers(param);
                break;
            case ConstantUtil.GET_INVESTMENTFUND:
                obj = getInvestmentFund(param);
                break;

            default:
                break;
        }
        return obj;
    }



    private Map<String,Object>getMembers(String param){
        JSONObject jsonObject = JSONObject.parseObject(param);
        String memberType = jsonObject.getString("memberType");
        String enterpriseName = jsonObject.getString("enterpriseName");
        Map<String, Object> result = new HashMap<>();
        List<Member> checkMember = memberMapper.getMembers(memberType,enterpriseName);
        Constants.getSuccMsg(result, checkMember);
        return result;
    }


    private Map<String,Object>getInvestmentFund(String param){
        Map<String, Object> result = new HashMap<>();
        List<Member> checkMember = memberMapper.getInvestmentFund();
        Constants.getSuccMsg(result, checkMember);
        return result;
    }

    /**
     * 向当前用户添加信息，并验证公司信息
     */
    private Map<String, Object> checkMember(String param) {
        Map<String, Object> result = new HashMap<>();
        if (super.getUserInfo() != null) {
            String userId = super.getUserInfo().getId();        //获取当前用户编号
            Authentication authentication = JSONObject.parseObject(param, Authentication.class);
            User user = authentication.getUser() == null ? new User() : authentication.getUser();
            user.setId(userId);
            authentication.setUser(user);
            Member checkMember = memberMapper.checkCodeAndPWD(authentication.getMember());
            MemberUser memberUser = new MemberUser();
            int addMemberDetailResult = 0 ;
            if (checkMember != null) {
                super.insertBaseInfo(memberUser);//使用父类中通用的新增代码
                memberUser.setMemberId(checkMember.getId());
                memberUser.setUserId(userId);
                memberUser.setURole(authentication.getMember().getMemberType());
                memberUser.setDelFlag(Integer.valueOf(1));//默认存在
                memberUser.setJob(authentication.getMember().getJob());
                user.setJob(authentication.getMember().getJob());
                //供应商
                if(authentication.getMember().getMemberType().equals("4")){
                    memberUser.setDepartmentId(checkMember.getId());
                }else{
                    memberUser.setDepartmentId("001");
                }
                 addMemberDetailResult = memberUserMapper.addMemberUser(memberUser);

                int addURole = userMapper.addUserDetail(user);
                Constants.getSuccMsg(result, addMemberDetailResult);

               // int addInvest = memberMapper.addInvest( authentication.getMember().getEnterpriseCode(),authentication.getMember().getEnterpriseName());
                Constants.getSuccMsg(result, addURole);
            } else {
                Map map = new HashMap();
                map.put("code", 500);
                map.put("msg", "未找到绑定的公司信息");
                Constants.getErrMsg(result, map);
            }
            logger.info(String.format("修改用户相关信息返回的结果: [%s]", result));
            //发送消息通知
            if(addMemberDetailResult>0){
                //具有审批权限
                List<String> kechuangs = memberMapper.getuserIds("2","memberapproval");
                //创业公司
                if(authentication.getMember().getMemberType().equals("5")){
                    //查找对应的投资基金对应的GP，下的人员，且job为IR
                    List<String> gps = memberMapper.getuserIdbyfun("1",checkMember.getId(),"IR");
                    if(gps != null && gps.size()>0){
                        kechuangs.addAll(gps);
                    }
                }
                //申请通知
                this.setNotes(checkMember.getEnterpriseName(),"002","002",kechuangs, user);

            }
        } else {
            logger.info(String.format("未登录，不能进行操作！"));
            Constants.getErrMsg(result, "登录之后方可执行该操作！！！");
        }
        return result;
    }

    /**
     * 设置消息
     * 申请通知审批
     * @param title
     * @param content
     * @return
     */
    public Integer setNotes(String entername,String title,String content,List<String> userIds,User user){
        Notes notes = new Notes();

        List<NotesDetail> details = new ArrayList<NotesDetail>();

        if(userIds == null || userIds.size() <= 0){
            return -1;
        }
        Integer i1 = 0;
        //获取消息id
       // Dictionary dnotes = memberMapper.selectDictionaryByCode(notesid,"notesid");
       // if(dnotes == null || dnotes.equals("") || dnotes.getName() == null ||dnotes.getName().equals("")){
            //数据库获取title
            Dictionary dtitle = memberMapper.selectDictionaryByCode(title,"title");
            //Dictionary dcontent = memberMapper.selectDictionaryByCode(content,"content");
            /*if(dcontent == null || dcontent.equals("") || dcontent.getName() == null ||dcontent.getName().equals("")){
                notes.setContent("您好，您收到一条新的申请，待处理");
            }else{
                notes.setContent(dcontent.getName());
            }*/
            if(dtitle == null || dtitle.equals("") || dtitle.getName() == null ||dtitle.getName().equals("")){
                notes.setTitle("用户认证待审核知");
            }else{
                notes.setTitle(dtitle.getName());
            }
            if(user.getJob() == null || user.getJob().equals("")){
                user.setJob("未填");
            }
            notes.setContent("您有一条新的申请消息待处理。申请人："+user.getRealName()+
                    "；申请内容：公司名称："+entername+"；职位："+user.getJob());
            notes.setTaskType("user");
            notes.setIsFeedBack(0);
            notes.setStatus("1");
            notes.setFlag(3);
            super.insertBaseInfo(notes);

            for(int i = 0;i<userIds.size();i++){
                NotesDetail notesDetail = new NotesDetail();
                notesDetail.setNotesId(notes.getId());
                notesDetail.setUserId(userIds.get(i));
                notesDetail.setStatus("0");
                super.insertBaseInfo(notesDetail);
                details.add(notesDetail);
            }
            notes.setNotesDetailList(details);
            i1 = memberMapper.insertNotes(notes);
        //}else {
            //获取消息
/*            List<Notes> n = notesMapper.getNoteById(dnotes.getName());
            if(n == null || n.size() <= 0){
                //添加消息
                //数据库获取title
                Dictionary dtitle = memberMapper.selectDictionaryByCode(title,"title");
                Dictionary dcontent = memberMapper.selectDictionaryByCode(content,"content");
                if(dcontent == null || dcontent.equals("") || dcontent.getName() == null ||dcontent.getName().equals("")){
                    notes.setContent("您好，您收到一条新的申请，待处理");
                }else{
                    notes.setContent(dcontent.getName());
                }
                if(dtitle == null || dtitle.equals("") || dtitle.getName() == null ||dtitle.getName().equals("")){
                    notes.setTitle("用户认证待审核知");
                }else{
                    notes.setTitle(dtitle.getName());
                }
                notes.setTaskType("user");
                notes.setIsFeedBack(0);
                notes.setStatus("1");
                notes.setFlag(3);
                super.insertBaseInfo(notes);
                notes.setId(dnotes.getName());
                int i2 = notesMapper.insertNote(notes);
            }else{
                notes = n.get(0);
            }
            for(int i = 0;i<userIds.size();i++){
                NotesDetail notesDetail = new NotesDetail();
                notesDetail.setNotesId(dnotes.getName());
                notesDetail.setUserId(userIds.get(i));
                notesDetail.setStatus("0");
                super.insertBaseInfo(notesDetail);
                details.add(notesDetail);
            }
            i1 = memberMapper.insertNotesDetails(details);*/
       // }
        notes.setUserIds(userIds);
        this.sendEmaillist(notes);
        //String content1 = notes.getContent();
       //notes.setContent("您好，您收到一条来自"+user.getRealName()+"的认证申请，待处理。");
        notes.setContent("公司名称："+entername+"；职位："+user.getJob());
        notes.setUserName(user.getRealName());
        notes.setTemplateId("xoXOLUaSB3zJ9PIQyIL8n5_nA5YAAuImbcy5eRoUosg");
        this.sendweixin(notes);


        return i1;

    }

    /**
     * 是否需要做异步
     * @param notes
     */
    public void sendweixin(Notes notes){

            Thread t =  new SendWechatThread(sendwechat,notes);
            t.start();

    }

    public void sendEmaillist(Notes notes){
        List<String> emailList = null;

        if(notes.getUserIds()!=null && notes.getUserIds().size()>0) {
            //获取nodes_detail表中对应人员的数据
            emailList = notesMapper.getUserEmails(notes.getUserIds());
        }
        if(emailList != null && emailList.size() > 0){
            //根据kind获取字典表list
            List<Dictionary> d = memberMapper.getEmail("email");
            if(d != null && d.size() > 0) {
                //获取通知类型flag，2和3发送邮件.通知人群 all
                ExecutorService es = Executors.newFixedThreadPool(10);
                for (String email : emailList) {
                    es.execute(new SendMailThread(email, notes.getTitle(), notes.getContent(), d.get(0).getName(), d.get(0).getCode(), d.get(0).getPicurl()));
                }
                es.shutdown();
            }
        }
    }

    /**
     * 向当前用户添加信息
     */
    private Map<String, Object> memberAuthentication(String param) {
        logger.info(String.format("修改用户信息传递参数", param));
        Map<String, Object> result = new HashMap<>();
        if (super.getUserInfo() != null) {
            JSONObject jsonObject = JSONObject.parseObject(param);
            String enterpriseCode = jsonObject.getString("enterpriseCode");
            Member memberInfo = memberMapper.getMemberBycodeAndPWD(enterpriseCode);
            if (memberInfo != null) {
                Constants.getSuccMsg(result, memberInfo);
            } else {
                Map map = new HashMap();
                map.put("code", 500);
                map.put("msg", "未找到公司信息");
                Constants.getErrMsg(result, map);
            }
            logger.info(String.format("修改公司信息结果：", result));
        } else {
            logger.info(String.format("未登录，不能进行修改收藏操作！"));
            Constants.getErrMsg(result, "登录之后方可执行该操作！！！");
        }
        return result;
    }
}
