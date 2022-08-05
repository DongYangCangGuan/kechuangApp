package com.cloud.servicemanage.common;

/**
 * @Author: liu.ly
 * @Date: 2019/3/7 16:15
 * @Description:
 */
public class ConstantUtil {
    public static final String DATE_FORMAT = "yyyy-MM-dd HH24:mi:ss";
    public static final String USERID = "99990000";


    //------------------------------------我的-------------------------------------------------------------------------
    public static final String GET_CUSTOMIZE_QUESTION_LIST = "getCustomizeQuestionList";

    public static final String ADD_CUSTOMIZE_QUESTION = "addCustomizeQuestion";

    public static final String GET_CUSTOMIZE_QUESTION_BY_ID = "getCustomizeQuestionById";

    public static final String UPDATE_CUSTOMIZE_QUESTION = "updateCustomizeQuestion";

    public static final String GET_CUSTOMIZE_QUESTION_DETAIL_BY_ID = "getCustomizeQuestionDetailById";

    public static final String RELEASE_CUSTOMIZE_QUESTION = "releaseCustomizeQuestion";

    public static final String DELETE_CUSTOMIZE_QUESTION = "deleteCustomizeQuestion";

    public static final String GET_CUSTOMIZE_ANSWER_LIST = "getCustomizeAnswerList";

    public static final String GET_REPLIED_CUSTOMIZE_ANSWER_LIST = "getRepliedCustomizeAnswerList";

    public static final String GET_UN_REPLIED_CUSTOMIZE_ANSWER_LIST = "getUnRepliedCustomizeAnswerList";

    public static final String GET_CUSTOMIZE_ANSWER_DETAIL_BY_ID = "getCustomizeAnswerDetailById";

    public static final String REPLY_CUSTOMIZE_ANSWER = "replyCustomizeAnswer";
    //-----------------------------------菜单-菜单管理------------------------------------------------------------------
    //新增菜单
    public static final String ADD_MENU = "insertMenu";
    //更新菜单
    public static final String UPDATE_MENU = "ModifyMenu";

    public static final String UPDATE_MENUTREE = "modifyMenuTree";
    //获取菜单
    public static final String GET_MENULIST = "getMenuList";

    public static final String GET_MENU_LIST_BY_ID = "getMenuListByID";

    public static final String GET_MENU_INFO_BY_ID = "getMenuInfoByID";

    public static final String GET_MENU_BY_ROLEID = "selectMenuByRoleId";

    public static final String GET_MENU_SECURITYINFO = "getSecurityInfoAll";

    public static final String GET_MENU_BY_PARENTID = "selectMenuByParentId";

    public static final String SAVE_MENU_BY_ROLEID = "saveMenuByRoleId";


    //--------------------------------角色管理-角色基本信息--------------------------------------------------------------
    //获取角色基本信息
    public static final String GET_ROLE_BASE_MSG = "getRole";
    //添加角色基本信息
    public static final String ADD_ROLE_BASE_MSG = "addRole";
    //更新角色基本信息
    public static final String UPDATE_ROLE_BASE_MSG = "updateRole";
    //删除角色基本信息
    public static final String DELETE_ROLE_BASEMSG = "deleteRole";
    //编码是否重复
    public static final String CHECK_ROLE_CODE_BASEMSG = "checkRoleCode";
    //名称是否重复
    public static final String CHECK_ROLE_NAME_BASEMSG = "checkRoleName";
    //判断该角色下是否有人员
    public static final String IS_ROLE_USED = "isRoleUsed";

    //根据角色id查属于该角色的用户信息
//    public static final String SELECT_USER_BY_ID = "selectUserByRoleId";
//
//    public static final String CHECK_USER_PASSWORD = "vaildatorPassword";
//
//    public static final String UPDATE_USER_PASSWORD = "editUserPassword";

    public static final String GET_USER_AND_ROLES = "getUserAndRoles";

    public static final String DEL_USER_ROLES = "delRoleforUser";


    //--------------------------------字典管理-字典基本信息--------------------------------------------------------------
    //获取字典基本信息
    public static final String GET_Dictionary_BASE_MSG = "getDictionaryInfo";
    //添加字典基本信息
    public static final String ADD_Dictionary_BASE_MSG = "addDictionary";
    //更新字典基本信息
    public static final String UPDATE_Dictionary_BASE_MSG = "updateDictionary";
    //删除字典基本信息
    public static final String DELETE_Dictionary_BASEMSG = "deleteDictionary";
    //下拉框选项大类
    public static final String GETKIND_Dictionary_CODE_BASEMSG = "getKindnameInfo";
    //添加大类
    public static final String ADD_classDictionary_BASE_MSG = "addclassDictionary";
    //判断大类名称是否重复
    public static final String SelectNameDictionary_BASE_MSG = "selectName";
    //判断id是否重复
    public static final String SelectIdDictionary_BASE_MSG = "selectId";
    //判断大类code编码是否重复
    public static final String SelectCodeDictionary_BASE_MSG = "selectCode";
    //判断名称是否重复
    public static final String SelectName1Dictionary_BASE_MSG = "selectName1";
    //判断code编码是否重复
    public static final String SelectCode1Dictionary_BASE_MSG = "selectCode1";

    //根据id查询字典信息
    public static final String GET_DICTIONARY_BY_ID = "getDictionaryById";

    //--------------------------------人员管理-人员基本信息--------------------------------------------------------------
    //获取本机构下所有人员基本信息
    public static final String GET_STAFF_BY_ID = "getStaffById";
    //获取本机构下所有人员总数
    public static final String GET_STAFF_SUM = "getStaffSum";
    //添加人员信息
    public static final String INSERT_STAFF = "insertStaff";
    //根据名字获取人员基本信息
    public static final String SELECT_BY_NAME = "selectByName";
    //停用人员
    public static final String DELETE_STAFF_INFO = "deleteStaffInfo";
    //获取角色下拉框
    public static final String SELECT_ROLE = "selectRole";
    //获取学历或者类型下拉框
    public static final String SELECT_EDUCATION_OR_TYPE = "selectEducationOrType";
    //判断人员编号是否存在
    public static final String IS_STAFFID_EXISTED = "isStaffIdExisted";
    //判断人员登录名是否存在
    public static final String IS_LOGINNAME_EXISTED = "isStaffLoginNameExisted";
    //获取人员学历
    public static final String SELECT_USER_EDUCATION = "selectUserEdu";
    //获取人员部门经历
    public static final String SELECT_USER_DEPARTMENT = "selectUserDepart";
    //获取人员角色ID
    public static final String SELECT_USER_ROLE_ID = "selectUserRoleId";
    //获取人员角色NAME
    public static final String SELECT_USER_ROLE_NAME = "selectUserRoleName";
    //更新人员信息
    public static final String UPDATE_STAFF = "updateStaff";
    //人员excel上传之前 校验表格是否为空
    public static final String IMPORT_STAFF_BEFORE = "importStaffBefore";
    //解析Excel并导入的文件信息
    public static final String INSERT_FILE_IMPORT = "insertFileImport";
    //数据回显
    public static final String SHOW_STAFF_DATA = "showStaffData";
    //导入User表
    public static final String INSERT_USER_DATA = "insertUserData";
    //判断临时表ErrorMsg是否为空
    public static final String IS_EMPTY_ERRORMSG = "isEmptyErrorMsg";
    //errorMsg
    public static final String SELECT_ERROR_MSG = "selectErrorMsg";
    //修改密码
    public static final String UPDATE_PASSWORD = "updatePassword";

    //--------------------------------部门管理--------------------------------------------------------------
    //获取全部机构信息
    public static final String GET_DEPT_INFO_LIST = "getDeptInfoList";
    //根据机构Id获取该机构信息
    public static final String GET_DEPT_INFO_BY_DEPTID = "getDeptInfoByDeptId";
    //根据机构Id获取其下级机构列表
    public static final String GET_SUB_DEPTS_BY_DEPTID = "getSubDeptsByDeptId";
    //判断前端传入的部门机构Code是否重复
    public static final String IS_DEPT_CODE_EXISTED = "isDeptCodeExisted";
    //新增机构信息
    public static final String ADD_DEPT_INFO = "addDeptInfo";
    //修改机构信息
    public static final String EDIT_DEPT_INFO = "editDeptInfo";
    //获取下拉框类型的机构树
    public static final String GET_DEPT_TREE_BY_PARENTID = "getDeptTreeByParentId";

    //--------------------------------日志信息--------------------------------------------------------------
    public static final String GET_LOG_INFO_NUM = "getLoginfoNum";
    public static final String GET_LOG_INFO_LIST = "getLoginfoList";
    public static final String GET_LOG_INFO = "getLogInfo";
    public static final String GET_LOG_CHARTS = "getLogCharts";
    public static final String GET_LINE_CHART_COUNT = "getLineChartCount";

    //--------------------------------------问答----------------------------------------------------------------------
    public static final String GET_QUESTION_INFO = "getQuestionInfo";
    public static final String GET_DICTIONARY = "getDictionary";
    public static final String ADD_QUESTION = "addQuestion";
    public static final String BATCH_DEL_QUESTION = "batchDelQuestion";
    public static final String DEL_QUEST = "delQuest";
    public static final String UPDATE_QUESTION = "updateQuestion";
    public static final String UPDATE_ANSWER = "updateAnswer";

    //--------------------------------图片管理--------------------------------------------------------------
    //获取图片基本信息
    public static final String GET_PICTURE_BASE_MSG = "getPictureInfo";
    //添加图片
    public static final String ADD_PICTURE_BASE_MSG = "addPicture";
    //删除图片
    public static final String DELETE_PICTURE_BASE_MSG = "deletePicture";
    //改变图片状态
    public static final String CHANGE_PIC_STATUS = "changePicStatus";
    //取消上传
    public static final String CANCEL_UPLOAD = "cancelUpload";

    //--------------------------------报告管理--------------------------------------------------------------
    //分页获取报告信息
    public static final String GET_REPORT_PAGE_VO = "getReportPageVo";
    //根据编号获取报告信息
    public static final String GET_REPORT_BY_ID = "getReportById";



    //新增报告信息
    public static final String INSERT_REPORT = "insertReport";
    //修改报告信息
    public static final String UPDATE_REPORT = "updateReport";
    //删除报告信息
    public static final String DELETE_REPORT = "deleteReport";
    //判断报告编号是否已存在
    public static final String CHECK_REPORT_CODE_BASEMSG = "checkReportCode";

    public static final String SESECT_BY_KIND = "selectByKind";

    public static final String SELECT_BY_PARENT = "selectByParent";

    //根据id获取报告信息
    public static final String GET_REPORT_INFO_BY_ID = "getReportInfoById";

    //--------------------------------消息管理-------------------------------------------------------------
    //分页获取所有的消息信息
    public static final String GET_NOTES_PAGE_VO = "getNotesPageVo";

    //新增消息信息
    public static final String INSERT_NOTES = "insertNotes";

    //修改消息信息
    public static final String UPDATE_NOTES = "updateNotes";

    //删除消息信息
    public static final String DELETE_NOTES = "deleteNotes";

    //根据编号查询消息信息
    public static final String GET_NOTES_BY_ID = "getNotesById";

    //发布消息
    public static final String ISSUE_NOTES = "issueNotes";

    //查询详细的消息信息（包含用户信息）
    public static final String SELECT_NOTES_DETAIL_BY_NOTES_ID = "selectNotesDetailByNotesId";

    //获取码表中的消息类型
    public static final String SELECT_DICTIONARY_BY_KIND_EQ_TASK_TYPE = "selectDictionaryByKindEqTaskType";

    //获取所有消息
    public static final String GET_READ_USER = "getReadUser";

    //获取已发布消息的删除记录
    public static final String GET_DEL_NOTES = "getDelNotesList";

    //--------------------------------用户管理-------------------------------------------------------------
    //分页获取用户信息
    public static final String GET_USER_INFO_PAGE_VO = "getUserInfoPageVo";

    //根据用户编号批量查询用户信息
    public static final String GET_USER_INFO_BATCH_BY_ID = "getUserInfoBatchById";

    //分页查询待审核的用户信息（客户）
    public static final String GET_USER_INFO_BY_CERTIFICATION_MARK_PAGE_VO = "getUserInfoByCertificationMarkPageVo";

    //分页查询小程序端的用户信息
    public static final String GET_USER_BY_UROLE_EQ_ONE_PAGE_VO = "getUserByUroleEqOnePageVo";

    //--------------------------------会员管理-------------------------------------------------------------
    //分页获取会员的信息
    public static final String GET_MEMBER_INFO_PAGE_VO = "getMemberInfoPageVo";

    //新增会员信息
    public static final String INSERT_MEMBER = "insertMember";

    //修改会员信息
    public static final String UPDATE_MEMBER = "updateMember";

    //删除会员信息
    public static final String DELETE_MEMBER = "deleteMember";

    //会员审核未通过
    public static final String MEMBER_REVIEW_FAILED = "memberReviewFaild";

    //会员审核通过
    public static final String MEMBER_REVIEW_SUCCESS = "memberReviewSuccess";

    //查询会员套餐的代码和名称信息
    public static final String SELECT_COMBO_OF_CODE_AND_NAME_LIST = "selectComboOfCodeAndNameList";

    //查询客户经理相关信息
    public static final String SELECT_USER_BY_UROLE_EQ_ZERO_LIST = "selectUserByUroleEqZeroList";

    //根据编号查询会员相关信息
    public static final String GET_MEMBER_BY_ID = "getMemberById";

    //根据会员编号查询会员下属用户信息
    public static final String SELECT_USER_BY_MEMBER_ID_PAGE_VO = "selectUserByMemberIdPageVo";

    //查询最高级机构的代码和名称信息
    public static final String SELECT_DEPARTMENT_OF_CODE_AND_NAME_LIST = "selectDepartmentOfCodeAndNameList";

    //查询机构的代码和名称信息(包含客户经理的用户信息在其机构子节点下)
    public static final String SELECT_DEPARTMENT_AND_USER_OF_CODE_AND_NAME_LIST = "selectDepartmentAndUserOfCodeAndNameList";

    //根据最高级机构编号查询出该机构下属用户的信息（包含子机构的用户信息)
    public static final String SELECT_USER_BY_DEPARTMENT_ID_AND_UROLE_EQ_ZERO_LIST = "selectUserByDepartmentIdAndUroleEqZeroList";

    //获取小程序端的全部用户信息
    public static final String SELECT_USER_BY_UROLE_EQ_ONE_LIST = "selectUserByUroleEqOneList";

    //获取报告的属性信息
    public static final String SELECT_DICTIONARY_BY_PROPERTY_LIST = "selectDictionaryByPropertyList";

    //批量导入会员信息
    public static final String BATCH_IMPORT_MEMBER = "batchImportMember";

    //判断会员名称是否存在
    public static final String GET_MEMBER_BY_ENTERPRISE_NAME_CHECK = "getMemberByEnterpriseNameCheck";

    //判断企业代码是否存在
    public static final String GET_MEMBER_BY_ENTERPRISE_CODE_CHECK = "getMemberByEnterpriseCodeCheck";

    //获取认证审核list
    public static final String GET_MEMBER_DETAIL_LIST = "getmemberDetailList";

    //获取认证审批明细
    public static final String GET_MEMBER_DETAIL = "getMemberDetailById";

    //更新认证审批状态
    public static final String UPDATE_APPROVAL_STATUS = "updateApprovalStatus";

    //获取会员类型list
    public static final String GET_MEMBER_TYPES = "getMemberTypeList";

    //获取会员类型list
    public static final String GET_MEMBER_TYPES2 = "getMemberTypeList2";

    //获取字典表
    public static final String GET_DICTIONARY_LIST = "getdictionarylist";

    //根据会员类型获取会员  id和name
    public static final String GET_MEMBERS_BY_TYPE = "getMemberByType";

    //获取未审批数量
    public static final String GET_UNAPPROVED_COUNT = "getUnApprovedCount";
    //删除用户认证
    public static final String DEL_MEMBERUSER = "delMemberUserById";
    //更新认证信息
    public static final String UPDATE_MEMBERUSER = "updateMemberUser";
    //删除记录memberuser
    public static final String GET_DEL_MEMBERUSERS = "getDelMemberUsers";
    //获取GP公司
    public static final String GET_GP_MEMBERS = "getGPMemberlist";
    //获取部门
    public static final String GET_GY_LIST = "getGYList";

    //--------------------------------关于我们---------------------------------------------------------------
    //查询关于我们
    public static final String GET_ABOUTUS = "getAboutus";

    //添加一条新的关于我们
    public static final String INSERT_ABOUTUS = "insertAboutus";

    //--------------------------------反馈意见---------------------------------------------------------------
    //查询 反馈建议
    public static final String GET_FEEDBACK_PAGE_VO  = "getFeedbackPageVo";

    //查看建议
    public static final String GET_FEEDBACK_DETAIL_BY_ID = "getFeedbackDetailById";

    //删除反馈建议
    public static final String DELETE_FEEDBACK="deleteFeedback";

    //--------------------------------首页---------------------------------------------------------------
    //获取运营数据
    public static final String GET_OPERATIONAL_DATA = "getOperationalData";

    //根据时间查询每个月后每天的用户数和会员单位
    public static final String SELECT_USER_AND_MEMBER_MONTH_OR_DAY_NUM_BY_DATE = "selectUserAndMemberMonthOrDayNumByDate";

    //--------------------------------轮播图管理---------------------------------------------------------------
    //分页获取轮播图信息
    public static final String GET_SLIDER_PAGE_VO = "getSliderPageVo";

    //根据编号查询轮播图信息
    public static final String GET_SLIDER_BY_ID = "getSliderById";

    //新增轮播图信息
    public static final String INSERT_SLIDER = "insertSlider";

    //修改轮播图信息
    public static final String UPDATE_SLIDER = "updateSlider";

    //删除轮播图信息
    public static final String DELETE_SLIDER = "deleteSlider";

    //判断优先级是否存在
    public static final String CHECK_SLIDER_BY_INDEX = "checkSliderByIndex";
    //------------------------------产品管理-------------------------------------------------------------------------
    //新增产品信息
    public static final String  INSERT_PRODUCT = "insertproduct";

    //获取产品list
    public static final String GET_PRODUCTS = "getproduces";

    //获取产品类型list
    public static final String GET_PRODCUTS_TYPES = "getProductTypes";

    //根据产品Id获取合作模式信息
    public static final String GET_COOPERATION_MODE_BY_PRODUCTID = "getCooperationbyProductId";

    //根据产品Id删除产品
    public static final String DELETE_PRODUCT_BYID = "deleteProductById";

    //根据产品Id发布产品
    public static final String RELEASE_PRODUCT_BYID = "releaseProductById";


    //修改合作信息
    public static final String UPDATE_COOPERATION_MODE = "updatecooperationmode";

    //添加合作信息
    public static final String ADD_COOPERATION_MODE = "addcooperationmode";

    //更新产品信息
    public static final String UPDATE_PRODUCT = "updateproduct";

    //获取产品图片list
    public static final String GET_PRODUCT_PICTURES = "getProductPictures";

    //根据id获取产品图片
    public static final String GET_PRODUCT_BYID = "getProductById";

    //判断是否有发布权限
    public static final String GET_PRODUCT_PERMISSION = "getProductPermission";
    //——————————————————————活动管理————————————————————

    //活动管理列表
    public static final String GET_ACTIVITY_LIST = "getActivityList";

    public static final String GET_SIGN_LIST= "getSignList";

    //活动发布
    public static final String SET_ACTIVITY = "setActivity";

    //问卷管理列表
    public static final String GET_QUESTIONNAIRE_LIST = "getQuestionnaireList";

    //新增问卷
    public static final String SET_QUESTIONNAIRE = "setQuestionnaire";

    //删出问卷
    public static final String DELETE_QUESTIONNAIRE = "deleteQuestionnaire";

    //删除活动
    public static final String DELETE_ACTIVITY= "deleteActivity";

    //修改活动信息
    public static final String UPDATE_ACTIVITY = "updateActivity";

    public static final String UPDATE_QUESTIONNAIRE= "updateQuestionnaire";

    public static final String PRODUCT_DOWNLOAD = "ActivityLoad";

    public static final String GET_ANSWER_MEMBER = "getAnswerMember";

    public static final String GET_ANSWER_DETAIL = "getAnswerDetail";




    //数据统计
    public static final String GET_ACTIVE_USER = "getActiveUser";

    public static final String GET_NEW_USER = "getNewUser";

    public static final String ACTIVE_TREND = "activeTrend";

    public static final String DAY_AVG_TIME = "dayAvgTime";

    public static final String LOGIN_TIMES = "loginTimes";

    public static final String EACH_AVG_TIME = "eachAvgTime";

    public static final String GET_TIMES = "getTimes";

    public static final String GET_STATISTICS = "getStatistics";

    public static final String GET_PUBLISH = "getPublish";

    public static final String GET_PERSONNEL_DIS = "getPersonnelDis";

    public static final String GET_PRODUCT_DIS = "getProductDis";


    //用户管理
    public static final String GET_USER_LIST = "getUserList";

    public static final String INSERT_USER = "insertUser";

    public static final String UPDATE_USER = "updateUser";

    public static final String DELETE_USER = "deleteUser";

    public static final String EXPORT_USER = "exportUser";

    //新闻管理
    public static final String GET_NEW_LIST = "getNewsList";

    public static final String INSERT_NEW = "insertNews";

    public static final String UPDATE_NEW = "updateNews";

    public static final String DELETE_NEW = "deleteNews";
    //推送
    public static final String PUSH_MASSAGE="pushMessage";

    public static final String DELETE_PUSH="deletePush";

    public static final String WECHATUSERS = "getwechatUserlist";

    public static final String GETPUSHDETAIL = "getPushDetail";

    //咨询信息管理
    public static final String GET_CONSULTING_List = "getConsultingList";

}
