package com.cloud.servicewechat.service;

import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.entity.appletEntity.*;
import com.cloud.commonsmng.factory.BaseService;
import com.cloud.servicewechat.common.ConstantUtil;
import com.cloud.servicewechat.common.PageUtil;
import com.cloud.servicewechat.common.PageVo;
import com.cloud.servicewechat.mapper.ActivityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("activity")
public class ActivityService extends BaseService {

   @Autowired
    private ActivityMapper activityMapper;

    @Override
    public Map<String, Object> exec(String method, String param) {
        Map<String, Object> obj = new HashMap<>();
        switch (method) {
            case ConstantUtil.GET_SIGN_LIST:
                obj = getSignList(param);
                break;
            case ConstantUtil.GET_SIGN_DETAIL:
                obj = getSignDetail(param);
                break;
            default:
                break;
        }
        return obj;
    }

    /*查询个人的活动列表
     * author:xh
     */
    private Map<String, Object> getSignList(String param) {
        Map<String, Object> result = new HashMap<>();
        PageVo pageVo = JSONObject.parseObject(param, PageVo.class);//把前端传来的参数分装成分页类
        PageUtil pageUtil = pageVo.getPage();
        pageVo.setUserId(this.getUserInfo().getId());
        int totalNum = activityMapper.countSignList(pageVo);
        if (totalNum > 0) {
            List<ActivityEntity> activityList = activityMapper.getSignList(pageVo);
            PageVo<ActivityEntity> pageVo1 = new PageVo<>();
            pageVo1.setPage(new PageUtil(pageUtil.getPageIndex(), pageUtil.getPageSize(), totalNum));
            pageVo1.setDataList(activityList);
            Constants.getSuccMsg(result, pageVo1);
        }
        return result;
    }

    private Map<String,Object>getSignDetail( String param){
        Map<String,Object> result  = new HashMap<>();
        JSONObject json=JSONObject.parseObject(param);
        String userId =super.getUserInfo().getId();
        String activityEventId =json.getString("activityEventId");
        SignEntity a=activityMapper.getSignDetail(activityEventId,userId);
        List<SignDetail> sdList = activityMapper.getSignTemAs(a.getId());
        a.setSdList(sdList);
        Constants.getSuccMsg(result, a);
        return result;

    }

}
