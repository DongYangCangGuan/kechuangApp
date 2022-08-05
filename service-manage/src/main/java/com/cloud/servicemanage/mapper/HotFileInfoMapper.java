package com.cloud.servicemanage.mapper;

import com.cloud.servicemanage.entity.HotFileInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HotFileInfoMapper {
     Integer InsertHotFile(List<HotFileInfo> hotFileInfos);
}
