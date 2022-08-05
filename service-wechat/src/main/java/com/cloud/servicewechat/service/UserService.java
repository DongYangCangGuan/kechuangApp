package com.cloud.servicewechat.service;

import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.entity.BaseUserInfo;
import com.cloud.commonsmng.factory.BaseService;
import com.cloud.commonsmng.entity.appletEntity.User;
import com.cloud.servicewechat.common.ConstantUtil;
import com.cloud.servicewechat.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Map;
import java.util.HashMap;

@Service("user")
public class UserService extends BaseService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Map<String, Object> exec(String method, String param) {
        Map<String, Object> obj = new HashMap<>();
        switch (method) {
            case ConstantUtil.GET_USER:
                obj = getUser(param);
                break;
            case ConstantUtil.UPDATE_USER:
                obj = upDateUser(param);
                break;
            default:
                break;
        }
        return obj;
    }

    /**
     * 根据用户id 查询用户详细信息
     */
    private Map<String, Object> getUser(String param) {
        Map<String, Object> result = new HashMap<>();
        BaseUserInfo user = super.getUserInfo();
        if (user != null) {
            User user1 = (User) user;
            if (user1.getId() != null && !"".equals(user1.getId())) {
                User getUser = userMapper.getUser(user1.getId());  //获取当前用户编号
                Constants.getSuccMsg(result, getUser);// 拼装返回前端的数据
            } else if (user1.getMember() != null && user1.getMember().getEnterpriseCode() != null && !"".equals(user1.getMember().getEnterpriseCode())) {
                User userInMemberInfo = userMapper.getUserInMemberInfo(user1.getMember().getEnterpriseCode());  //获取当前企业账号
                Constants.getSuccMsg(result, userInMemberInfo);// 拼装返回前端的数据
            } else {
                Constants.getErrMsg(result, "登录失效");// 拼装返回前端的数据
            }
        } else {
            Constants.getErrMsg(result, "未登录");// 拼装返回前端的数据
        }
        return result;
    }

    /**
     * 根据id修改用户信息
     */
    private Map<String, Object> upDateUser(String param) {
        Map<String, Object> result = new HashMap<>();
        User user = JSONObject.parseObject(param, User.class);
        String userId = super.getUserInfo().getId();
        user.setUserId(userId);
        int userNew = userMapper.upDateUser(user);
        if (userNew > 0) {
            result.put("code", "200");
            result.put("msg", "修改成功");
        } else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//强制事务回滚
            result.put("code", "-1");
            result.put("msg", "修改失败");
        }
        Constants.getSuccMsg(result, userNew);
        return result;
    }
}
