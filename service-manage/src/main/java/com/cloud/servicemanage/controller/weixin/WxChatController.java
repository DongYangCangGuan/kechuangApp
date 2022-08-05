package com.cloud.servicemanage.controller.weixin;

import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.entity.appletEntity.NewsEntity;
import com.cloud.servicemanage.entity.weixin.ArticleRespModel;
import com.cloud.servicemanage.entity.weixin.NewsMessageRespModel;
import com.cloud.servicemanage.entity.weixin.TemplateMessageModel;
import com.cloud.servicemanage.entity.weixin.WeChatTemplateMsg;
import com.cloud.servicemanage.service.weixin.TemplateService;
import com.cloud.servicemanage.utils.weixin.MessageUtil;
import com.cloud.servicemanage.utils.weixin.WeixinUtil;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Controller
@Slf4j
@RequestMapping("/manage/api")
public class WxChatController {

  @Autowired
  private WxMpService wxMpService;


  @Value("$wx.mp.configs.appId")
  private String appId;
  @Value("$wx.mp.configs.secret")
  private String secret;
  @Value("$imageUrl")
  private String imageUrl;

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  public static String SEND_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
  public static String customUrl="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";

  @Autowired
  RedisTemplate redisTemplate;

  @Autowired
  TemplateService templateService;

  @RequestMapping(value = {"/testPush"}, method = {RequestMethod.POST})
  @ResponseBody
  public void testPush(String param){

    NewsEntity news = JSONObject.parseObject(param,NewsEntity.class);

    String accessToken = (String) redisTemplate.opsForValue().get("access_token");
    String url = customUrl.replace("ACCESS_TOKEN", accessToken);
    String openid="ooBG35m7H8nNJLe9Ouur9jnj5vfg";
    String title=news.getTitle();
    String description =news.getDescribe();
    String pathUrl=news.getNewsLink();

    String respMessage="{\n" +
      "    \"touser\":\""+openid+"\",\n" +
      "    \"msgtype\":\"news\",\n" +
      "    \"news\":{\n" +
      "        \"articles\": [\n" +
      "         {\n" +
      "             \"title\":\""+title+"\",\n" +
      "             \"description\":\""+description+"\",\n" +
      "             \"url\":\""+pathUrl+"\",\n" +
      "             \"picurl\":\""+imageUrl+"\"\n" +
      "         }\n" +
      "         ]\n" +
      "    }\n" +
      "}";


     // 模板Id
    JSONObject jsonObject = WeixinUtil.httpRequest(url, "POST", respMessage);
    System.out.println(jsonObject);

  }

  /**
   * 1、发送模版消息-拼接数据（测试信息）
   * @param openId 微信用户的openId
   */
  public String sendMessage(String openId, String templateId,String machineAlias,String shopName,String openStatus, String dropStatus,int number) {
    // 模板参数
    Map<String, WeChatTemplateMsg> sendMag = new HashMap<String, WeChatTemplateMsg>();
    String content = "店铺："+shopName+",机器别名："+machineAlias+",当前库存："+number;
    sendMag.put("first", new WeChatTemplateMsg("请注意!"));
    sendMag.put("system", new WeChatTemplateMsg(content));
    sendMag.put("time", new WeChatTemplateMsg(String.valueOf( System.currentTimeMillis() / 1000)));
    sendMag.put("account", new WeChatTemplateMsg("1"));
    sendMag.put("remark", new WeChatTemplateMsg("请尽快补货，谢谢！"));
    // 发送
    String send = send(openId, templateId, sendMag);
    return send;

  }


  /**
   * 2、发送模版消息
   * openId     用户Id
   * templateId 模板Id
   * data       模板参数
   * @param data
   */
  private String send(String openId, String templateId, Map<String, WeChatTemplateMsg> data) {
    RestTemplate restTemplate = new RestTemplate();
    String accessToken = (String) redisTemplate.opsForValue().get("access_token");
    String url = SEND_URL.replace("ACCESS_TOKEN", accessToken);
    //拼接base参数
    Map<String, Object> sendBody = new HashMap<>();
    sendBody.put("touser", openId);               // openId
//        sendBody.put("url", "www.baidu.com");         // 点击模板信息跳转地址
    sendBody.put("topcolor", "#FF0000");          // 顶色
    sendBody.put("data", data);                   // 模板参数
    sendBody.put("template_id", templateId);      // 模板Id
    ResponseEntity<String> forEntity = restTemplate.postForEntity(url, sendBody, String.class);
    return forEntity.getBody();
  }
//  @GetMapping("/authorize")
//  public String authorize(@RequestParam("returnUrl") String returnUrl) {
//    String url = "http://abc123.ticp.io/wechat/userInfo";   //可以看到,这里的url即为上面申请并授权的域名
//    String redirectURL = wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAuth2Scope.SNSAPI_USERINFO, URLEncoder.encode(returnUrl));
//    log.info("微信网页授权:获取code,redirectURL={}", redirectURL);
//    return "redirect:" + redirectURL;
//  }
//
//  @GetMapping("/userInfo")
//  public String userInfo(@RequestParam("code") String code, @RequestParam("state") String returnUrl) throws Exception {
//    log.info("微信网页授权: code={}", code);
//    log.info("微信网页授权: state={}",returnUrl);	//即authorize中的returnUrl
//    WxMpOAuth2AccessToken wxMpOAuth2AccessToken;
//    try {
//      wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
//    }catch (WxErrorException e) {
//      log.info("微信网页授权: {}", e);
//      throw new Exception(e.getError().getErrorMsg());
//    }
//    String openId = wxMpOAuth2AccessToken.getOpenId();
//    log.info("微信网页授权: openId={}", openId);
//    return "redirect:" + returnUrl;
//  }
//  @GetMapping("/index")
//  public String index(){
//   return "index";
//  }
////
//  @ResponseBody
//  @GetMapping("/getWxlogin")
//  public String wxLogin(){
//    String url="https://open.weixin.qq.com/connect/oauth2/authorize?" +
//      "appid="+appId +
//      "&redirect_uri=REDIRECT_URI" +
//      "&response_type=code" +
//      "&scope=snsapi_userinfo" +
//      "&state=STATE#wechat_redirect";
//    return "redirect:" + url;
//  }
//  @ResponseBody
//  @GetMapping("/callBack")
//  public String callBck(String code) throws IOException {
//    String url="https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appId +
//      "&secret="+secret +
//      "&code="+code +
//      "&grant_type=authorization_code";
//
//    JSONObject json = HttpClientUtils.doGet(url);
//    String openId="";
//    String accessToken="";
//    if (json != null) {
//      openId = json.getString("openid");//得到用户微信号
//      accessToken =json.getString("access_token");
//    }
//    return "Sucess";
//  }


  @GetMapping("/push")
  public void push() {
    //1，配置
    WxMpInMemoryConfigStorage wxStorage = new WxMpInMemoryConfigStorage();
    wxStorage.setAppId("wx46b5365b0ba7022e");
    wxStorage.setSecret("229311d5d3e9fddb87cf46368228f9dd");
    WxMpService wxMpService = new WxMpServiceImpl();
    wxMpService.setWxMpConfigStorage(wxStorage);

    List<WxMpTemplateData> data = Arrays.asList(
            new WxMpTemplateData("first", "亲，请记得收货。"),
            new WxMpTemplateData("Content1", "微信点餐"),
            new WxMpTemplateData("Good", "18868812345"),
            new WxMpTemplateData("expDate", "132ss"),
            new WxMpTemplateData("name", "aaa"),
            new WxMpTemplateData("menu", "￥" +"300"),
            new WxMpTemplateData("remark", "欢迎再次光临！")
    );

    //2,推送消息
    WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
      .toUser("ooBG35m7H8nNJLe9Ouur9jnj5vfg")//要推送的用户openid
      .templateId("Slxjlr_FPAdcwrIFuR4KIUoA53KggalBxkS2ICbQUik")//模版id
      .url("https://30paotui.com/")//点击模版消息要访问的网址
       .data(data)
      .build();
    //3,如果是正式版发送模版消息，这里需要配置你的信息
    //        templateMessage.addData(new WxMpTemplateData("name", "value", "#FF00FF"));
    //                templateMessage.addData(new WxMpTemplateData(name2, value2, color2));
    //发起推送
    try {
      String msg = wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
      System.out.println("推送成功：" + msg);
    } catch (Exception e) {
      System.out.println("推送失败：" + e.getMessage());
      e.printStackTrace();
    }

  }


//      public void SendWeChatMsg(String token) {
//        // 接口地址
//        String sendMsgApi = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+token;
//        //openId
//        String toUser = "XXXXXXXXXXXXXX";
//        //消息模板ID
//        String template_id = "XXXXXXXXXXXXXXXXXXXXXX";
//        //整体参数map
//        Map<String, Object> paramMap = new HashMap<String, Object>();
//        //消息主题显示相关map
//        Map<String, Object> dataMap = new HashMap<String, Object>();
//        //根据自己的模板定义内容和颜色
//        dataMap.put("first","详细内容XXXXXXX","#173177");
//        dataMap.put("keyword1",new DataEntity("私有化部署XXX","#173177"));
//        dataMap.put("keyword2",new DataEntity("2020-08-18XXX" ,"#173177"));
//        dataMap.put("remark",new DataEntity("申请成功XXX","#173177"));
//        paramMap.put("touser", toUser);
//        paramMap.put("template_id", template_id);
//        paramMap.put("data", dataMap);
//        paramMap.put("url",toUrl);
//        //需要实现跳转网页的，可以添加下面一行代码实现跳转
//        // paramMap.put("url","http://xxxxxx.html");
//        System.out.println(doGetPost(sendMsgApi,"POST",paramMap));
//      }

  }
