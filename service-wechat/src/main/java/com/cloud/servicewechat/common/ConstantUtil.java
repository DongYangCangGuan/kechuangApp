package com.cloud.servicewechat.common;

/**
 * @Author: liu.ly
 * @Date: 2019/3/7 16:15
 * @Description:
 */
public class ConstantUtil {


    public static final String DATE_FORMAT = "yyyy-MM-dd HH24:mi:ss";
    public static final String USERID = "99990000";


    //------------------------------------埋点-------------------------------------------------------------------------
    public static final String UPDATE_LOCATION_AND_ADDRESS = "updateLocationAndAddress";

    public static final String ADD_STAUTS_POINT = "addStatusPoint";      //添加登录/退出埋点信息

    public static final String ADD_ACTION_POINT = "addActionPoint";      //添加行为埋点信息

    public static final String ADD_POINT = "addPoint";      //添加埋点信息

    public static final String  GET_POINT_LIST = "getPointList";


    //-------------------------------反馈建议----------------------------------------------------------------
    public static final String GET_SIGN_LIST = "getSignList";
    public static final String GET_SIGN_DETAIL  = "getSignDetail";

    //-------------------------------我的足迹----------------------------------------------------------------
    public static final String GET_TRACK = "getTrack";


    //-------------------------------私人定制-----------------------------------------------------------------

    public static final String ADD_CUSTOMIZE_ANSWER = "addCustomizeAnswer";

    public static final String GET_QUESTION_LIST = "getQuestionList";


    //--------------------------------我的收藏---------------------------------------------------------------
    //获取全部收藏
    public static final String GET_COLLECT = "getCollect";

    public static final String ADD_COLLECT = "addCollect";

    public static final String DEL_COLLECT = "delCollect";

    public static final String GET_COLLECT_DETAIL  = "getCollectDetail";


    //---------------------------------------用户信息-----------------------------------------------
    //获取用户信息
    public static final String GET_USER = "getUser";

    // 修改用户信息
    public static final String UPDATE_USER = "upDateUser";

    //--------------------------------关于我们---------------------------------------------------------------
    public static final String GET_ABOUTUS = "getAboutus";

    //--------------------------------轮播图--------------------------------------------------------------
    //获取轮播图
    public static final String GET_SLIDER = "getSlider";
    //--------------------------------报告信息--------------------------------------------------------------
    public static final String GET_REPORT_INFO = "getReportInfo";

    public static final String ADD_ARRICLEVIEWS = "addArticleviews";

    //验证权限，获取报告访问路径
    public static final String GET_PATH_AND_VERIFY_PERMISSION = "getPathAndVerifyPermission";

    //--------------------------------消息管理--------------------------------------------------------------
    public static final String GET_NOTES = "getNotes";
    public static final String ADD_NOTES = "addNotes";
    public static final String GET_USER_NOTES_NO_READ_NUMBER = "getUserNotesNoReadNumber";     //根据用户id获取用户未读消息个数

    public static final String UPDATE_STATUS = "updateStatus";      //修改状态
    //--------------------------------通知管理--------------------------------------------------------------
    public static final String GET_NOTIFY = "getNotify";            //获取通知

    //--------------------------------研报-----------------------------------------------------------------
    // 获取研报列表
    public static final String GET_TECH_REPORT_INFO = "getTechReportInfo";

    //-----------------------------------用户认证--------------------------------------------------------------

    public static final String CHECK_MEMBER = "checkMember";        //认证用户

    public static final String MEMBER_AUTHENTICATION = "memberAuthentication";        //根据code和id查询用户信息

    public static final String GET_MEMBERS = "getMembers";
    public static final String  GET_INVESTMENTFUND="getInvestmentFund";


    //-----------------------------------筛选研报--------------------------------------------------------------

    public static final String GET_FILTER_TECH_REPORT_INFO = "getFilterTechReportInfo";

    //-----------------------------------搜索研报--------------------------------------------------------------

    public static final String GET_SEARCH_TECH_REPORT_INFO = "getSearchTechReportInfo";

    public static final String GET_QY = "getQy";

    //------------------------------------字典表---------------------------------------------

    public static final String SELECT_BY_KIND = "selectByKind";

    public static final String SELECT_BY_PARENT = "selectByParent";

    //-----------------------------------私人订制问题信息详情--------------------------------------------------------------
    //根据问题编号查询问题的回答信息
    public static final String GET_CUSTOMIZE_ANSWER_PAGE_VO_BY_QUESTION_ID = "getCustomizeAnswerPageVoByQuestionId";

//-----------------------------------我的（问卷）--------------------------------------------------------------
public static final String GET_ANSWER_LIST = "getAnswerList";

public static final String  GET_ANSWER_DETAIL ="getAnswerDetail";

//-----------------------------------我的（审批）--------------------------------------------------------------
    public static final String GET_APPROVE_LIST ="getApproveList";
    public static final String GET_MEMBER_DETAIL="getMemberDetail";
    public static final String APPROVE ="approve";

    //------------------------------------首页---------------------------------------------
    public static final String GET_BANNER = "getBanner";
    public static final String GET_NEWS = "getNews";
    public static final String GET_ACTIVITY = "getActivity";
    public static final String SIGN_UP = "signUp";
    public static final String GET_MEMBER_NAME = "getMemberName";

    //------------------------------------产品---------------------------------------------
    public static final String ANSWERS_QUESTIONNAIRE = "answersQuestionnaire";
    public static final String GET_QUESTIONNAIRE = "getQuestionnaire";
    public static final String GET_ANSWERS_MSG = "getAnswersMsg";
    public static final String GET_RECOMMEND = "getRecommend";
    public static final String GET_LATEST = "getLatest";
    public static final String GET_HOT = "getHot";
    public static final String GET_CONTACT_US = "getContactUs";
    public static final String ADD_CONSULTING_INFO = "addconsultingInfo";

    //问卷管理列表
    public static final String GET_QUESTIONNAIRE_LIST = "getQuestionnaireList";

    public static final String GET_ANSWER_MEMBER = "getAnswerMember";

    public static final String GET_ANSWER_MEMBER_DETAIL = "getAnswerMemberDetail";
}
