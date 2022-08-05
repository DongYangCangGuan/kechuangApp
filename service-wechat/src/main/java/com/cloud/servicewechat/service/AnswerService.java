package com.cloud.servicewechat.service;

import com.alibaba.fastjson.JSONObject;
import com.amazonaws.util.StringUtils;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.entity.BaseRole;
import com.cloud.commonsmng.entity.BaseUserInfo;
import com.cloud.commonsmng.entity.appletEntity.*;
import com.cloud.commonsmng.entity.appletEntity.Dictionary;
import com.cloud.commonsmng.factory.BaseService;
import com.cloud.servicewechat.common.ConstantUtil;


import com.cloud.servicewechat.common.PageUtil;
import com.cloud.servicewechat.common.PageVo;
import com.cloud.servicewechat.mapper.AnswerMapper;
import com.cloud.servicewechat.mapper.UserMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

@Service("Answer")
public class AnswerService extends BaseService {

    @Autowired
    private AnswerMapper answerMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Map<String, Object> exec(String method, String param) {
        Map<String, Object> obj = new HashMap<>();
        switch (method) {
            case ConstantUtil.GET_ANSWER_LIST:
                obj = getAnswerList(param);
                break;
            case ConstantUtil.GET_ANSWER_DETAIL:
                obj = getAnswerDetail(param);
                break;
            case ConstantUtil.GET_QUESTIONNAIRE_LIST: //getQuestionnaireList  问卷管理列表
                obj = getQuestionnaireList(param);
                break;
            case ConstantUtil.GET_ANSWER_MEMBER: //getAnswerMember 获取问卷填写公司信息
                obj = getAnswerMember(param);
                break;
            case ConstantUtil.GET_ANSWER_MEMBER_DETAIL: //getAnswerMember 获取问卷填写公司信息
                obj = getQuestionDetail(param);
                break;
            default:
                break;
        }
        return obj;
    }


    //查询我的问卷列表
    private Map<String, Object> getAnswerList(String param) {
        Map<String, Object> result = new HashMap<>();
        PageVo pageVo = JSONObject.parseObject(param, PageVo.class);//把前端传来的参数分装成分页类
        PageUtil pageUtil = pageVo.getPage();
        pageVo.setUserId(this.getUserInfo().getId());
        int totalNum = answerMapper.countAnswerList(pageVo);
        if (totalNum > 0) {
            List<Dictionary> answer = answerMapper.getAnswerList(pageVo);
            PageVo<Dictionary> pageVo1 = new PageVo<>();
            pageVo1.setPage(new PageUtil(pageUtil.getPageIndex(), pageUtil.getPageSize(), totalNum));
            pageVo1.setDataList(answer);
            Constants.getSuccMsg(result, pageVo1);
        }
        return result;
    }

    //我的问卷--详情信息
    private Map<String, Object> getAnswerDetail(String param) {
        Map<String, Object> result = new HashMap<>();
        JSONObject json = JSONObject.parseObject(param);
        String userId = super.getUserInfo() != null ? super.getUserInfo().getId() : null;
        String kindId = json.getString("kindId");
        String createTime = json.getString("createTime");
        if (StringUtils.isNullOrEmpty(userId)) {
            result.put("code", "500");
            result.put("msg", "未登录");
            return result;
        }
        List<Answer> question = answerMapper.getAnswerDetail(kindId, userId,createTime);
        Constants.getSuccMsg(result, question);
        return result;
    }

    /**
     * 问卷管理列表
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

        //查询问卷
        List<QuestionnaireEntity> questionnaireEntityList =answerMapper.selectQuestionnaireList(map);
        for (int i = 0; i < questionnaireEntityList.size() ; i++) {
            //获取问卷信息
            QuestionnaireEntity q=answerMapper.getQuestionMsg(questionnaireEntityList.get(i).getQuestionBelong());
            questionnaireEntityList.set(i,q);
            //根据问卷名称查询问卷下的所有问题
            List<QuestionEntity> questionList=answerMapper.selectQuestionList(questionnaireEntityList.get(i).getQuestionBelong());
            for (int j = 0; j <questionList.size() ; j++) {
                //根据问题id查询问题选项
                List<QuestionOptionsEntity> myQuestionList=answerMapper.selectMyQuestionList(questionList.get(j).getId());
                questionList.get(j).setMyquestion(myQuestionList);
            }
            questionnaireEntityList.get(i).setQuestion(questionList);
        }

        Map<String,Object> map1=new HashMap<>();
        map1.put("total", questionnaireEntityList.size());
        questionnaireEntityList = questionnaireEntityList.stream().skip((index-1)*size).limit(size).collect(Collectors.toList());
        map1.put("questionnaireList", questionnaireEntityList);
        Constants.getSuccMsg(result,map1);
        return result;
    }


    /**
     * 获取问卷填写公司信息
     * @param param
     * @return
     */
    public Map<String,Object> getAnswerMember(String param){
        Map<String,Object> result=new HashMap<>();
        PageVo pagevo = JSONObject.parseObject(param,PageVo.class);
        PageUtil pageUtil = pagevo.getPage();//获取前端的页面分页信息
        String uRole = "";
        BaseUserInfo user = super.getUserInfo();
        User getUser = userMapper.getUser(user.getId());

        if(org.apache.commons.lang3.StringUtils.equals("005",getUser.getDepartmentId())){
            uRole ="pf";
        }else if(org.apache.commons.lang3.StringUtils.equals(userMapper.getRole(user.getId(),"xcxwj"),"xcxwj")){
            uRole ="pf";
        }else if(org.apache.commons.lang3.StringUtils.equals("006",getUser.getDepartmentId())){
            uRole ="pg";
        } else{
            Constants.getSuccMsg(result,null);
            return result;
        }

        //获取用户角色
        User u1 =  userMapper.getUser(user.getId());

        if(org.apache.commons.lang3.StringUtils.equals("2",u1.getURole())){
            uRole ="pf";
        }

        Object searchdata = pagevo.getSearchdata();
        Map<String, Object> searchMap = JSONObject.parseObject(JSONObject.toJSONString(searchdata), HashMap.class);
        pagevo.setSearchdata(searchMap);

        int totalNum = answerMapper.countQesMemberList(pagevo);
        List<User> products = answerMapper.getQesMemberList(pagevo);
        products.forEach((User u) -> {
            Member m =  answerMapper.getMemberByUserId(u.getId());
            u.setMember(m);
        });
        PageVo<User> productPageVo = new PageVo<>();
        productPageVo.setPage(new PageUtil(pageUtil.getPageIndex(), pageUtil.getPageSize(), totalNum));
        productPageVo.setDataList(products);
        Constants.getSuccMsg(result,productPageVo);
        return result;
    }

    /**
     * 获取问卷填写公司信息
     * @param param
     * @return
     */
    public Map<String,Object> getQuestionDetail(String param){
        Map<String, Object> result = new HashMap<>();
        JSONObject json = JSONObject.parseObject(param);
        String userId = super.getUserInfo() != null ? super.getUserInfo().getId() : null;
        String questionBelong = json.getString("questionBelong");
        List<QuestionEntity>  lq= answerMapper.getUserAnswerQuestion(userId,questionBelong);
        lq.forEach((QuestionEntity q ) ->{
            String answer = q.getAnswer();
            List<String> answers = new ArrayList<>();
            if(answer.contains(",")){
                answers = Arrays.asList(answer.split(","));
            }else{
                answers.add(answer);
            }
            List<QuestionOptionsEntity>  lqe = answerMapper.getUserAllAnswers(q.getId(),answers);
            q.setMyquestion(lqe);
        });
        Constants.getSuccMsg(result,lq);
        return result;
    }

}
