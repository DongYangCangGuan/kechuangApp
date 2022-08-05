package com.cloud.servicewechat.service;

import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.entity.appletEntity.Setting;
import com.cloud.commonsmng.factory.BaseService;
import com.cloud.servicewechat.common.SnowflakeIdGenerator;
import com.cloud.servicewechat.mapper.SettingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("setting")
public class SettingService extends BaseService {

    @Autowired
    private SettingMapper settingMapper;

    private SnowflakeIdGenerator snowflakeIdGenerator;

    @Override
    public Map<String, Object> exec(String method, String param) {
        Map<String, Object> obj = new HashMap<>();
        switch (method) {
//            case ConstantUtil.ADD_SETTING: // addSetting
//                obj = addSetting(param);
//                break;
        }
        return obj;
    }

    private Map<String, Object> addSetting(String param) {
        Map<String, Object> result = new HashMap<>();
        String userId = super.getUserInfo().getId();
        Setting setting = JSONObject.parseObject(param, Setting.class);
        setting.setId(SnowflakeIdGenerator.getIdWorker(snowflakeIdGenerator));
        setting.setUserId(userId);
        int addResult = settingMapper.insert(setting);
        Constants.getSuccMsg(result, addResult);
        return result;
    }
}
