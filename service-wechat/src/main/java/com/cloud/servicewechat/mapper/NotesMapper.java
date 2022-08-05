package com.cloud.servicewechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.commonsmng.entity.appletEntity.Notes;
import com.cloud.commonsmng.entity.appletEntity.User;
import com.cloud.commonsmng.entity.appletEntity.WeixinInfo;
import com.cloud.servicewechat.common.PageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface NotesMapper extends BaseMapper<Notes> {

    int countNoteList (@Param("pageVo")PageVo pageVo);
    //根据用户id和状态查询消息
    List<Notes> getNoteList(@Param("pageVo")PageVo pageVo);

    //根据id修改消息状态
    int updateStatus(@Param("notesId") String notesId);

    //获取user表中对应用户的emial
    List<String> getUserEmails(@Param("userlist")List<String> userlist);

    //根据Id查消息
    List<Notes> getNoteById(@Param("id")String id);
    //插入消息004
    int insertNote(@Param("notes")Notes notes);


    List<User> getUserUnionId(@Param("userIds")List<String> userIds);

    List<String> getNewOpenId();

    List<WeixinInfo> getUnionidempty();

    String getRealName(@Param("userId")String userId);
}
