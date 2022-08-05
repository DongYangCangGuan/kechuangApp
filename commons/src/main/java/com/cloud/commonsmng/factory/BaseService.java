package com.cloud.commonsmng.factory;

import com.cloud.commonsmng.constants.util.snowflakeId.SnowflakeIdGenerator;
import com.cloud.commonsmng.entity.BaseEntity;
import com.cloud.commonsmng.entity.BaseUserInfo;


import java.util.Date;
import java.util.Map;

public class BaseService implements ExecuteService {

    @Override
    public Map<String, Object> exec(String method, String param) throws Exception {
        return null;
    }

    protected BaseUserInfo usr;

    public void setUserInfo(BaseUserInfo user) {
//        if (this.usr == null || (user != null && this.usr.getId() != user.getId())) {
//            this.usr = user;
//        }
        this.usr = user;
    }

    public BaseUserInfo getUserInfo() {
        return usr;
    }

    //2021-5-18  tjs add//
    //雪花算法
    protected SnowflakeIdGenerator snowflakeIdGenerator;

    public void setSnowflakeIdGenerator(SnowflakeIdGenerator snowflakeIdGenerator) {
        this.snowflakeIdGenerator = snowflakeIdGenerator;
    }

    public SnowflakeIdGenerator getSnowflakeIdGenerator() {
        return snowflakeIdGenerator;
    }

    public String getIdWorker() {
        return SnowflakeIdGenerator.getIdWorker(this.snowflakeIdGenerator);
    }
    //2021-5-18 tjs add//

    /*
     * 通用代码 --> 新增赋值操作
     * baseEntity 修改的实体
     * author: tjs
     */
    public void insertBaseInfo(BaseEntity baseEntity) {

        baseEntity.setId(this.getIdWorker());
        Date date = new Date();
        baseEntity.setCreateTime(date);
        baseEntity.setModifyTime(date);
        if (this.getUserInfo() != null) {
            String userId = this.getUserInfo().getId();
            baseEntity.setCreatorId(userId);
            baseEntity.setModifierId(userId);
        }
    }

    /*
     * 通用代码 --> 修改赋值操作
     * baseEntity 修改的实体
     * updateId 修改的编号
     * author: tjs
     */
    public void updateBaseInfo(BaseEntity baseEntity, String updateId) {
        if (updateId != null && !"".equals(updateId))
            baseEntity.setId(updateId);
        if (this.getUserInfo() != null)
            baseEntity.setModifierId(this.getUserInfo().getId());
        baseEntity.setModifyTime(new Date());
    }
}
