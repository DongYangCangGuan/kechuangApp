package com.cloud.servicewechat.utils.weixin;

import com.cloud.servicewechat.entity.weixin.MenuModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author:cat
 * @Description 拼装菜单类
 * @Date: 2018-04-12  10:00
 * @Modified By:
 */
public class MenuUtil {

    /**
     * 拼装菜单
     * @return
     */
    public static Map<String, Object> makeMenu() {
        /**第一栏菜单*/
        MenuModel menu1=new MenuModel();
        menu1.setId("1");
        menu1.setName("第一个菜单");
        menu1.setType("view");
        menu1.setUrl("www.baidu.com");


        /**第二栏菜单*/
        MenuModel menu2=new MenuModel();
        menu2.setId("2");
        menu2.setName("第二个菜单");
        menu2.setType("view");
        menu2.setUrl("www.baidu.com");


        /**第三栏菜单*/
        MenuModel menu3=new MenuModel();
        menu3.setId("3");
        menu3.setName("第三个菜单");
        menu3.setType("click");
        menu3.setKey("3");

        MenuModel menu31=new MenuModel();
        menu31.setId("31");
        menu31.setName("第三个菜单子菜单1");
        menu31.setType("view");
        menu31.setUrl("www.baidu.com");

        MenuModel menu32=new MenuModel();
        menu32.setId("32");
        menu32.setName("第三个菜单子菜单2");
        menu32.setType("view");
        menu32.setUrl("www.baidu.com");

        MenuModel menu33=new MenuModel();
        menu33.setId("33");
        menu33.setName("第三个菜单子菜单3");
        menu33.setType("view");
        menu33.setUrl("www.baidu.com");

        /**最外一层大括号*/
        Map<String, Object> wechatMenuMap = new HashMap<>();

        /**包装button的List*/
        List<Map<String, Object>> wechatMenuMapList = new ArrayList<>();

        /**包装第一栏*/
        Map<String, Object> menuMap1 = new HashMap<>();

        menuMap1.put("name",menu1.getName());
        menuMap1.put("type",menu1.getType());
        menuMap1.put("url",menu1.getUrl());

        /**包装第二栏*/
        Map<String, Object> menuMap2 = new HashMap<>();

        menuMap2.put("name",menu2.getName());
        menuMap2.put("type",menu2.getType());
        menuMap2.put("url",menu2.getUrl());

        /**包装第三栏*/
        Map<String, Object> menuMap3 = new HashMap<>();
        Map<String, Object> menuMap31 = new HashMap<>();
        Map<String, Object> menuMap32 = new HashMap<>();
        Map<String, Object> menuMap33 = new HashMap<>();
        List<Map<String, Object>> subMenuMapList3 = new ArrayList<>();

        /**第三栏第一个*/
        menuMap31.put("name",menu31.getName());
        menuMap31.put("type",menu31.getType());
        menuMap31.put("url",menu31.getUrl());
        subMenuMapList3.add(menuMap31);

        /**第三栏第二个*/
        menuMap32.put("name",menu32.getName());
        menuMap32.put("type",menu32.getType());
        menuMap32.put("url",menu32.getUrl());
        subMenuMapList3.add(menuMap32);

        /**第三栏第三个*/
        menuMap33.put("name",menu33.getName());
        menuMap33.put("type",menu33.getType());
        menuMap33.put("url",menu33.getUrl());
        subMenuMapList3.add(menuMap33);

        menuMap3.put("name",menu3.getName());
        menuMap3.put("sub_button",subMenuMapList3);

        wechatMenuMapList.add(menuMap1);
        wechatMenuMapList.add(menuMap2);
        wechatMenuMapList.add(menuMap3);
        wechatMenuMap.put("button",wechatMenuMapList);
        return  wechatMenuMap;
    }


}
