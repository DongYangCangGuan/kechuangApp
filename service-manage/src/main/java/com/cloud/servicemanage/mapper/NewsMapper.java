package com.cloud.servicemanage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.commonsmng.entity.appletEntity.NewsEntity;
import com.cloud.servicemanage.common.PageVo;
import com.cloud.servicemanage.entity.Users;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NewsMapper extends BaseMapper<Users> {


    int  getUserCount(@Param("pageVo") PageVo pageVo);

    List<NewsEntity>  getUserList(@Param("pageVo") PageVo pageVo);

    int  insertUser(NewsEntity news);

    int  deleteUser(String id);

    int  updateUser(NewsEntity news);
}
