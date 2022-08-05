package com.cloud.servicemanage.service;

import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.entity.BaseUserInfo;
import com.cloud.commonsmng.factory.BaseService;
import com.cloud.servicemanage.common.ConstantUtil;
import com.cloud.servicemanage.common.UUIDGenerator;
import com.cloud.servicemanage.entity.Menu;
import com.cloud.servicemanage.mapper.MenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("Menu")
public class MenuService extends BaseService {

    @Autowired
    MenuMapper mapper;

    public Map<String,Object> exec(String method, String param) {
        Map<String,Object> obj = new HashMap<>();
        switch (method){
            case ConstantUtil.GET_MENULIST:// "getMenuList":
                obj = getMenuList(param);
                break;
            case ConstantUtil.GET_MENU_LIST_BY_ID: // "getMenuListByID":
                obj = getMenuListByID(param);
                break;
            case ConstantUtil.UPDATE_MENU:// "ModifyMenu":
                obj = ModifyMenu(param);
                break;
            case ConstantUtil.ADD_MENU:// "insertMenu":
                obj = insertMenu(param);
                break;
            case ConstantUtil.GET_MENU_INFO_BY_ID:// "getMenuInfoByID":
                obj = getMenuInfoByID(param);
                break;
            case ConstantUtil.UPDATE_MENUTREE://  "modifyMenuTree":
                obj = modifyMenuTree(param);
                break;
            case ConstantUtil.GET_MENU_SECURITYINFO:// "getSecurityInfoAll":
                obj = getSecurityInfoAll(param);
                break;
            case ConstantUtil.GET_MENU_BY_PARENTID: //"selectMenuByParentId":
                obj = selectMenuByParentId(param);
                break;
            case ConstantUtil.GET_MENU_BY_ROLEID: //"selectMenuByParentId":
                obj = selectMenuByRoleId(param);
                break;
            case ConstantUtil.SAVE_MENU_BY_ROLEID: //"selectMenuByParentId":
                obj = saveMenuByRoleId(param);
                break;
            default:
                break;
        }

        return obj;
    }

    /**
     * 分配用户菜单
     *
     * @param param
     * @return
     */
    private Map<String,Object> saveMenuByRoleId(String param) {
        Map<String,Object> result = new HashMap<>();

        JSONObject jsonObject = JSONObject.parseObject(param);
        String menuIds = jsonObject.getString("menuIds");
        String roleId = jsonObject.getString("roleId");

        String[] menuId = menuIds.split(",");
        List<Menu> list = new ArrayList<>();
        for (String s : menuId) {
            Menu m = new Menu();
            m.setModifierid(usr.getId());
            m.setCreatorid(usr.getId());
            m.setId(s);
            list.add(m);
        }
        int i = mapper.insertMenuByRoleId(roleId, list);
        if (i > 0) {
            Constants.getSuccMsg(result, i);
        } else {
            Constants.getSuccMsg(result, Constants.RESULT_NO_DATA);
        }
        return  result;

    }
    public Map<String,Object> getSecurityInfoAll(String param) {
        Map<String, Object> result = new HashMap<>();

        JSONObject jsonObject = JSONObject.parseObject(param);
        String role = jsonObject.getString("role");

        BaseUserInfo usrs = this.getUserInfo(); //StringUtils.trimToEmpty(json.getString("id"));
        String id = usrs.getId();
        List<Menu> list = mapper.querySecurityMenuTreeList(role);//usrs.getId()
        if (list.size() > 0) {
            result.put("code", 200);
            result.put("data", list);
            List<String> l = new ArrayList<>();

            l.add("admin");
            result.put("roles", l);
        } else {
          Constants.getSuccMsg(result,Constants.RESULTCODE_ERR);
        }

        return result;
    }

    /**
     * 根据roleid获取菜单列表
     *
     * @param param
     * @return
     */
    private Map<String,Object> selectMenuByRoleId(String param) {
        Map<String,Object> result = new HashMap<>();

        JSONObject jsonObject = JSONObject.parseObject(param);
        String roleId = jsonObject.getString("roleid");

        List<Menu> menus = mapper.selectMenuByRoleId(roleId);
        List<String> ids = new ArrayList<>();
        for (Menu m : menus) {
            ids.add(m.getId());
        }
        Constants.getSuccMsg(result, ids.toArray());

        return result;
    }

    /**
     * 获取整个菜单树
     *
     * @param param
     * @return
     */
    private Map<String,Object> selectMenuByParentId(String param) {
        Map<String,Object> result = new HashMap<>();

        List<Menu> list = mapper.selectMenuByParentId("0");
        if (list.size() > 0) {
            Constants.getSuccMsg(result, list);
        } else {
            Constants.getSuccMsg(result, Constants.RESULTCODE_ERR);
        }
        return result;
    }

    /**
     * 获取一级菜单列表
     *
     * @param param
     * @return
     */

    public Map<String,Object> getMenuList(String param) {
        Map<String,Object> result = new HashMap<>();

        List<Menu> list = mapper.getMenuList();
        Constants.getSuccMsg(result, list);

        return result;
    }

    /**
     * 根据菜单id获取其下级菜单列表
     *
     * @param param
     * @return
     */
    public Map<String,Object> getMenuListByID(String param) {
        Map<String,Object> result=new HashMap<>();

        JSONObject jsonObject = JSONObject.parseObject(param);
        String ID = jsonObject.getString("id");

        List<Menu> subMenuList = mapper.getMenuListById(ID);
        Constants.getSuccMsg(result, subMenuList);

        return result;
    }

    public Map<String,Object> getMenuInfoByID(String param){
        Map<String,Object> result=new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(param);
        String ID = jsonObject.getString("id");
       // System.out.println(ID);
        try {
            List<Menu> list = mapper.getMenuInfoById(ID);
            if(list.size()>0) {
                result.put("code", 200);
                result.put("data",list.get(0));
            } else {
                result.put("code", 201);
                result.put("message", "未找到菜单信息");
            }
        }catch (Exception e){
            result.put("code", 500);
            result.put("message", e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 机构树拖拽修改
     *
     * @param param
     * @return
     */
    public Map<String,Object> modifyMenuTree(String param){
        Map<String,Object> result=new HashMap<>();

        JSONObject jsonObject = JSONObject.parseObject(param);
        String ID =jsonObject.getString("id");
        String PARENTID =jsonObject.getString("parentid");

        int i = mapper.modifyMenuTree(ID, PARENTID);
        if(i == 1) {
            Constants.getSuccMsg(result, Constants.RESULTCODE_SUCC);
        } else {
            Constants.getSuccMsg(result, Constants.RESULTCODE_ERR);
        }

        return result;
    }

    /**
     * 修改菜单信息
     *
     * @param param
     * @return
     */
    public Map<String,Object> ModifyMenu(String param) {
        Map<String,Object> result=new HashMap<>();

        Menu menu = buildBaseMenu(param);
        menu.setModifytime(new Date());
        menu.setModifierid(getUserInfo().getId());
        menu.setCreatetime(new Date());
        menu.setCreatorid(getUserInfo().getId());

        int i = mapper.modifyMenuInfo(menu);
        if(i == 1) {
            Constants.getSuccMsg(result, Constants.RESULTCODE_SUCC);
        } else {
            Constants.getSuccMsg(result, Constants.RESULTCODE_ERR);
        }

        return result;
    }

    /**
     * 新增菜单信息
     *
     * @param param
     * @return
     */
    public Map<String,Object> insertMenu(String param) {
        Map<String,Object> result=new HashMap<>();

        Menu menu = buildBaseMenu(param);
        menu.setId(UUIDGenerator.getUUID());
        menu.setCreatorid(getUserInfo().getId());
        menu.setModifierid(getUserInfo().getId());
        if(menu.getParentid() == null || menu.getParentid().isEmpty()){
            menu.setParentid("0");
        }
        if(menu.getIndex() == null || menu.getIndex().isEmpty()){
            menu.setIndex("99");
        }

        int i = mapper.insertMenu(menu);
        if(i == 1) {
            Constants.getSuccMsg(result, Constants.RESULTCODE_SUCC);
        } else {
            Constants.getSuccMsg(result, Constants.RESULTCODE_ERR);
        }

        return result;
    }

    /**
     * BaseMenu对象赋值
     * @param parama
     * @return
     */
    private Menu buildBaseMenu(String parama){

        JSONObject jsonObject = JSONObject.parseObject(parama);


        Menu menu=(Menu)JSONObject.toJavaObject(jsonObject, Menu.class);

        if(  menu.getParentid()==null || menu.getParentid().isEmpty()){
            menu.setParentid("0");
        }

        menu.setModifytime(new Date());
        return menu;
    }

}
