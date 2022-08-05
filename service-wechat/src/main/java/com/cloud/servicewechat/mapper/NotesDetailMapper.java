package com.cloud.servicewechat.mapper;

import com.cloud.commonsmng.entity.appletEntity.NotesDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface NotesDetailMapper {
    int addNotes(@Param("notesDetail") NotesDetail notesDetail);
}
