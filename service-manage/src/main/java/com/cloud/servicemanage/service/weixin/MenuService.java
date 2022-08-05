package com.cloud.servicemanage.service.weixin;


import com.alibaba.fastjson.JSONObject;

/**
 * @Author:cat
 * @Description 菜单服务类
 * @Date: 2018-04-12  10:01
 * @Modified By:
 */
public interface MenuService {
    JSONObject getMenu(String accessToken);
    int createMenu(String accessToken);
    int deleteMenu(String accessToken);

}
