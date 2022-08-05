package com.cloud.servicemanage.service;

import com.alibaba.fastjson.JSONObject;
import com.amazonaws.services.s3.AmazonS3;
import com.cloud.commonsmng.aop.OperationLogDetail;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.factory.BaseService;
import com.cloud.servicemanage.common.ConstantUtil;
import com.cloud.servicemanage.common.PageUtil;
import com.cloud.servicemanage.common.PageVo;
import com.cloud.servicemanage.entity.*;
import com.cloud.servicemanage.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 角色管理
 */
@Service("Role")
public class RoleService extends BaseService {
    @Autowired
    public RoleMapper roleBaseMsgMapper;

    @Override
    public Map<String, Object> exec(String method, String param) {
        Map<String,Object> obj = new HashMap<>();
        switch (method) {
            case ConstantUtil.GET_ROLE_BASE_MSG:    // getRole
                obj = getRole(param);
                break;
            case ConstantUtil.DELETE_ROLE_BASEMSG:  // deleteRole
                obj = deleteRole(param);
                break;
            case ConstantUtil.ADD_ROLE_BASE_MSG:    // addRole
                obj = addRole(param);
                break;
            case ConstantUtil.UPDATE_ROLE_BASE_MSG: // updateRole
                obj = updateRole(param);
                break;
            case ConstantUtil.CHECK_ROLE_CODE_BASEMSG:  // checkRoleCode
                obj = checkRoleCode(param);
                break;
            case ConstantUtil.CHECK_ROLE_NAME_BASEMSG:  // checkRoleName
                obj = checkRoleName(param);
                break;
            case ConstantUtil.IS_ROLE_USED:  //isRoleUsed
                obj = isRoleUsed(param);
                break;
            default:
                break;
        }

        return obj;
    }

    /**
     * 判断该角色下是否有人员
     *
     * @param param
     * @return
     */
    private Map<String,Object> isRoleUsed(String param) {
        Map<String,Object> result = new HashMap<>();

        JSONObject jsonObject = JSONObject.parseObject(param);
        String code = jsonObject.getString("code");
        int data = roleBaseMsgMapper.isRoleUsed(code);
        Constants.getSuccMsg(result,data);

        return result;
    }

    /**
     * 校验角色code是否已经存在
     *
     * @param param
     * @return
     */
    private Map<String,Object> checkRoleCode(String param) {
        Map<String,Object> result = new HashMap<>();

        JSONObject jsonObject = JSONObject.parseObject(param);
        String id = jsonObject.getString("id");
        String code = jsonObject.getString("code");

        int i = roleBaseMsgMapper.checkRoleCode((id == null || id.isEmpty()) ? "-99999" : id, code);
        if (i > 0) {
            Constants.getSuccMsg(result, Constants.RESULTCODE_REPEAT);
        } else {
            Constants.getSuccMsg(result, Constants.RESULT_NO_DATA);
        }

        return result;
    }

    /**
     * 校验角色名称是否已经存在
     *
     * @param param
     * @return
     */
    private Map<String,Object> checkRoleName(String param) {
        Map<String,Object> result = new HashMap<>();

        JSONObject jsonObject = JSONObject.parseObject(param);
        String id = jsonObject.getString("id");
        String name = jsonObject.getString("name");

        int i = roleBaseMsgMapper.checkRoleName((id == null || id.isEmpty()) ? "-99999" : id, name);
        if (i > 0){
            Constants.getSuccMsg(result, Constants.RESULTCODE_REPEAT);
        } else {
            Constants.getSuccMsg(result, Constants.RESULT_NO_DATA);
        }

        return result;
    }

    /**
     * 获取角色
     *
     * @param param
     * @return
     */
    @OperationLogDetail(detail = "角色管理-获取角色基本信息")
    private Map<String, Object> getRole(String param) {
        Map<String, Object> roleBaseMsgMap = new HashMap<>();
        PageVo pageVo = JSONObject.parseObject(param, PageVo.class);   //把前端传过来的数据转化为object类型
        PageUtil pageUtil = pageVo.getPage();//获取前端的页面分页信息
       try {
            int totalNum = roleBaseMsgMapper.getPageTotal(pageVo); //调用rolemapper对应的总页数查询方法，吧前端获取到的数据当参数传入，得到返回结果totalNum总页数
            if (totalNum > 0) {
                //从数据库拿到数据放到roleBeanBaseMsgList里面
                List<Role> roleBeanBaseMsgList = roleBaseMsgMapper.getRole(pageVo);
                //然后在使用pagevo类里面的datalist数组存放返回来的数组
                PageVo<Role> pageVo1 = new PageVo<>();   //在把roleBeanBaseMsgList 放到pagevo类(该类存放了page类的引用和datalist)里面的list中
                pageVo1.setPage(new PageUtil(pageUtil.getPageIndex(), pageUtil.getPageSize(), totalNum));
                pageVo1.setDataList(roleBeanBaseMsgList);
                Constants.getSuccMsg(roleBaseMsgMap, pageVo1);
            }
        } catch (Exception e){
           e.printStackTrace();
        }

        return roleBaseMsgMap;
    }

    /**
     * 添加角色
     *
     * @param param
     * @return
     */
    @OperationLogDetail(detail = "角色管理-添加角色")
    private Map<String,Object> addRole (String param) {
        Map<String,Object> roleBaseMsgMap = new HashMap<>();

        Role bean = JSONObject.parseObject(param, Role.class);
        bean.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
        bean.setCreatorid(usr.getId());
        bean.setModifierid(usr.getId());

        int i = roleBaseMsgMapper.addRole(bean);
        if (i > 0) {
            Constants.getSuccMsg(roleBaseMsgMap, Constants.RESULTCODE_SUCC);
        } else{
            Constants.getSuccMsg(roleBaseMsgMap, Constants.RESULTCODE_ERR);
        }

        return roleBaseMsgMap;
    }

    /**
     * 删除角色
     *
     * @param param
     * @return
     */
    @OperationLogDetail(detail = "角色管理-删除角色")
    private Map<String,Object> deleteRole(String param) {
        Map<String,Object> roleBaseMsgMap = new HashMap<>();

        JSONObject jsonObject = JSONObject.parseObject(param);
        String id = jsonObject.getString("id");

        int i = roleBaseMsgMapper.deleteRole(id);
        if (i > 0) {
            Constants.getSuccMsg(roleBaseMsgMap, Constants.RESULTCODE_SUCC);
        } else {
            Constants.getSuccMsg(roleBaseMsgMap, Constants.RESULTCODE_REPEAT);
        }

        return roleBaseMsgMap;
    }

    /**
     * 修改角色
     * @param param
     * @return
     */
    @OperationLogDetail(detail = "角色管理-修改角色")
    private Map<String,Object> updateRole(String param) {
        Map<String,Object> roleBaseMsgMap = new HashMap<>();

        Role bean = JSONObject.parseObject(param, Role.class);
        bean.setModifierid(usr.getId());

       int i = roleBaseMsgMapper.updateRole(bean);
        if (i > 0) {
            Constants.getSuccMsg(roleBaseMsgMap, Constants.RESULTCODE_SUCC);
        } else {
            Constants.getSuccMsg(roleBaseMsgMap, Constants.RESULTCODE_ERR);
        }

        return roleBaseMsgMap;
    }
}