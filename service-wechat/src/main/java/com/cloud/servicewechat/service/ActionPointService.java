package com.cloud.servicewechat.service;

import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.entity.appletEntity.ActionPoint;
import com.cloud.commonsmng.entity.appletEntity.StatusPoint;
import com.cloud.commonsmng.entity.appletEntity.User;
import com.cloud.commonsmng.factory.BaseService;
import com.cloud.servicewechat.common.ConstantUtil;
import com.cloud.servicewechat.common.SnowflakeIdGenerator;
import com.cloud.servicewechat.mapper.ActionPointMapper;
import com.cloud.servicewechat.mapper.StatusPointMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("actionPoint")
public class ActionPointService extends BaseService {

    @Autowired
    private ActionPointMapper actionPointMapper;

    //实例化日志对象，引用jar包org.slf4j.Logger
    private static final Logger logger = LoggerFactory.getLogger(ActionPointService.class);

    @Value("${SnowFlakeIdGenerator.workerId}")
    private long workerId;

    @Value("${SnowFlakeIdGenerator.datacenterId}")
    private long datacenterId;

    private SnowflakeIdGenerator snowflakeIdGenerator;

    @Override
    public Map<String, Object> exec(String method, String param) {
        Map<String, Object> obj = new HashMap<>();
        snowflakeIdGenerator = new SnowflakeIdGenerator(this.workerId, this.datacenterId);
        switch (method) {
            case ConstantUtil.ADD_ACTION_POINT: //addActionPoint;
                obj = addActionPoint(param);
                break;
            default:
                break;
        }
        return obj;
    }

    //新增行为埋点信息
    private Map<String, Object> addActionPoint(String param) {
        logger.info("添加埋点信息的传参: [%s]", param);
        Map<String, Object> result = new HashMap<>();
        ActionPoint actionPoint = JSONObject.parseObject(param, ActionPoint.class);
        super.insertBaseInfo(actionPoint);
        String userId = null;
        String enterpriseCode = null;

        if (super.getUserInfo() != null) {
            User user = (User) super.getUserInfo();
            logger.info(String.format("添加埋点中获取父级Service存储的用户信息: [%s]", user));
            if (user.getId() != null && !"".equals(user.getId())) {
                userId = user.getId();
            } else if (user.getMember() != null && user.getMember().getEnterpriseCode() != null && !"".equals(user.getMember().getEnterpriseCode())) {
                enterpriseCode = user.getMember().getEnterpriseCode();
            }
        }
        actionPoint.setUserId(userId);
        actionPoint.setEnterpriseCode(enterpriseCode);
        logger.info(String.format("当前添加埋点中的埋点表相关信息: [%s]", actionPoint));
        int addResult = actionPointMapper.insert(actionPoint);
        Constants.getSuccMsg(result, addResult > 0);
        logger.info(String.format("当前添加埋点的结果: [%s]", result));
        return result;
    }
}
