package com.cloud.servicewechat.service;


import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.entity.BaseUserInfo;
import com.cloud.commonsmng.entity.appletEntity.*;
import com.cloud.commonsmng.factory.BaseService;
import com.cloud.servicewechat.common.ConstantUtil;
import com.cloud.servicewechat.mapper.AnswerMapper;
import com.cloud.servicewechat.mapper.ProductMapper;
import com.cloud.servicewechat.mapper.UserMapper;
import com.cloud.servicewechat.utils.SendWechatThread;
import com.cloud.servicewechat.utils.sendWechat;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service("product")
public class ProductService extends BaseService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private AnswerMapper answerMapper;

    @Autowired
    private UserMapper userMapper ;

    @Autowired
    private sendWechat sendwechat;

    //实例化日志对象，引用jar包org.slf4j.Logger
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Override
    public Map<String, Object> exec(String method, String param) {
        Map<String, Object> obj = new HashMap<>();
        switch (method) {
            case ConstantUtil.GET_ABOUTUS:
                obj = getAboutus(param);
                break;
            case ConstantUtil.GET_QUESTIONNAIRE:
                obj = getQuestionnaire(param);
                break;
            case ConstantUtil.ANSWERS_QUESTIONNAIRE:
                obj = answersQuestionnaire(param);
                break;
            case ConstantUtil.GET_ANSWERS_MSG:
                obj = getAnswersMsg(param);
                break;
            case ConstantUtil.GET_RECOMMEND:
                obj = getRecommend(param);
                break;
            case ConstantUtil.GET_LATEST:
                obj = getLatest(param);
                break;
            case ConstantUtil.GET_HOT:
                obj = getHot(param);
                break;
            case ConstantUtil.GET_CONTACT_US:
                obj = getContactUs(param);
                break;
            case ConstantUtil.ADD_CONSULTING_INFO:
                obj = addConsultingInfo(param);
                break;
            default:
                break;
        }
        return obj;
    }

    //获取问卷信息
    private Map<String, Object> getAboutus(String param) {
        Map<String, Object> result = new HashMap<>();
        Aboutus au = productMapper.getAboutus();
        Constants.getSuccMsg(result,au);
        logger.info(String.format("获取问卷信息:", result));
        return result;
    }

    //获取问卷信息
    private Map<String, Object> getQuestionnaire(String param) {
        Map<String, Object> result = new HashMap<>();
        JSONObject json = JSONObject.parseObject(param);
        String type = json.getString("type");
        List<QuestionEntity> list = productMapper.getQuestionnaire(type);
        if(list != null){
            for(QuestionEntity qe : list){
                List<QuestionOptionsEntity> opList = productMapper.getOpinion(qe.getId());
                qe.setQuestionOptionsList(opList);
            }
        }
        Constants.getSuccMsg(result,list);
        logger.info(String.format("获取问卷信息:", result));
        return result;
    }

    //问卷回答
    private Map<String, Object> answersQuestionnaire(String param) {
        Map<String, Object> result = new HashMap<>();
        List<AnswerEntity> ae = JSONObject.parseArray(param, AnswerEntity.class);

        if(null==ae || ae.size()<=0){
            Constants.getSuccMsg(result,null);
            logger.info("问卷回答完成");
            return result;
        }

        String  questionBelong = productMapper.getQuestionBelong(ae.get(0).getQuestionId());
        productMapper.deleteAnswers(ae.get(0).getUserId(),questionBelong);
//        String ordinal =  productMapper.getUserordinal(ae.get(0).getUserId());
        ae.forEach((AnswerEntity a) -> {
            a.setQuestionBelong(questionBelong);
            a.setAnswer(a.getAnswer().replace("[","").replace("]","").replace("\"",""));
//            int nowOrdinal = 0;
//            if(ordinal !=null & ordinal !=""){
//                nowOrdinal = Integer.parseInt(ordinal)+1;
//            }
//            a.setOrdinal(nowOrdinal);
            productMapper.insertAnswers(a);
        });

        productMapper.setTime(ae.get(0).getUserId(),questionBelong);
        //进行推送设置
        productMapper.setAnswerPush(ae.get(0).getUserId(),questionBelong);
        //进行推送
        questionPush(questionBelong,ae.get(0).getUserId());

        Constants.getSuccMsg(result,null);
        logger.info("问卷回答完成");
        return result;
    }

    //根据用户id推荐产品
    private Map<String, Object> getRecommend(String param) {
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject json = JSONObject.parseObject(param);
        String userId = json.getString("userId");
        String type = json.getString("type");
        List<AnswerEntity> la =  productMapper.getAnswers(userId);
        if(null==la || la.size()<=0 ||StringUtils.isBlank(type) || !type.startsWith("21")){
            List<Product> pe = productMapper.getProductByProSeq(null,type,null,"recommned");
            Constants.getSuccMsg(result,pe);
            logger.info(String.format("获取推荐完成:", result));
            return result;
        }
        String a1 = la.get(0).getAnswer();
        String a2 = la.get(1).getAnswer();
        String a4 = la.get(3).getAnswer();
        List<String> pf  = new ArrayList();
        List<String>  pfgg  = new ArrayList();
        switch (a1){
            case "A":
                switch (a2){
                    case "A":
                        switch (a4){
                            case "A":
                                pf.add("10");
                                break;
                            case "B":
                                pf.add("1");
                                pf.add("5");
                                break;
                            case "C":
                            case "D":
                                pf.add("2");
                                break;
                            case "E":
                                pf.add("3");
                                break;
                            default:
                                break;
                        }
                        break;
                    case "B":
                        break;
                    default:
                        break;
                }
                pfgg.add("4");
                pfgg.add("6");
                break;
            case "B":
                pf.add("10");
                pfgg.add("11");
                break;
            case "C":
                pf.add("7");
                pf.add("8");
                pfgg.add("9");
                break;
            default:
                break;
        }
        pf.addAll(pfgg);
        List<Product> tjList  = new ArrayList();
        for(String seq : pf){
            List<Product> pe = productMapper.getProductByProSeq(seq,type,null,null);
            if(null!=pe && pe.size()>0){
                tjList.add(pe.get(0));
            }
        }
        if(tjList==null || tjList.size()<=0){
            tjList = productMapper.getProductByProSeq(null,type,null,"recommned");
        }
        tjList.forEach((Product p) -> {

            List<ProductsExpand> peList = productMapper.getProductExpandList(p.getId());
            p.setProductsExpandList(peList);
            int count = productMapper.checkProduct(p.getId(),userId);
            if(count>0){
                p.setIsCollect("1");
            }
            int times =  productMapper.getProductReadTimes(p.getId());
            p.setTimes(times+"");
        });
        Constants.getSuccMsg(result,tjList);
        logger.info(String.format("获取推荐完成:", result));
        return result;
    }

    //根据用户最新
    private Map<String, Object> getLatest(String param) {
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject json = JSONObject.parseObject(param);
        String type = json.getString("type");
        String id = json.getString("id");
        String userId = json.getString("userId");
        String sort = json.getString("sort");
        List<Product> pe = productMapper.getProductByProSeq(null,type,id,sort);
        if(null!= pe && pe.size()>0){
            pe.forEach((Product p) -> {

                List<ProductsExpand> peList = productMapper.getProductExpandList(p.getId());
                p.setProductsExpandList(peList);
                int count = productMapper.checkProduct(p.getId(),userId);
                if(count>0){
                    p.setIsCollect("1");
                }
                int times =  productMapper.getProductReadTimes(p.getId());
                p.setTimes(times+"");
            });
        }
        Constants.getSuccMsg(result,pe);
        logger.info(String.format("获取推荐完成:", result));
        return result;
    }

    //根据用户最热
    private Map<String, Object> getHot(String param) {
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject json = JSONObject.parseObject(param);
        String type = json.getString("type");
        BaseUserInfo user = this.getUserInfo();
        List<Product> pe = productMapper.getHotProduct(type);
        if(null==pe || pe.size()<=0){
            pe = productMapper.getProductByProSeq(null,type,null,null);
        }
        pe.forEach((Product p) -> {
            List<ProductsExpand> peList = productMapper.getProductExpandList(p.getId());
            p.setProductsExpandList(peList);
            int count = productMapper.checkProduct(p.getId(),user.getId());
            if(count>0){
                p.setIsCollect("1");
            }
            int times =  productMapper.getProductReadTimes(p.getId());
            p.setTimes(times+"");
        });
        Constants.getSuccMsg(result,pe);
        logger.info(String.format("获取推荐完成:", result));
        return result;
    }

    //获取用户回答
    private Map<String, Object> getAnswersMsg(String param) {
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject json = JSONObject.parseObject(param);
        String userId = json.getString("userId");
        List<AnswerEntity> la =  productMapper.getAnswers(userId);

        List<String>  asList= new ArrayList<>();
        la.forEach((AnswerEntity ae) -> {
            String opinion =  ae.getAnswer();
            String questionId = ae.getQuestionId();
            if(opinion.contains(",")){
                String[] opinions = opinion.split(",");
                for(int i = 0; i < opinions.length; i++){
                   String msg=  productMapper.getAnswersMsg(questionId,opinions[i]);
                    asList.add(msg);
                }
            }else{
                String msg=  productMapper.getAnswersMsg(questionId,opinion);
                asList.add(msg);
            }

        });
        Constants.getSuccMsg(result,asList);
        logger.info(String.format("获取推荐完成:", result));
        return result;
    }

    //根据用户最热
    private Map<String, Object> getContactUs(String param) {
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject json = JSONObject.parseObject(param);
        String type = json.getString("id");
        Product  user = productMapper.getContactUs(type);
        Constants.getSuccMsg(result,user);
        logger.info(String.format("获取产品联系方式:", result));
        return result;
    }

    //根据用户最热
    private Map<String, Object> questionPush(String questionBelong,String userId) {
        List<String> userList= userMapper.getUserListByRole("answerPushPF");
        User u =  userMapper.getUser(userId);
        String questionName =  answerMapper.getQuestionName(questionBelong);
        Notes n = new Notes();
        n.setTitle("您好，有用户提交了问卷，请至管理端查看");
        n.setContent(questionName);
        n.setUserName(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
        n.setUserIds(userList);
        n.setRemark(u.getMember().getEnterpriseName()+"  "+u.getRealName()+"("+u.getPhone()+")填写了该问卷");
        n.setTemplateId("udS_q-BZ4PDMsuUGfMr1wRY6ajt--NG3-wxEAGKWSiQ");
//        博远资本 张三(18529918234)填写了该问卷
        Thread t =  new SendWechatThread(sendwechat,n);
        t.start();
        return null;
    }

    private Map<String, Object> addConsultingInfo(String param){
        Map<String, Object> result = new HashMap<>();
        logger.info(String.format("addConsultingInfo:", param));
        ConsultingInfo info = JSONObject.parseObject(param, ConsultingInfo.class);
        if(info != null && info.getUserId() != null && !info.getUserId().equals("")){
            super.insertBaseInfo(info);
            int i = productMapper.addConsultingInfo(info);
            Constants.getSuccMsg(result,i>0);
        }else{
            Constants.getErrMsg(result,"传参为空");
        }
        return result;
    }
}
