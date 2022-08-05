package com.cloud.servicemanage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.commonsmng.entity.appletEntity.Dictionary;
import com.cloud.commonsmng.entity.appletEntity.Notes;
import com.cloud.commonsmng.entity.appletEntity.User;
import com.cloud.commonsmng.entity.appletEntity.WeixinInfo;
import com.cloud.servicemanage.common.PageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 消息的mapper
 * author: tjs
 */
@Mapper
public interface NotesMapper extends BaseMapper<Notes> {

    //获取分页区间的消息信息
    List<Notes> getNotesPageVo(@Param("pageVo") PageVo pageVo);

    //获取总条数
    int getPageTotal(@Param("pageVo") PageVo pageVo);

    //根据编号查询消息信息
    Notes getNotesById(@Param("id") String id);

    //新增通知信息
    int insertNotes(@Param("notes") Notes notes);

    //修改通知消息
    int updateNotes(@Param("notes") Notes notes);

    //获取码表中的消息类型
    List<Dictionary> selectDictionaryByKindEqTaskType();

    //获取已读用户信息
    List<User> getReadUser(@Param("id") String id);

    //获取已发布消息的删除记录
    List<Notes> getDelNotesList(@Param("status")String status);

    Notes getnoteInfobyId(@Param("id")String id);

    //获取notes_detail表中对应用户的emial
    List<String> getNotesDetailEmails(@Param("id")String id);

    int insertNotesDetails(@Param("notes") Notes notes);
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
