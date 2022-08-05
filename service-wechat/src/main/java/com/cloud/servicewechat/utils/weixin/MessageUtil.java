package com.cloud.servicewechat.utils.weixin;


import com.cloud.servicewechat.entity.weixin.NewsMessageRespModel;
import com.cloud.servicewechat.entity.weixin.TextMessageRespModel;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:cat
 * @Description 微信消息工具类
 * @Date: 2018-04-12  10:00
 * @Modified By:
 */
public class MessageUtil {
    /**
     * 返回消息类型：文本
     */
    public static final String RESP_MESSAGE_TYPE_TEXT = "text";

    /**
     * 返回消息类型：音乐
     */
    public static final String RESP_MESSAGE_TYPE_MUSIC = "music";

    /**
     * 返回消息类型：图文
     */
    public static final String RESP_MESSAGE_TYPE_NEWS = "news";

    /**
     * 请求消息类型：文本
     */
    public static final String REQ_MESSAGE_TYPE_TEXT = "text";

    /**
     * 请求消息类型：图片
     */
    public static final String REQ_MESSAGE_TYPE_IMAGE = "image";

    /**
     * 请求消息类型：链接
     */
    public static final String REQ_MESSAGE_TYPE_LINK = "link";

    /**
     * 请求消息类型：地理位置
     */
    public static final String REQ_MESSAGE_TYPE_LOCATION = "location";

    /**
     * 请求消息类型：音频
     */
    public static final String REQ_MESSAGE_TYPE_VOICE = "voice";

    /**
     * 请求消息类型：推送
     */
    public static final String REQ_MESSAGE_TYPE_EVENT = "event";

    /**
     * 事件类型：subscribe(订阅)and 未关注群体扫描二维码
     */
    public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";

    /**
     * 事件类型：已关注群体扫描二维码
     */
    public static final String EVENT_TYPE_SCAN="SCAN";
    /**
     * 事件类型：unsubscribe(取消订阅)
     */
    public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";

    /**
     * 事件类型：CLICK(自定义菜单点击事件)
     */
    public static final String EVENT_TYPE_CLICK = "CLICK";
    /**
     * 事件类型：VIEW(点击自定义菜单跳转链接时的事件)
     */
    public static final String EVENT_TYPE_VIEW = "VIEW";

    /**
     * 事件类型：transfer_customer_service(把消息推送给客服)
     */
    public static final String EVENT_TYPE_TRANSFER_CUSTOMER_SERVICE = "transfer_customer_service";


    /**
     * 解析微信发来的请求（XML）
     *
     * @param request
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    //屏蔽某些编译时的警告信息(在强制类型转换的时候编译器会给出警告)
    public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {
        // 将解析结果存储在HashMap中
        Map<String, String> map = new HashMap<String, String>();

        // 从request中取得输入流
        InputStream inputStream = request.getInputStream();
        // 读取输入流
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        // 得到xml根元素
        Element root = document.getRootElement();
        // 得到根元素的所有子节点
        List<Element> elementList = root.elements();

        // 遍历所有子节点
        for (Element e : elementList)
            map.put(e.getName(), e.getText());

        // 释放资源
        inputStream.close();
        inputStream = null;

        return map;
    }

    /**
     * 文本消息对象转换成xml
     *
     * @param textMessage 文本消息对象
     * @return xml
     */
    public static String textMessageToXml(TextMessageRespModel textMessage) {
//        String res = String.format("<xml><ToUserName><![CDATA[%s]]></ToUserName><FromUserName><![CDATA[%s]]></FromUserName><CreateTime>%s</CreateTime><MsgType><![CDATA[text]]></MsgType> <Content><![CDATA[%s]]></Content> <FuncFlag>0</FuncFlag></xml> ",
//                textMessage.getToUserName(), textMessage.getFromUserName(), textMessage.getCreateTime(), textMessage.getContent());
//        return res;
          XStream xStream =new XStream();
          xStream.alias("xml",textMessage.getClass());
          return xStream.toXML(textMessage);
    }

    /**
     * 图文消息对象转换成xml
     *
     * @param newsMessage 图文消息对象
     * @return xml
     */
    public static String newsMessageToXml(NewsMessageRespModel newsMessage) {
//        xstream.alias("xml", newsMessage.getClass());
////        xstream.alias("Articles",newsMessage.getArticleRespModelList().getClass());
//        xstream.alias("item", new ArticleRespModel().getClass());
//        return xstream.toXML(newsMessage);
        StringBuilder sb = new StringBuilder("<xml><ToUserName><![CDATA["+newsMessage.getToUserName()+"]]></ToUserName>");
        sb.append("<FromUserName><![CDATA["+newsMessage.getFromUserName()+"]]></FromUserName>");
        sb.append("<CreateTime>"+newsMessage.getCreateTime()+"</CreateTime>");
        sb.append("<MsgType><![CDATA[news]]></MsgType>");
        sb.append("<ArticleCount>"+newsMessage.getArticleCount()+"</ArticleCount>");
        sb.append("<Articles>");
        for (int i = 0; i <newsMessage.getArticles().size(); i++) {
            sb.append("<item>");
            sb.append("<Title><![CDATA["+newsMessage.getArticles().get(i).getTitle()+"]]></Title>");
            sb.append("<Description><![CDATA["+newsMessage.getArticles().get(i).getDescription()+"]]></Description>");
            sb.append("<PicUrl><![CDATA["+newsMessage.getArticles().get(i).getPicUrl()+"]]></PicUrl>");
            sb.append("<Url><![CDATA["+newsMessage.getArticles().get(i).getUrl()+"]]></Url>");
            sb.append("</item>");
        }
        sb.append("</Articles></xml>");
         return sb.toString();
    }

    /**
     * 扩展xstream，使其支持CDATA块
     *
     * @date
     */
    private static XStream xstream = new XStream(new XppDriver() {
        public HierarchicalStreamWriter createWriter(Writer out) {
            return new PrettyPrintWriter(out) {
                // 对所有xml节点的转换都增加CDATA标记
                boolean cdata = true;

                @SuppressWarnings("unchecked")
                public void startNode(String name, Class clazz) {
                    super.startNode(name, clazz);
                }

                protected void writeText(QuickWriter writer, String text) {
                    if (cdata) {
                        writer.write("<![CDATA[");
                        writer.write(text);
                        writer.write("]]>");
                    } else {
                        writer.write(text);
                    }
                }
            };
        }
    });
}
