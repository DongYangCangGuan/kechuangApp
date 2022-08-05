package com.cloud.servicemanage.service;

import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.factory.BaseService;
import com.cloud.commonsmng.entity.appletEntity.User;
import com.cloud.servicemanage.common.ConstantUtil;
import com.cloud.servicemanage.common.PageUtil;
import com.cloud.servicemanage.common.PageVo;
import com.cloud.servicemanage.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户的业务逻辑层
 * author: tjs
 */
@Service("UserInfo")
public class UserInfoService extends BaseService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public Map<String, Object> exec(String method, String param) {
        Map<String, Object> obj = new HashMap<>();
        switch (method) {
            case ConstantUtil.GET_USER_INFO_PAGE_VO: //getUserInfoPageVo
                obj = getUserInfoPageVo(param);
                break;
            case ConstantUtil.GET_USER_INFO_BATCH_BY_ID: //getUserInfoBatchById
                obj = getUserInfoBatchById(param);
                break;
            case ConstantUtil.GET_USER_INFO_BY_CERTIFICATION_MARK_PAGE_VO: //getUserInfoByCertificationMarkPageVo
                obj = getUserInfoByCertificationMarkPageVo(param);
                break;
            case ConstantUtil.GET_USER_BY_UROLE_EQ_ONE_PAGE_VO:  //getUserByUroleEqOnePageVo
                obj = getUserByUroleEqOnePageVo(param);
                break;
            default:
                break;
        }

        return obj;
    }

    //分页查询小程序端的用户信息
    private Map<String, Object> getUserByUroleEqOnePageVo(String param) {
        Map<String, Object> result = new HashMap<>();
        PageVo pageVo = JSONObject.parseObject(param, PageVo.class);
        PageUtil page = pageVo.getPage();
        Integer count = userInfoMapper.getUserByUroleEqOnePageVoCount(pageVo);
        List<User> userList = userInfoMapper.getUserByUroleEqOnePageVo(pageVo);
        PageVo userPageVo = new PageVo();
        userPageVo.setPage(new PageUtil(page.getPageIndex(), page.getPageSize(), count));
        userPageVo.setDataList(userList);
        Constants.getSuccMsg(result, userPageVo);
        return result;
    }


    //分页查询待审核的用户信息（客户）
    private Map<String, Object> getUserInfoByCertificationMarkPageVo(String param) {
        Map<String, Object> result = new HashMap<>();

        PageVo pageVo = JSONObject.parseObject(param, PageVo.class);
        PageUtil pageUtil = pageVo.getPage();
        try {
            int UserInfoByCertificationMarkPageTotal = userInfoMapper.getUserInfoByCertificationMarkPageTotal(pageVo);
            List<User> UserInfoByCertificationMarkPageVo = userInfoMapper.getUserInfoByCertificationMarkPageVo(pageVo);
            PageVo<User> userPageVo = new PageVo<>();
            userPageVo.setPage(new PageUtil(pageUtil.getPageIndex(), pageUtil.getPageSize(), UserInfoByCertificationMarkPageTotal));
            userPageVo.setDataList(UserInfoByCertificationMarkPageVo);
            Constants.getSuccMsg(result, userPageVo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //根据用户编号批量查询用户信息
    private Map<String, Object> getUserInfoBatchById(String param) {
        return this.getUserByUroleEqOnePageVo(param);
    }

    //分页获取用户信息
    private Map<String, Object> getUserInfoPageVo(String param) {
        Map<String, Object> result = new HashMap<>();
        PageVo pageVo = JSONObject.parseObject(param, PageVo.class);
        PageUtil page = pageVo.getPage();
        Integer pageTotal = userInfoMapper.getPageTotal(pageVo);
        List<User> userList = userInfoMapper.getUserInfoPageVo(pageVo);
        PageVo<User> userPageVo = new PageVo<>();
        userPageVo.setPage(new PageUtil(page.getPageIndex(), page.getPageSize(), pageTotal));
        userPageVo.setDataList(userList);
        Constants.getSuccMsg(result, userPageVo);
        return result;
    }

}
