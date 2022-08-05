package com.cloud.servicemanage.service.weixin.Impl;

import com.alibaba.fastjson.JSONObject;
import com.cloud.servicemanage.entity.weixin.ArticleRespModel;
import com.cloud.servicemanage.entity.weixin.NewsMessageRespModel;
import com.cloud.servicemanage.entity.weixin.TextMessageRespModel;
import com.cloud.servicemanage.service.weixin.CoreService;
import com.cloud.servicemanage.service.weixin.MenuService;
import com.cloud.servicemanage.utils.weixin.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.cloud.servicemanage.utils.weixin.WeixinUtil.httpRequest;

/**
 * @Author:cat
 * @Description 微信控制服务实现类
 * @Date: 2018-04-12  10:01
 * @Modified By:
 */
@Service
public class CoreServiceImpl implements CoreService {

    private static Logger log = LoggerFactory.getLogger(CoreServiceImpl.class);

    @Autowired
    private MenuService menuService;

    public static String userUrl="https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

    @Value("$wx.mp.configs.appId")
    private String appId;
    @Value("$wx.mp.configs.secret")
    private String secret;


    @Autowired
    RedisTemplate redisTemplate;

    /**
     * @Author:cat
     * @Description 处理微信发来的请求（包括事件的推送）
     * @params:
     * @Date:2018-04-12 14:03
     * @Return:
     * @Modified By:
     */
    @Override
    public String processRequest(HttpServletRequest request) {

        String respMessage = null;
        try {
            // 默认返回的文本消息内容
            String respContent = "请求处理异常，请稍候尝试！";
            // xml请求解析
            Map<String, String> requestMap = MessageUtil.parseXml(request);
            // 发送方帐号（open_id）
            String fromUserName = requestMap.get("FromUserName");
            // 公众帐号
            String toUserName = requestMap.get("ToUserName");
            // 消息类型
            String msgType = requestMap.get("MsgType");
            // 回复文本消息
            TextMessageRespModel textMessage = new TextMessageRespModel();
            textMessage.setToUserName(fromUserName);
            textMessage.setFromUserName(toUserName);
            textMessage.setCreateTime(new Date().getTime());
            textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
            textMessage.setFuncFlag(0);
            textMessage.setContent(requestMap.get("Content"));

            // 多条图文消息
            NewsMessageRespModel newsMessage = new NewsMessageRespModel();
            newsMessage.setToUserName(fromUserName);
            newsMessage.setFromUserName(toUserName);
            newsMessage.setCreateTime(new Date().getTime());
            newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);

            List<ArticleRespModel> articleList = new ArrayList<ArticleRespModel>();

            //点击菜单id
            String EventKey =requestMap.get("EventKey");
            // 接收文本消息内容
            String content = requestMap.get("Content");
            // 自动回复文本消息
            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {


                switch (content) {
                    //1.回复固定消息--向公众号发送“你好”，得到公众号回复“你好”
                    case "你好": {
//                        respMessage ="你好";
//                        textMessage.setContent("你好");
//                        respMessage = MessageUtil.textMessageToXml(textMessage);


                      ArticleRespModel articleRespModel =new ArticleRespModel();
                      articleRespModel.setTitle("猫和老鼠");
                      articleRespModel.setDescription("《猫和老鼠》以闹剧为特色，描绘了一对水火不容的冤家：汤姆和杰瑞猫鼠之间的战争，片中的汤姆经常使用狡诈的诡计来对付杰瑞，而杰瑞则时常利用汤姆诡计中的漏洞逃脱他的迫害并给予报复");
                      articleRespModel.setPicUrl("https://image.baidu.com/search/detail?ct=503316480&z=0&ipn=d&word=jerry&step_word=&hs=2&pn=0&spn=0&di=7077204560107798529&pi=0&rn=1&tn=baiduimagedetail&is=0%2C0&istype=0&ie=utf-8&oe=utf-8&in=&cl=2&lm=-1&st=undefined&cs=1158846336%2C1210540534&os=1373028253%2C3358424749&simid=3422474665%2C249484137&adpicid=0&lpn=0&ln=1859&fr=&fmq=1650198317049_R&fm=&ic=undefined&s=undefined&hd=undefined&latest=undefined&copyright=undefined&se=&sme=&tab=0&width=undefined&height=undefined&face=undefined&ist=&jit=&cg=&bdtype=0&oriquery=&objurl=https%3A%2F%2Fgimg2.baidu.com%2Fimage_search%2Fsrc%3Dhttp%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201510%2F20%2F20151020171602_BVmts.thumb.700_0.jpeg%26refer%3Dhttp%3A%2F%2Fb-ssl.duitang.com%26app%3D2002%26size%3Df9999%2C10000%26q%3Da80%26n%3D0%26g%3D0n%26fmt%3Dauto%3Fsec%3D1652790057%26t%3D908c93cc1fcca8fcb0075a628bbcc791&fromurl=ippr_z2C%24qAzdH3FAzdH3Fooo-kjpw8_z%26e3B17tpwg2_z%26e3Bv54AzdH3Fks52AzdH3F%3Ft1%3D00dbn0cll&gsm=1&rpstart=0&rpnum=0&islist=&querylist=&nojc=undefined");
                      articleRespModel.setUrl("https://image.baidu.com/search/index?tn=baiduimage&ct=201326592&lm=-1&cl=2&ie=gb18030&word=jerry&fr=ala&ala=1&alatpl=adress&pos=0&hs=2&xthttps=111111");
                      articleList.add(articleRespModel);



                      ArticleRespModel articleRespModel1 =new ArticleRespModel();
                      articleRespModel1.setTitle("哈哈");
                      articleRespModel1.setDescription("一张照片");
                      articleRespModel1.setPicUrl("http://changhaiwx.pagekite.me/photo-wall/a/iali11.jpg");
                      articleRespModel1.setUrl("http://changhaiwx.pagekite.me/page/photowall");
                      articleList.add(articleRespModel1);
                      newsMessage.setArticles(articleList);
                      newsMessage.setArticleCount(2);
                      respMessage = MessageUtil.newsMessageToXml(newsMessage);
                      break;
                    }
                    //2.设置菜单--向公众号发送“设置菜单”，设置公众号自定义菜单（需要等待新菜单生效or取消关注，重新关注能马上生效）
                    case "设置菜单": {
                        /**从数据库获取微信token
                         *
                         *
                         * */
                        String at = "aaaaaaaaaaaaa";//从数据库获取到的token
                        int result = 0;
                        if (at != null) {
                            // 调用接口创建菜单
                            result = menuService.createMenu(at);
                            // 判断菜单创建结果
                            if (0 == result) {
                                log.info("菜单创建成功！");
                            } else {
                                log.info("菜单创建失败，错误码：" + result);
                            }
                        }
                      break;
                    }
                    default:{
                      respMessage = MessageUtil.textMessageToXml(textMessage);
                      break;
                    }

                }
            }
            // 事件推送
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
                // 事件类型
                String eventType =requestMap.get("Event");

                // 订阅
                if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
                    //3.欢迎消息--新关注公众号用户收到欢迎图文消息
                    newsMessage.setArticleCount(1);

//                    List<ArticleRespModel> articleRespModels = new ArrayList<>();
//
//                    ArticleRespModel articleRespModel =new ArticleRespModel();
//                    articleRespModel.setTitle("欢迎使用科创生态公众号");
//                    articleRespModel.setDescription("欢迎图文消息内容");
//                    articleRespModel.setPicUrl("欢迎图文消息图片链接");
//                    articleRespModel.setUrl("欢迎图文消息跳转链接");
//                    articleRespModels.add(articleRespModel);

               //     newsMessage.setArticles(articleRespModels);
                    //新增用户信息到数据库
                    respMessage = MessageUtil.newsMessageToXml(newsMessage);



                    String accessToken=(String) redisTemplate.opsForValue().get("access_token");
                    //获取用户信息保存到数据库
                    String url = userUrl.replace("ACCESS_TOKEN",accessToken)
                      .replace("OPENID", fromUserName);
                     JSONObject jsonObject = httpRequest(url, "GET", null);
                     System.out.println(jsonObject);
                }
                // 取消订阅
                else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
                    // 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
                }
                // 扫描带参数二维码
                else if (eventType.equals(MessageUtil.EVENT_TYPE_SCAN)) {
                  // TODO 处理扫描带参数二维码事件
                }
                // 上报地理位置
                else if (eventType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
                  // TODO 处理上报地理位置事件
                }
                // 自定义菜单
                else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
                  // TODO 处理菜单点击事件
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }

        return respMessage;
    }


}


