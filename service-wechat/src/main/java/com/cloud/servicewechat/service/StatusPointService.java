package com.cloud.servicewechat.service;

import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.entity.appletEntity.StatusPoint;
import com.cloud.commonsmng.factory.BaseService;
import com.cloud.servicewechat.common.ConstantUtil;
import com.cloud.servicewechat.common.SnowflakeIdGenerator;
import com.cloud.servicewechat.mapper.StatusPointMapper;
import com.netflix.discovery.converters.Auto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("statusPoint")
public class StatusPointService extends BaseService {

    @Autowired
    private StatusPointMapper statusPointMapper;

    //实例化日志对象，引用jar包org.slf4j.Logger
    private static final Logger logger = LoggerFactory.getLogger(StatusPointService.class);

    @Value("${SnowFlakeIdGenerator.workerId}")
    private long workerId;

    @Value("${SnowFlakeIdGenerator.datacenterId}")
    private long datacenterId;

    private SnowflakeIdGenerator snowflakeIdGenerator;

    @Override
    public Map<String, Object> exec(String method, String param) {
        Map<String,Object> obj = new HashMap<>();
        snowflakeIdGenerator = new SnowflakeIdGenerator(this.workerId,this.datacenterId);
        switch (method){
            case ConstantUtil.ADD_STAUTS_POINT: //getNotes;
                obj = addStatusPoint(param);
                break;
            default:
                break;
        }
        return obj;
    }

    //新增登录或者是退出埋点信息
    private Map<String, Object> addStatusPoint(String param) {
        Map<String,Object> result = new HashMap<>();
        StatusPoint statusPoint = JSONObject.parseObject(param, StatusPoint.class);
        statusPoint.setId(SnowflakeIdGenerator.getIdWorker(snowflakeIdGenerator));  //雪花算法赋值id
        statusPoint.setCreateTime(new Date());
        int addResult = statusPointMapper.insert(statusPoint);
        Constants.getSuccMsg(result,addResult>0);
        return result;
    }


}
