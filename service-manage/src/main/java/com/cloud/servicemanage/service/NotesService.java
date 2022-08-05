package com.cloud.servicemanage.service;

import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.entity.appletEntity.*;
import com.cloud.commonsmng.entity.appletEntity.Dictionary;
import com.cloud.commonsmng.factory.BaseService;
import com.cloud.commonsmng.factory.ExecuteService;
import com.cloud.servicemanage.common.*;
import com.cloud.servicemanage.mapper.MemberMapper;
import com.cloud.servicemanage.mapper.NotesDetailMapper;
import com.cloud.servicemanage.mapper.NotesMapper;
import com.cloud.servicemanage.mapper.UserInfoMapper;
import com.cloud.servicemanage.utils.SendMailThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 通知管理的业务逻辑代码
 * author: tjs
 */
@Service("Notes")
public class NotesService extends BaseService {

    @Autowired
    private NotesMapper notesMapper;

    @Autowired
    private NotesDetailMapper notesDetailMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private MemberMapper memberMapper;

    //实例化日志对象，引用jar包org.slf4j.Logger
    private static final Logger logger = LoggerFactory.getLogger(NotesService.class);

    @Override
    public Map<String, Object> exec(String method, String param) {
        Map<String, Object> obj = new HashMap<>();
        switch (method) {
            case ConstantUtil.GET_NOTES_PAGE_VO: //getNotesPageVo
                obj = getNotesPageVo(param);
                break;
            case ConstantUtil.INSERT_NOTES: //insertNotes
                obj = insertNotes(param);
                break;
            case ConstantUtil.UPDATE_NOTES: // updateNotes
                obj = updateNotes(param);
                break;
            case ConstantUtil.DELETE_NOTES: //deleteNotes
                obj = deleteNotes(param);
                break;
            case ConstantUtil.GET_NOTES_BY_ID: //getNotesById
                obj = getNotesById(param);
                break;
            case ConstantUtil.ISSUE_NOTES: //issueNotes
                obj = issueNotes(param);
                break;
            case ConstantUtil.SELECT_NOTES_DETAIL_BY_NOTES_ID: //selectNotesDetailByNotesId
                obj = selectNotesDetailByNotesId(param);
                break;
            case ConstantUtil.SELECT_DICTIONARY_BY_KIND_EQ_TASK_TYPE: //selectDictionaryByKindEqTaskType
                obj = selectDictionaryByKindEqTaskType(param);
                break;
            case ConstantUtil.GET_READ_USER: //selectDictionaryByKindEqTaskType
                obj = getReadUser(param);
                break;
            case ConstantUtil.GET_DEL_NOTES: //selectDictionaryByKindEqTaskType
                obj = getDelNotesList();
                break;
            default:
                break;
        }
        return obj;
    }

    //获取码表中的消息类型
    private Map<String, Object> selectDictionaryByKindEqTaskType(String param) {
        Map<String, Object> result = new HashMap<>();
        //mybatis-plus 自带修改
        List<Dictionary> dictionaryList = notesMapper.selectDictionaryByKindEqTaskType();
        Constants.getSuccMsg(result, dictionaryList);
        return result;
    }

    //查询详细的消息信息（包含用户信息）
    private Map<String, Object> selectNotesDetailByNotesId(String param) {
        Map<String, Object> result = new HashMap<>();

        PageVo pageVo = JSONObject.parseObject(param, PageVo.class);
        PageUtil pageUtil = pageVo.getPage();
        try {
            int pageTotal = notesDetailMapper.getPageTotal(pageVo);//获取页面数据总条数
            if (pageTotal > 0) {
                List<NotesDetail> notesDetailList = notesDetailMapper.selectNotesDetailByNotesId(pageVo);//获取页面信息

                //创建PageVo对象
                PageVo<NotesDetail> notesPageVo = new PageVo<>();
                //将前端传递的页面数据，和查询到的数据总条数赋值
                notesPageVo.setPage(new PageUtil(pageUtil.getPageIndex(), pageUtil.getPageSize(), pageTotal));
                notesPageVo.setDataList(notesDetailList);

                Constants.getSuccMsg(result, notesPageVo);
            } else {
                Constants.getSuccMsg(result, "没有数据");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return result;
    }

    //发布消息
    private Map<String, Object> issueNotes(String param) {
        Map<String, Object> result = new HashMap<>();

        //获取前端传递的id
        String id = JSONObject.parseObject(param).getString("id");

        Notes notes = new Notes();
        super.updateBaseInfo(notes, id);//使用父类中通用的修改代码
        notes.setStatus("1");//在数据库中1表示发布

        //mybatis-plus 自带修改
        int update = notesMapper.updateById(notes);

        Constants.getSuccMsg(result, update > 0);
        if(update > 0){
            //获取通知类型flag，2和3发送邮件.通知人群 all
            Notes notes1 = notesMapper.getnoteInfobyId(id);
            //判断通知人群 all 发送全部人员
            if(notes1.getTaskType().equals("all")){
                //根据人员角色类型获取人员
                List<String> users = userInfoMapper.getUserIdList();
                notes1.setUserIds(users);
                setNotesDetailList(notes1);//给notes中List<NotesDetail赋值
                int nd = notesMapper.insertNotesDetails(notes1);
            }
            //判断发送类型为会员 1，2，3，4，5
            if(!notes1.getTaskType().equals("user")&&!notes1.getTaskType().equals("all")){
                //根据人员角色类型获取人员
                List<String> users = userInfoMapper.getUserListByuRole(notes1.getTaskType());
                notes1.setUserIds(users);
                setNotesDetailList(notes1);//给notes中List<NotesDetail赋值
                int nd = notesMapper.insertNotesDetails(notes1);
            }
            if(notes1.getFlag()==2||notes1.getFlag()==3){
                sendEmaillist(notes1);
            }
        }
        //发送邮件
        //获取通知类型flag，2和3发送邮件.通知人群 all
       // ExecutorService es = Executors.newFixedThreadPool(10);
       // es.execute(new SendMailThread("1441327160@qq.com","测试","你好，测试邮箱","jibijun123@126.com","AFGRCGMGYWQADBES"));
        return result;
    }

    public void sendEmaillist(Notes notes){
        List<String> emailList = null;

        //获取nodes_detail表中对应人员的数据
        emailList = notesMapper.getNotesDetailEmails(notes.getId());

        /*//判断通知人群 all 发送全部人员
        if(notes.getTaskType().equals("all")){
            //获取数据库中所有人的email
           emailList = userInfoMapper.getUseremailAll();
        }
        //判断通知类型为user
        if(notes.getTaskType().equals("user")){
            //获取nodes_detail表中对应人员的数据
            emailList = notesMapper.getNotesDetailEmails(notes.getId());
        }
        //判断发送类型为会员 1，2，3，4，5
        if(!notes.getTaskType().equals("user")&&!notes.getTaskType().equals("all")){
            //根据人员角色类型获取人员
            List<String> users = userInfoMapper.getUserListByuRole(notes.getTaskType());
            notes.setUserIds(users);
            setNotesDetailList(notes);//给notes中List<NotesDetail赋值
            int nd = notesMapper.insertNotesDetails(notes);
            if(nd > 0){
                //获取nodes_detail表中对应人员的数据
                emailList = notesMapper.getNotesDetailEmails(notes.getId());
            }
        }*/
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

    //根据id查询消息信息
    private Map<String, Object> getNotesById(String param) {
        Map<String, Object> result = new HashMap<>();

        //获取前端传递的id
        String id = JSONObject.parseObject(param).getString("id");

        //根据id查询
        Notes notes = notesMapper.getNotesById(id);

        Constants.getSuccMsg(result, notes);
        return result;
    }

    //删除消息信息
    private Map<String, Object> deleteNotes(String param) {
        Map<String, Object> result = new HashMap<>();

        //获取前端传递的id
        String id = JSONObject.parseObject(param).getString("id");

        //创建Notes对象
        Notes notes = new Notes();
        super.updateBaseInfo(notes, id);//使用父类中通用的修改代码
        //逻辑删除，修改delFlag属性为false
        notes.setDelFlag(false);

        //mybatis-plus 自带根据id修改
        int deleteNotes = notesMapper.updateById(notes);
        Constants.getSuccMsg(result, deleteNotes > 0);
        return result;
    }

    //修改消息信息
    private Map<String, Object> updateNotes(String param) {
        Map<String, Object> result = new HashMap<>();
        //转化json格式数据为Notes对象
        Notes notes = JSONObject.parseObject(param, Notes.class);
        //设置修改者为当前用户
        super.updateBaseInfo(notes, notes.getId());//使用父类中通用的修改代码
        setNotesDetailList(notes);//给notes中List<NotesDetail赋值

        int updateNotes = notesMapper.updateNotes(notes);
        Constants.getSuccMsg(result, updateNotes > 0);
        return result;
    }

    //新增消息信息
    private Map<String, Object> insertNotes(String param) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> map = new HashMap<>();
        //获取当前用户编号
        String userId = super.getUserInfo().getId();
        //转化json格式数据为Notes对象
        Notes notes = JSONObject.parseObject(param, Notes.class);

        if(notes.getTaskType()==null || notes.getTaskType().equals("")){
            map.put("code",203);
            map.put("msg","请选择发送对象");
            Constants.getSuccMsg(result, map);
            return result;

        }
        if(notes.getTaskType().equals("all")){

        }
        /*if(!notes.getTaskType().equals("")&&!notes.getTaskType().equals("all")){
            //根据人员角色类型获取人员
            List<String> users = userInfoMapper.getUserListByuRole(notes.getTaskType());
            notes.setUserIds(users);
        }*/
        super.insertBaseInfo(notes);//使用父类中通用的新增代码
        setNotesDetailList(notes);//给notes中List<NotesDetail赋值
        //新增
        int insertNotes = notesMapper.insertNotes(notes);
        Constants.getSuccMsg(result, insertNotes > 0);
        return result;
    }

    //分页获取所有的消息信息
    private Map<String, Object> getNotesPageVo(String param) {
        Map<String, Object> result = new HashMap<>();

        //转化json格式数据为PageVo对象
        PageVo pageVo = JSONObject.parseObject(param, PageVo.class);
        PageUtil pageUtil = pageVo.getPage();//获取前端页面信息
        try {
            int pageTotal = notesMapper.getPageTotal(pageVo);//获取页面数据总条数
            if (pageTotal > 0) {
                List<Notes> notesList = notesMapper.getNotesPageVo(pageVo);//获取页面信息

                //创建PageVo对象
                PageVo<Notes> notesPageVo = new PageVo<>();
                //将前端传递的页面数据，和查询到的数据总条数赋值
                notesPageVo.setPage(new PageUtil(pageUtil.getPageIndex(), pageUtil.getPageSize(), pageTotal));
                notesPageVo.setDataList(notesList);

                Constants.getSuccMsg(result, notesPageVo);
            } else {
                Constants.getSuccMsg(result, "没有数据");
            }
        } catch (Exception e) {
            Constants.getErrMsg(result, "查询失败");
            logger.error(e.getMessage());
        }
        return result;
    }

    //设置消息详情的值
    private void setNotesDetailList(Notes notes) {
        List<NotesDetail> notesDetailList = new ArrayList<>();
        if (null != notes.getUserIds() && notes.getUserIds().size() > 0) {
            for (String uid : notes.getUserIds()) {
                NotesDetail notesDetail = new NotesDetail();
                super.insertBaseInfo(notesDetail);//使用父类中通用的新增代码
                notesDetail.setNotesId(notes.getId());
                notesDetail.setStatus("0");//默认为未读
                notesDetail.setUserId(uid);
                notesDetailList.add(notesDetail);
            }
        }
        notes.setNotesDetailList(notesDetailList);
    }


    //获取已读消息
    private Map<String, Object> getReadUser(String param) {
        Map<String, Object> result = new HashMap<>();
        try {
            String  id = JSONObject.parseObject(param).getString("id");
            List<User> list =  notesMapper.getReadUser(id);
            Constants.getSuccMsg(result,list);
        } catch (Exception e) {
            e.printStackTrace();
            Constants.getErrMsg(result,Constants.RESULT_NO_DATA);
        }
        return result;
    }

    //获取已经发布消息的删除记录
    private Map<String, Object> getDelNotesList(){
        Map<String, Object> result = new HashMap<>();
        try {
            List<Notes> notes = notesMapper.getDelNotesList("1");
            Constants.getSuccMsg(result,notes);
        } catch (Exception e) {
            e.printStackTrace();
            Constants.getErrMsg(result,Constants.RESULT_NO_DATA);
        }
        return result;

    }
}
