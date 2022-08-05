package com.cloud.servicemanage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.commonsmng.entity.appletEntity.*;
import com.cloud.servicemanage.common.PageVo;
import com.cloud.servicemanage.entity.Department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.hibernate.validator.constraints.ParameterScriptAssert;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface MemberMapper extends BaseMapper<Member> {
    //查询页面总行数
    int getPageTotal(@Param("pageVo") PageVo pageVo);

    //分页查询会员信息
    List<Member> getMemberInfoPageVo(@Param("pageVo") PageVo pageVo);

    //添加会员信息
    int insertMember(@Param("member") Member member);

    //修改会员信息
    int updateMember(@Param("member") Member member);

    //会员审核成功
    // int memberReviewSuccess(@Param("memberDetail") MemberDetail memberDetail);

    //查询会员套餐的代码和名称信息
    List<Dictionary> selectComboOfCodeAndNameList();

    //查询客户经理相关信息
    List<User> selectUserByUroleEqZeroList();

    //根据编号查询会员相关信息
    Member getMemberById(@Param("id") String id);

    int getUserPageTotal(@Param("pageVo") PageVo pageVo);

    //根据会员编号查询会员下属用户信息
    List<User> selectUserByMemberIdPageVo(@Param("pageVo") PageVo pageVo);

    //查询最高级机构的代码和名称信息
    List<Department> selectDepartmentOfCodeAndNameList();

    //根据最高级机构编号查询出该机构下属用户的信息（包含子机构的用户信息)
    List<User> selectUserByDepartmentIdAndUroleEqZeroList(@Param("departmentId") String departmentId);

    //根据编号查询在与该用户在同一个机构的用户信息
    List<User> selectUserOfDepartmentByIdList(@Param("userId") String userId);

    //获取小程序端的全部用户信息
    List<User> selectUserByUroleEqOneList();

    //根据父级编号查询报告的属性信息
    List<Dictionary> selectDictionaryByPropertyParentId(@Param("id") String id);

    //获取报告的属性信息
    List<Dictionary> selectDictionaryByPropertyList();

    //根据字典编号查询字典的相关信息
    List<Dictionary> selectDictionaryById(@Param("dictionaryIdList") List<String> dictionaryIdList);

    //批量导入
    int batchImportMember(@Param("insertList") List insertList, @Param("updateList") List updateList,@Param("updatetype")int updatetype);

    //去重
    int deleteWeight();
    int deleteWeightMemberUser();
    int deleteWeightbyType(@Param("type")String type);

    //根据会员代码查询会员信息
    Member getMemberByEnterpriseCode(@Param("enterpriseCode") String enterpriseCode);

    //根据会员名称查询会员信息
    Member getMemberByEnterpriseName(@Param("enterpriseName") String enterpriseName);

    //根据套餐名称得到套餐信息
    Dictionary selectDictionaryCodeByName(@Param("name") String name);

    //根据code，kind 字典表中内容
    Dictionary selectDictionaryByCode(@Param("code") String code, @Param("kind")String kind);

    //根据报告属性名称查询报告属性的相关信息
    List<Dictionary> selectDictionaryByName(@Param("dictionaryNameList") List<String> dictionaryNameList);

    //根据编号和真实姓名查询用户信息
    List<User> selectUserInfoByIdAndRealName(@Param("user") User user);

    //获取相同Gp的Id,投资基金不同
    List<String> getsameGp(@Param("memberId")String memberId);
    //获取用户认证审核总条数
    Integer getMemberDetailPageTotal(@Param("pageVo") PageVo pageVo);

    //获取用户认证审核list
    List<MemberUser> getMemberDetails(@Param("pageVo") PageVo pageVo);

    //根据Id获取审批明细
    MemberUser getMemberDetailById(@Param("id")String id);

    //更新审批状态
    Integer updateApprovalstatus(@Param("md")MemberUser md);

    //获取审批状态
    String getMemberDetailstatus(@Param("md")MemberUser md);


    //获取会员类型list
    List<Dictionary> getMemberTypelist(@Param("kind")String kind);

    //根据会员类型获取会员list   IdAndName
    List<Member> getMemberByType(@Param("memberType") String memberType);
    //获取GP的投资基金
    List<Member> getinvestmentFundlist(@Param("memberType") String memberType);

    //根据kind和name获取字典表
    Dictionary getDictionaryCodeByName(@Param("kind")String kind, @Param("name")String name);
    //新增通知信息
    int insertNotes(@Param("notes") Notes notes);

    //更新审批状态
    Integer batchupdateApprovalstatus(@Param("updatelist")List<MemberUser> memberUsers);

    //新增notes_details
    int insertNotesDetails(@Param("notesDetails")List<NotesDetail> notesDetails);

    //获取notes
    int getNotesCountById(@Param("id")String id);

    //获取未审批的总数
    int getUnApprovedCount(@Param("approvalstatus") String approvalstatus);

    //获取会员基金
    String getInvestmentFundByUser(@Param("userId") String userId);
    String getmemberIdByUser(@Param("userId") String userId);

    List<Member> exportMemberInfo(@Param("pageVo") PageVo pageVo);

    int selectmember(@Param("uRole")String uROle,@Param("memberId")String memberId);
    //更新member_user表，和 user
    int updateMemberUser(@Param("memberuser")MemberUser memberUser);

    //更新申请相关信息 member_user表，和 user 不更新角色
    int updateMemberUsernoUrole(@Param("memberuser")MemberUser memberUser);
    //更新申请相关信息 member_user表，和 user 不更新角色
    int updateMemberUsernoUrole2(@Param("memberuser")MemberUser memberUser);

    List<MemberUser> getDelMemberUsers(@Param("pageVo") PageVo pageVo);
    //删除memberuser
    int delMemberuserById(@Param("mu")MemberUser mu);

    //更新user表uRole
    int updateUseruRole(@Param("id")String userId);

    //获取导出数据的List集合
    List<MemberUserExport>  filterQuery(@Param("memberId")String memberId);

    //获取犀牛标签
    String[] getDictionaryLabels(@Param("kind")String kind,@Param("labels")String[] labels);

    //获取犀牛标签
    String[] getDictionaryLabelsbyCode(@Param("kind")String kind,@Param("labels")String[] labels);

    //测试 添加犀牛标签
    int insertDictionary(@Param("id")String id,@Param("name")String name,@Param("code")String code,@Param("index")String index);
    //根据投资基金获取Gpid
    String getGPIdbyFund(@Param("investmentFund")String investmentFund,@Param("memberType")String memberType);
    //根据Id获取投资基金
    String getFundbyId(@Param("id")String id);
    List<Dictionary> getDictionarysMap(@Param("kind")String kind);
    //批量导入部门
    int batchImportDepartment(@Param("insertList") List<Department> insertList);
    //获取部门byId
    List<Department> getDepartmentById(@Param("id")String id);
    //更新部门
    int updateDepartment(@Param("name")String name,@Param("code")String code,@Param("id")String id);
    //获取部门lIST
    List<Department> getDeptList(@Param("memberType")String memberType);

    //获取邮箱
    List<Dictionary> getEmail(@Param("kind")String kind);
    MemberUser getMemberDetailById2(@Param("id")String id);
    //人员所属机构及类型
    User getUserInfo(@Param("id")String id);
    int updateUserCherk(@Param("id")String id);
}
