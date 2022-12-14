package com.cloud.servicemanage.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.entity.appletEntity.PushDetail;
import com.cloud.commonsmng.entity.appletEntity.User;
import com.cloud.commonsmng.factory.BaseService;
import com.cloud.servicemanage.common.ConstantUtil;
import com.cloud.servicemanage.common.PageUtil;
import com.cloud.servicemanage.common.PageVo;
import com.cloud.servicemanage.entity.PushEntity;
import com.cloud.servicemanage.entity.weixin.WeChatTemplateMsg;
import com.cloud.servicemanage.mapper.PushMapper;
import com.cloud.servicemanage.service.weixin.TemplateService;
import com.cloud.servicemanage.utils.sendWechat;
import com.cloud.servicemanage.utils.wechatpushUtil;
import com.cloud.servicemanage.utils.weixin.WeixinUtil;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;


@Service("push")
public class PushService extends BaseService {

  @Autowired
  private WxMpService wxMpService;


  @Value("${wx.mp.configs.appId}")
  private String appId;
  @Value("${wx.mp.configs.secret}")
  private String secret;
  @Value("$imageUrl")
  private String imageUrl;

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  public static String SEND_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
  public static String customUrl="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";
  public static String getALLUsersURL="https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN";

  @Autowired
  RedisTemplate redisTemplate;

  @Autowired
  TemplateService templateService;

  @Autowired
  private PushMapper pushMapper;

  @Autowired
  private sendWechat sendwechat;


  @Override
  public Map<String, Object> exec(String method, String param) {
    Map<String, Object> obj = new HashMap<>();
    switch (method) {
      case ConstantUtil.PUSH_MASSAGE: //
        obj = testPush(param);
        break;
      case "searchPushList":
        obj=searchPushList(param);
        break;
      case "addPush":
        obj=addPush(param);
        break;
      case "updatePush":
        obj=updatePush(param);
        break;
      case ConstantUtil.DELETE_PUSH:
        obj=deletePushbyId(param);
        break;
      case ConstantUtil.WECHATUSERS:
        obj=getweixinGongzhlist(param);
        break;
      case ConstantUtil.GETPUSHDETAIL:
        obj=getPushDetailById(param);
        break;
      default:
        break;
    }
    return obj;
  }
  public Map<String,Object> searchPushList(String param){
    Map<String,Object> result=new HashMap<>();
    try {
      PageVo pagevo = JSONObject.parseObject(param,PageVo.class);
      PageUtil page = pagevo.getPage();//?????????????????????????????????
      int pageTotal = pushMapper.getPushCount(pagevo);
      List<PushEntity> list = pushMapper.getPushList(pagevo);
      PageVo<PushEntity> pageVo = new PageVo<>();
      pageVo.setPage(new PageUtil(page.getPageIndex(), page.getPageSize(), pageTotal));
      pageVo.setDataList(list);
      Constants.getSuccMsg(result, pageVo);
    }catch (Exception e) {
      e.printStackTrace();
      Constants.getErrMsg(result, Constants.RESULT_NO_DATA);
    }

    return result;

  }

  private Map<String,Object> addPush(String param){
    Map<String, Object> result = new HashMap<>();
    try {
      PushEntity push = JSONObject.parseObject(param,PushEntity.class);
      //0?????????
      push.setStatus(0);
      super.insertBaseInfo(push);
      if(push.getOpenIds()!=null && push.getOpenIds().size() > 0){
          if(push.getSendType().equals("6")){
              setPushDetailList(push);
          }

      }
      int count = pushMapper.addPush(push);
      Constants.getSuccMsg(result, count>0);
    }catch (Exception e) {
      e.printStackTrace();
      Constants.getErrMsg(result, Constants.RESULT_NO_DATA);
    }
    return result;
  }

  //????????????????????????
  private void setPushDetailList(PushEntity push) {
    List<PushDetail> pushDetailslist = new ArrayList<>();
    if (null != push.getOpenIds() && push.getOpenIds().size() > 0) {
      for (String openId : push.getOpenIds()) {
        PushDetail pushDetail = new PushDetail();
        super.insertBaseInfo(pushDetail);//????????????????????????????????????
        pushDetail.setPushId(push.getId());
        pushDetail.setStatus("1");
        pushDetail.setOpenId(openId);
        pushDetailslist.add(pushDetail);
      }
    }
    push.setPushDetailList(pushDetailslist);
  }

  private Map<String,Object> updatePush(String param){
    Map<String, Object> result = new HashMap<>();
    try {
      PushEntity push = JSONObject.parseObject(param,PushEntity.class);
      //??????????????????
      PushEntity pushEntity1 = pushMapper.getPushById(push.getPushMessageId());
      //????????????????????????????????????
      if(pushEntity1.getStatus()==1){
        Constants.getErrMsg(result, "???????????????????????????????????????");
        return result;
      }
      if(push.getSendType().equals("6")){
        push.setId(push.getPushMessageId());
        setPushDetailList(push);
        //??????pushdetail???
        pushMapper.updatePushDetail(push.getPushMessageId(),push);
      }
      super.updateBaseInfo(push, push.getPushMessageId());
      int count = pushMapper.modifyPush(push);
      Constants.getSuccMsg(result, count>0);
    }catch (Exception e) {
      e.printStackTrace();
      Constants.getErrMsg(result, Constants.RESULT_NO_DATA);
    }

    return result;
  }


  public Map<String, Object> testPush(String param){
    PushEntity pushEntity=JSONObject.parseObject(param,PushEntity.class);

    Map<String, Object> result = new HashMap<>();

    //??????????????????
    PushEntity pushEntity1 = pushMapper.getPushById(pushEntity.getPushMessageId());
    //????????????????????????????????????
    if(pushEntity1.getStatus()==1){
      Constants.getErrMsg(result, "???????????????????????????????????????");
      return result;
    }

   // String accessToken = (String) redisTemplate.opsForValue().get("access_token");
    //String url = SEND_URL.replace("ACCESS_TOKEN", accessToken);

    //??????????????????
    //JSONObject data = new JSONObject();
    //String templateId="Slxjlr_FPAdcwrIFuR4KIUoA53KggalBxkS2ICbQUik";
   // String templateId = pushEntity.getTemplateType();
    //??????????????????????????????openId

    if(pushEntity1.getSendType()!=null && !pushEntity1.getSendType().equals("")
            && !pushEntity1.getSendType().equals("6")){
      List<String> userList= GetOpenIdList(pushEntity1.getSendType());
      pushEntity1.setUserIds(userList);
    }else {
      //??????pushdetial
      List<String> openIds = pushMapper.getpushDatailopenIds(pushEntity.getPushMessageId());
      pushEntity1.setOpenIds(openIds);
    }
    if((pushEntity1.getUserIds()==null || pushEntity1.getUserIds().size()<=0) &&
            (pushEntity1.getOpenIds()==null ||pushEntity1.getOpenIds().size()<=0)){
      Constants.getSuccMsg(result,"??????????????????");
      return result;
    }
    int re = sendwechat.sendweixin2(pushEntity1);
    //String unionid = wechatpushUtil.getUnionId("ooBG35mkxHtkUI2p8SangD_gSSlc",accessToken);
   // sendMsg(pushEntity, templateId,userList);
    /*result.put("code",200);
    result.put("msg","succ");*/
    int count = 0;
    if( re > 0 ){
      super.updateBaseInfo(pushEntity, pushEntity.getPushMessageId());//????????????????????????????????????
      pushEntity.setStatus(1);
      count = pushMapper.modifyPush(pushEntity);
    }
    Constants.getSuccMsg(result,count>0);
    return result;
  }


  /**
   * 2?????????????????????
   * openId     ??????Id
   * templateId ??????Id
   * data       ????????????
   * @param
   */
  private void sendMsg(PushEntity pushEntity, String templateId,List<String> openIds){
    WxMpInMemoryConfigStorage wxStorage = new WxMpInMemoryConfigStorage();
    wxStorage.setAppId(appId);
    wxStorage.setSecret(secret);
    WxMpService wxMpService = new WxMpServiceImpl();
    wxMpService.setWxMpConfigStorage(wxStorage);

    List<WxMpTemplateData> data = Arrays.asList(
            new WxMpTemplateData("first", pushEntity.getTitle()),
            new WxMpTemplateData("Content1", pushEntity.getKeywords1()),
            new WxMpTemplateData("Good", pushEntity.getKeywords2()),
            new WxMpTemplateData("expDate", pushEntity.getKeywords3()),
            new WxMpTemplateData("name", pushEntity.getKeywords4()),
            new WxMpTemplateData("menu", pushEntity.getKeywords5()),
            new WxMpTemplateData("remark", pushEntity.getRemark())
    );

    //2,????????????
   /* WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
            .toUser("ooBG35m7H8nNJLe9Ouur9jnj5vfg")//??????????????????openid
            .templateId(templateId)//??????id
            .url(pushEntity.getUrl())//????????????????????????????????????
            .data(data)
            .build();*/
   // TreeMap<String, String> miniprograms = new TreeMap<String, String>();
    WxMpTemplateMessage.MiniProgram miniprograms = new WxMpTemplateMessage.MiniProgram();
    miniprograms.setAppid(pushEntity.getAppId());
    //miniprograms.setAppid("wx46b5365b0ba7022e");
    miniprograms.setPath(pushEntity.getUrl());// ??????????????????????????????????????????
    //----------------------------------------------------------------------
   // openIds.clear();
   // openIds.set(0,"ooBG35mkxHtkUI2p8SangD_gSSlc");

   if(openIds!=null && openIds.size()>0){
     for(int i=0;i<openIds.size();i++){
       //System.out.println("---------------------------------------"+openIds.get(i));

       WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
               .toUser(openIds.get(i))//??????????????????openid
               .templateId(templateId)//??????id
               //.url(pushEntity.getUrl())//????????????????????????????????????
               .miniProgram(miniprograms)
               .data(data)
               .build();
       try {
         String msg = wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
         System.out.println("???????????????" + msg);
       } catch (Exception e) {
         System.out.println("???????????????" + e.getMessage());
         e.printStackTrace();
       }
     }
   }

  }


  //??????????????????
  public List<String> GetOpenIdList(String sendtype)
  {
    //??????????????????
    if(sendtype.equals("0")){
        sendtype = "";
    }
    List<String> userIds = pushMapper.getUserIds(sendtype);
    return userIds;

  }

  /**
   * ?????????????????????????????????
   */
  private Map<String,Object> deletePushbyId(String param){
    Map<String, Object> result = new HashMap<>();
    String pushMessageId = JSONObject.parseObject(param).getString("pushMessageId");
    PushEntity pushEntity = new PushEntity();
    pushEntity.setPushMessageId(pushMessageId);
    super.updateBaseInfo(pushEntity, pushMessageId);//????????????????????????????????????
   pushEntity.setDelFlag(Integer.valueOf(0));//0???????????????
   //????????????????????????????????????????????????????????????
    PushEntity pushEntity1 = pushMapper.getPushById(pushMessageId);
    if(pushEntity1.getStatus()==1){
      Constants.getErrMsg(result, "??????????????????????????????????????????" );
      return result;
    }else{
      int update = pushMapper.modifyPush(pushEntity);
      Constants.getSuccMsg(result, update > 0);
      return result;
    }
  }

  /**
   * ???????????????????????????
   * realname,openId,nickName,
   * @param
   * @return
   */
  private Map<String ,Object> getweixinGongzhlist(String param){
    Map<String, Object> result = new HashMap<>();
    sendwechat.updateWechatusers();
    try {
      PageVo pagevo = JSONObject.parseObject(param,PageVo.class);
      PageUtil page = pagevo.getPage();//?????????????????????????????????
      int pageTotal = pushMapper.getUserInfoCount(pagevo);
      List<User> list = pushMapper.getUserInfoList(pagevo);
      PageVo<User> pageVo = new PageVo<>();
      pageVo.setPage(new PageUtil(page.getPageIndex(), page.getPageSize(), pageTotal));
      pageVo.setDataList(list);
      Constants.getSuccMsg(result, pageVo);
    }catch (Exception e) {
      e.printStackTrace();
      //Constants.getErrMsg(result, Constants.RESULT_NO_DATA);
    }
    return result;

  }

  private Map<String, Object> getPushDetailById(String param){
    Map<String ,Object> result = new HashMap<>();
    String pushMessageId = JSONObject.parseObject(param).getString("pushMessageId");
    List<PushDetail> details = pushMapper.getPushDetail(pushMessageId);
    Constants.getSuccMsg(result,details);
    return result;
  }
}
