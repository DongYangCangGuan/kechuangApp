package com.cloud.servicemanage.service;

import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.aop.OperationLogDetail;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.factory.BaseService;
import com.cloud.servicemanage.common.ConstantUtil;
import com.cloud.servicemanage.common.PageUtil;
import com.cloud.servicemanage.common.PageVo;
import com.cloud.servicemanage.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("roleUsers")
public class RoleUserService extends BaseService {

    @Autowired
    public com.cloud.servicemanage.mapper.RoleUserMapper mapper;

    public Map<String, Object> exec(String method, String param) {
        Map<String, Object> obj = new HashMap<>();
        switch (method) {
//            case  ConstantUtil.UPDATE_USER_PASSWORD://GET_ROLE_BASE_MSG:// "editUserPassword":
//                obj=editUserPassword(param);
//                break;
//            case  ConstantUtil.CHECK_USER_PASSWORD: // "vaildatorPassword":
//                obj=vaildatorPassword(param);
//                break;

            case ConstantUtil.GET_USER_AND_ROLES: // "getUserAndRoles":
                obj = getUserAndRoles(param);
                break;
            case ConstantUtil.DEL_USER_ROLES: //delUserRoles
                obj = delRoleforUser(param);
                break;
            default:
                break;
        }
        return obj;
    }

    @OperationLogDetail(detail = "用户-删除用户的角色")
    private Map<String, Object> delRoleforUser(String param) {
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject jsonObject = JSONObject.parseObject(param);
        String userId = jsonObject.getString("userId");
        String roleId = jsonObject.getString("roleId");

        int rows = mapper.delRoleforUser(roleId, userId);
        if (rows >= 1) {
            Constants.getSuccMsg(result, Constants.RESULTCODE_SUCC);
        } else {
            Constants.getErrMsg(result, Constants.RESULTCODE_ERR);
        }

        return result;
    }


    @OperationLogDetail(detail = "用户-获取用户列表&角色基本信息")
    private Map<String, Object> getUserAndRoles(String param) {
        Map<String, Object> Map = new HashMap<>();
        /* Page page = requestRoleBaseMsg.getPage();//获取到前端传过来的分页信息*/
        PageVo pageVo = JSONObject.parseObject(param, PageVo.class);   //把前端传过来的数据转化为object类型
        PageUtil pageUtil = pageVo.getPage();//获取前端的页面分页信息
        // Role role=pageVo.getRole();
        PageVo<Users> pageVo1 = new PageVo<>();
        //  try {
        int totalNum = mapper.getPageTotal(pageVo); //调用rolemapper对应的总页数查询方法，吧前端获取到的数据当参数传入，得到返回结果totalNum总页数
        if (totalNum > 0) {
            //从数据库拿到数据放到roleBeanBaseMsgList 里面
            List<Users> usersList = mapper.getUsers(pageVo);
            //返回参数
            //然后在使用pagevo类里面的datalist数组存放返回来的数组
            //在把roleBeanBaseMsgList 放到pagevo类(该类存放了page类的引用和datalist)里面的list中
            pageVo1.setPage(new PageUtil(pageUtil.getPageIndex(), pageUtil.getPageSize(), totalNum));
            pageVo1.setDataList(usersList);
            Constants.getSuccMsg(Map, pageVo1);
        } else {
            pageVo1.setPage(new PageUtil(pageUtil.getPageIndex(), pageUtil.getPageSize(), totalNum));
            pageVo1.setDataList(null);
            Constants.getSuccMsg(Map, pageVo1);
        }
//        }catch (Exception e){
//                e.printStackTrace();
//        }
        return Map;

    }
//    public Map<String,Object> editUserPassword(String param){
//        Map<String,Object> result=new HashMap<>();
//        JSONObject jsonObject = JSONObject.parseObject(param);
//        String USERID = jsonObject.getString("id");
//        String NEWPWD = jsonObject.getString("confirmPassword");
//        try{
//            int i = mapper.updateUserPwd(USERID,NEWPWD);
//            if( i > 0) {
//                result.put("code", 200);
//                result.put("message", "�޸ĳɹ�");
//            } else {
//                result.put("code", 201);
//                result.put("message", "succ");
//            }
//        }catch (Exception e){
//            result.put("code", 500);
//            result.put("message", e.getMessage());
//        }
//        return result;
//    }
//    public Map<String,Object> vaildatorPassword(String param){
//        Map<String,Object> result=new HashMap<>();
//        JSONObject jsonObject = JSONObject.parseObject(param);
//        String USERID = jsonObject.getString("USERID");
//        String PWD = jsonObject.getString("PWD");
//        try {
//            List<BaseUsers> list = mapper.getBaseUserInfo(USERID,PWD);
//            if(list.size()>0) {
//                result.put("code", 200);
//                result.put("message", "�޸ĳɹ�");
//            } else {
//                result.put("code", 201);
//                result.put("message", "succ");
//            }
//        }catch (Exception e){
//            result.put("code", 500);
//            result.put("message", e.getMessage());
//            e.printStackTrace();
//        }
//        return result;
//    }
}
