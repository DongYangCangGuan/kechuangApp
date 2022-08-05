package com.cloud.servicemanage.utils;

import com.cloud.commonsmng.entity.appletEntity.Dictionary;
import com.cloud.commonsmng.entity.appletEntity.Notes;
import com.cloud.commonsmng.entity.appletEntity.User;
import com.cloud.commonsmng.entity.appletEntity.WeixinInfo;

import com.cloud.servicemanage.entity.PushEntity;
import com.cloud.servicemanage.mapper.MemberMapper;
import com.cloud.servicemanage.mapper.NotesMapper;
import com.cloud.servicemanage.mapper.PushMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class sendWechat {
    private static final Logger logger = LoggerFactory.getLogger(sendWechat.class);
    @Value("${wx.mp.configs.appId}")
    private String appId;
    @Value("${wx.mp.configs.secret}")
    private String secret;

    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    private PushMapper pushMapper;
    @Autowired
    private NotesMapper notesMapper;
    @Autowired
    private MemberMapper memberMapper;

    /**
     * 是否需要做异步
     * @param notes
     */
    public void sendweixin(Notes notes){
        //String templateId="Slxjlr_FPAdcwrIFuR4KIUoA53KggalBxkS2ICbQUik";
        //审核结果通知
        //String templateId = "2WYFptTPrbIExm97CLGZ-iaveFA19WQGxwi5q7yyeRI";
        String templateId = notes.getTemplateId();
        List<String> userIds = notes.getUserIds();
        List<User> users = new ArrayList<>();
        if(userIds!=null && userIds.size()>0){
            //根据userId,获取user表中unionid是否存在
            users = notesMapper.getUserUnionId(userIds);
        }
        if(users!=null && users.size()>0){
            //设置消息内容
            PushEntity pushEntity = new PushEntity();
            pushEntity.setTitle(notes.getTitle());
            pushEntity.setKeywords1(notes.getContent());
            pushEntity.setKeywords2(notes.getTitle());
            pushEntity.setKeywords4(notes.getUserName());
            Date date = new Date();
            SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            pushEntity.setKeywords3(dateFormat.format(date));
            List<String> openIds = new ArrayList<>();
            List<String> unusers = new ArrayList<>();
            for (int i = 0; i<users.size() ; i++) {
                if (users.get(i) != null && !users.get(i).equals("")) {
                    if (users.get(i).getOpenId() != null && !users.get(i).getOpenId().equals("")) {
                        openIds.add(users.get(i).getOpenId());
                    } else {
                        //unionid为空，不做处理
                        //unionid不为空，openId为空，加入到list
                        if (users.get(i).getUnionid() != null && !users.get(i).getUnionid().equals("")) {
                            unusers.add(users.get(i).getId());
                        }
                    }
                }
            }
            //如果有unionud不为空，openId为空的情况，进行一次微信公众号同步操作
            if(unusers!=null && unusers.size()>0){
                String accessToken = (String) redisTemplate.opsForValue().get("access_token");
                //获取最新的公众号openId
                /*List<String> nextopenId = notesMapper.getNewOpenId();
                List<String> list = new ArrayList<>();
                if(nextopenId!=null && nextopenId.size()>0){
                    list = wechatpushUtil.GetOpenIdList(nextopenId.get(0),accessToken);
                }else{
                    list = wechatpushUtil.GetOpenIdList("",accessToken);
                }*/
                List<String> list = wechatpushUtil.GetOpenIdList("",accessToken);
                if(list!=null && list.size()>0){
                    //将openId存库
                    int e = pushMapper.insertweixinIfo(list);
                }
                //获取weixin_info表中unionid为空的对象
                List<WeixinInfo> unionidemptys = notesMapper.getUnionidempty();
                for (int j=0;j<unionidemptys.size();j++){
                    String unionid = wechatpushUtil.getUnionId(unionidemptys.get(j).getOpenId(),accessToken);
                    unionidemptys.get(j).setUnionid(unionid);
                }
                if(unionidemptys!=null && unionidemptys.size()>0){
                    //将unionid入库
                    pushMapper.insertUnionid(unionidemptys);
                }
                List<User> users1 = notesMapper.getUserUnionId(unusers);
                for (int i = 0; i<users1.size() ; i++) {
                    if (users1.get(i) != null && !users1.get(i).equals("")) {
                        if (users1.get(i).getOpenId() != null && !users1.get(i).getOpenId().equals("")) {
                            openIds.add(users1.get(i).getOpenId());
                        }
                    }
                }
            }
            //获取appid /url
            //根据kind获取字典表list
            List<Dictionary> appids = memberMapper.getMemberTypelist("weixinappId");
            List<Dictionary> urls = memberMapper.getMemberTypelist("weixinurl");
            pushEntity.setAppId(appids.get(0).getName());
            pushEntity.setUrl(urls.get(0).getName());
            logger.info("微信推送数量：",openIds.size());
            //发送消息
            wechatpushUtil.sendMsg(appId,secret,pushEntity,templateId,openIds);
        }
    }

    /**
     * 推送
     * @param
     */
    public int sendweixin2(PushEntity pushEntity){
        //String templateId="Slxjlr_FPAdcwrIFuR4KIUoA53KggalBxkS2ICbQUik";
        //审核结果通知
        //String templateId = "2WYFptTPrbIExm97CLGZ-iaveFA19WQGxwi5q7yyeRI";
        int result = 0;
        String templateId = pushEntity.getTemplateType();
        String accessToken = (String) redisTemplate.opsForValue().get("access_token");
        List<String> openIds = new ArrayList<>();
        if(pushEntity.getOpenIds()!=null && pushEntity.getOpenIds().size()>0){
            openIds = pushEntity.getOpenIds();
        }else {
            List<String> userIds = pushEntity.getUserIds();
            List<User> users = new ArrayList<>();
            //根据userId,获取user表中unionid是否存在
            if (userIds != null && userIds.size() > 0) {
                users = notesMapper.getUserUnionId(userIds);
            }
            if (users != null && users.size() > 0) {


                List<String> unusers = new ArrayList<>();
                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i) != null && !users.get(i).equals("")) {
                        if (users.get(i).getOpenId() != null && !users.get(i).getOpenId().equals("")) {
                            openIds.add(users.get(i).getOpenId());
                        } else {
                            //unionid为空，不做处理
                            //unionid不为空，openId为空，加入到list
                            if (users.get(i).getUnionid() != null && !users.get(i).getUnionid().equals("")) {
                                unusers.add(users.get(i).getId());
                            }
                        }
                    }
                }

                //如果有unionud不为空，openId为空的情况，进行一次微信公众号同步操作
                if (unusers != null && unusers.size() > 0) {

                    //获取最新的公众号openId
                /*List<String> nextopenId = notesMapper.getNewOpenId();
                List<String> list = new ArrayList<>();
                if(nextopenId!=null && nextopenId.size()>0){
                    list = wechatpushUtil.GetOpenIdList(nextopenId.get(0),accessToken);
                }else{
                    list = wechatpushUtil.GetOpenIdList("",accessToken);
                }*/
                    List<String> list = wechatpushUtil.GetOpenIdList("", accessToken);
                    if (list != null && list.size() > 0) {
                        //将openId存库
                        int e = pushMapper.insertweixinIfo(list);
                    }
                    //获取weixin_info表中unionid为空的对象
                    List<WeixinInfo> unionidemptys = notesMapper.getUnionidempty();
                    for (int j = 0; j < unionidemptys.size(); j++) {
                        String unionid = wechatpushUtil.getUnionId(unionidemptys.get(j).getOpenId(), accessToken);
                        unionidemptys.get(j).setUnionid(unionid);
                    }
                    if (unionidemptys != null && unionidemptys.size() > 0) {
                        //将unionid入库
                        pushMapper.insertUnionid(unionidemptys);
                    }
                    List<User> users1 = notesMapper.getUserUnionId(unusers);
                    for (int i = 0; i < users1.size(); i++) {
                        if (users1.get(i) != null && !users1.get(i).equals("")) {
                            if (users1.get(i).getOpenId() != null && !users1.get(i).getOpenId().equals("")) {
                                openIds.add(users1.get(i).getOpenId());
                            }
                        }
                    }
                }
            }
        }
            //获取appid /url
            //根据kind获取字典表list
           // List<Dictionary> appids = memberMapper.getMemberTypelist("weixinappId");
           //List<Dictionary> urls = memberMapper.getMemberTypelist("weixinurl");
           // pushEntity.setAppId(appids.get(0).getName());
            //pushEntity.setUrl(urls.get(0).getName());
            logger.info("微信推送开始----------------------：");
            //发送消息
            //result = wechatpushUtil.sendMsg(appId,secret,pushEntity,templateId,openIds);
            result = wechatpushUtil.sendMsg2(pushEntity,templateId,openIds,accessToken);

        return result;
    }

    //同步人员信息
    public int  updateWechatusers(){
        try{
        String accessToken = (String) redisTemplate.opsForValue().get("access_token");
        List<String> list = wechatpushUtil.GetOpenIdList("", accessToken);
        if (list != null && list.size() > 0) {
            //将openId存库
            int e = pushMapper.insertweixinIfo(list);
        }
        //获取weixin_info表中unionid为空的对象
        List<WeixinInfo> unionidemptys = notesMapper.getUnionidempty();
        for (int j = 0; j < unionidemptys.size(); j++) {
            String unionid = wechatpushUtil.getUnionId(unionidemptys.get(j).getOpenId(), accessToken);
            unionidemptys.get(j).setUnionid(unionid);
        }
        if (unionidemptys != null && unionidemptys.size() > 0) {
            //将unionid入库
            pushMapper.insertUnionid(unionidemptys);
        }
        }catch (Exception e){
            logger.error("微信公众号人员更新失败");
            e.printStackTrace();
        }
        return 0;
    }
}
