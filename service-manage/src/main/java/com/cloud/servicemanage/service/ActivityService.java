package com.cloud.servicemanage.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.entity.BaseUserInfo;
import com.cloud.commonsmng.entity.appletEntity.*;
import com.cloud.commonsmng.factory.BaseService;
import com.cloud.servicemanage.common.ConstantUtil;
import com.cloud.servicemanage.common.PageUtil;
import com.cloud.servicemanage.common.PageVo;
import com.cloud.servicemanage.controller.MainController;
import com.cloud.servicemanage.entity.*;
import com.cloud.servicemanage.mapper.ActivityMapper;
import com.cloud.servicemanage.mapper.UserInfoMapper;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service("Activity")
public class ActivityService extends BaseService {

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    private static final Logger logger = LoggerFactory.getLogger(ActivityService.class);

    @Override
    public Map<String, Object> exec(String method, String param) {
        Map<String, Object> obj = new HashMap<>();
        switch (method) {
            case ConstantUtil.GET_ACTIVITY_LIST: //getActivityList  ??????????????????
                obj = getActivityList(param);
                break;
            case ConstantUtil.GET_SIGN_LIST: //getActivityList  ??????????????????
                obj = getSignList(param);
                break;
            case ConstantUtil.SET_ACTIVITY: //setActivity  ????????????
                obj = setActivity(param);
                break;
            case ConstantUtil.GET_QUESTIONNAIRE_LIST: //getQuestionnaireList  ??????????????????
                obj = getQuestionnaireList(param);
                break;
            case ConstantUtil.SET_QUESTIONNAIRE: //setQuestionnaire  ????????????
                obj = setQuestionnaire(param);
                break;
            case ConstantUtil.UPDATE_QUESTIONNAIRE: //deleteQuestionnaire  ????????????
                obj = updateQuestionnaire(param);
                break;
            case ConstantUtil.DELETE_QUESTIONNAIRE: //deleteQuestionnaire  ????????????
                obj = deleteQuestionnaire(param);
                break;
            case ConstantUtil.DELETE_ACTIVITY: //deleteActivity  ????????????
                obj = deleteActivity(param);
                break;
            case ConstantUtil.UPDATE_ACTIVITY: //updateActivity  ??????????????????
                obj = updateActivity(param);
                break;
            case ConstantUtil.PRODUCT_DOWNLOAD: //ActivityLoad  ????????????
                obj = DownloadFile(param);
                break;
            case ConstantUtil.GET_ANSWER_MEMBER: //getAnswerMember ??????????????????????????????
                obj = getAnswerMember(param);
                break;
            case ConstantUtil.GET_ANSWER_DETAIL: //getAnswerMember ??????????????????????????????
                obj = getAnswerDetail(param);
                break;  
            default:
                break;
        }
        return obj;
    }


    /**
     *
     * @param param
     * @return
     */
    public Map<String,Object> getActivityList(String param){
        Map<String, Object> result = new HashMap<>();
        PageVo pageVo = JSONObject.parseObject(param, PageVo.class);//??????????????????????????????????????????
        PageUtil pageUtil = pageVo.getPage();
        int totalNum = activityMapper.countActivityList(pageVo);
        if (totalNum > 0) {
            List<ActivityEntity> activityList = activityMapper.getActivityList(pageVo);
            for (ActivityEntity activityEntity : activityList) {
                String id=activityEntity.getId();
                Map<String,Object> sign = new HashMap<>();
                sign.put("id",id);
                pageVo.setSearchdata(sign);
                int num = activityMapper.countSignList(pageVo);
                activityEntity.setSignTolNum(num);
            }
            PageVo<ActivityEntity> pageVo1 = new PageVo<>();
            pageVo1.setPage(new PageUtil(pageUtil.getPageIndex(), pageUtil.getPageSize(), totalNum));
            pageVo1.setDataList(activityList);
            Constants.getSuccMsg(result, pageVo1);
        }
        return result;
    }

    public Map<String,Object> getSignList(String param){
        Map<String, Object> result = new HashMap<>();
        PageVo pageVo = JSONObject.parseObject(param, PageVo.class);
        PageUtil pageUtil = pageVo.getPage();
        int totalNum = activityMapper.countSignList(pageVo);
        if (totalNum > 0) {
            List<SignEntity> signList = activityMapper.getSignList(pageVo);
            for (SignEntity signEntity : signList) {
                List<SignDetail> sdList = activityMapper.getSignTemAs(signEntity.getId());
                signEntity.setSdList(sdList);
            }
            PageVo<SignEntity> pageVo1 = new PageVo<>();
            pageVo1.setPage(new PageUtil(pageUtil.getPageIndex(), pageUtil.getPageSize(), totalNum));
            pageVo1.setDataList(signList);
            Constants.getSuccMsg(result, pageVo1);
        }
        return result;
    }


//
//    public Map<String,Object> getActivityList(String param){
//        Map<String, Object> result = new HashMap<>();
//        PageVo pageVo = JSONObject.parseObject(param, PageVo.class);//??????????????????????????????????????????
//        PageUtil pageUtil = pageVo.getPage();
//        int totalNum = activityMapper.countActivityList(pageVo);
//        if (totalNum > 0) {
//            List<ActivityEntity> activityList = activityMapper.getActivityList(pageVo);
//            PageVo<ActivityEntity> pageVo1 = new PageVo<>();
//            pageVo1.setPage(new PageUtil(pageUtil.getPageIndex(), pageUtil.getPageSize(), totalNum));
//            pageVo1.setDataList(activityList);
//            Constants.getSuccMsg(result, pageVo1);
//        }
//        return result;
//    }

    /**
     * ????????????
     * @param param
     * @return
     */
    public Map<String,Object> setActivity(String param){
        Map<String,Object> result=new HashMap<>();
        ActivityEntity activity=JSONObject.parseObject(param,ActivityEntity.class);
        activity.setDelflag("1");
        activity.setIsused("1");
        super.insertBaseInfo(activity);//????????????????????????????????????
        List<ActivityTemplate> at =activity.getActivityTemplate();
        if(at!=null){
            for(ActivityTemplate activityTemplate: at){
                if(activityTemplate==null){
                    continue;
                }else{
                    super.insertBaseInfo(activityTemplate);
                    activityTemplate.setActivityId(activity.getId());
                    activityTemplate.setDelFlag("1");
                    activityTemplate.setIsused("1");
                    boolean f =  activityMapper.insertActivityTemplate(activityTemplate);
                }
            }
            boolean flag=activityMapper.insertActivity(activity);
            Constants.getSuccMsg(result,flag);
        }
        return result;
    }


    /**
     * ??????????????????
     * @param param
     * @return
     */
    public Map<String,Object> updateActivity(String param){
        Map<String,Object> result=new HashMap<>();
        ActivityEntity activity=JSONObject.parseObject(param,ActivityEntity.class);
        activity.setDelflag("1");
        super.updateBaseInfo(activity,activity.getId());//????????????????????????????????????
        List<ActivityTemplate> at =activity.getActivityTemplate();
        for(ActivityTemplate activityTemplate: at){
            super.updateBaseInfo(activityTemplate,activityTemplate.getActivityTemId());
            boolean f =  activityMapper.updateActivityTemplate(activityTemplate);
        }
        boolean flag=activityMapper.updateActivity(activity);
        Constants.getSuccMsg(result,flag);
        return result;
    }


    /**
     * ??????????????????
     * @param param
     * @return
     */
    public Map<String,Object> getQuestionnaireList(String param){
        Map<String,Object> result=new HashMap<>();
        JSONObject jsonObject=JSONObject.parseObject(param);

        JSONObject page=jsonObject.getJSONObject("page");
        JSONObject searchData=jsonObject.getJSONObject("searchData");

        Map<String,Object> map=searchData.getInnerMap();
        String pageIndex=page.getString("pageIndex");
        String pageSize=page.getString("pageSize");

        int index= Integer.parseInt(pageIndex);
        int size= Integer.parseInt(pageSize);

        //????????????
        List<Questionnaire> questionnaireList=activityMapper.selectQuestionnaireList(map);
        for (int i = 0; i <questionnaireList.size() ; i++) {
            //??????????????????
            Questionnaire q=activityMapper.getQuestionMsg(questionnaireList.get(i).getQuestionBelong());
            questionnaireList.set(i,q);
            //????????????????????????????????????????????????
            List<Question> questionList=activityMapper.selectQuestionList(questionnaireList.get(i).getQuestionBelong());
            for (int j = 0; j <questionList.size() ; j++) {
                //????????????id??????????????????
                List<MyQuestion> myQuestionList=activityMapper.selectMyQuestionList(questionList.get(j).getId());
                questionList.get(j).setMyQuestion(myQuestionList);
            }
            questionnaireList.get(i).setQuestion(questionList);
        }

        Map<String,Object> map1=new HashMap<>();
        map1.put("total",questionnaireList.size());
        questionnaireList=questionnaireList.stream().skip((index-1)*size).limit(size).collect(Collectors.toList());
        map1.put("questionnaireList",questionnaireList);
        Constants.getSuccMsg(result,map1);
        return result;
    }


//    /**
//     * ????????????
//     * @param param
//     * @return
//     */
//    public Map<String,Object> updateQuestionnaire(String param){
//        Map<String,Object> result=new HashMap<>();
//        QuestionEntity question = JSONObject.parseObject(param, QuestionEntity.class);
//        Date date=new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String modifyTime = sdf.format(date);
//        question.setModifierId(usr.getEmpid());
//        question.setModifyTime(modifyTime);
//        boolean a=false;
//        boolean flag=activityMapper.updateQuestion(question);
//        List<QuestionOptionsEntity> myquestions=question.getMyquestion();
//            for (QuestionOptionsEntity myquestion : myquestions ) {
//
//                a =activityMapper.updateMyQuestion(myquestion);
//            }
//            Constants.getSuccMsg(result,a);
//        return result;
//    }



    /**
     * ????????????
     * @param param
     * @return
     */
    public Map<String,Object> setQuestionnaire(String param){
        Map<String,Object> result=new HashMap<>();

        JSONObject jsonObject=JSONObject.parseObject(param);
        //????????????
        String questionname=jsonObject.getString("questionname");
        String questionBelong=jsonObject.getString("questionBelong");
        String questionnairetypes=jsonObject.getString("questionnairetypes");
        JSONArray jsonArray=jsonObject.getJSONArray("ques");

        deleteQuestion(questionBelong);

        //??????????????????
        Date date=new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = sdf.format(date);
        boolean flag1=false;
        for (int i = 0; i <jsonArray.size() ; i++) {
            JSONObject obj=jsonArray.getJSONObject(i);
            //uuid
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            Map<String,Object> map=obj.getInnerMap();
            map.put("uuid",uuid);
            map.put("questionname",questionname);
            map.put("questionnairetypes",questionnairetypes);
            map.put("questionBelong",questionBelong);
            map.put("creatorId",usr.getEmpid());
            map.put("createTime",createTime);
            map.put("delFlag","1");
            boolean flag=activityMapper.insertQuestion(map);
            JSONArray array=obj.getJSONArray("myQuestion");
            for (int j = 0; j <array.size() ; j++) {
                JSONObject json=array.getJSONObject(j);
                Map<String,Object> map1=json.getInnerMap();
                map1.put("uuid",uuid);
                map1.put("creatorId",usr.getEmpid());
                map1.put("createTime",createTime);
                map1.put("delFlag","1");
                flag1=activityMapper.insertMyQuestion(map1);
            }
        }
        Constants.getSuccMsg(result,flag1);
        return result;
    }


    /**
     * ????????????
     * @param param
     * @return
     */
    public Map<String,Object> updateQuestionnaire(String param){
        Map<String,Object> result=new HashMap<>();

        //??????????????????
        Date date=new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = sdf.format(date);

        //????????????????????????

        boolean flag=false;
        JSONObject jsonObject=JSONObject.parseObject(param);
        //????????????
        String questionname=jsonObject.getString("questionname");
        String questionnairetypes=jsonObject.getString("questionnairetypes");
        String questionBelong=jsonObject.getString("questionBelong");
        //??????????????????
        JSONArray ja=jsonObject.getJSONArray("delQuest");
        //????????????????????????
        JSONArray jr=jsonObject.getJSONArray("delMy");

        //????????????
        JSONArray jsonArray=jsonObject.getJSONArray("ques");

        if (null!= ja &&ja.size()>0){
            for (int i = 0; i <ja.size() ; i++) {
                JSONObject jo=ja.getJSONObject(i);
                String id=jo.getString("id");
                //????????????????????????
                boolean flag1=activityMapper.deleteQuest(id);
                //??????????????????????????????
                flag=activityMapper.deleteMy(id);
            }
        }
        if (null!= jr &&jr.size()>0){
            for (int i = 0; i <jr.size() ; i++) {
                JSONObject jb=jr.getJSONObject(i);
                String id=jb.getString("id");
                //??????????????????????????????
                flag=activityMapper.deleteMyquest(id);
            }
        }

        for (int i = 0; i <jsonArray.size() ; i++) {
            JSONObject obj=jsonArray.getJSONObject(i);
            String id=obj.getString("id");
            if (id == null ||id.equals("")){
                //??????id?????????????????????
                //uuid
                String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                Map<String,Object> map=obj.getInnerMap();
                map.put("uuid",uuid);
                map.put("questionname",questionname);
                map.put("questionnairetypes",questionnairetypes);
                map.put("questionBelong",questionBelong);
                map.put("creatorId",usr.getEmpid());
                map.put("createTime",createTime);
                map.put("delFlag","1");
                flag=activityMapper.insertQuestion(map);
                JSONArray array=obj.getJSONArray("myQuestion");
                for (int j = 0; j <array.size() ; j++) {
                    JSONObject json=array.getJSONObject(j);
                    Map<String,Object> map1=json.getInnerMap();
                    map1.put("uuid",uuid);
                    map1.put("creatorId",usr.getEmpid());
                    map1.put("createTime",createTime);
                    map1.put("delFlag","1");
                    flag=activityMapper.insertMyQuestion(map1);
                }
            }else {
                //id?????????????????????
                Map<String,Object> map=obj.getInnerMap();
                map.put("questionname",questionname);
                map.put("questionnairetypes",questionnairetypes);
                map.put("questionBelong",questionBelong);
                map.put("creatorId",usr.getEmpid());
                map.put("createTime",createTime);
                flag=activityMapper.updateQuest(map);
                JSONArray array=obj.getJSONArray("myQuestion");
                for (int j = 0; j <array.size() ; j++) {
                    JSONObject json=array.getJSONObject(j);
                    String myId=json.getString("id");
                    if (null == myId ||myId.equals("")){
                        //???????????????  ??????
                        Map<String,Object> map1=json.getInnerMap();
                        map1.put("uuid",id);
                        map1.put("creatorId",usr.getEmpid());
                        map1.put("createTime",createTime);
                        map1.put("delFlag","1");
                        flag=activityMapper.insertMyQuestion(map1);
                    }else {
                        //????????????   ??????
                        Map<String,Object> map1=json.getInnerMap();
                        map1.put("creatorId",usr.getEmpid());
                        map1.put("createTime",createTime);
                        flag=activityMapper.updateMyQuest(map1);
                    }

                }
            }

        }
        Constants.getSuccMsg(result,flag);
        return result;
    }




    /**
     * ????????????
     * @param param
     * @return
     */
    public Map<String,Object> deleteQuestionnaire(String param){
        Map<String,Object> result=new HashMap<>();

        JSONObject jsonObject=JSONObject.parseObject(param);
        String questionname=jsonObject.getString("questionBelong");
        //????????????????????????????????????id
        List<Question> list=activityMapper.selectQuestionList(questionname);
        for (int i = 0; i <list.size() ; i++) {
            //??????????????????????????????
            boolean flag=activityMapper.deleteMyQuestion(list.get(i).getId());
        }
        //?????????????????????????????????
        boolean flag=activityMapper.deleteQuestion(questionname);
        Constants.getSuccMsg(result,flag);
        return result;
    }


    /**
     * ????????????--????????????
     * @param param
     * @return
     */
    public Map<String,Object> deleteActivity(String param){
        Map<String,Object> result=new HashMap<>();
        JSONObject jsonObject=JSONObject.parseObject(param);
        String id=jsonObject.getString("id");
        boolean flag=activityMapper.deleteActivityId(id);
        Constants.getSuccMsg(result,flag);
        return result;
    }

    /**
     * ??????
     * @param
     * @return
     */
//    @RequestMapping(value = {"/ActivityLoad"}, method = RequestMethod.POST, produces = "application/x-msdownload;charset=utf-8")
    public Map<String,Object> DownloadFile(String param) {
        Map<String,Object> result = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(param);
        String id = jsonObject.getString("ID");
        String fileName= "";
        Map<String,Object>  list =activityMapper.SelectActivityFile(id);
        logger.info(String.format("?????????????????????list: [%s]", list));


        //??????ServletRequestAttributes??????
        ServletRequestAttributes servletRequestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());

        //??????HttpServletRequest??????
        HttpServletResponse response = servletRequestAttributes.getResponse();


        try(FileInputStream inputStream = new FileInputStream(new File(list.get("pic").toString()));
            ServletOutputStream out = response.getOutputStream();
        ){
            fileName = list.get("name").toString();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1){
                os.write(buffer,0,length);
            }
            byte[] data=os.toByteArray();
            response.resetBuffer();
            response.setHeader("content-Disposition","attachment");
            response.addHeader("fileName", fileName);
            response.addHeader("Contern-Length",""+data.length);
            response.setContentType("application/x-msdownload;charset=utf-8");

            IOUtils.write(data,out);
//            out.flush();
            Constants.getSuccMsg(result,true);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * ????????????
     * @return
     */
    public boolean deleteQuestion(String questionname){
        //????????????????????????????????????id
        List<Question> list=activityMapper.selectQuestionList(questionname);
        for (int i = 0; i <list.size() ; i++) {
            //??????????????????????????????
            boolean flag=activityMapper.deleteMyQuestion(list.get(i).getId());
        }
        //?????????????????????????????????
        boolean flag=activityMapper.deleteQuestion(questionname);
        return flag;
    }

    /**
     * ??????????????????????????????
     * @param param
     * @return
     */
    public Map<String,Object> getAnswerMember(String param){
        Map<String,Object> result=new HashMap<>();
        PageVo pagevo = JSONObject.parseObject(param,PageVo.class);
        PageUtil pageUtil = pagevo.getPage();//?????????????????????????????????

        ActivityEntity activity=JSONObject.parseObject(param,ActivityEntity.class);
        BaseUserInfo user = this.getUserInfo();
        String uRole="";
        if(StringUtils.equals("005",user.getDepartmentId())){
            uRole ="pf";
        }else if(StringUtils.equals("006",user.getDepartmentId())){
            uRole ="pg";
        } else{
            Constants.getSuccMsg(result,null);
            return result;
        }

        //??????????????????
        User u1 = userInfoMapper.getuRolebyId(user.getId());

        if(StringUtils.equals("2",u1.getURole())){
            uRole ="pf";
        }

        Object searchdata = pagevo.getSearchdata();
        Map<String, Object> searchMap = JSONObject.parseObject(JSONObject.toJSONString(searchdata), HashMap.class);
//        searchMap.put("uRole",uRole);
        pagevo.setSearchdata(searchMap);

        int totalNum = activityMapper.countQesMemberList(pagevo);
        List<User> products = activityMapper.getQesMemberList(pagevo);
        products.forEach((User u) -> {
           Member m =  activityMapper.getMemberByUserId(u.getId());
           u.setMember(m);
        });

        PageVo<User> productPageVo = new PageVo<>();
        productPageVo.setPage(new PageUtil(pageUtil.getPageIndex(), pageUtil.getPageSize(), totalNum));
        productPageVo.setDataList(products);
        Constants.getSuccMsg(result,productPageVo);
        return result;
    }

    /**
     * ??????????????????????????????
     * @param param
     * @return
     */
    public Map<String,Object> getAnswerDetail(String param){
        Map<String,Object> result=new HashMap<>();

        JSONObject json = JSONObject.parseObject(param);
        String questionBelong = json.getString("questionBelong");
        String userId = json.getString("userId");
        List<QuestionEntity>  list=   activityMapper.getUserAnswer(userId,questionBelong);
        list.forEach((QuestionEntity q) -> {
            String opinion = activityMapper.getUserAnswerByQuestionId(q.getId(),userId);
            List<String> ls= new ArrayList<>();
            if(StringUtils.isNotBlank(opinion) && opinion.contains(",")){
                String[] os = opinion.split(",");
                Collections.addAll(ls,os);
            }else {
                ls.add(opinion);
            }
            List<QuestionOptionsEntity> lq = activityMapper.getUserAnswerDetail(q.getId(),ls);
            q.setQuestionOptionsList(lq);
        });
        Constants.getSuccMsg(result,list);
        return result;
    }
}



