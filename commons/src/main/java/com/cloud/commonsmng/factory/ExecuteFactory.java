package com.cloud.commonsmng.factory;

import com.cloud.commonsmng.constants.util.snowflakeId.SnowflakeIdGenerator;
import com.cloud.commonsmng.entity.BaseUserInfo;

public class ExecuteFactory {
    //工厂方法，拿到service执行
    public static ExecuteService getExecuteService(String type, BaseUserInfo usr, SnowflakeIdGenerator snowflakeIdGenerator) {

        BaseService o = (BaseService) ApplicationContextHolder.getBean(type);
        o.setUserInfo(usr);
        o.setSnowflakeIdGenerator(snowflakeIdGenerator);
        return (ExecuteService) o;
    }
}
