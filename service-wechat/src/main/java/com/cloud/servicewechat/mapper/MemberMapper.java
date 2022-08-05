package com.cloud.servicewechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.commonsmng.entity.appletEntity.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import redis.clients.jedis.BinaryClient;

import java.util.List;

@Mapper
public interface MemberMapper extends BaseMapper<Member> {

    Member getMemberBycodeAndPWD(@Param("enterpriseCode") String enterpriseCode);

    Member checkCodeAndPWD(@Param("member")Member member);

   List<Member> getMembers(@Param("memberType") String memberType,@Param("enterpriseName") String enterpriseName);

    List<Member> getInvestmentFund();

    //根据code，kind 字典表中内容
    Dictionary selectDictionaryByCode(@Param("code") String code, @Param("kind")String kind);

    int insertNotes(@Param("notes") Notes notes);

    //新增notes_details
    int insertNotesDetails(@Param("notesDetails")List<NotesDetail> notesDetails);

     int  addInvest(@Param("enterpriseCode")String enterpriseCode,@Param("enterpriseName")String enterpriseName);

    String getInvestmentFundByUser(@Param("userId") String userId);
    String getmemberIdByUser(@Param("userId") String userId);
    int deleteWeightMemberUser();

    List<String> getuserIds(@Param("urole")String urole, @Param("code")String code);

    List<String> getuserIdbyfun(@Param("uRole")String uRole,@Param("memberId")String memberId,@Param("job")String job);

    //获取邮箱
    List<Dictionary> getEmail(@Param("kind")String kind);

 //获取会员类型list
 List<Dictionary> getMemberTypelist(@Param("kind")String kind);

 //获取相同Gp的Id,投资基金不同
 List<String> getsameGp(@Param("memberId")String memberId);
 MemberUser getMemberDetailById2(@Param("id")String id);
 //人员所属机构及类型
 User getUserInfo(@Param("id")String id);
}
