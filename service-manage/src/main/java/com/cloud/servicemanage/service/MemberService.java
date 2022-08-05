package com.cloud.servicemanage.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.entity.BaseUserInfo;
import com.cloud.commonsmng.entity.appletEntity.*;
import com.cloud.commonsmng.entity.appletEntity.Dictionary;
import com.cloud.commonsmng.factory.BaseService;
import com.cloud.servicemanage.common.*;
import com.cloud.servicemanage.entity.Department;
import com.cloud.servicemanage.entity.PushEntity;
import com.cloud.servicemanage.mapper.*;
import com.cloud.servicemanage.utils.SendMailThread;
import com.cloud.servicemanage.utils.SendWechatThread;
import com.cloud.servicemanage.utils.sendWechat;
import com.cloud.servicemanage.utils.wechatpushUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.google.gson.JsonArray;
import com.sun.corba.se.spi.ior.ObjectKey;
import io.swagger.models.auth.In;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Null;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 会员管理的业务逻辑代码
 * author: tjs
 */
@Service("Member")
@RequestMapping("/manage/manage/Member")
public class MemberService extends BaseService {
    @Value("${wx.mp.configs.appId}")
    private String appId;
    @Value("${wx.mp.configs.secret}")
    private String secret;

    @Autowired
    private NotesMapper notesMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    private PushMapper pushMapper;
    @Autowired
    private sendWechat sendwechat;



    //实例化日志对象，引用jar包org.slf4j.Logger
    private static final Logger logger = LoggerFactory.getLogger(MemberService.class);

    @Override
    public Map<String, Object> exec(String method, String param) {
        Map<String, Object> obj = new HashMap<>();
        switch (method) {
            case ConstantUtil.GET_MEMBER_INFO_PAGE_VO: //getMemberInfoPageVo
                obj = getMemberInfoPageVo(param);
                break;
            case ConstantUtil.INSERT_MEMBER: //insertMember
                obj = insertMember(param);
                break;
            case ConstantUtil.UPDATE_MEMBER:  //updateMember
                obj = updateMember(param);
                break;
            case ConstantUtil.DELETE_MEMBER: //deleteMember
                obj = deleteMember(param);
                break;
            case ConstantUtil.MEMBER_REVIEW_FAILED: //memberReviewFaild
                obj = memberReviewFaild(param);
                break;
           /* case ConstantUtil.MEMBER_REVIEW_SUCCESS: //memberReviewSuccess
                obj = memberReviewSuccess(param);
                break;*/
            case ConstantUtil.SELECT_COMBO_OF_CODE_AND_NAME_LIST: //selectComboOfCodeAndNameList
                obj = selectComboOfCodeAndNameList(param);
                break;
            case ConstantUtil.SELECT_USER_BY_UROLE_EQ_ZERO_LIST: //selectUserByUroleEqZeroList
                obj = selectUserByUroleEqZeroList(param);
                break;
            case ConstantUtil.GET_MEMBER_BY_ID: //getMemberById
                obj = getMemberById(param);
                break;
            case ConstantUtil.SELECT_USER_BY_MEMBER_ID_PAGE_VO: //selectUserByMemberIdPageVo
                obj = selectUserByMemberIdPageVo(param);
                break;
            case ConstantUtil.SELECT_DEPARTMENT_OF_CODE_AND_NAME_LIST: // selectDepartmentOfCodeAndNameList
                obj = selectDepartmentOfCodeAndNameList(param);
                break;
            case ConstantUtil.SELECT_DEPARTMENT_AND_USER_OF_CODE_AND_NAME_LIST:   //selectDepartmentAndUserOfCodeAndNameList
                obj = selectDepartmentAndUserOfCodeAndNameList(param);
                break;
            case ConstantUtil.SELECT_USER_BY_DEPARTMENT_ID_AND_UROLE_EQ_ZERO_LIST:  //selectUserByDepartmentIdAndUroleEqZeroList
                obj = selectUserByDepartmentIdAndUroleEqZeroList(param);
                break;
            case ConstantUtil.SELECT_USER_BY_UROLE_EQ_ONE_LIST: //selectUserByUroleEqOneList
                obj = selectUserByUroleEqOneList(param);
                break;
            case ConstantUtil.SELECT_DICTIONARY_BY_PROPERTY_LIST:   //selectDictionaryByPropertyList
                obj = selectDictionaryByPropertyList(param);
                break;
            case ConstantUtil.BATCH_IMPORT_MEMBER: //batchImportMember
                obj = batchImportMember(param);
                break;
            case ConstantUtil.GET_MEMBER_BY_ENTERPRISE_NAME_CHECK:
                obj = getMemberByEnterpriseNameCheck(param);
                break;
            case ConstantUtil.GET_MEMBER_BY_ENTERPRISE_CODE_CHECK:
                obj = getMemberByEnterpriseCodeCheck(param);
                break;
            case ConstantUtil.GET_MEMBER_DETAIL_LIST:
                obj = getmemberDetailList(param);
                break;
            case ConstantUtil.GET_MEMBER_DETAIL:
                obj = getMemberDetailById(param);
                break;
            case ConstantUtil.UPDATE_APPROVAL_STATUS:
                obj = updateMemberDetailApprovalstatus(param);
                break;
            case ConstantUtil.GET_MEMBER_TYPES:
                obj = getMemberTypeList();
                break;
            case ConstantUtil.GET_MEMBER_TYPES2:
                obj = getMemberTypeList2();
                break;
            case ConstantUtil.GET_DICTIONARY_LIST:
                obj = getDictionaryList(param);
                break;
            case ConstantUtil.GET_MEMBERS_BY_TYPE:
                obj = getMemberByType(param);
                break;
            case ConstantUtil.GET_UNAPPROVED_COUNT:
                obj = getUnApprovedCount(param);
                break;
            case ConstantUtil.DEL_MEMBERUSER:
                obj = delMemberUserById(param);
                break;
            case ConstantUtil.UPDATE_MEMBERUSER:
                obj = updateMemberUser(param);
                break;
            case ConstantUtil.GET_DEL_MEMBERUSERS:
                obj = getDelMemberUsers();
                break;
            case ConstantUtil.GET_GP_MEMBERS:
                obj = getGPMemberlist();
                break;
            case ConstantUtil.GET_GY_LIST:
                obj = getGYList();
                break;
            default:
                break;
        }
        return obj;
    }

    //判断企业代码是否存在
    private Map<String, Object> getMemberByEnterpriseCodeCheck(String param) {
        Map<String, Object> result = new HashMap<>();
        String enterpriseCode = JSONObject.parseObject(param).getString("enterpriseCode");
        Member memberByEnterpriseCode = memberMapper.getMemberByEnterpriseCode(enterpriseCode);
        boolean arr = memberByEnterpriseCode == null ? false : true;
        Constants.getSuccMsg(result, arr);
        return result;
    }

    //判断会员名称是否存在
    private Map<String, Object> getMemberByEnterpriseNameCheck(String param) {
        Map<String, Object> result = new HashMap<>();
        String enterpriseName = JSONObject.parseObject(param).getString("enterpriseName");
        Member memberByEnterpriseName = memberMapper.getMemberByEnterpriseName(enterpriseName);
        boolean arr = memberByEnterpriseName == null ? false : true;
        Constants.getSuccMsg(result, arr);
        return result;
    }


    //批量导入会员信息
    public Map<String, Object> batchImportMember(String param) {
        Map<String, Object> result = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(param);
        Map map = new HashMap();
        String filepath = jsonObject.getString("filePath");
        //String filepath = "D:\\data\\uploadedFile\\创业公司.xlsx";
        if (filepath.endsWith(".xlsx") || filepath.endsWith(".xls")) {
            Workbook workbook = null;
            Sheet sheet = null;
            try {
                //获取文件输入流
                File in = new File(filepath);
                //获取excel工作簿对象
                if (filepath.endsWith("xlsx")) {
                    workbook = new XSSFWorkbook(new FileInputStream(in));
                } else if (filepath.endsWith("xls")) {
                    workbook = new HSSFWorkbook(new FileInputStream(in));
                }

                sheet = workbook.getSheetAt(0);
                //根据kind为membertype获取会员类型
                //List<Dictionary> d = memberMapper.getMemberTypelist("membertype");
                //获取第1行第一列，会员类型
                Row row = sheet.getRow(0);
                if(row.getCell(0) != null){
                    row.getCell(0).setCellType(CellType.STRING);

                    if (row.getCell(0).getStringCellValue().isEmpty()){
                        map.put("code", "207");
                        map.put("msg", "第一行会员类型不可为空");
                        Constants.getSuccMsg(result, map);
                        return result;
                    }
                    String name = row.getCell(0).getStringCellValue();
                    Dictionary membertype = memberMapper.getDictionaryCodeByName("membertype",name);
                    if(membertype == null || membertype.equals("")){
                        map.put("code", "207");
                        map.put("msg", "第一行会员类型不匹配");
                        Constants.getSuccMsg(result, map);
                        return result;
                    }
                    if( name.equals("科创")) { //1
                          map = batchKechuang(sheet,membertype.getCode());
                        Constants.getSuccMsg(result, map);

                       }else if(name.equals("GP")){ //2
                        map = batchGP(sheet,membertype.getCode());
                        Constants.getSuccMsg(result, map);
                       }
                       else if(name.equals("创业公司")){ //3
                        map = batchchuangye(sheet,membertype.getCode());
                       // map = batchlabel(sheet);
                        Constants.getSuccMsg(result, map);
                       }else if(name.equals("LP")){//4
                        map = batchLP(sheet,membertype.getCode());
                        Constants.getSuccMsg(result, map);
                       }else if(name.equals("供应商")){//5
                        map = batchgongyingshang(sheet,membertype.getCode());
                        Constants.getSuccMsg(result, map);
                        }
                       else{
                           map.put("code", "300");
                           map.put("msg", "仅支持第一行会员类型为科创、GP、创业公司、LP、供应商");
                           Constants.getSuccMsg(result, map);
                           return result;
                       }

                }else{
                    map.put("code", "207");
                    map.put("msg", "第一行会员类型不可为空");
                    Constants.getSuccMsg(result, map);
                    return result;
                }



            } catch (Exception e) {
                map.put("code", "500");
                map.put("msg", "导入失败");
                Constants.getSuccMsg(result, map);
                e.printStackTrace();
                logger.error(e.getMessage());
            } finally {
                try {
                    workbook.close();
                } catch (IOException e) {
                    map.put("code", 500);
                    map.put("msg", "Excel文件读取失败");
                    Constants.getErrMsg(result, map);
                    logger.error(e.getMessage());
                }
            }
        } else {
            map.put("code", "203");
            map.put("msg", "只支持xlsx和xls结尾的文件");
            Constants.getSuccMsg(result, map);
        }
        return result;
    }

  /*  @Transactional
    public Map<String,Object> batchlabel(Sheet sheet){
        Map<String,Object> map = new HashMap<>();
        int id = 115;
        int code = 41;
        int index = 41;
        //物理总行数
        int rowTotal = sheet.getPhysicalNumberOfRows();
        //判断表格非空
        if (rowTotal <= 2) {
            map.put("code", "201");
            map.put("msg", "表格为空");
            // Constants.getSuccMsg(result, map);
            return map;
        }
        //获取最后一行
        int lastRowNum = sheet.getLastRowNum() + 1;
        //判断是否有空行
        if (rowTotal != lastRowNum) {
            map.put("code", "202");
            map.put("msg", "表格有空行");
            // Constants.getSuccMsg(result, map);
            return map;
        }
        //
        //解析
        Member member = null;
        for (int i = 2; i < lastRowNum; i++) {
            member = new Member();
            Row row = sheet.getRow(i);



                if (row.getCell(14) != null && !row.getCell(14).getStringCellValue().isEmpty()) {
                    row.getCell(14).setCellType(CellType.STRING);
                    String labelname = row.getCell(14).getStringCellValue();
                    String[] label = labelname.split("，");
                    for(int r =0;r<label.length;r++) {
                        Dictionary rhinocerosLabel = memberMapper.getDictionaryCodeByName("rhinocerosLabel", label[r]);
                        if(rhinocerosLabel == null || rhinocerosLabel.equals("")){
                            id = id+1;
                            code = code + 1;
                            index = index + 1;
                            String id1 = Integer.toString(id);
                            String code1 = Integer.toString(code);
                            String index1 = Integer.toString(index);
                            memberMapper.insertDictionary(id1,label[r],code1,index1);
                        }

                    }

                }


            }



        return map;
    }*/

    /**
     * 科创Excel
     * @param
     * sheet  10列
     * @return
     */
    @Transactional
    public Map<String,Object> batchKechuang(Sheet sheet,String code){
        Map<String,Object> map = new HashMap<>();
        List<Member> insertMemberList = new ArrayList<>();
        List<Member> updateMemberList = new ArrayList<>();
        //物理总行数
        int rowTotal = sheet.getPhysicalNumberOfRows();
        //判断表格非空
        if (rowTotal <= 2) {
            map.put("code", "201");
            map.put("msg", "表格为空");
           // Constants.getSuccMsg(result, map);
            return map;
        }
        //获取最后一行
        int lastRowNum = sheet.getLastRowNum() + 1;
        //判断是否有空行
        if (rowTotal != lastRowNum) {
            map.put("code", "202");
            map.put("msg", "表格有空行");
           // Constants.getSuccMsg(result, map);
            return map;
        }

        //
        //解析
        Member member = null;
        for (int i = 2; i < lastRowNum; i++) {
            member = new Member();
            Row row = sheet.getRow(i);
            if (row.getCell(0) == null || row.getCell(1) == null ||
                    row.getCell(3) == null || row.getCell(4) == null || row.getCell(5) == null ||
                    row.getCell(6) == null || row.getCell(7) == null||
                    row.getCell(8) == null || row.getCell(9) == null) {
                map.put("code", "207");
                map.put("msg", "文件中有空格");
                //Constants.getSuccMsg(result, map);
                return map;
            }

            row.getCell(0).setCellType(CellType.STRING);
            row.getCell(1).setCellType(CellType.STRING);
            row.getCell(2).setCellType(CellType.STRING);
            row.getCell(3).setCellType(CellType.STRING);
            try {
                row.getCell(4).setCellType(CellType.STRING);
            } catch (Exception e) {
                e.printStackTrace();
                map.put("code", "204");
                map.put("msg", "文件第" + i + "行中手机号格式应为文本格式");
                //Constants.getSuccMsg(result, map);
                return map;
            }
            row.getCell(5).setCellType(CellType.STRING);
            row.getCell(6).setCellType(CellType.STRING);
            row.getCell(7).setCellType(CellType.STRING);
            row.getCell(8).setCellType(CellType.STRING);
            try {
                row.getCell(9).setCellType(CellType.STRING);
            } catch (Exception e) {
                e.printStackTrace();
                map.put("code", "204");
                map.put("msg", "文件第" + i + "行中客户经理编号应为文本格式");
                //Constants.getSuccMsg(result, map);
                return map;
            }
            //row.getCell(10).setCellType(CellType.STRING);

            if (row.getCell(0).getStringCellValue().isEmpty() ||
                    row.getCell(1).getStringCellValue().isEmpty() ||
                    //row.getCell(2).getStringCellValue().isEmpty() ||  //企业简称  可为空
                    row.getCell(3).getStringCellValue().isEmpty() ||
                    row.getCell(4).getStringCellValue().isEmpty() ||
                    row.getCell(5).getStringCellValue().isEmpty() ||
                    row.getCell(6).getStringCellValue().isEmpty() ||
                    row.getCell(7).getStringCellValue().isEmpty() ||
                    row.getCell(8).getStringCellValue().isEmpty() ||
                    row.getCell(9).getStringCellValue().isEmpty()
                   ) {

                StringBuffer str = new StringBuffer();
                if (row.getCell(0).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "企业名称不能为空");
                if (row.getCell(1).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "企业帐号不能为空");
                /*if (row.getCell(2).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "企业简称不能为空");*/
                if (row.getCell(3).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "行中联系人不能为空");
                if (row.getCell(4).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "行中联系电话不能为空");
                if (row.getCell(5).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "行中邮箱不能为空");
                if (row.getCell(6).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "行中职位不能为空");
                if (row.getCell(7).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "行中企业地址不能为空");
                if (row.getCell(8).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "行中客户经理名称不能为空");
                if (row.getCell(9).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "行中客户经理编号不能为空");



                map.put("code", "207");
                map.put("msg", str);
               // Constants.getSuccMsg(result, map);
                return map;
            } else {
                if(row.getCell(1).getStringCellValue().length()>10){
                    map.put("code", "205");
                    map.put("msg", "文件第" + i + "行企业账号限制长度10");
                    return map;
                }
               /* if (!Pattern.matches("2\\d{9}", row.getCell(1).getStringCellValue())) {
                    map.put("code", "205");
                    map.put("msg", "文件第" + i + "行企业账号由以2开头的10位数字组成");
                   // Constants.getSuccMsg(result, map);
                    return map;
                }*/

               /* String pwd = row.getCell(1).getStringCellValue();
                if (!Pattern.matches(".{6,20}", pwd) || Pattern.matches("\\d+", pwd) || Pattern.matches("[a-zA-Z]+", pwd) || Pattern.matches("[\\W_]+", pwd)) {
                    map.put("code", "205");
                    map.put("msg", "文件第" + i + "行中密码为6-20位，并且由字母，数字和符号两种以上组合");
                    Constants.getSuccMsg(result, map);
                    return result;
                }*/


               /* Dictionary dictionary = memberMapper.selectDictionaryCodeByName(row.getCell(6).getStringCellValue());
                if (dictionary == null || "".equals(dictionary.getCode())) {
                    map.put("code", "205");
                    map.put("msg", "文件第" + i + "行中会员套餐不存在");
                    Constants.getSuccMsg(result, map);
                    return result;
                }*/

                User user = new User();
                user.setRealName(row.getCell(8).getStringCellValue());
                user.setId(row.getCell(9).getStringCellValue());

                List<User> userList = memberMapper.selectUserInfoByIdAndRealName(user);
                if (userList == null || userList.size() == 0) {
                    map.put("code", "204");
                    map.put("msg", "文件第" + i + "行中客户经理名称和客户经理编号不存在");
                    //Constants.getSuccMsg(result, map);
                    return map;
                } else if (userList != null && !userList.get(0).getRealName().equals(user.getRealName())) {
                    map.put("code", "204");
                    map.put("msg", "文件第" + i + "行中客户经理名称和客户经理编号信息不匹配");
                   // Constants.getSuccMsg(result, map);
                    return map;
                }


                member.setEnterpriseName(row.getCell(0).getStringCellValue());//企业名称
                member.setEnterpriseCode(row.getCell(1).getStringCellValue());//企业帐号
                member.setAbbreviation(row.getCell(2).getStringCellValue());//企业简称
                member.setContact(row.getCell(3).getStringCellValue());//联系人
                member.setPhone(row.getCell(4).getStringCellValue());//联系方式
                member.setEmail(row.getCell(5).getStringCellValue());//邮箱
                member.setJob(row.getCell(6).getStringCellValue());//职位
                member.setAccountManager(user.getId());//客户经理
                member.setAddress(row.getCell(7).getStringCellValue());//联系地址
                if (row.getCell(10) != null && !row.getCell(10).getStringCellValue().isEmpty()) {
                    row.getCell(10).setCellType(CellType.STRING);
                    member.setRemark(row.getCell(10).getStringCellValue());//备注
                }
                //member.setRemark(row.getCell(10).getStringCellValue());//备注
                member.setDelFlag(1);//默认存在
                member.setMemberType(code);//会员类型

                Member memberByEnterpriseCode = memberMapper.getMemberByEnterpriseCode(member.getEnterpriseCode());
                // Member memberByEnterpriseName = memberMapper.getMemberByEnterpriseName(member.getEnterpriseName());
                //判断企业代码和会员名称是否存在，如果不存在则添加，如果存在则修改  && (memberByEnterpriseName == null || "".equals(memberByEnterpriseName.getId()))
                if (memberByEnterpriseCode == null || "".equals(memberByEnterpriseCode.getId()) ) {
                    super.insertBaseInfo(member); //调用父类中的通用新增方法
                    //setMemberDictionaryList(member, dictionaryList);//将新编号和套餐选择的集合赋值给会员信息
                    String pwd = getRandomNumAndChacters(6);
                    member.setPwd(pwd);
                    insertMemberList.add(member);
                } else {
                    if (memberByEnterpriseCode != null && !"".equals(memberByEnterpriseCode.getId()) &&
                            // memberByEnterpriseName != null && !"".equals(memberByEnterpriseName.getId()) &&
                            // memberByEnterpriseCode.getId().equals(memberByEnterpriseName.getId())
                            memberByEnterpriseCode.getEnterpriseName().equals(member.getEnterpriseName())) {
                        super.updateBaseInfo(member, memberByEnterpriseCode.getId());//调用父类中的通用修改方法
                        // setMemberDictionaryList(member, dictionaryList);//将查询的会员编号和套餐选择的集合赋值给会员信息
                        updateMemberList.add(member);
                    } else {
                        map.put("code", "204");
                        map.put("msg", "第" + i + "行中企业账号或者会员名称已存在，但企业账号和会员名称信息不匹配");
                        // Constants.getSuccMsg(result, map);
                        return map;
                    }
                }
            }
        }
        Integer arr = memberMapper.batchImportMember(insertMemberList, updateMemberList,1);
       // memberMapper.deleteWeight();
        if (arr > 0) {
            map.put("code", "200");
            map.put("msg", "导入成功");
           // Constants.getSuccMsg(result, map);
        } else {
            map.put("code", "200");
            map.put("msg", "未知错误，执行行为0");
           // Constants.getSuccMsg(result, map);
        }
        return map;
    }

    /**
     * GP  Excel
     * @param
     * sheet  16列
     * @return
     */
    @Transactional
    public Map<String,Object> batchGP(Sheet sheet,String code){
        Map<String,Object> map = new HashMap<>();
        List<Member> insertMemberList = new ArrayList<>();
        List<Member> updateMemberList = new ArrayList<>();
        //物理总行数
        int rowTotal = sheet.getPhysicalNumberOfRows();
        //判断表格非空
        if (rowTotal <= 2) {
            map.put("code", "201");
            map.put("msg", "表格为空");
            // Constants.getSuccMsg(result, map);
            return map;
        }
        //获取最后一行
        int lastRowNum = sheet.getLastRowNum() + 1;
        //判断是否有空行
        if (rowTotal != lastRowNum) {
            map.put("code", "202");
            map.put("msg", "表格有空行");
            // Constants.getSuccMsg(result, map);
            return map;
        }

        //
        //解析
        Member member = null;
        for (int i = 2; i < lastRowNum; i++) {
            member = new Member();
            Row row = sheet.getRow(i);
            if (row.getCell(0) == null || row.getCell(1) == null ||row.getCell(2) == null
                    //row.getCell(3) == null ||
                   // row.getCell(4) == null || row.getCell(5) == null ||
                  // row.getCell(6) == null || row.getCell(7) == null||
                    //row.getCell(8) == null ||
                    //row.getCell(9) == null||
                    //row.getCell(10) == null ||
                    //row.getCell(11) == null ||
                   // row.getCell(12) == null ||
                   // row.getCell(13) == null
                    ) {
                map.put("code", "207");
                map.put("msg", "文件中有空格");
                //Constants.getSuccMsg(result, map);
                return map;
            }

            row.getCell(0).setCellType(CellType.STRING);
            row.getCell(1).setCellType(CellType.STRING);
            row.getCell(2).setCellType(CellType.STRING);
            //row.getCell(3).setCellType(CellType.STRING);
           /* try {
                row.getCell(4).setCellType(CellType.STRING);
            } catch (Exception e) {
                e.printStackTrace();
                map.put("code", "204");
                map.put("msg", "文件第" + i + "行中手机号格式应为文本格式");
                //Constants.getSuccMsg(result, map);
                return map;
            }*/
           // row.getCell(5).setCellType(CellType.STRING);
           // row.getCell(6).setCellType(CellType.STRING);
           // row.getCell(7).setCellType(CellType.STRING);
           /* try {
                row.getCell(8).setCellType(CellType.STRING);
            } catch (Exception e) {
                e.printStackTrace();
                map.put("code", "204");
                map.put("msg", "文件第" + i + "行中客户经理编号应为文本格式");
                //Constants.getSuccMsg(result, map);
                return map;
            }*/
           // row.getCell(9).setCellType(CellType.STRING);
           // row.getCell(10).setCellType(CellType.STRING);
           // row.getCell(11).setCellType(CellType.STRING);
            //row.getCell(12).setCellType(CellType.STRING);
           // row.getCell(13).setCellType(CellType.STRING);
            //row.getCell(14).setCellType(CellType.STRING);  //关联数据未导入
           // row.getCell(15).setCellType(CellType.STRING);



            if (row.getCell(0).getStringCellValue().isEmpty() ||
                    row.getCell(1).getStringCellValue().isEmpty() ||
                    row.getCell(2).getStringCellValue().isEmpty()
                    //row.getCell(3).getStringCellValue().isEmpty() ||
                   // row.getCell(4).getStringCellValue().isEmpty() ||
                    //row.getCell(5).getStringCellValue().isEmpty() ||
                    //row.getCell(6).getStringCellValue().isEmpty() ||
                   // row.getCell(7).getStringCellValue().isEmpty() ||
                   // row.getCell(8).getStringCellValue().isEmpty() ||
                   // row.getCell(9).getStringCellValue().isEmpty() ||
                   // row.getCell(10).getStringCellValue().isEmpty() ||
                   //  row.getCell(11).getStringCellValue().isEmpty() ||
                   // row.getCell(12).getStringCellValue().isEmpty() ||
                   // row.getCell(13).getStringCellValue().isEmpty()


                    ) {

                StringBuffer str = new StringBuffer();
                if (row.getCell(0).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "机构名称不能为空");
                if (row.getCell(1).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "机构帐号不能为空");
                if (row.getCell(2).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "机构简称不能为空");
               /* if (row.getCell(3).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "行中主要合伙人不能为空");*/
               /* if (row.getCell(4).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "行中联系电话不能为空");
                if (row.getCell(5).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "行中邮箱不能为空");
                if (row.getCell(6).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "行中职位不能为空");

                if (row.getCell(7).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "行中客户经理名称不能为空");
                if (row.getCell(8).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "行中客户经理编号不能为空");*/
               /* if (row.getCell(9).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "行中总部所在省份城市不能为空");*/
               /* if (row.getCell(10).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "行中上海是否有办公室不能为空");
                if (row.getCell(11).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "行中关注行业不能为空");*/
               /* if (row.getCell(12).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "行中科关注阶段不能为空");
                if (row.getCell(13).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "行中项科投后项目经理不能为空");*/




                map.put("code", "207");
                map.put("msg", str);
                // Constants.getSuccMsg(result, map);
                return map;
            } else {
                if(row.getCell(1).getStringCellValue().length()>10){
                    map.put("code", "205");
                    map.put("msg", "文件第" + i + "行企业账号限制长度10");
                    return map;
                }
               /* if (!Pattern.matches("2\\d{9}", row.getCell(0).getStringCellValue())) {
                    map.put("code", "205");
                    map.put("msg", "文件第" + i + "行企业账号由以2开头的10位数字组成");
                    Constants.getSuccMsg(result, map);
                    return result;
                }*/

               /* String pwd = row.getCell(1).getStringCellValue();
                if (!Pattern.matches(".{6,20}", pwd) || Pattern.matches("\\d+", pwd) || Pattern.matches("[a-zA-Z]+", pwd) || Pattern.matches("[\\W_]+", pwd)) {
                    map.put("code", "205");
                    map.put("msg", "文件第" + i + "行中密码为6-20位，并且由字母，数字和符号两种以上组合");
                    Constants.getSuccMsg(result, map);
                    return result;
                }*/


               /* Dictionary dictionary = memberMapper.selectDictionaryCodeByName(row.getCell(6).getStringCellValue());
                if (dictionary == null || "".equals(dictionary.getCode())) {
                    map.put("code", "205");
                    map.put("msg", "文件第" + i + "行中会员套餐不存在");
                    Constants.getSuccMsg(result, map);
                    return result;
                }*/
                if (row.getCell(7) != null && !row.getCell(7).getStringCellValue().isEmpty()
                        && row.getCell(8) != null && !row.getCell(8).getStringCellValue().isEmpty()) {
                    try {
                        row.getCell(8).setCellType(CellType.STRING);
                    } catch (Exception e) {
                        e.printStackTrace();
                        map.put("code", "204");
                        map.put("msg", "文件第" + i + "行中客户经理编号应为文本格式");
                        //Constants.getSuccMsg(result, map);
                        return map;
                    }
                    row.getCell(7).setCellType(CellType.STRING);
                   // row.getCell(8).setCellType(CellType.STRING);
                    User user = new User();
                    user.setRealName(row.getCell(7).getStringCellValue());
                    user.setId(row.getCell(8).getStringCellValue());

                    List<User> userList = memberMapper.selectUserInfoByIdAndRealName(user);
                    if (userList == null || userList.size() == 0) {
                        map.put("code", "204");
                        map.put("msg", "文件第" + i + "行中客户经理名称和客户经理编号不存在");
                        //Constants.getSuccMsg(result, map);
                        return map;
                    } else if (userList != null && !userList.get(0).getRealName().equals(user.getRealName())) {
                        map.put("code", "204");
                        map.put("msg", "文件第" + i + "行中客户经理名称和客户经理编号信息不匹配");
                        // Constants.getSuccMsg(result, map);
                        return map;
                    }
                    member.setAccountManager(user.getId());//客户经理
                }

                member.setEnterpriseName(row.getCell(0).getStringCellValue());//项目法定名称
                member.setEnterpriseCode(row.getCell(1).getStringCellValue());//企业帐号
                member.setAbbreviation(row.getCell(2).getStringCellValue());//项目简称
                if (row.getCell(3) != null && !row.getCell(3).getStringCellValue().isEmpty()) {
                    row.getCell(3).setCellType(CellType.STRING);
                    member.setContact(row.getCell(3).getStringCellValue());//联系人
                }
                if (row.getCell(4) != null && !row.getCell(4).getStringCellValue().isEmpty()) {
                    try {
                        row.getCell(4).setCellType(CellType.STRING);
                        member.setPhone(row.getCell(4).getStringCellValue());//联系方式
                    } catch (Exception e) {
                        e.printStackTrace();
                        map.put("code", "204");
                        map.put("msg", "文件第" + i + "行中手机号格式应为文本格式");
                        //Constants.getSuccMsg(result, map);
                        return map;
                    }
                }
                if (row.getCell(5) != null && !row.getCell(5).getStringCellValue().isEmpty()) {
                    row.getCell(5).setCellType(CellType.STRING);
                    member.setEmail(row.getCell(5).getStringCellValue());//邮箱
                }
                if (row.getCell(6) != null && !row.getCell(6).getStringCellValue().isEmpty()) {
                    row.getCell(6).setCellType(CellType.STRING);
                    member.setJob(row.getCell(6).getStringCellValue());//职位
                }
                if (row.getCell(9) != null && !row.getCell(9).getStringCellValue().isEmpty()) {
                    row.getCell(9).setCellType(CellType.STRING);
                    member.setRegistrationProvince(row.getCell(9).getStringCellValue());//总部所在省份
                }
                if (row.getCell(10) != null && !row.getCell(10).getStringCellValue().isEmpty()) {
                    row.getCell(10).setCellType(CellType.STRING);
                    member.setAddress(row.getCell(10).getStringCellValue());//总部所在城市
                }
                if (row.getCell(11) != null && !row.getCell(11).getStringCellValue().isEmpty()) {
                    row.getCell(11).setCellType(CellType.STRING);
                    if(row.getCell(11).getStringCellValue().equals("是")){
                        member.setSHOffice(1);
                    }else {
                        member.setSHOffice(0);
                    }

                }
                if (row.getCell(12) != null && !row.getCell(12).getStringCellValue().isEmpty()) {
                    row.getCell(12).setCellType(CellType.STRING);
                    String attentionIndustryname = row.getCell(12).getStringCellValue();
                    String[] attentionIndustry = attentionIndustryname.split("，");
                    // Dictionary rhinocerosLabel = memberMapper.getDictionaryCodeByName("rhinocerosLabel",row.getCell(14).getStringCellValue());

                    String[] attentionIndustrys = memberMapper.getDictionaryLabels("attentionIndustry",attentionIndustry);
                    if(attentionIndustrys == null || attentionIndustrys.length<=0){
                        map.put("code", "204");
                        map.put("msg", "文件第" + i + "行中关注行业信息不匹配,（多个关注行业使用，分开）");
                        return map;
                    }
                    String attentionIndustrys1 = JSONArray.toJSON(attentionIndustrys).toString();

                    member.setAttentionIndustryId(attentionIndustrys1);//关注行业
                }
                //Dictionary attentionIndustry = memberMapper.getDictionaryCodeByName("attentionIndustry",row.getCell(11).getStringCellValue());
                /*if(attentionIndustry == null || attentionIndustry.equals("") || attentionIndustry.getName() == null ||attentionIndustry.getName().equals("")){
                    map.put("code", "204");
                    map.put("msg", "文件第" + i + "行中关注行业信息不匹配");
                    // Constants.getSuccMsg(result, map);
                    return map;
                }
                member.setAttentionIndustryId(attentionIndustry.getCode());//关注行业*/

                if (row.getCell(13) != null && !row.getCell(13).getStringCellValue().isEmpty()) {
                    row.getCell(13).setCellType(CellType.STRING);
                    String attentionStagename = row.getCell(13).getStringCellValue();
                    String[] attentionStage = attentionStagename.split("，");
                    // Dictionary rhinocerosLabel = memberMapper.getDictionaryCodeByName("rhinocerosLabel",row.getCell(14).getStringCellValue());

                    String[] attentionStages = memberMapper.getDictionaryLabels("attentionStage",attentionStage);
                    if(attentionStages == null || attentionStages.length<=0){
                        map.put("code", "204");
                        map.put("msg", "文件第" + i + "行中行业阶段信息不匹配,（多个行业阶段使用，分开）");
                        return map;
                    }
                    String attentionStages1 = JSONArray.toJSON(attentionStages).toString();

                    member.setAttentionStageId(attentionStages1);//行业阶段
                }
                /*Dictionary attentionStage = memberMapper.getDictionaryCodeByName("attentionStage",row.getCell(12).getStringCellValue());
                if(attentionStage == null || attentionStage.equals("") || attentionStage.getName() == null ||attentionStage.getName().equals("")){
                    map.put("code", "204");
                    map.put("msg", "文件第" + i + "行中行业阶段信息不匹配");
                    // Constants.getSuccMsg(result, map);
                    return map;
                }
                member.setAttentionStageId(attentionStage.getCode());*/


                if (row.getCell(14) != null && !row.getCell(14).getStringCellValue().isEmpty()) {
                    row.getCell(14).setCellType(CellType.STRING);
                    member.setInvestmentManager(row.getCell(14).getStringCellValue()); //投后项目经理
                }

                if (row.getCell(15) != null && !row.getCell(15).getStringCellValue().isEmpty()) {
                    row.getCell(15).setCellType(CellType.STRING);
                    member.setInvestmentFund(row.getCell(15).getStringCellValue()); //科创投资基金
                }
                if (row.getCell(16) != null && !row.getCell(16).getStringCellValue().isEmpty()) {
                    row.getCell(16).setCellType(CellType.STRING);
                    member.setRemark(row.getCell(16).getStringCellValue());//备注
                }

                member.setDelFlag(1);//默认存在
                member.setMemberType(code);//会员类型
                Member memberByEnterpriseCode = memberMapper.getMemberByEnterpriseCode(member.getEnterpriseCode());
                // Member memberByEnterpriseName = memberMapper.getMemberByEnterpriseName(member.getEnterpriseName());
                //判断企业代码和会员名称是否存在，如果不存在则添加，如果存在则修改  && (memberByEnterpriseName == null || "".equals(memberByEnterpriseName.getId()))
                if (memberByEnterpriseCode == null || "".equals(memberByEnterpriseCode.getId()) ) {
                    super.insertBaseInfo(member); //调用父类中的通用新增方法
                    //setMemberDictionaryList(member, dictionaryList);//将新编号和套餐选择的集合赋值给会员信息
                    String pwd = getRandomNumAndChacters(6);
                    member.setPwd(pwd);
                    insertMemberList.add(member);
                } else {
                    if (memberByEnterpriseCode != null && !"".equals(memberByEnterpriseCode.getId()) &&
                            // memberByEnterpriseName != null && !"".equals(memberByEnterpriseName.getId()) &&
                            // memberByEnterpriseCode.getId().equals(memberByEnterpriseName.getId())
                            memberByEnterpriseCode.getEnterpriseName().equals(member.getEnterpriseName())) {
                        super.updateBaseInfo(member, memberByEnterpriseCode.getId());//调用父类中的通用修改方法
                        // setMemberDictionaryList(member, dictionaryList);//将查询的会员编号和套餐选择的集合赋值给会员信息
                        updateMemberList.add(member);
                    } else {
                        map.put("code", "204");
                        map.put("msg", "第" + i + "行中企业账号或者会员名称已存在，但企业账号和会员名称信息不匹配");
                        // Constants.getSuccMsg(result, map);
                        return map;
                    }
                }
            }
        }
        Integer arr = memberMapper.batchImportMember(insertMemberList, updateMemberList,2);
        //memberMapper.deleteWeight();
        if (arr > 0) {
            map.put("code", "200");
            map.put("msg", "导入成功");
            // Constants.getSuccMsg(result, map);
        } else {
            map.put("code", "200");
            map.put("msg", "未知错误，执行行为0");
            // Constants.getSuccMsg(result, map);
        }
        return map;
    }
    /**
     * 创业公司Excel
     * @param
     * sheet  19列
     * @return
     */
    @Transactional
    public Map<String,Object> batchchuangye(Sheet sheet,String code){
        Map<String,Object> map = new HashMap<>();
        List<Member> insertMemberList = new ArrayList<>();
        List<Member> updateMemberList = new ArrayList<>();
        //物理总行数
        int rowTotal = sheet.getPhysicalNumberOfRows();
        //判断表格非空
        if (rowTotal <= 2) {
            map.put("code", "201");
            map.put("msg", "表格为空");
            // Constants.getSuccMsg(result, map);
            return map;
        }
        //获取最后一行
        int lastRowNum = sheet.getLastRowNum() + 1;
        //判断是否有空行
        if (rowTotal != lastRowNum) {
            map.put("code", "202");
            map.put("msg", "表格有空行");
            // Constants.getSuccMsg(result, map);
            return map;
        }

        //
        //解析
        Member member = null;
        for (int i = 2; i < lastRowNum; i++) {
            member = new Member();
            Row row = sheet.getRow(i);
            if (row.getCell(0) == null || row.getCell(1) == null ||row.getCell(2) == null ||
                   // row.getCell(3) == null || row.getCell(4) == null || row.getCell(5) == null ||
                   // row.getCell(6) == null || row.getCell(7) == null|| row.getCell(8) == null  ||
                    //row.getCell(9) == null||
                   // row.getCell(10) == null ||
                    //row.getCell(11) == null ||
                    row.getCell(12) == null ||
                    //row.getCell(13) == null ||
                    //row.getCell(14) == null ||
                    row.getCell(15) == null ||
                    row.getCell(16) == null //|| row.getCell(17) == null
                    ) {
                map.put("code", "207");
                map.put("msg", "文件中有空格");
                //Constants.getSuccMsg(result, map);
                return map;
            }

            row.getCell(0).setCellType(CellType.STRING);
            row.getCell(1).setCellType(CellType.STRING);
            row.getCell(2).setCellType(CellType.STRING);
           // row.getCell(3).setCellType(CellType.STRING);
           /* try {
                row.getCell(4).setCellType(CellType.STRING);
            } catch (Exception e) {
                e.printStackTrace();
                map.put("code", "204");
                map.put("msg", "文件第" + i + "行中手机号格式应为文本格式");
                //Constants.getSuccMsg(result, map);
                return map;
            }
            row.getCell(5).setCellType(CellType.STRING);
            row.getCell(6).setCellType(CellType.STRING);
            row.getCell(7).setCellType(CellType.STRING);
            try {
                row.getCell(8).setCellType(CellType.STRING);
            } catch (Exception e) {
                e.printStackTrace();
                map.put("code", "204");
                map.put("msg", "文件第" + i + "行中客户经理编号应为文本格式");
                //Constants.getSuccMsg(result, map);
                return map;
            }*/
           // row.getCell(9).setCellType(CellType.STRING);
           // row.getCell(10).setCellType(CellType.STRING);
           // row.getCell(11).setCellType(CellType.STRING);
            row.getCell(12).setCellType(CellType.STRING);
           // row.getCell(13).setCellType(CellType.STRING);
            //row.getCell(14).setCellType(CellType.STRING);
            row.getCell(15).setCellType(CellType.STRING);
            row.getCell(16).setCellType(CellType.STRING);
           // row.getCell(17).setCellType(CellType.STRING);
           // row.getCell(18).setCellType(CellType.STRING);
            //row.getCell(19).setCellType(CellType.STRING);

            if (row.getCell(0).getStringCellValue().isEmpty() ||
                    row.getCell(1).getStringCellValue().isEmpty() ||
                    //row.getCell(2).getStringCellValue().isEmpty() ||  //企业简称  可为空
                   // row.getCell(3).getStringCellValue().isEmpty() ||
                   // row.getCell(4).getStringCellValue().isEmpty() ||
                   // row.getCell(5).getStringCellValue().isEmpty() ||
                   // row.getCell(6).getStringCellValue().isEmpty() ||
                  //  row.getCell(7).getStringCellValue().isEmpty() ||
                   // row.getCell(8).getStringCellValue().isEmpty() ||
                   // row.getCell(9).getStringCellValue().isEmpty() ||
                   // row.getCell(10).getStringCellValue().isEmpty() ||
                   // row.getCell(11).getStringCellValue().isEmpty() ||
                    row.getCell(12).getStringCellValue().isEmpty() ||
                   // row.getCell(13).getStringCellValue().isEmpty() ||
                   // row.getCell(14).getStringCellValue().isEmpty() ||
                    row.getCell(15).getStringCellValue().isEmpty() ||
                    row.getCell(16).getStringCellValue().isEmpty()
                   // || row.getCell(17).getStringCellValue().isEmpty()

                    ) {

                StringBuffer str = new StringBuffer();
                if (row.getCell(0).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "项目法定名称不能为空");
                if (row.getCell(1).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "企业帐号不能为空");
                if (row.getCell(2).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "项目简称不能为空");
               /* if (row.getCell(3).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "行中联系人不能为空");
                if (row.getCell(4).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "行中联系电话不能为空");
                if (row.getCell(5).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "行中邮箱不能为空");
                if (row.getCell(6).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "行中职位不能为空");

                if (row.getCell(7).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "行中客户经理名称不能为空");
                if (row.getCell(8).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "行中客户经理编号不能为空");*/
               /* if (row.getCell(9).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "行中项目注册地不能为空");*/
               /* if (row.getCell(10).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "行中项目经营地不能为空");*/
                /*if (row.getCell(11).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "行中投资基金不能为空");*/
                if (row.getCell(12).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "行中科创行业分类不能为空");
                /*if (row.getCell(13).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "行中项科创直投分类不能为空");*/
               /* if (row.getCell(14).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "行中项犀牛标签不能为空");*/
                if (row.getCell(15).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "行中首次投资时间不能为空");
                if (row.getCell(16).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "行中首次投资阶段不能为空");
               /* if (row.getCell(17).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "行中首次是否领投不能为空");*/



                map.put("code", "207");
                map.put("msg", str);
                // Constants.getSuccMsg(result, map);
                return map;
            } else {
                if(row.getCell(1).getStringCellValue().length()>10){
                    map.put("code", "205");
                    map.put("msg", "文件第" + i + "行企业账号限制长度10");
                    return map;
                }
               /* if (!Pattern.matches("2\\d{9}", row.getCell(0).getStringCellValue())) {
                    map.put("code", "205");
                    map.put("msg", "文件第" + i + "行企业账号由以2开头的10位数字组成");
                    Constants.getSuccMsg(result, map);
                    return result;
                }*/

               /* String pwd = row.getCell(1).getStringCellValue();
                if (!Pattern.matches(".{6,20}", pwd) || Pattern.matches("\\d+", pwd) || Pattern.matches("[a-zA-Z]+", pwd) || Pattern.matches("[\\W_]+", pwd)) {
                    map.put("code", "205");
                    map.put("msg", "文件第" + i + "行中密码为6-20位，并且由字母，数字和符号两种以上组合");
                    Constants.getSuccMsg(result, map);
                    return result;
                }*/


               /* Dictionary dictionary = memberMapper.selectDictionaryCodeByName(row.getCell(6).getStringCellValue());
                if (dictionary == null || "".equals(dictionary.getCode())) {
                    map.put("code", "205");
                    map.put("msg", "文件第" + i + "行中会员套餐不存在");
                    Constants.getSuccMsg(result, map);
                    return result;
                }*/
               //String d = row.getCell(15).getStringCellValue();
                String time1 = null;
                try {
                    Date setupTime = HSSFDateUtil.getJavaDate(Double.valueOf(row.getCell(15).getStringCellValue()));
                    //boolean vdate =  isValidDate(row.getCell(15).getStringCellValue());
                    SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                     time1 = format0.format(setupTime.getTime());//这个就是把时间戳经过处理得到期望格式的时间
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    map.put("code", "204");
                    map.put("msg", "文件第" + i + "行中首次投资时间格式不正确");
                    //Constants.getSuccMsg(result, map);
                    return map;
                }

                if (row.getCell(7) != null && !row.getCell(7).getStringCellValue().isEmpty()
                        && row.getCell(8) != null && !row.getCell(8).getStringCellValue().isEmpty()) {
                    try {
                        row.getCell(8).setCellType(CellType.STRING);
                    } catch (Exception e) {
                        e.printStackTrace();
                        map.put("code", "204");
                        map.put("msg", "文件第" + i + "行中客户经理编号应为文本格式");
                        //Constants.getSuccMsg(result, map);
                        return map;
                    }
                    row.getCell(7).setCellType(CellType.STRING);
                    //row.getCell(8).setCellType(CellType.STRING);
                    User user = new User();
                    user.setRealName(row.getCell(7).getStringCellValue());
                    user.setId(row.getCell(8).getStringCellValue());

                    List<User> userList = memberMapper.selectUserInfoByIdAndRealName(user);
                    if (userList == null || userList.size() == 0) {
                        map.put("code", "204");
                        map.put("msg", "文件第" + i + "行中客户经理名称和客户经理编号不存在");
                        //Constants.getSuccMsg(result, map);
                        return map;
                    } else if (userList != null && !userList.get(0).getRealName().equals(user.getRealName())) {
                        map.put("code", "204");
                        map.put("msg", "文件第" + i + "行中客户经理名称和客户经理编号信息不匹配");
                        // Constants.getSuccMsg(result, map);
                        return map;
                    }
                    member.setAccountManager(user.getId());//客户经理
                }


                member.setEnterpriseName(row.getCell(0).getStringCellValue());//项目法定名称
                member.setEnterpriseCode(row.getCell(1).getStringCellValue());//企业帐号
                member.setAbbreviation(row.getCell(2).getStringCellValue());//项目简称
                if (row.getCell(3) != null && !row.getCell(3).getStringCellValue().isEmpty()) {
                    row.getCell(3).setCellType(CellType.STRING);
                    member.setContact(row.getCell(3).getStringCellValue());//联系人
                }
                if (row.getCell(4) != null && !row.getCell(4).getStringCellValue().isEmpty()) {
                    try {
                        row.getCell(4).setCellType(CellType.STRING);
                        member.setPhone(row.getCell(4).getStringCellValue());//联系方式
                    } catch (Exception e) {
                        e.printStackTrace();
                        map.put("code", "204");
                        map.put("msg", "文件第" + i + "行中手机号格式应为文本格式");
                        //Constants.getSuccMsg(result, map);
                        return map;
                    }
                }
                if (row.getCell(5) != null && !row.getCell(5).getStringCellValue().isEmpty()) {
                    row.getCell(5).setCellType(CellType.STRING);
                    member.setEmail(row.getCell(5).getStringCellValue());//邮箱
                }
                if (row.getCell(6) != null && !row.getCell(6).getStringCellValue().isEmpty()) {
                    row.getCell(6).setCellType(CellType.STRING);
                    member.setJob(row.getCell(6).getStringCellValue());//职位
                }


                if (row.getCell(9) != null && !row.getCell(9).getStringCellValue().isEmpty()) {
                    row.getCell(9).setCellType(CellType.STRING);
                    member.setAddress1(row.getCell(9).getStringCellValue());//项目注册地
                }

                if (row.getCell(10) != null && !row.getCell(10).getStringCellValue().isEmpty()) {
                    row.getCell(10).setCellType(CellType.STRING);
                    member.setAddress(row.getCell(10).getStringCellValue());//项目经营地
                }

                if (row.getCell(11) != null && !row.getCell(11).getStringCellValue().isEmpty()) {
                    row.getCell(11).setCellType(CellType.STRING);
                    //获取GPid
                    String gpId = memberMapper.getGPIdbyFund(row.getCell(11).getStringCellValue(),"1");
                    if(gpId != null && !gpId.equals("")){
                        member.setInvestmentFund(gpId);
                    }else{
                        member.setInvestmentFund(row.getCell(11).getStringCellValue());//投资基金
                    }
                }
                Dictionary industryClassification = memberMapper.getDictionaryCodeByName("industryClassification",row.getCell(12).getStringCellValue());
                if(industryClassification == null || industryClassification.equals("") || industryClassification.getName() == null ||industryClassification.getName().equals("")){
                    map.put("code", "204");
                    map.put("msg", "文件第" + i + "行中科创行业分类信息不匹配");
                    // Constants.getSuccMsg(result, map);
                    return map;
                }
                member.setIndustryClassificationId(industryClassification.getCode());//科创行业分类
                if (row.getCell(13) != null && !row.getCell(13).getStringCellValue().isEmpty()) {
                    row.getCell(13).setCellType(CellType.STRING);
                    Dictionary directInvestmentClassification = memberMapper.getDictionaryCodeByName("directInvestmentClassification",row.getCell(13).getStringCellValue());
                    if(directInvestmentClassification == null || directInvestmentClassification.equals("") || directInvestmentClassification.getName() == null ||directInvestmentClassification.getName().equals("")){
                        map.put("code", "204");
                        map.put("msg", "文件第" + i + "行中科创直投分类信息不匹配");
                        // Constants.getSuccMsg(result, map);
                        return map;
                    }
                    member.setDirectInvestmentClassificationId(directInvestmentClassification.getCode());//科创直投分类
                }
                if (row.getCell(14) != null && !row.getCell(14).getStringCellValue().isEmpty()) {
                    row.getCell(14).setCellType(CellType.STRING);
                    String labelname = row.getCell(14).getStringCellValue();
                    String[] label = labelname.split("，");
                   // Dictionary rhinocerosLabel = memberMapper.getDictionaryCodeByName("rhinocerosLabel",row.getCell(14).getStringCellValue());
                    String[] rhinocerosLabel = memberMapper.getDictionaryLabels("rhinocerosLabel",label);
                    if(rhinocerosLabel == null || rhinocerosLabel.length<=0){
                        map.put("code", "204");
                        map.put("msg", "文件第" + i + "行中犀牛标签信息不匹配,（多个犀牛标签使用，分开）");
                        // Constants.getSuccMsg(result, map);
                        return map;
                    }
                    String rhinocerosLabel1 = JSONArray.toJSON(rhinocerosLabel).toString();
                   // member.setRhinocerosLabelId(Arrays.toString(rhinocerosLabel));//犀牛标签
                    member.setRhinocerosLabelId(rhinocerosLabel1);//犀牛标签
                }

                member.setFirstInvestmentTime(time1);//首次投资时间
                Dictionary firstInvestmentStage = memberMapper.getDictionaryCodeByName("firstInvestmentStage",row.getCell(16).getStringCellValue());
                if(firstInvestmentStage == null || firstInvestmentStage.equals("") || firstInvestmentStage.getName() == null ||firstInvestmentStage.getName().equals("")){
                    map.put("code", "204");
                    map.put("msg", "文件第" + i + "行中首次投资阶段信息不匹配");
                    // Constants.getSuccMsg(result, map);
                    return map;
                }
                member.setFirstInvestmentStageId(firstInvestmentStage.getCode());//首次投资阶段
                if (row.getCell(17) != null && !row.getCell(17).getStringCellValue().isEmpty()) {
                    row.getCell(17).setCellType(CellType.STRING);
                    if(row.getCell(17).getStringCellValue().equals("是")){
                        member.setLeadInvestment(1);
                    }else {
                        member.setLeadInvestment(0);
                    }//是否领投
                }
                if (row.getCell(18) != null && !row.getCell(18).getStringCellValue().isEmpty()) {
                    row.getCell(18).setCellType(CellType.STRING);
                    member.setRegistrationProvince(row.getCell(18).getStringCellValue());//注册省份
                }
                if (row.getCell(19) != null && !row.getCell(19).getStringCellValue().isEmpty()) {
                    row.getCell(19).setCellType(CellType.STRING);
                    member.setRemark(row.getCell(19).getStringCellValue());//备注
                }
                //member.setRemark(row.getCell(18).getStringCellValue());//备注
                member.setDelFlag(1);//默认存在
                member.setMemberType(code);//会员类型

                Member memberByEnterpriseCode = memberMapper.getMemberByEnterpriseCode(member.getEnterpriseCode());
               // Member memberByEnterpriseName = memberMapper.getMemberByEnterpriseName(member.getEnterpriseName());
                //判断企业代码和会员名称是否存在，如果不存在则添加，如果存在则修改  && (memberByEnterpriseName == null || "".equals(memberByEnterpriseName.getId()))
                if (memberByEnterpriseCode == null || "".equals(memberByEnterpriseCode.getId()) ) {
                    super.insertBaseInfo(member); //调用父类中的通用新增方法
                    //setMemberDictionaryList(member, dictionaryList);//将新编号和套餐选择的集合赋值给会员信息
                    String pwd = getRandomNumAndChacters(6);
                    member.setPwd(pwd);
                    insertMemberList.add(member);
                } else {
                    if (memberByEnterpriseCode != null && !"".equals(memberByEnterpriseCode.getId()) &&
                           // memberByEnterpriseName != null && !"".equals(memberByEnterpriseName.getId()) &&
                           // memberByEnterpriseCode.getId().equals(memberByEnterpriseName.getId())
                            memberByEnterpriseCode.getEnterpriseName().equals(member.getEnterpriseName())) {
                        super.updateBaseInfo(member, memberByEnterpriseCode.getId());//调用父类中的通用修改方法
                        // setMemberDictionaryList(member, dictionaryList);//将查询的会员编号和套餐选择的集合赋值给会员信息
                        updateMemberList.add(member);
                    } else {
                        map.put("code", "204");
                        map.put("msg", "第" + i + "行中企业账号或者会员名称已存在，但企业账号和会员名称信息不匹配");
                        // Constants.getSuccMsg(result, map);
                        return map;
                    }
                }
            }
        }
        Integer arr = memberMapper.batchImportMember(insertMemberList, updateMemberList,3);
       // memberMapper.deleteWeight();
        if (arr > 0) {
            map.put("code", "200");
            map.put("msg", "导入成功");
            // Constants.getSuccMsg(result, map);
        } else {
            map.put("code", "200");
            map.put("msg", "未知错误，执行行为0");
            // Constants.getSuccMsg(result, map);
        }
        return map;
    }

    /**
     * LP  Excel
     * @param
     * sheet  10列
     * @return
     */
    @Transactional
    public Map<String,Object> batchLP(Sheet sheet,String code){
        Map<String,Object> map = new HashMap<>();
        List<Member> insertMemberList = new ArrayList<>();
        List<Member> updateMemberList = new ArrayList<>();
        //物理总行数
        int rowTotal = sheet.getPhysicalNumberOfRows();
        //判断表格非空
        if (rowTotal <= 2) {
            map.put("code", "201");
            map.put("msg", "表格为空");
            // Constants.getSuccMsg(result, map);
            return map;
        }
        //获取最后一行
        int lastRowNum = sheet.getLastRowNum() + 1;
        //判断是否有空行
        if (rowTotal != lastRowNum) {
            map.put("code", "202");
            map.put("msg", "表格有空行");
            // Constants.getSuccMsg(result, map);
            return map;
        }

        //
        //解析
        Member member = null;
        for (int i = 2; i < lastRowNum; i++) {
            member = new Member();
            Row row = sheet.getRow(i);
            if (row.getCell(0) == null || row.getCell(1) == null ||
                    row.getCell(2) == null
                    //|| row.getCell(4) == null || row.getCell(5) == null ||
                   // row.getCell(3) == null || row.getCell(6) == null || row.getCell(7) == null
                   // || row.getCell(8) == null
                    ){
                map.put("code", "207");
                map.put("msg", "文件中有空格");
                //Constants.getSuccMsg(result, map);
                return map;
            }

            row.getCell(0).setCellType(CellType.STRING);
            row.getCell(1).setCellType(CellType.STRING);
            row.getCell(2).setCellType(CellType.STRING);
           // row.getCell(3).setCellType(CellType.STRING);
           /* try {
                row.getCell(4).setCellType(CellType.STRING);
            } catch (Exception e) {
                e.printStackTrace();
                map.put("code", "204");
                map.put("msg", "文件第" + i + "行中手机号格式应为文本格式");
                //Constants.getSuccMsg(result, map);
                return map;
            }*/
           // row.getCell(5).setCellType(CellType.STRING);
            //row.getCell(6).setCellType(CellType.STRING);
            //row.getCell(7).setCellType(CellType.STRING);
           /* try {
                row.getCell(8).setCellType(CellType.STRING);
            } catch (Exception e) {
                e.printStackTrace();
                map.put("code", "204");
                map.put("msg", "文件第" + i + "行中客户经理编号应为文本格式");
                //Constants.getSuccMsg(result, map);
                return map;
            }*/


            if (row.getCell(0).getStringCellValue().isEmpty() ||
                    row.getCell(1).getStringCellValue().isEmpty() ||
                    row.getCell(2).getStringCellValue().isEmpty()
                   // row.getCell(3).getStringCellValue().isEmpty() ||
                   // row.getCell(4).getStringCellValue().isEmpty() ||
                   // row.getCell(5).getStringCellValue().isEmpty() ||
                    //row.getCell(6).getStringCellValue().isEmpty() ||
                   // row.getCell(7).getStringCellValue().isEmpty() ||
                   // row.getCell(8).getStringCellValue().isEmpty()
                    ) {

                StringBuffer str = new StringBuffer();
                if (row.getCell(0).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "LP全称不能为空");
                if (row.getCell(1).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "企业帐号不能为空");
                if (row.getCell(2).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "LP简称不能为空");
              /*  if (row.getCell(3).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "行中联系人不能为空");
                if (row.getCell(4).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "行中联系电话不能为空");
                if (row.getCell(5).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "行中邮箱不能为空");
                if (row.getCell(6).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "行中职位不能为空");
                if (row.getCell(7).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "行中客户经理名称不能为空");
                if (row.getCell(8).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "行中客户经理编号不能为空");*/

                map.put("code", "207");
                map.put("msg", str);
                // Constants.getSuccMsg(result, map);
                return map;
            } else {
                if(row.getCell(1).getStringCellValue().length()>10){
                    map.put("code", "205");
                    map.put("msg", "文件第" + i + "行企业账号限制长度10");
                    return map;
                }
               /* if (!Pattern.matches("2\\d{9}", row.getCell(0).getStringCellValue())) {
                    map.put("code", "205");
                    map.put("msg", "文件第" + i + "行企业账号由以2开头的10位数字组成");
                    Constants.getSuccMsg(result, map);
                    return result;
                }*/

               /* String pwd = row.getCell(1).getStringCellValue();
                if (!Pattern.matches(".{6,20}", pwd) || Pattern.matches("\\d+", pwd) || Pattern.matches("[a-zA-Z]+", pwd) || Pattern.matches("[\\W_]+", pwd)) {
                    map.put("code", "205");
                    map.put("msg", "文件第" + i + "行中密码为6-20位，并且由字母，数字和符号两种以上组合");
                    Constants.getSuccMsg(result, map);
                    return result;
                }*/


               /* Dictionary dictionary = memberMapper.selectDictionaryCodeByName(row.getCell(6).getStringCellValue());
                if (dictionary == null || "".equals(dictionary.getCode())) {
                    map.put("code", "205");
                    map.put("msg", "文件第" + i + "行中会员套餐不存在");
                    Constants.getSuccMsg(result, map);
                    return result;
                }*/


                member.setEnterpriseName(row.getCell(0).getStringCellValue());//lp全称
                member.setEnterpriseCode(row.getCell(1).getStringCellValue());//企业帐号
                member.setAbbreviation(row.getCell(2).getStringCellValue());//lp简称
                if (row.getCell(3) != null && !row.getCell(3).getStringCellValue().isEmpty()) {
                    row.getCell(3).setCellType(CellType.STRING);
                    member.setContact(row.getCell(3).getStringCellValue());//联系人
                }
                if (row.getCell(4) != null && !row.getCell(4).getStringCellValue().isEmpty()) {
                    try {
                        row.getCell(4).setCellType(CellType.STRING);
                        member.setPhone(row.getCell(4).getStringCellValue());//联系方式
                    } catch (Exception e) {
                        e.printStackTrace();
                        map.put("code", "204");
                        map.put("msg", "文件第" + i + "行中手机号格式应为文本格式");
                        //Constants.getSuccMsg(result, map);
                        return map;
                    }
                }
                if (row.getCell(5) != null && !row.getCell(5).getStringCellValue().isEmpty()) {
                    row.getCell(5).setCellType(CellType.STRING);
                    member.setEmail(row.getCell(5).getStringCellValue());//邮箱
                }
                if (row.getCell(6) != null && !row.getCell(6).getStringCellValue().isEmpty()) {
                    row.getCell(6).setCellType(CellType.STRING);
                    member.setJob(row.getCell(6).getStringCellValue());//职位
                }
                if (row.getCell(7) != null && !row.getCell(7).getStringCellValue().isEmpty()) {
                    row.getCell(7).setCellType(CellType.STRING);
                    String participationFundname = row.getCell(7).getStringCellValue();
                    String[] participationFund = participationFundname.split("，");
                    String[] participationFundIds = memberMapper.getDictionaryLabels("participationFund",participationFund);
                    if(participationFundIds == null || participationFundIds.length<=0){
                        map.put("code", "204");
                        map.put("msg", "文件第" + i + "行中参与科创基金信息不匹配,（多个参与科创基金使用，分开）");
                        // Constants.getSuccMsg(result, map);
                        return map;
                    }
                    String participationFundIds1 = JSONArray.toJSON(participationFundIds).toString();
                    member.setParticipationFundId(participationFundIds1);//参与科创基金
                }

               // member.setAddress(row.getCell(7).getStringCellValue());//联系地址
                if (row.getCell(8) != null && !row.getCell(8).getStringCellValue().isEmpty()) {
                    row.getCell(8).setCellType(CellType.STRING);
                    member.setRemark(row.getCell(8).getStringCellValue());//备注
                }

                member.setDelFlag(1);//默认存在
                member.setMemberType(code);//会员类型
                Member memberByEnterpriseCode = memberMapper.getMemberByEnterpriseCode(member.getEnterpriseCode());
                Member memberByEnterpriseName = memberMapper.getMemberByEnterpriseName(member.getEnterpriseName());
                //判断企业代码和会员名称是否存在，如果不存在则添加，如果存在则修改  && (memberByEnterpriseName == null || "".equals(memberByEnterpriseName.getId()))
                if (memberByEnterpriseCode == null || "".equals(memberByEnterpriseCode.getId()) ) {
                    super.insertBaseInfo(member); //调用父类中的通用新增方法
                    //setMemberDictionaryList(member, dictionaryList);//将新编号和套餐选择的集合赋值给会员信息
                    String pwd = getRandomNumAndChacters(6);
                    member.setPwd(pwd);
                    insertMemberList.add(member);
                } else {
                    if (memberByEnterpriseCode != null && !"".equals(memberByEnterpriseCode.getId()) &&
                            memberByEnterpriseName != null && !"".equals(memberByEnterpriseName.getId()) &&
                            memberByEnterpriseCode.getId().equals(memberByEnterpriseName.getId())) {
                        super.updateBaseInfo(member, memberByEnterpriseCode.getId());//调用父类中的通用修改方法
                        // setMemberDictionaryList(member, dictionaryList);//将查询的会员编号和套餐选择的集合赋值给会员信息
                        updateMemberList.add(member);
                    } else {
                        map.put("code", "204");
                        map.put("msg", "第" + i + "行中企业账号或者会员名称已存在，但企业账号和会员名称信息不匹配");
                        // Constants.getSuccMsg(result, map);
                        return map;
                    }
                }
            }
        }
        Integer arr = memberMapper.batchImportMember(insertMemberList, updateMemberList,4);
       // memberMapper.deleteWeight();
        if(code!=null && !code.equals(""))
            memberMapper.deleteWeightbyType(code);
        if (arr > 0) {
            map.put("code", "200");
            map.put("msg", "导入成功");
            // Constants.getSuccMsg(result, map);
        } else {
            map.put("code", "200");
            map.put("msg", "未知错误，执行行为0");
            // Constants.getSuccMsg(result, map);
        }
        return map;
    }

    /**
     * 供应商  Excel
     * @param
     * sheet  10列
     * @return
     */
    @Transactional
    public Map<String,Object> batchgongyingshang(Sheet sheet,String code){
        Map<String,Object> map = new HashMap<>();
        List<Member> insertMemberList = new ArrayList<>();
        List<Member> updateMemberList = new ArrayList<>();
        //更新department表
        List<Department> insertDeptList = new ArrayList<>();
        //物理总行数
        int rowTotal = sheet.getPhysicalNumberOfRows();
        //判断表格非空
        if (rowTotal <= 2) {
            map.put("code", "201");
            map.put("msg", "表格为空");
            // Constants.getSuccMsg(result, map);
            return map;
        }
        //获取最后一行
        int lastRowNum = sheet.getLastRowNum() + 1;
        //判断是否有空行
        if (rowTotal != lastRowNum) {
            map.put("code", "202");
            map.put("msg", "表格有空行");
            // Constants.getSuccMsg(result, map);
            return map;
        }

        //
        //解析
        Member member = null;
        for (int i = 2; i < lastRowNum; i++) {
            member = new Member();
            Row row = sheet.getRow(i);
            if (row.getCell(0) == null || row.getCell(1) == null ||
                    row.getCell(2) == null
                    //|| row.getCell(4) == null || row.getCell(5) == null ||
                    //row.getCell(3) == null || row.getCell(6) == null || row.getCell(7) == null
                   // || row.getCell(8) == null
                    ){
                map.put("code", "207");
                map.put("msg", "文件中有空格");
                //Constants.getSuccMsg(result, map);
                return map;
            }

            row.getCell(0).setCellType(CellType.STRING);
            row.getCell(1).setCellType(CellType.STRING);
            row.getCell(2).setCellType(CellType.STRING);
           // row.getCell(3).setCellType(CellType.STRING);
           /* try {
                row.getCell(4).setCellType(CellType.STRING);
            } catch (Exception e) {
                e.printStackTrace();
                map.put("code", "204");
                map.put("msg", "文件第" + i + "行中手机号格式应为文本格式");
                //Constants.getSuccMsg(result, map);
                return map;
            }*/
            //row.getCell(5).setCellType(CellType.STRING);
          // row.getCell(6).setCellType(CellType.STRING);
           // row.getCell(7).setCellType(CellType.STRING);
           // row.getCell(8).setCellType(CellType.STRING);
            /*try {
                row.getCell(8).setCellType(CellType.STRING);
            } catch (Exception e) {
                e.printStackTrace();
                map.put("code", "204");
                map.put("msg", "文件第" + i + "行中客户经理编号应为文本格式");
                //Constants.getSuccMsg(result, map);
                return map;
            }*/


            if (row.getCell(0).getStringCellValue().isEmpty() ||
                    row.getCell(1).getStringCellValue().isEmpty() ||
                    row.getCell(2).getStringCellValue().isEmpty()
                   // row.getCell(3).getStringCellValue().isEmpty() ||
                   // row.getCell(4).getStringCellValue().isEmpty() ||
                   // row.getCell(5).getStringCellValue().isEmpty() ||
                   // row.getCell(6).getStringCellValue().isEmpty() ||
                  //  row.getCell(7).getStringCellValue().isEmpty() ||
                   // row.getCell(8).getStringCellValue().isEmpty()
                    ) {

                StringBuffer str = new StringBuffer();
                if (row.getCell(0).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "行中供应商全称不能为空");
                if (row.getCell(1).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "行中企业帐号不能为空");
                if (row.getCell(2).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "行中供应商简称不能为空");
                /*if (row.getCell(3).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "行中联系人不能为空");
                if (row.getCell(4).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "行中联系电话不能为空");
                if (row.getCell(5).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "行中邮箱不能为空");
                if (row.getCell(6).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "行中职位不能为空");
                if (row.getCell(7).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "行中提供产品版块不能为空");
                if (row.getCell(8).getStringCellValue().isEmpty())
                    str.append("文件第" + i + "行中提供产品名称不能为空");*/

                map.put("code", "207");
                map.put("msg", str);
                // Constants.getSuccMsg(result, map);
                return map;
            } else {
                if(row.getCell(1).getStringCellValue().length()>10){
                    map.put("code", "205");
                    map.put("msg", "文件第" + i + "行企业账号限制长度10");
                    return map;
                }
               /* if (!Pattern.matches("2\\d{9}", row.getCell(0).getStringCellValue())) {
                    map.put("code", "205");
                    map.put("msg", "文件第" + i + "行企业账号由以2开头的10位数字组成");
                    Constants.getSuccMsg(result, map);
                    return result;
                }*/

               /* String pwd = row.getCell(1).getStringCellValue();
                if (!Pattern.matches(".{6,20}", pwd) || Pattern.matches("\\d+", pwd) || Pattern.matches("[a-zA-Z]+", pwd) || Pattern.matches("[\\W_]+", pwd)) {
                    map.put("code", "205");
                    map.put("msg", "文件第" + i + "行中密码为6-20位，并且由字母，数字和符号两种以上组合");
                    Constants.getSuccMsg(result, map);
                    return result;
                }*/


               /* Dictionary dictionary = memberMapper.selectDictionaryCodeByName(row.getCell(6).getStringCellValue());
                if (dictionary == null || "".equals(dictionary.getCode())) {
                    map.put("code", "205");
                    map.put("msg", "文件第" + i + "行中会员套餐不存在");
                    Constants.getSuccMsg(result, map);
                    return result;
                }*/

                /*User user = new User();
                user.setRealName(row.getCell(7).getStringCellValue());
                user.setId(row.getCell(8).getStringCellValue());*/

               /* List<User> userList = memberMapper.selectUserInfoByIdAndRealName(user);
                if (userList == null || userList.size() == 0) {
                    map.put("code", "204");
                    map.put("msg", "文件第" + i + "行中客户经理名称和客户经理编号不存在");
                    //Constants.getSuccMsg(result, map);
                    return map;
                } else if (userList != null && !userList.get(0).getRealName().equals(user.getRealName())) {
                    map.put("code", "204");
                    map.put("msg", "文件第" + i + "行中客户经理名称和客户经理编号信息不匹配");
                    // Constants.getSuccMsg(result, map);
                    return map;
                }*/


                member.setEnterpriseName(row.getCell(0).getStringCellValue());//lp全称
                member.setEnterpriseCode(row.getCell(1).getStringCellValue());//企业帐号
                member.setAbbreviation(row.getCell(2).getStringCellValue());//lp简称
                if (row.getCell(3) != null && !row.getCell(3).getStringCellValue().isEmpty()) {
                    row.getCell(3).setCellType(CellType.STRING);
                    member.setContact(row.getCell(3).getStringCellValue());//联系人
                }
                if (row.getCell(4) != null && !row.getCell(4).getStringCellValue().isEmpty()) {
                    try {
                        row.getCell(4).setCellType(CellType.STRING);
                        member.setPhone(row.getCell(4).getStringCellValue());//联系方式
                    } catch (Exception e) {
                        e.printStackTrace();
                        map.put("code", "204");
                        map.put("msg", "文件第" + i + "行中手机号格式应为文本格式");
                        //Constants.getSuccMsg(result, map);
                        return map;
                    }
                }
                if (row.getCell(5) != null && !row.getCell(5).getStringCellValue().isEmpty()) {
                    row.getCell(5).setCellType(CellType.STRING);
                    member.setEmail(row.getCell(5).getStringCellValue());//邮箱
                }
                if (row.getCell(6) != null && !row.getCell(6).getStringCellValue().isEmpty()) {
                    row.getCell(6).setCellType(CellType.STRING);
                    member.setJob(row.getCell(6).getStringCellValue());//职位
                }
                if (row.getCell(7) != null && !row.getCell(7).getStringCellValue().isEmpty()) {
                    row.getCell(7).setCellType(CellType.STRING);
                    member.setProductSection(row.getCell(7).getStringCellValue());//提供产品版块
                }
                if (row.getCell(8) != null && !row.getCell(8).getStringCellValue().isEmpty()) {
                    row.getCell(8).setCellType(CellType.STRING);
                    member.setProductName(row.getCell(8).getStringCellValue());//提供产品名称
                }

                if (row.getCell(9) != null && !row.getCell(9).getStringCellValue().isEmpty()) {
                    row.getCell(9).setCellType(CellType.STRING);
                    member.setRemark(row.getCell(9).getStringCellValue());//备注
                }

                member.setDelFlag(1);//默认存在
                member.setMemberType(code);//会员类型
                String pwd = getRandomNumAndChacters(6);
                member.setPwd(pwd);
                Member memberByEnterpriseCode = memberMapper.getMemberByEnterpriseCode(member.getEnterpriseCode());
                Member memberByEnterpriseName = memberMapper.getMemberByEnterpriseName(member.getEnterpriseName());
                //判断企业代码和会员名称是否存在，如果不存在则添加，如果存在则修改
                if ((memberByEnterpriseCode == null || "".equals(memberByEnterpriseCode.getId())) && (memberByEnterpriseName == null || "".equals(memberByEnterpriseName.getId()))) {
                    super.insertBaseInfo(member); //调用父类中的通用新增方法
                    //setMemberDictionaryList(member, dictionaryList);//将新编号和套餐选择的集合赋值给会员信息
                    Department dept = new Department();
                    dept.setId(member.getId());
                    dept.setCode(member.getEnterpriseCode());
                    dept.setName(member.getEnterpriseName());
                    dept.setParentid("001");
                    dept.setCreatorid(member.getCreatorId());
                    dept.setModifierid(member.getCreatorId());
                    dept.setIsused(true);
                    dept.setTreeabout("0/001/"+member.getId());
                    insertDeptList.add(dept);
                    insertMemberList.add(member);
                } else {
                    if (memberByEnterpriseCode != null && !"".equals(memberByEnterpriseCode.getId()) &&
                            memberByEnterpriseName != null && !"".equals(memberByEnterpriseName.getId()) &&
                            memberByEnterpriseCode.getId().equals(memberByEnterpriseName.getId())) {
                        super.updateBaseInfo(member, memberByEnterpriseCode.getId());//调用父类中的通用修改方法
                        // setMemberDictionaryList(member, dictionaryList);//将查询的会员编号和套餐选择的集合赋值给会员信息
                        updateMemberList.add(member);
                    } else {
                        map.put("code", "204");
                        map.put("msg", "第" + i + "行中企业账号或者会员名称已存在，但企业账号和会员名称信息不匹配");
                        // Constants.getSuccMsg(result, map);
                        return map;
                    }
                }
            }
        }
        Integer arr = memberMapper.batchImportMember(insertMemberList, updateMemberList,5);
        if(insertDeptList!=null && insertDeptList.size()>0) {
            Integer arr1 = memberMapper.batchImportDepartment(insertDeptList);
        }
       // memberMapper.deleteWeight();
        if(code!=null && !code.equals(""))
          memberMapper.deleteWeightbyType(code);
        if (arr > 0) {
            map.put("code", "200");
            map.put("msg", "导入成功");
            // Constants.getSuccMsg(result, map);
        } else {
            map.put("code", "200");
            map.put("msg", "未知错误，执行行为0");
            // Constants.getSuccMsg(result, map);
        }
        return map;
    }

    //根据机构编号查询出该机构下属用户的信息
    private Map<String, Object> selectUserByDepartmentIdAndUroleEqZeroList(String param) {
        Map<String, Object> result = new HashMap<>();
        String departmentId = JSONObject.parseObject(param).getString("departmentId");
        List<User> userList = memberMapper.selectUserByDepartmentIdAndUroleEqZeroList(departmentId);
        Constants.getSuccMsg(result, userList);
        return result;
    }

    //获取报告的属性信息
    private Map<String, Object> selectDictionaryByPropertyList(String param) {
        Map<String, Object> result = new HashMap<>();
        List<Dictionary> dictionaryList = memberMapper.selectDictionaryByPropertyList();
        Constants.getSuccMsg(result, dictionaryList);
        return result;
    }

    //获取小程序端的全部用户信息
    private Map<String, Object> selectUserByUroleEqOneList(String param) {
        Map<String, Object> result = new HashMap<>();
        List<User> userList = memberMapper.selectUserByUroleEqOneList();
        Constants.getSuccMsg(result, userList);
        return result;
    }

    /*
     * 查询机构的代码和名称信息(包含客户经理的用户信息在其机构子节点下)
     * param ---> {"userId":"xxx"}
     * userId 客户经理的用户编号
     */
    private Map<String, Object> selectDepartmentAndUserOfCodeAndNameList(String param) {
        Map<String, Object> result = new HashMap<>();
        String userId = JSONObject.parseObject(param).getString("userId");
        List<Department> departmentList = memberMapper.selectDepartmentOfCodeAndNameList();
        List<User> userList = memberMapper.selectUserOfDepartmentByIdList(userId);
        for (Department department : departmentList) {
            User user = userList.get(0);
            if (department != null && user != null && user.getDepartmentId().equals(department.getId())) {
                department.setChildren(userList);
                break;
            }
        }
        Constants.getSuccMsg(result, departmentList);
        return result;
    }

    //查询最高级机构的代码和名称信息
    private Map<String, Object> selectDepartmentOfCodeAndNameList(String param) {
        Map<String, Object> result = new HashMap<>();
        List<Department> departmentList = memberMapper.selectDepartmentOfCodeAndNameList();
        Constants.getSuccMsg(result, departmentList);
        return result;
    }


    /**
     * 根据会员编号查询会员下属用户信息
     * @param param
     * @return
     */
    private Map<String, Object> selectUserByMemberIdPageVo(String param) {
        Map<String, Object> result = new HashMap<>();
        PageVo pageVo = JSONObject.parseObject(param, PageVo.class);
        PageUtil page = pageVo.getPage();
        try {
            int userPageTotal = memberMapper.getUserPageTotal(pageVo);
            List<User> userList = memberMapper.selectUserByMemberIdPageVo(pageVo);
            PageVo<User> userPageVo = new PageVo<>();
            userPageVo.setPage(new PageUtil(page.getPageIndex(), page.getPageSize(), userPageTotal));
            userPageVo.setDataList(userList);
            Constants.getSuccMsg(result, userPageVo);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return result;
    }


    /**
     * 根据编号查询会员相关信息
     * @param param
     * @return
     */
    private Map<String, Object> getMemberById(String param) {
        Map<String, Object> result = new HashMap<>();
        try {
            String memberId = JSONObject.parseObject(param).getString("memberId");
            if(memberId != null && !memberId.equals("")){
                Member member = memberMapper.getMemberById(memberId);

                //创业公司  投资基金
                String memberType = member.getMemberType();
                if(memberType.equals("5")){
                    //犀牛标签，创业公司
                    String label = member.getRhinocerosLabelId();
                    if(label != null && !label.equals("")){
                        JSONArray jsonArray = JSONArray.parseArray(label);
                        String[] labels = jsonArray.toArray(new String[]{});
                        String[] labelsName = memberMapper.getDictionaryLabelsbyCode("rhinocerosLabel",labels);
                        String labelname = Arrays.toString(labelsName);
                        String labelname1 = labelname.substring(labelname.indexOf("[")+1, labelname.lastIndexOf("]"));
                        member.setRhinocerosLabel(labelname1);
                        // member.setRhinocerosLabelId(labelname1);
                    }
                    if(member.getInvestmentFundName()!=null && !member.getInvestmentFundName().equals("")){
                         member.setInvestmentFund(member.getInvestmentFundName());
                    }
                   /* //获取Gp对应的投资基金
                    String fundname = null;
                    if(member.getInvestmentFund()!=null && !member.getInvestmentFund().equals("")){
                        fundname = memberMapper.getFundbyId(member.getInvestmentFund());
                    }
                    member.setInvestmentFundName(fundname);*/
                }
                //GP关注行业
                String attentionIndustry = member.getAttentionIndustryId();
                if(attentionIndustry != null && !attentionIndustry.equals("")){
                    JSONArray jsonArray = JSONArray.parseArray(attentionIndustry);
                    String[] attentionIndustrys = jsonArray.toArray(new String[]{});
                    String[] attentionIndustrysName = memberMapper.getDictionaryLabelsbyCode("attentionIndustry",attentionIndustrys);
                    String attentionIndustryName = Arrays.toString(attentionIndustrysName);
                    String attentionIndustryName1 = attentionIndustryName.substring(attentionIndustryName.indexOf("[")+1, attentionIndustryName.lastIndexOf("]"));
                    member.setAttentionIndustry(attentionIndustryName1);
                }
                //GP关注阶段
                String attentionStage = member.getAttentionStageId();
                if(attentionStage != null && !attentionStage.equals("")){
                    JSONArray jsonArray = JSONArray.parseArray(attentionStage);
                    String[] attentionStages = jsonArray.toArray(new String[]{});
                    String[] attentionStagesname = memberMapper.getDictionaryLabelsbyCode("attentionStage",attentionStages);
                    String attentionStagename = Arrays.toString(attentionStagesname);
                    String attentionStagename1 = attentionStagename.substring(attentionStagename.indexOf("[")+1, attentionStagename.lastIndexOf("]"));
                    member.setAttentionStage(attentionStagename1);
                }
                //lp 参与科创基金
                String participationFundId = member.getParticipationFundId();
                if(participationFundId != null && !participationFundId.equals("")){
                    JSONArray jsonArray = JSONArray.parseArray(participationFundId);
                    String[] participationFundIds = jsonArray.toArray(new String[]{});
                    String[] participationFund = memberMapper.getDictionaryLabelsbyCode("participationFund",participationFundIds);
                    String participationFundname = Arrays.toString(participationFund);
                    String participationFundname1 = participationFundname.substring(participationFundname.indexOf("[")+1, participationFundname.lastIndexOf("]"));
                    member.setParticipationFund(participationFundname1);
                }

                Constants.getSuccMsg(result, member);
            }else{
                Constants.getErrMsg(result, "信息加载失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Constants.getErrMsg(result, "信息加载失败");
        }
        return result;
    }

    //查询客户经理相关信息
    private Map<String, Object> selectUserByUroleEqZeroList(String param) {
        Map<String, Object> result = new HashMap<>();
        List<User> mapList = memberMapper.selectUserByUroleEqZeroList();
        Constants.getSuccMsg(result, mapList);
        return result;
    }

    //查询会员套餐的代码和名称信息
    private Map<String, Object> selectComboOfCodeAndNameList(String param) {
        Map<String, Object> result = new HashMap<>();
        List<Dictionary> mapList = memberMapper.selectComboOfCodeAndNameList();
        Constants.getSuccMsg(result, mapList);
        return result;
    }

    //会员审核通过
   /* private Map<String, Object> memberReviewSuccess(String param) {
        Map<String, Object> result = new HashMap<>();

        JSONObject jsonObject = JSONObject.parseObject(param);
        String userId = jsonObject.getString("userId");//得到用户编号
        String memberId = jsonObject.getString("memberId");//得到会员编号

        MemberDetail memberDetail = new MemberDetail();
        super.insertBaseInfo(memberDetail);//使用父类中通用的新增代码
        memberDetail.setMemberId(memberId);
        memberDetail.setUserId(userId);
        memberDetail.setDelFlag(Integer.valueOf(1));//默认存在

        int update = memberMapper.memberReviewSuccess(memberDetail);
        Constants.getSuccMsg(result, update > 0);
        return result;
    }*/

    //会员审核未通过
    private Map<String, Object> memberReviewFaild(String param) {
        Map<String, Object> result = new HashMap<>();

        String userId = JSONObject.parseObject(param).getString("userId");
        User user = new User();
        super.updateBaseInfo(user, userId);//使用父类中通用的修改代码
        user.setCertificationMark("2"); //2表示审核失败

        int update = userInfoMapper.updateById(user);
        Constants.getSuccMsg(result, update > 0);
        return result;
    }

    //删除会员信息
    private Map<String, Object> deleteMember(String param) {
        Map<String, Object> result = new HashMap<>();

        String memberId = JSONObject.parseObject(param).getString("memberId");
        Member member = new Member();
        super.updateBaseInfo(member, memberId);//使用父类中通用的修改代码
        member.setDelFlag(Integer.valueOf(0));//数据库中0表示已删除
        Member m = memberMapper.getMemberById(memberId);
        //获取会员类型
        //供应商关联department操作
        if(m.getMemberType().equals("4")){
            Department dept = new Department();
            super.updateBaseInfo(dept,memberId);
            dept.setIsused(false);
            int update1 = departmentMapper.updateById(dept);
        }
        int update = memberMapper.updateById(member);
        Constants.getSuccMsg(result, update > 0);
        return result;
    }

    /**
     * 修改会员信息
     * @param param
     * @return
     */
    private Map<String, Object> updateMember(String param) {
        Map<String, Object> result = new HashMap<>();

        Member member = JSONObject.parseObject(param, Member.class);
        super.updateBaseInfo(member, member.getId());//使用父类中通用的修改代码

       /* if (null != member.getReportKinds() && member.getReportKinds().size() > 0) {
            List<Dictionary> dictionaryList = memberMapper.selectDictionaryById(member.getReportKinds());
            setMemberDictionaryList(member, dictionaryList);//设置会员关联的报告属性的值
        }*/
        Member memberByEnterpriseCode = memberMapper.getMemberByEnterpriseCode(member.getEnterpriseCode());
        if(memberByEnterpriseCode != null && !"".equals(memberByEnterpriseCode.getId())){
            if(!memberByEnterpriseCode.getId().equals(member.getId())){
                Constants.getSuccMsg(result, "企业帐号已存在");
                return result;
            }
        }
        //供应商，关联操作department表
        if(member.getMemberType().equals("4")){
            //根据Id获取部门department
            List<Department> depts = memberMapper.getDepartmentById(member.getId());
            if(depts!=null && depts.size()>0){
                if(!depts.get(0).getCode().equals(member.getEnterpriseCode()) || !depts.get(0).getName().equals(member.getEnterpriseName())){
                    //更新部门
                    int ud = memberMapper.updateDepartment(member.getEnterpriseName(),member.getEnterpriseCode(),depts.get(0).getId());
                }
            }else{
                //添加部门表
                Department dept = new Department();
                dept.setId(member.getId());
                dept.setCode(member.getEnterpriseCode());
                dept.setName(member.getEnterpriseName());
                dept.setParentid("001");
                dept.setCreatorid(member.getCreatorId());
                dept.setModifierid(member.getCreatorId());
                dept.setIsused(true);
                dept.setTreeabout("0/001/"+member.getId());
                List<Department> insertDeptList = new ArrayList<>();
                insertDeptList.add(dept);
                int arr = memberMapper.batchImportDepartment(insertDeptList);
            }
        }
        int update = memberMapper.updateMember(member);
        Constants.getSuccMsg(result, update > 0);
        return result;
    }


    /**
     * 新增会员信息
     * @param param
     * @return
     */
    private Map<String, Object> insertMember(String param) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> map = new HashMap<>();

        Member member = JSONObject.parseObject(param, Member.class);
        if(member == null || member.equals("")){
            Constants.getErrMsg(result, "数据添加失败");
            return result;
        }
        Member memberByEnterpriseCode = memberMapper.getMemberByEnterpriseCode(member.getEnterpriseCode());
        if(memberByEnterpriseCode != null && !"".equals(memberByEnterpriseCode.getId())){
            Constants.getErrMsg(result, "企业帐号已经存在");
            return result;
        }

        super.insertBaseInfo(member);//使用父类中通用的新增代码
        //添加会员信息表
        String pwd = getRandomNumAndChacters(6);
        member.setPwd(pwd);
        //会员类型为供应商，添加机构department
        if(member.getMemberType().equals("4")){
            //添加部门表
            Department dept = new Department();
            dept.setId(member.getId());
            dept.setCode(member.getEnterpriseCode());
            dept.setName(member.getEnterpriseName());
            dept.setParentid("001");
            dept.setCreatorid(member.getCreatorId());
            dept.setModifierid(member.getCreatorId());
            dept.setIsused(true);
            dept.setTreeabout("0/001/"+member.getId());
            List<Department> insertDeptList = new ArrayList<>();
            insertDeptList.add(dept);
            int arr = memberMapper.batchImportDepartment(insertDeptList);
        }
        int insert = memberMapper.insertMember(member);
        Constants.getSuccMsg(result, insert > 0);
        return result;
    }



    /**
     * 分页获取会员的信息
     * @param param
     * @return
     */
    private Map<String, Object> getMemberInfoPageVo(String param) {
        Map<String, Object> result = new HashMap<>();
        //memberMapper.deleteWeight();
        PageVo pageVo = JSONObject.parseObject(param, PageVo.class);
        PageUtil page = pageVo.getPage();
        try {
            int pageTotal = memberMapper.getPageTotal(pageVo);
            List<Member> memberList = memberMapper.getMemberInfoPageVo(pageVo);
            PageVo<Member> memberPageVo = new PageVo<>();
            memberPageVo.setPage(new PageUtil(page.getPageIndex(), page.getPageSize(), pageTotal));
            memberPageVo.setDataList(memberList);
            Constants.getSuccMsg(result, memberPageVo);
        } catch (Exception e) {
            logger.error(e.getMessage());
            Constants.getErrMsg(result, "信息加载失败");
        }
        return result;
    }

  /*  //设置会员关联的报告属性的值
    private void setMemberDictionaryList(Member member, List<Dictionary> dictionaryList) {
        //用来接收会员关联的报告属性信息
        List<MemberDictionary> memberDictionaryList = new ArrayList<>();
        //循环得到选中的属性值
        if (dictionaryList != null && dictionaryList.size() > 0) {
            for (Dictionary dictionary : dictionaryList) {
                MemberDictionary memberDictionary = new MemberDictionary();
                super.insertBaseInfo(memberDictionary);//调用父类中通用的新增赋值模块
                memberDictionary.setMemberId(member.getId());
                memberDictionary.setReportKind(dictionary.getCode());
                memberDictionary.setPropertyKind(dictionary.getKind());

                memberDictionaryList.add(memberDictionary);
            }
        }
        //member.setMemberDictionaryList(memberDictionaryList);
    }*/

    /**
     * 获取会员审批list分页
     * @param param
     * @return
     */
    private Map<String,Object> getmemberDetailList(String param){
        Map<String, Object> result = new HashMap<>();
        PageVo pageVo = JSONObject.parseObject(param,PageVo.class);
        memberMapper.deleteWeightMemberUser();
        PageUtil page = pageVo.getPage();
        try {
            BaseUserInfo u = super.getUserInfo();
            //获取用户角色
            User u1 = userInfoMapper.getuRolebyId(u.getId());
            Integer count =  userInfoMapper.getCountByUser(u.getId(),"memberapproval");
            if(count <=0 && !StringUtils.equalsIgnoreCase("IR",u1.getJob())){
                Constants.getSuccMsg(result, null);
                return result;
            }
            if(u1==null||u1.equals("")||u1.getURole()== null ||u1.getURole().equals("")){
                Constants.getErrMsg(result, "用户角色获取失败");
                return result;
            }

            if(!u1.getURole().equals("0")){
                List<String> uroles1 = new ArrayList<>();
                uroles1 = this.geturolelist("membertype",u1.getURole(),null,uroles1);
                if(uroles1 == null || uroles1.size() <= 0){
                    uroles1 = new ArrayList<>();
                    uroles1.add("0");
                }
                if(u1.getURole().equals("2")){
                    uroles1.add("2");
                }
                Object searchdata = pageVo.getSearchdata();
                Map<String, Object> searchMap = JSONObject.parseObject(JSONObject.toJSONString(searchdata), HashMap.class);
                //urole.put("uRole",u1.getURole());
                searchMap.put("uRole1",uroles1);
                pageVo.setSearchdata(searchMap);
            }else{
                Constants.getErrMsg(result, "用户角色获取失败");
                return result;
            }
            //GP用户获取所属公司基金
            String ci = "";
            if(u1.getURole().equals("1")){
                //ci =  memberMapper.getInvestmentFundByUser(u1.getId());
                ci =  memberMapper.getmemberIdByUser(u1.getId());
                List<String> gpids = memberMapper.getsameGp(ci);
                Object searchdata = pageVo.getSearchdata();
                Map<String, Object> searchMap = JSONObject.parseObject(JSONObject.toJSONString(searchdata), HashMap.class);
                searchMap.put("ci",gpids);
                pageVo.setSearchdata(searchMap);
            }

            //获取总条数
            Integer total = memberMapper.getMemberDetailPageTotal(pageVo);
            List<MemberUser> memberDetails = memberMapper.getMemberDetails(pageVo);
            memberDetails.forEach((MemberUser a) -> {
                if(StringUtils.isNotEmpty(a.getApprovalstatus()) && !StringUtils.equals("1",a.getApprovalstatus())){
                    a.setModifierName("");
                }
            });
            PageVo<MemberUser> memberDetailsPageVo = new PageVo<>();
            memberDetailsPageVo.setPage(new PageUtil(page.getPageIndex(), page.getPageSize(), total));
            memberDetailsPageVo.setDataList(memberDetails);
            //System.out.println("-----"+memberDetails.toString());
            Constants.getSuccMsg(result, memberDetailsPageVo);
        } catch (Exception e) {
            e.printStackTrace();
            Constants.getErrMsg(result, "信息加载失败");
        }
            return result;
    }

    /**
     * 获取子集list
     * @param kind
     * @param parentId
     * @param dictionaries
     * @return
     */
    private List<String> geturolelist(String kind, String parentId, List<Dictionary> dictionaries,List<String> uroles1){

        if(parentId!=null && !parentId.equals("")){
            dictionaries = productMapper.getProductTypes(kind,parentId);
        }
        if(dictionaries != null && dictionaries.size()>0){
            for(Dictionary d:dictionaries){
                uroles1.add(d.getCode());
            }
            for(int i = 0; i < dictionaries.size(); i++){
                //获取dictionary子集
               // Dictionary dictionary = dictionaries.get(i);
                List<Dictionary> children = productMapper.getProductTypes(kind,dictionaries.get(i).getCode());
                dictionaries.get(i).setChildren(children);
               // dictionary.setChildren(children);
              //  dictionaries.set(i,dictionary);
                this.geturolelist(kind, "", children,uroles1);
            }
            return uroles1;
        }else{
            return null;
        }
    }
    /**
     * 获取认证审核明细byid
     * @param param
     * @return
     */
    private Map<String,Object> getMemberDetailById(String param){
        Map<String,Object> result = new HashMap<>();
        try {
            String  id = JSONObject.parseObject(param).getString("id");
            MemberUser d = memberMapper.getMemberDetailById(id);
            //System.out.println("-------"+d.toString());
            Constants.getSuccMsg(result,d);
        } catch (Exception e) {
            e.printStackTrace();
            Constants.getErrMsg(result, "信息加载失败");
        }
        return result;
    }

    private int setchuangyeApprovalstatus(String memberuserId){
        //获取审核记录相关信息
        MemberUser memberUser = memberMapper.getMemberDetailById2(memberuserId);
        BaseUserInfo u = super.getUserInfo();
        //当前审批人的所属机构
        User u1 = memberMapper.getUserInfo(u.getId());
        //创业公司，科创人员审批，不可再次驳回  1GP, 2科创
        if(memberUser.getURole().equals("5") && u1.getURole().equals("1")){

            //数据库中状态是3，判断审批人是哪家gp,已经审批过的，返回不可审批
            if(memberUser.getApprovalstatus().equals("3")){

                if(u1.getMemberId().equals(memberUser.getModifymemberId())){
                    return 0;
                }else{
                    return 2;
                }
            }//数据库中状态是0，判断关联基金的GP相同的数量，>1 状态设为3，<=1 设为2
            else if(memberUser.getApprovalstatus().equals("0")){
                List<String> gps = memberMapper.getsameGp(memberUser.getInvestmentFund());
                if(gps!=null && gps.size()>1){
                    return 3;
                }else{
                    return 2;
                }
            }else{
                return 2;
            }
        }else{
            return 2;
        }
    }

    /**
     *
     * 更新审批状态
     * @param param
     * @return
     */
    @Transactional
    public Map<String,Object> updateMemberDetailApprovalstatus(String param){
        Map<String,Object> result = new HashMap<>();
        MemberUser md = null;
        try {
             md = JSONObject.parseObject(param,MemberUser.class);
        } catch (Exception e) {
            e.printStackTrace();
            Constants.getErrMsg(result, "信息加载失败");
            return  result;
        }
        super.updateBaseInfo(md,md.getId());
        int u1 = 0;
        //获取审批状态
        String status = memberMapper.getMemberDetailstatus(md);
        if(md.getMemberType().equals("4")){
            md.setDepartmentId(md.getMemberId());
        }else{
            md.setDepartmentId("001");
        }
        // 0  未审批
        if(status.equals("0")){
            u1 = memberMapper.updateMemberUsernoUrole(md);
        }else if(status.equals("1")){
            //更新member_user表，user表中的 联系电话，邮箱
            u1 = memberMapper.updateMemberUser(md);
        }else if(status.equals("2")){
            u1 = memberMapper.updateMemberUsernoUrole2(md);
        }
            if(md != null && !md.equals("")) {
                if(md.getApprovalstatus().equals("2")){
                    int re = this.setchuangyeApprovalstatus(md.getId());
                    if(re == 2){
                        md.setApprovalstatus("2");
                    }else if(re == 3){
                        md.setApprovalstatus("3");
                    }else{
                        Constants.getErrMsg(result, "已经审批过了！");
                        return result;
                    }
                }
               // BaseUserInfo u = super.getUserInfo();
               // String status = memberMapper.getMemberDetailstatus(md);
                //if(status.equals("0")){
                    //super.updateBaseInfo(md, md.getId());//使用父类中通用的修改代码
                    Integer i = memberMapper.updateApprovalstatus(md);
                    //状态为2时，驳回，记录到消息表notes
                    if(i>0 && md.getApprovalstatus().equals("2")){
                        int i2 = memberMapper.updateUserCherk(md.getId());
                        MemberUser memberUser = memberMapper.getMemberDetailById(md.getId());
                        Integer i1 = setNotes("001","001","001",memberUser.getUserId());
                    }
                    if(i>0 && md.getApprovalstatus().equals("1")){
                        //更新user表 uRole
                      int i2 =  memberMapper.updateUseruRole(md.getId());
                        MemberUser memberUser = memberMapper.getMemberDetailById(md.getId());
                        Integer i1 = setNotes("003","003","003",memberUser.getUserId());
                  }
                    Constants.getSuccMsg(result,i>0);
                /*}else{
                    Constants.getErrMsg(result,"已审批，不可重复审批");
                }*/
            }else{
                Constants.getErrMsg(result, "信息加载失败");
            }

        return  result;
    }

    /**
     * 设置消息
     * 审批通知
     * @param title
     * @param content
     * @return
     */
    public Integer setNotes(String notesid,String title,String content,String userId){
        Notes notes = new Notes();

        List<NotesDetail> details = new ArrayList<NotesDetail>();

        if(userId == null || userId.equals("")){
            return -1;
        }
        Integer i1 = 0;
        //获取消息id
        Dictionary dnotes = memberMapper.selectDictionaryByCode(notesid,"notesid");
        //Notes notes1 = new Notes();
        if(dnotes == null || dnotes.equals("") || dnotes.getName() == null ||dnotes.getName().equals("")){
            //数据库获取title
            Dictionary dtitle = memberMapper.selectDictionaryByCode(title,"title");
            Dictionary dcontent = memberMapper.selectDictionaryByCode(content,"content");
            if(dcontent == null || dcontent.equals("") || dcontent.getName() == null ||dcontent.getName().equals("")){
                if(content.equals("003")){
                    notes.setContent("您好，您的认证申请已经通过。您现在可以浏览科创生态小程序全部信息和服务。");
                }else if(content.equals("001")){
                    notes.setContent("您好，您的认证申请未通过，请联系管理员。");
                }
            }else{
                notes.setContent(dcontent.getName());
            }
            if(dtitle == null || dtitle.equals("") || dtitle.getName() == null ||dtitle.getName().equals("")){
                if(title.equals("003")){
                    notes.setTitle("认证通过");
                }else if(title.equals("001")){
                    notes.setTitle("认证驳回");
                }

            }else{
                notes.setTitle(dtitle.getName());
            }
            notes.setTaskType("user");
            notes.setIsFeedBack(0);
            notes.setStatus("1");
            notes.setFlag(3);
            super.insertBaseInfo(notes);
            NotesDetail notesDetail = new NotesDetail();
            notesDetail.setNotesId(notes.getId());
            notesDetail.setUserId(userId);
            notesDetail.setStatus("0");
            super.insertBaseInfo(notesDetail);
            details.add(notesDetail);

            notes.setNotesDetailList(details);
            i1 = memberMapper.insertNotes(notes);
        }else {
            //获取消息
            List<Notes> n = notesMapper.getNoteById(dnotes.getName());
            if(n == null || n.size() <= 0){
                //添加消息
                //数据库获取title
                Dictionary dtitle = memberMapper.selectDictionaryByCode(title,"title");
                Dictionary dcontent = memberMapper.selectDictionaryByCode(content,"content");
                if(dcontent == null || dcontent.equals("") || dcontent.getName() == null ||dcontent.getName().equals("")){
                    if(content.equals("003")){
                        notes.setContent("您好，您的认证申请已经通过。您现在可以浏览科创生态小程序全部信息和服务。");
                    }else if(content.equals("001")){
                        notes.setContent("您好，您的认证申请未通过，请联系管理员。");
                    }
                }else{
                    notes.setContent(dcontent.getName());
                }
                if(dtitle == null || dtitle.equals("") || dtitle.getName() == null ||dtitle.getName().equals("")){
                    if(title.equals("003")){
                        notes.setTitle("认证通过");
                    }else if(title.equals("001")){
                        notes.setTitle("认证驳回");
                    }
                }else{
                    notes.setTitle(dtitle.getName());
                }
                notes.setTaskType("user");
                notes.setIsFeedBack(0);
                notes.setStatus("1");
                notes.setFlag(3);
                super.insertBaseInfo(notes);
                notes.setId(dnotes.getName());
                int i2 = notesMapper.insertNote(notes);
            }else{
                notes = n.get(0);
            }

            NotesDetail notesDetail = new NotesDetail();
            notesDetail.setNotesId(dnotes.getName());
            notesDetail.setUserId(userId);
            notesDetail.setStatus("0");
            super.insertBaseInfo(notesDetail);
            details.add(notesDetail);

            i1 = memberMapper.insertNotesDetails(details);
        }
        List<String> userIds = new ArrayList<String>();
        userIds.add(userId);
        notes.setUserIds(userIds);
        //发送微信消息
        String templateId = "2WYFptTPrbIExm97CLGZ-iaveFA19WQGxwi5q7yyeRI";
        notes.setTemplateId(templateId);
        this.sendweixin(notes);
        this.sendEmaillist(notes);

        return i1;

    }

   /* *//**
     * 是否需要做异步
     * @param notes
     *//*
    public void sendweixin(Notes notes){
        String templateId="Slxjlr_FPAdcwrIFuR4KIUoA53KggalBxkS2ICbQUik";
        List<String> userIds = notes.getUserIds();
        //根据userId,获取user表中unionid是否存在
        List<User> users = notesMapper.getUserUnionId(userIds);
        if(users!=null && users.size()>0){
            //设置消息内容
            PushEntity pushEntity = new PushEntity();
            pushEntity.setTitle(notes.getTitle());
            pushEntity.setKeywords1(notes.getContent());
            pushEntity.setKeywords2(notes.getTitle());
            Date date = new Date();
            SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            pushEntity.setKeywords3(dateFormat.format(date));
            List<String> openIds = new ArrayList<>();
            List<String> unusers = new ArrayList<>();
            for (int i = 0; i<users.size() ; i++) {
                if (users.get(i) != null && !users.get(i).equals("")) {
                    if (users.get(i).getOpenId() != null && !users.get(i).getOpenId().equals("")) {
                        openIds.add(users.get(i).getOpenId());
                    } else {
                        //unionid为空，不做处理
                        //unionid不为空，openId为空，加入到list
                        if (users.get(i).getUnionid() != null && !users.get(i).getUnionid().equals("")) {
                            unusers.add(users.get(i).getId());
                        }
                    }
                }
            }
                //如果有unionud不为空，openId为空的情况，进行一次微信公众号同步操作
                if(unusers!=null && unusers.size()>0){
                    String accessToken = (String) redisTemplate.opsForValue().get("access_token");
                    //获取最新的公众号openId
                    List<String> nextopenId = notesMapper.getNewOpenId();
                    List<String> list = wechatpushUtil.GetOpenIdList(nextopenId.get(0),accessToken);
                    //将openId存库
                    int e = pushMapper.insertweixinIfo(list);
                    //获取weixin_info表中unionid为空的对象
                    List<WeixinInfo> unionidemptys = notesMapper.getUnionidempty();
                    for (int j=0;j<unionidemptys.size();j++){
                        String unionid = wechatpushUtil.getUnionId(unionidemptys.get(j).getOpenId(),accessToken);
                        unionidemptys.get(j).setUnionid(unionid);
                    }
                    //将unionid入库
                    int r = pushMapper.insertUnionid(unionidemptys);
                    List<User> users1 = notesMapper.getUserUnionId(unusers);
                    for (int i = 0; i<users1.size() ; i++) {
                        if (users1.get(i) != null && !users1.get(i).equals("")) {
                            if (users1.get(i).getOpenId() != null && !users1.get(i).getOpenId().equals("")) {
                                openIds.add(users1.get(i).getOpenId());
                            }
                        }
                    }
                }
                //获取appid /url
            //根据kind获取字典表list
            List<Dictionary> appids = memberMapper.getMemberTypelist("weixinappId");
            List<Dictionary> urls = memberMapper.getMemberTypelist("weixinurl");
            pushEntity.setAppId(appids.get(0).getName());
            pushEntity.setUrl(urls.get(0).getName());
            //发送消息
            wechatpushUtil.sendMsg(appId,secret,pushEntity,templateId,openIds);
        }
    }*/
    /**
     * 是否需要做异步
     * @param notes
     */
    public void sendweixin(Notes notes){
        //审核人姓名
        String userName = notesMapper.getRealName(notes.getUserIds().get(0));
        notes.setUserName(userName);
        Thread t =  new SendWechatThread(sendwechat,notes);
        t.start();

    }
    public void sendEmaillist(Notes notes){
        List<String> emailList = null;

        if(notes.getUserIds()!=null && notes.getUserIds().size()>0) {
            //获取nodes_detail表中对应人员的数据
            emailList = notesMapper.getUserEmails(notes.getUserIds());
        }
        if(emailList != null && emailList.size() > 0){
            //根据kind获取字典表list
            List<Dictionary> d = memberMapper.getEmail("email");
            if(d != null && d.size() > 0) {
                //获取通知类型flag，2和3发送邮件.通知人群 all
                ExecutorService es = Executors.newFixedThreadPool(10);
                for (String email : emailList) {
                    es.execute(new SendMailThread(email, notes.getTitle(), notes.getContent(), d.get(0).getName(), d.get(0).getCode(), d.get(0).getPicurl()));
                }
                es.shutdown();
            }
        }
    }

    /**
     * 批量审核
     * @param param
     * @return
     */
    /*private Map<String,Object> batchupdateApprovalstatus(String param){
        Map<String,Object> result = new HashMap<>();
        List<MemberUser> lists = new ArrayList<MemberUser>();
        try {
            lists = JSONObject.parseArray(param,MemberUser.class);
        } catch (Exception e) {
            e.printStackTrace();
            Constants.getErrMsg(result, "信息加载失败");
            return result;
        }
        if(lists==null||lists.size()<=0){
            Constants.getErrMsg(result, "信息加载失败");
        }else {
            super.updateBaseInfo(md, md.getId());//使用父类中通用的修改代码
            Integer i = memberMapper.batchupdateApprovalstatus(lists);
            if(i>0 && lists.get(0).getApprovalstatus().equals("2")){
                //获取消息id
                Dictionary dnotes = memberMapper.selectDictionaryByCode("001","notesid");
                if(dnotes == null || dnotes.equals("") || dnotes.getName() == null ||dnotes.getName().equals("")){
                    Notes notes = new Notes();
                    //数据库获取title
                    Dictionary dtitle = memberMapper.selectDictionaryByCode("001","title");
                    Dictionary dcontent = memberMapper.selectDictionaryByCode("001","content");
                    if(dcontent == null || dcontent.equals("") || dcontent.getName() == null ||dcontent.getName().equals("")){
                        notes.setContent("您好，您的认证申请被驳回，请联系管理员。");
                    }else{
                        notes.setContent(dcontent.getName());
                    }
                    if(dtitle == null || dtitle.equals("") || dtitle.getName() == null ||dtitle.getName().equals("")){
                        notes.setTitle("用户认证驳回通知");
                    }else{
                        notes.setTitle(dtitle.getName());
                    }
                }else{

                }
            }
        }

    }*/

    /**
     * 获取会员类型list
     * 一级展示，无层次
     * @return
     */
    private Map<String,Object> getMemberTypeList(){
        Map<String,Object> result = new HashMap<>();
        //根据kind为membertype获取会员类型
        List<Dictionary> d = memberMapper.getMemberTypelist("membertype");
        Constants.getSuccMsg(result,d);
        return result;
    }


    /**
     * 获取字典表
     *  @param //kind
     * membertype  会员类型
     *  attentionIndustry  关注行业
     *  attentionStage 关注阶段
     * industryClassification 科创行业分类
     * directInvestmentClassification 科创直投分类
     * rhinocerosLabel 犀牛标签
     * firstInvestmentStage 首次投资阶段
     * participationFund 参与科创基金
     * 产品标签  productlabel
     * @return
     */
    private Map<String,Object> getDictionaryList(String param){
        Map<String,Object> result = new HashMap<>();
        String kind = JSONObject.parseObject(param).getString("kind");
        //根据kind获取字典表list
        List<Dictionary> d = memberMapper.getMemberTypelist(kind);
        Constants.getSuccMsg(result,d);
        return result;
    }

    /**
     * 获取会员类型list
     * 多层次
     * @return
     */
    private Map<String,Object> getMemberTypeList2(){
        Map<String,Object> result = new HashMap<>();
        try {
            List<Dictionary> membertype = this.getdictionarys("membertype","0",null);
            //System.out.println("--------"+prodcuttype1.toString());
            Constants.getSuccMsg(result,membertype);
        } catch (Exception e) {
            e.printStackTrace();
            Constants.getErrMsg(result, Constants.RESULT_NO_DATA);
        }
        return result;
    }

    /**
     * 获取会员类型list
     * @param kind
     * @param parentId
     * @param dictionaries
     * @return
     */
    private List<Dictionary> getdictionarys(String kind, String parentId, List<Dictionary> dictionaries){

        if(parentId.equals("0")){
            dictionaries = productMapper.getProductTypes(kind,"0");
        }
        if(dictionaries != null && dictionaries.size()>0){
            for(int i = 0; i < dictionaries.size(); i++){
                //获取dictionary子集
                Dictionary dictionary = dictionaries.get(i);
                List<Dictionary> children = productMapper.getProductTypes(kind,dictionary.getCode());
                dictionary.setChildren(children);
                dictionaries.set(i,dictionary);
                this.getdictionarys(kind, "", children);
            }
            return dictionaries;
        }else{
            return null;
        }
    }

    /**
     * 产生字母和数字的随机组合,长度为length
     * @param length
     * @return 一个字母和数字随机组合的String型数据
     */
    public static String getRandomNumAndChacters(int length){
        Random random = new Random();
        String str = null;
        do {
            str = new String();
            for (int i = 0; i < length; i++) {
                boolean b = random.nextBoolean();
                if(b){
                    int choice = random.nextBoolean() ? 65 : 97;//随机到65：大写字母  97：小写字母
                    str += (char)(choice+random.nextInt(26));
                }else{
                    str += String.valueOf(random.nextInt(10));
                }
            }
        } while (validate(str));//验证是否为字母和数字的组合
        return str;
    }
    /**
     * 验证产生的随机字母数字组合是否是纯数字或者存字母
     * @param str
     * @return   true:纯字母或者纯数字组成；false：不是纯字母或者纯数字组成
     */
    public static boolean validate(String str){
        Pattern pattern = Pattern.compile("^([0-9]+)|([A-Za-z]+)$");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * 根据会员类型
     * 获取会员id 和 name
     * @param param
     * @return
     */
   private Map<String,Object> getMemberByType(String param){

       Map<String,Object> result = new HashMap<>();
       try {
           String memberType = JSONObject.parseObject(param).getString("memberType");
           if(memberType == null || memberType.equals("")){
               Constants.getErrMsg(result, "数据异常");
               return result;
           }
           List<Member> members = memberMapper.getMemberByType(memberType);
           Constants.getSuccMsg(result,members);
       } catch (Exception e) {
           e.printStackTrace();
           Constants.getErrMsg(result, Constants.RESULT_NO_DATA);
       }
       return result;
   }

    /**
     *
     * 获取未审批的总条数
     * @return
     */
   private Map<String,Object> getUnApprovedCount(String param){
        Map<String,Object> result = new HashMap<>();
       try {
           memberMapper.deleteWeightMemberUser();
           String approvalstatus = JSONObject.parseObject(param).getString("approvalstatus");
           Integer t = memberMapper.getUnApprovedCount(approvalstatus);
           Constants.getSuccMsg(result,t);
       } catch (Exception e) {
           e.printStackTrace();
           Constants.getSuccMsg(result,"信息加载异常");
       }
       return result;
   }

    /**
     * 删除会员认证
     * @param param
     * @return
     */
    private Map<String,Object> delMemberUserById(String param){
       Map<String,Object> result = new HashMap<>();
        //获取前端传递的id
        String id = JSONObject.parseObject(param).getString("id");
        String userId = JSONObject.parseObject(param).getString("userId");
        MemberUser mu = new MemberUser();
        mu.setUserId(userId);
        super.updateBaseInfo(mu, id);//使用父类中通用的修改代码
        mu.setDelFlag(0);
        int delmember = memberMapper.delMemberuserById(mu);
        Constants.getSuccMsg(result, delmember > 0);
        return result;
    }

    /**
     * 获取删除记录list
     * @return
     */
    private Map<String,Object> getDelMemberUsers(){
        Map<String, Object> result = new HashMap<>();
        PageVo pageVo = new PageVo();
        Map<String, Object> deflag = new HashMap<>();
        deflag.put("delFlag","0");
        pageVo.setSearchdata(deflag);
        List<MemberUser> mu = memberMapper.getDelMemberUsers(pageVo);
        Constants.getSuccMsg(result, mu);
        return result;
    }
    /**
     * 更新认证信息
     * @param param
     * @return
     */
    private Map<String, Object> updateMemberUser(String param){
        Map<String, Object> result = new HashMap<>();
        MemberUser md = JSONObject.parseObject(param,MemberUser.class);
        //判断机构类型与用户urole是否一致
        int i = memberMapper.selectmember(md.getMemberType(),md.getMemberId());
        if(i <= 0){
            Constants.getErrMsg(result,"会员类型不匹配");
            return result;
        }else{
            super.updateBaseInfo(md,md.getId());
            int u1 = 0;
            //获取审批状态
            String status = memberMapper.getMemberDetailstatus(md);
            if(md.getMemberType().equals("4")){
                md.setDepartmentId(md.getMemberId());
            }else{
                md.setDepartmentId("001");
            }
            // 0  未审批
            if(status.equals("0")){
                u1 = memberMapper.updateMemberUsernoUrole(md);
            }else if(status.equals("1")){
                //更新member_user表，user表中的 联系电话，邮箱
                u1 = memberMapper.updateMemberUser(md);
            }else if(status.equals("2")){
                u1 = memberMapper.updateMemberUsernoUrole2(md);
            }
            Constants.getSuccMsg(result, u1>0);
            return result;
        }
    }
   //判断日期格式是否正确
   public static boolean isValidDate(String sDate) {

       String datePattern1 = "\\d{4}-\\d{2}-\\d{2}";

       String datePattern2 = "^((\\d{2}(([02468][048])|([13579][26]))"

               + "[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|"

               + "(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?"

               + "((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?("

               + "(((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?"

               + "((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";

       if ((sDate != null)) {

           Pattern pattern = Pattern.compile(datePattern1);

           Matcher match = pattern.matcher(sDate);

           if (match.matches()) {

                   pattern = Pattern.compile(datePattern2);

               match = pattern.matcher(sDate);

               return match.matches();

           }

           else {

               return false;

           }

       }

       return false;

   }

   /* *//**
     * 会员导出
     *//*
    private Map<String,Object> exportExcel(String param){
        Map<String,Object> result = new HashMap<>();

    }*/
    @RequestMapping(value = {"/Memberload"}, method = RequestMethod.POST, produces = "application/octet-stream;charset=utf-8")
    public String ExportExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String memberId = request.getParameter("memberId");// 设置文件名，根据业务需要替换成要下载的文件名
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYYMMDDhhmmss");
        String now = dateFormat.format(new Date());
        String exportFileName = "审核" + now + ".xlsx";
        String[] headers = {"用户编号", "公司编号", "会员类型", "审批状态", "会员类型名称", "会员名称", "联系人", "联系电话",
                "邮箱", "真实姓名", "修改人"};//导出的Excel头部，这个要根据自己项目改一下
        List<MemberUserExport> interfacesVOPageData = memberMapper.filterQuery(memberId);

        //下面的完全不动就行了（Excel数据中不包含图片）
        // 声明一个工作薄
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 生成一个表格
        XSSFSheet sheet = workbook.createSheet();
        // 设置表格默认列宽度为15个字节
        // sheet.setDefaultColumnWidth((short) 18);
        for (int i = 0; i < headers.length; i++) {
            int colWidth = sheet.getColumnWidth(i) * 2;
            if (colWidth < 255 * 256) {
                sheet.setColumnWidth(i, colWidth < 3000 ? 3000 : colWidth);
            } else {
                sheet.setColumnWidth(i, 6000);
            }
        }
        XSSFRow row = sheet.createRow(0);
        for (short i = 0; i < headers.length; i++) {
            XSSFCell cell = row.createCell(i);
            XSSFRichTextString text = new XSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }
        //遍历集合数据，产生数据行
        Iterator it = interfacesVOPageData.iterator();
        int index = 0;
        while (it.hasNext()) {
            index++;
            row = sheet.createRow(index);
            MemberUserExport objectValue = (MemberUserExport) it.next();
            //利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
            Field[] fields = objectValue.getClass().getDeclaredFields();
            for (short i = 0; i < fields.length; i++) { // 这里遍历fields 每个属性
                XSSFCell cell = row.createCell(i);
                Field field = fields[i];
                String fieldName = field.getName();
                String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                // 提取getName() 这样方法
                try {
                    Class tCls = objectValue.getClass();
                    Method getMethod = tCls.getMethod(getMethodName,
                            new Class[]{});
                    Object value = getMethod.invoke(objectValue, new Object[]{});
                    // 通过底层invoke 开启bug拿到值
                    String textValue = null;
                    // 对于日期的值 转为日期 非日期的值当做字符串处理
                    if (value instanceof Date) {
                        Date date = (Date) value;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        textValue = sdf.format(date);
                    } else {
                        //其它数据类型都当作字符串简单处理
                        if (value == null) {
                            textValue = "";
                        } else {
                            textValue = value.toString();
                        }
                    }
                    cell.setCellValue(textValue);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel;charset=utf-8");// 设置contentType为excel格式
        response.setHeader("Content-disposition", "attachment;filename=" + exportFileName);//默认Excel名称
        response.flushBuffer();
        workbook.write(response.getOutputStream());
        return "succ";
    }

    /**
     * 获取GP类型的公司，ID,NAME
     *
     */
    private Map<String, Object> getGPMemberlist(){
        Map<String, Object> result = new HashMap<>();
        //List<Member> members = memberMapper.getMemberByType("1");
        List<Member> members = memberMapper.getinvestmentFundlist("1");
        Constants.getSuccMsg(result,members);
        return result;
    }

    //获取供应商id name
    private Map<String, Object> getGYList(){
        Map<String, Object> result = new HashMap<>();
        List<Department> departments = memberMapper.getDeptList("4");
        Constants.getSuccMsg(result, departments);
        return result;
    }
}
