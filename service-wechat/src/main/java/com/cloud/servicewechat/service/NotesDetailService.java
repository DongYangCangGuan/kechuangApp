package com.cloud.servicewechat.service;

import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.entity.appletEntity.NotesDetail;
import com.cloud.commonsmng.factory.BaseService;
import com.cloud.servicewechat.common.ConstantUtil;
import com.cloud.servicewechat.common.SnowflakeIdGenerator;
import com.cloud.servicewechat.mapper.NotesDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("notesDetail")
public class NotesDetailService extends BaseService {
    @Autowired
    private NotesDetailMapper notesDetailMapper;

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
            case ConstantUtil.ADD_NOTES: //getNotes;
                obj = addNotes(param);
                break;
            default:
                break;
        }
        return obj;
    }


    /**
     * 添加一条notes
     *
     * @param param
     * @return
     */
    public Map<String,Object> addNotes(String param){
        Map<String,Object> result = new HashMap<>();
        String userId = super.getUserInfo().getId();        //获取当前用户编号
        //转化json格式数据为Notes对象
        NotesDetail notesDetail = JSONObject.parseObject(param, NotesDetail.class);

        notesDetail.setId(SnowflakeIdGenerator.getIdWorker(snowflakeIdGenerator));
        notesDetail.setUserId(userId);
        //新增
        int addResult = notesDetailMapper.addNotes(notesDetail);
        Constants.getSuccMsg(result,addResult);
        return result;
    }

}
