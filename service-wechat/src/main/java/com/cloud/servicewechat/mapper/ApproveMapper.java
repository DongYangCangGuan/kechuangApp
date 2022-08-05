package com.cloud.servicewechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.commonsmng.entity.BaseRole;
import com.cloud.commonsmng.entity.appletEntity.*;
import com.cloud.servicewechat.common.PageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ApproveMapper extends BaseMapper<Approve> {

    Integer  countApproveList (@Param("pageVo") PageVo pageVo,@Param("role")String role,@Param("memberId")String memberId);

    List<MemberUser> getApproveList(@Param("pageVo") PageVo pageVo,@Param("role")String role,@Param("memberId")String memberId);

    MemberUser getMemberDetail(@Param("memberId")String memberId,@Param("userId")String userId);

    Integer approve (@Param("mu") MemberUser mu);

    List<BaseRole> getUserRoleList (@Param("userId") String userId);

    Integer updateApprovalstatus(@Param("md")MemberUser md);

    MemberUser getMemberDetailById(@Param("id")String id);

}
