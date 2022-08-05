package com.cloud.servicewechat.service;

import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.entity.appletEntity.Notes;
import com.cloud.commonsmng.entity.appletEntity.User;
import com.cloud.commonsmng.factory.BaseService;
import com.cloud.servicewechat.common.ConstantUtil;
import com.cloud.servicewechat.common.PageUtil;
import com.cloud.servicewechat.common.PageVo;
import com.cloud.servicewechat.mapper.NotesMapper;
import com.cloud.servicewechat.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("notes")
public class NotesService extends BaseService {
    @Autowired
    private NotesMapper notesMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Map<String, Object> exec(String method, String param) {
        Map<String,Object> obj = new HashMap<>();
        switch (method){
            case ConstantUtil.GET_NOTES: //getNotes;
                obj = getNotes(param);
                break;
            case ConstantUtil.UPDATE_STATUS: //updateStatus;
                obj = updateStatus(param);
                break;
            default:
                break;
        }
        return obj;
    }

    /**
     * 拿到当前登录的人员的消息列表
     * @param param
     * @return
     */
    private Map<String, Object> getNotes(String param){
        Map<String,Object> result = new HashMap<>();
        PageVo pageVo = JSONObject.parseObject(param, PageVo.class);//把前端传来的参数分装成分页类
        PageUtil pageUtil = pageVo.getPage();
        User user=userMapper.getuRolebyId(super.getUserInfo().getId());
        pageVo.setUserId(this.getUserInfo().getId());
        pageVo.setuRole(user.getURole());
        int totalNum = notesMapper.countNoteList(pageVo);
        if (totalNum > 0) {
          List<Notes> notes = notesMapper.getNoteList(pageVo);
          PageVo<Notes> pageVo1 = new PageVo<>();
          pageVo1.setPage(new PageUtil(pageUtil.getPageIndex(), pageUtil.getPageSize(), totalNum));
          pageVo1.setDataList(notes);
          Constants.getSuccMsg(result, pageVo1);
        }
        return result;
    }

    //根据id修改消息状态
    private Map<String, Object> updateStatus(String param){
        Map<String,Object> result = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(param);
        String notesId = jsonObject.getString("notesId");
        //修改消息状态
        int updateStatus = notesMapper.updateStatus(notesId);
        Constants.getSuccMsg(result,updateStatus);
        return result;
    }
}
