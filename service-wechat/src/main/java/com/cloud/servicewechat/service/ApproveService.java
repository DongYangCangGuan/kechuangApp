package com.cloud.servicewechat.service;

import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.entity.BaseRole;
import com.cloud.commonsmng.entity.BaseUserInfo;
import com.cloud.commonsmng.entity.appletEntity.*;
import com.cloud.commonsmng.entity.appletEntity.Dictionary;
import com.cloud.commonsmng.factory.BaseService;
import com.cloud.servicewechat.common.ConstantUtil;
import com.cloud.servicewechat.common.PageUtil;
import com.cloud.servicewechat.common.PageVo;
import com.cloud.servicewechat.entity.PushEntity;
import com.cloud.servicewechat.mapper.*;
import com.cloud.servicewechat.utils.SendMailThread;
import com.cloud.servicewechat.utils.SendWechatThread;
import com.cloud.servicewechat.utils.sendWechat;
import com.cloud.servicewechat.utils.wechatpushUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service("Approve")
public class ApproveService extends BaseService {

    @Autowired
    private ApproveMapper ApproveMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private NotesMapper notesMapper;
    @Autowired
    private sendWechat sendwechat;


    private static final Logger logger = LoggerFactory.getLogger(ApproveService.class);

    @Override
    public Map<String, Object> exec(String method, String param) {
        Map<String,Object> obj = new HashMap<>();
        switch (method){
            case ConstantUtil.GET_APPROVE_LIST:
                obj = getApproveList(param);
                break;
            case ConstantUtil.GET_MEMBER_DETAIL:
                obj = getMemberDetail(param);
                break;
            case ConstantUtil.APPROVE:
                obj = approve(param);
                break;
            default:
                break;
        }

        return obj;
    }

    private Map<String,Object> getApproveList(String param){
        Map<String, Object> result = new HashMap<>();
        PageVo pageVo = JSONObject.parseObject(param,PageVo.class);
        Map<String, Object> schMap = new HashMap<>();
        pageVo.setSearchdata(schMap);
        memberMapper.deleteWeightMemberUser();
        PageUtil page = pageVo.getPage();
        try {
            BaseUserInfo u = super.getUserInfo();
            //获取用户角色
            User u1 = userMapper.getuRolebyId(u.getId());
            Integer count =  userMapper.getCountByUser(u.getId(),"memberapproval");
            if(count <=0 && !StringUtils.equalsIgnoreCase("IR",u1.getJob())){
                Constants.getSuccMsg(result, null);
                return result;
            }
            if(u1==null||u1.equals("")||u1.getURole()== null ||u1.getURole().equals("")){
                Constants.getErrMsg(result, "用户角色获取失败");
                return result;
            }
            if(!u1.getURole().equals("0")){
                List<String> uroles1 = new ArrayList<>();
                uroles1 = this.geturolelist("membertype",u1.getURole(),null,uroles1);
                if(uroles1 == null || uroles1.size() <= 0){
                    uroles1 = new ArrayList<>();
                    uroles1.add("0");
                }
                if(u1.getURole().equals("2")){
                    uroles1.add("2");
                }
                Object searchdata = pageVo.getSearchdata();
                Map<String, Object> searchMap = JSONObject.parseObject(JSONObject.toJSONString(searchdata), HashMap.class);
                //urole.put("uRole",u1.getURole());
                searchMap.put("uRole1",uroles1);
                pageVo.setSearchdata(searchMap);
            }else{
                List<String> uroles1 = new ArrayList<>();
                uroles1.add("0");
                Object searchdata = pageVo.getSearchdata();
                Map<String, Object> searchMap = JSONObject.parseObject(JSONObject.toJSONString(searchdata), HashMap.class);
                searchMap.put("uRole1",uroles1);
                pageVo.setSearchdata(searchMap);
            }
            String ci = "";
            if(u1.getURole().equals("1")){
                //ci =  memberMapper.getInvestmentFundByUser(u1.getId());
                ci =  memberMapper.getmemberIdByUser(u1.getId());
                List<String> gpids = memberMapper.getsameGp(ci);
                Object searchdata = pageVo.getSearchdata();
                Map<String, Object> searchMap = JSONObject.parseObject(JSONObject.toJSONString(searchdata), HashMap.class);
                searchMap.put("ci",gpids);
                pageVo.setSearchdata(searchMap);
            }

            //获取总条数
            Integer total = ApproveMapper.countApproveList(pageVo,u1.getURole(),ci);
            if(total>0){
            List<MemberUser> memberDetails = ApproveMapper.getApproveList(pageVo,u1.getURole(),ci);
            PageVo<MemberUser> memberDetailsPageVo = new PageVo<>();
            memberDetailsPageVo.setPage(new PageUtil(page.getPageIndex(), page.getPageSize(), total));
            memberDetailsPageVo.setDataList(memberDetails);
            Constants.getSuccMsg(result, memberDetailsPageVo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Constants.getErrMsg(result, "信息加载失败");
        }
        return result;
    }



    private List<String> geturolelist(String kind, String parentId, List<Dictionary> dictionaries,List<String> uroles1){

        if(parentId!=null && !parentId.equals("")){
            dictionaries = productMapper.getProductTypes(kind,parentId);
        }
        if(dictionaries != null && dictionaries.size()>0){
            for(Dictionary d:dictionaries){
                uroles1.add(d.getCode());
            }
            for(int i = 0; i < dictionaries.size(); i++){
                //获取dictionary子集
                // Dictionary dictionary = dictionaries.get(i);
                List<Dictionary> children = productMapper.getProductTypes(kind,dictionaries.get(i).getCode());
                dictionaries.get(i).setChildren(children);
                // dictionary.setChildren(children);
                //  dictionaries.set(i,dictionary);
                this.geturolelist(kind, "", children,uroles1);
            }
            return uroles1;
        }else{
            return null;
        }
    }

    private Map<String,Object>getMemberDetail( String param){
        Map<String,Object> result  = new HashMap<>();
        JSONObject json=JSONObject.parseObject(param);
        String userId=super.getUserInfo().getId();
        String memberId =json.getString("id");
        MemberUser user=ApproveMapper.getMemberDetail(memberId,userId);
        Constants.getSuccMsg(result, user);
        return result;
    }

    private int setchuangyeApprovalstatus(String memberuserId){
        //获取审核记录相关信息
        MemberUser memberUser = memberMapper.getMemberDetailById2(memberuserId);
        BaseUserInfo u = super.getUserInfo();
        //当前审批人的所属机构
        User u1 = memberMapper.getUserInfo(u.getId());
        //创业公司，科创人员审批，不可再次驳回  1GP, 2科创
        if(memberUser.getURole().equals("5") && u1.getURole().equals("1")){

            //数据库中状态是3，判断审批人是哪家gp,已经审批过的，返回不可审批
            if(memberUser.getApprovalstatus().equals("3")){

                if(u1.getMemberId().equals(memberUser.getModifymemberId())){
                    return 0;
                }else{
                    return 2;
                }
            }//数据库中状态是0，判断关联基金的GP相同的数量，>1 状态设为3，<=1 设为2
            else if(memberUser.getApprovalstatus().equals("0")){
                List<String> gps = memberMapper.getsameGp(memberUser.getInvestmentFund());
                if(gps!=null && gps.size()>1){
                    return 3;
                }else{
                    return 2;
                }
            }else{
                return 2;
            }
        }else{
            return 2;
        }
    }

    @Transactional
    public Map<String,Object> approve(String param){
        Map<String,Object> result = new HashMap<>();
        MemberUser md = null;
        try {
            md = JSONObject.parseObject(param,MemberUser.class);
        } catch (Exception e) {
            e.printStackTrace();
            Constants.getErrMsg(result, "信息加载失败");
            return  result;
        }
        if(md != null && !md.equals("")) {
            if(md.getApprovalstatus().equals("2")){
                int re = this.setchuangyeApprovalstatus(md.getId());
                if(re == 2){
                    md.setApprovalstatus("2");
                }else if(re == 3){
                    md.setApprovalstatus("3");
                }else{
                    Constants.getErrMsg(result, "已经审批过了！");
                    return result;
                }
            }
                super.updateBaseInfo(md, md.getId());//使用父类中通用的修改代码
                Integer i = ApproveMapper.updateApprovalstatus(md);
                if(i>0 && md.getApprovalstatus().equals("1")){
                    int addURole = userMapper.addURole(md.getId());
                    MemberUser memberUser = ApproveMapper.getMemberDetailById(md.getId());
                    Integer i1 = setNotes("003","003","003",memberUser.getUserId());
                }
                if(i>0 && md.getApprovalstatus().equals("2")){
                    int addURole = userMapper.updateUserCherk(md.getId());
                    MemberUser memberUser = ApproveMapper.getMemberDetailById(md.getId());
                    Integer i1 = setNotes("001","001","001",memberUser.getUserId());
                }
                Constants.getSuccMsg(result,i>0);
        }else{
            Constants.getErrMsg(result, "信息加载失败");
        }

        return  result;
    }

    /**
     * 设置消息
     * 审批通知
     * @param title
     * @param content
     * @return
     */
    public Integer setNotes(String notesid,String title,String content,String userId){
        Notes notes = new Notes();

        List<NotesDetail> details = new ArrayList<NotesDetail>();

        if(userId == null || userId.equals("")){
            return -1;
        }
        Integer i1 = 0;
        //获取消息id
        Dictionary dnotes = memberMapper.selectDictionaryByCode(notesid,"notesid");
        //Notes notes1 = new Notes();
        if(dnotes == null || dnotes.equals("") || dnotes.getName() == null ||dnotes.getName().equals("")){
            //数据库获取title
            Dictionary dtitle = memberMapper.selectDictionaryByCode(title,"title");
            Dictionary dcontent = memberMapper.selectDictionaryByCode(content,"content");
            if(dcontent == null || dcontent.equals("") || dcontent.getName() == null ||dcontent.getName().equals("")){
                if(content.equals("003")){
                    notes.setContent("您好，您的认证申请已经通过。您现在可以浏览科创生态小程序全部信息和服务。");
                }else if(content.equals("001")){
                    notes.setContent("您好，您的认证申请未通过，请联系管理员。");
                }
            }else{
                notes.setContent(dcontent.getName());
            }
            if(dtitle == null || dtitle.equals("") || dtitle.getName() == null ||dtitle.getName().equals("")){
                if(title.equals("003")){
                    notes.setTitle("认证通过");
                }else if(title.equals("001")){
                    notes.setTitle("认证驳回");
                }

            }else{
                notes.setTitle(dtitle.getName());
            }
            notes.setTaskType("user");
            notes.setIsFeedBack(0);
            notes.setStatus("1");
            notes.setFlag(3);
            super.insertBaseInfo(notes);
                NotesDetail notesDetail = new NotesDetail();
                notesDetail.setNotesId(notes.getId());
                notesDetail.setUserId(userId);
                notesDetail.setStatus("0");
                super.insertBaseInfo(notesDetail);
                details.add(notesDetail);

            notes.setNotesDetailList(details);
            i1 = memberMapper.insertNotes(notes);
        }else {
            //获取消息
            List<Notes> n = notesMapper.getNoteById(dnotes.getName());
            if(n == null || n.size() <= 0){
                //添加消息
                //数据库获取title
                Dictionary dtitle = memberMapper.selectDictionaryByCode(title,"title");
                Dictionary dcontent = memberMapper.selectDictionaryByCode(content,"content");
                if(dcontent == null || dcontent.equals("") || dcontent.getName() == null ||dcontent.getName().equals("")){
                    if(content.equals("003")){
                        notes.setContent("您好，您的认证申请已经通过。您现在可以浏览科创生态小程序全部信息和服务。");
                    }else if(content.equals("001")){
                        notes.setContent("您好，您的认证申请未通过，请联系管理员。");
                    }
                }else{
                    notes.setContent(dcontent.getName());
                }
                if(dtitle == null || dtitle.equals("") || dtitle.getName() == null ||dtitle.getName().equals("")){
                    if(title.equals("003")){
                        notes.setTitle("认证通过");
                    }else if(title.equals("001")){
                        notes.setTitle("认证驳回");
                    }
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

                NotesDetail notesDetail = new NotesDetail();
                notesDetail.setNotesId(dnotes.getName());
                notesDetail.setUserId(userId);
                notesDetail.setStatus("0");
                super.insertBaseInfo(notesDetail);
                details.add(notesDetail);

            i1 = memberMapper.insertNotesDetails(details);
        }
        List<String> userIds = new ArrayList<String>();
        userIds.add(userId);
        notes.setUserIds(userIds);
        //发送微信消息
        String templateId = "2WYFptTPrbIExm97CLGZ-iaveFA19WQGxwi5q7yyeRI";
        notes.setTemplateId(templateId);
        this.sendweixin(notes);
        this.sendEmaillist(notes);

        return i1;

    }

    /**
     * 是否需要做异步
     * @param notes
     */
    public void sendweixin(Notes notes){
        //审核人姓名
        String userName = notesMapper.getRealName(notes.getUserIds().get(0));
        notes.setUserName(userName);
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

}
