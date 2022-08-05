package com.cloud.servicemanage.service.weixin.Impl;

import com.alibaba.fastjson.JSONObject;
import com.cloud.servicemanage.service.weixin.MenuService;
import com.cloud.servicemanage.utils.weixin.MenuUtil;
import com.cloud.servicemanage.utils.weixin.WeixinUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("SpringJavaAutowiringInspection")
@Service("menuService")
/**
 * @Author:cat
 * @Description 菜单控制接口实现
 * @Date: 2018-04-12  10:01
 * @Modified By:
 */
public class MenuServiceImpl implements MenuService {

    private static Logger log = LoggerFactory.getLogger(MenuServiceImpl.class);

    // 菜单创建（POST） 限1000（次/天）
    public static String menu_create_url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

    // 菜单查询（POST） 限10000（次/天）
    public static String menu_get_url = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";

    // 菜单删除（POST） 限1000（次/天）
    public static String menu_delete_url = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";



    /**
     * 查询菜单
     *
     * @param accessToken 有效的access_token
     * @return
     */
    @Override
    public JSONObject getMenu(String accessToken) {

        // 拼装创建菜单的url
        String url = menu_get_url.replace("ACCESS_TOKEN", accessToken);
        // 调用接口查询菜单
        JSONObject jsonObject = WeixinUtil.httpRequest(url, "GET", null);

        return jsonObject;
    }
    /**
     * 创建菜单(替换旧菜单)
     *
     * @param accessToken 有效的access_token`
     * @return 0表示成功，其他值表示失败
     */
    @Override
    public int createMenu(String accessToken) {
        int result = 0;

        //拼装菜单
        Map<String, Object> map = MenuUtil.makeMenu();

        // 拼装创建菜单的url
        String url = menu_create_url.replace("ACCESS_TOKEN", accessToken);
        // 将菜单对象转换成json字符串
        String jsonMenu = JSONObject.toJSONString(map).toString();
        // 调用接口创建菜单
        JSONObject jsonObject = WeixinUtil.httpRequest(url, "POST", jsonMenu);

        if (null != jsonObject) {
            if (0 != jsonObject.getInteger("errcode")) {
                result = jsonObject.getInteger("errcode");
                log.error("创建菜单失败 errcode:{} errmsg:{}", jsonObject.getInteger("errcode"), jsonObject.getString("errmsg"));
                log.error("****"+jsonMenu+"****");
            }
        }
        return result;
    }

    private Map<String,Object> createSubMap(String title, String type, String key){
        Map<String,Object> m = new HashMap<String, Object>();
        m.put("type", type);
        m.put("name",title);
        if("view".equals(type)){
            m.put("url", key);
        }else if("click".equals(type)){
            m.put("key", key);
        }else {
            m.put("key", key);
        }

        return m;
    }
    /**
     * 删除菜单
     *
     * @param accessToken 有效的access_token
     * @return 0表示成功，其他值表示失败
     */
    @Override
    public int deleteMenu(String accessToken) {
        int result = 0;

        // 拼装创建菜单的url
        String url = menu_delete_url.replace("ACCESS_TOKEN", accessToken);

        // 调用接口创建菜单
        JSONObject jsonObject = WeixinUtil.httpRequest(url, "POST", null);

        if (null != jsonObject) {
            if (0 != jsonObject.getInteger("errcode")) {
                result = jsonObject.getInteger("errcode");
                log.error("删除菜单失败 errcode:{} errmsg:{}", jsonObject.getInteger("errcode"), jsonObject.getString("errmsg"));
            }
        }
        return result;
    }
}
