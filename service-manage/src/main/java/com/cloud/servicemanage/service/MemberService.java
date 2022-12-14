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
 * ?????????????????????????????????
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



    //??????????????????????????????jar???org.slf4j.Logger
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

    //??????????????????????????????
    private Map<String, Object> getMemberByEnterpriseCodeCheck(String param) {
        Map<String, Object> result = new HashMap<>();
        String enterpriseCode = JSONObject.parseObject(param).getString("enterpriseCode");
        Member memberByEnterpriseCode = memberMapper.getMemberByEnterpriseCode(enterpriseCode);
        boolean arr = memberByEnterpriseCode == null ? false : true;
        Constants.getSuccMsg(result, arr);
        return result;
    }

    //??????????????????????????????
    private Map<String, Object> getMemberByEnterpriseNameCheck(String param) {
        Map<String, Object> result = new HashMap<>();
        String enterpriseName = JSONObject.parseObject(param).getString("enterpriseName");
        Member memberByEnterpriseName = memberMapper.getMemberByEnterpriseName(enterpriseName);
        boolean arr = memberByEnterpriseName == null ? false : true;
        Constants.getSuccMsg(result, arr);
        return result;
    }


    //????????????????????????
    public Map<String, Object> batchImportMember(String param) {
        Map<String, Object> result = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(param);
        Map map = new HashMap();
        String filepath = jsonObject.getString("filePath");
        //String filepath = "D:\\data\\uploadedFile\\????????????.xlsx";
        if (filepath.endsWith(".xlsx") || filepath.endsWith(".xls")) {
            Workbook workbook = null;
            Sheet sheet = null;
            try {
                //?????????????????????
                File in = new File(filepath);
                //??????excel???????????????
                if (filepath.endsWith("xlsx")) {
                    workbook = new XSSFWorkbook(new FileInputStream(in));
                } else if (filepath.endsWith("xls")) {
                    workbook = new HSSFWorkbook(new FileInputStream(in));
                }

                sheet = workbook.getSheetAt(0);
                //??????kind???membertype??????????????????
                //List<Dictionary> d = memberMapper.getMemberTypelist("membertype");
                //?????????1???????????????????????????
                Row row = sheet.getRow(0);
                if(row.getCell(0) != null){
                    row.getCell(0).setCellType(CellType.STRING);

                    if (row.getCell(0).getStringCellValue().isEmpty()){
                        map.put("code", "207");
                        map.put("msg", "?????????????????????????????????");
                        Constants.getSuccMsg(result, map);
                        return result;
                    }
                    String name = row.getCell(0).getStringCellValue();
                    Dictionary membertype = memberMapper.getDictionaryCodeByName("membertype",name);
                    if(membertype == null || membertype.equals("")){
                        map.put("code", "207");
                        map.put("msg", "??????????????????????????????");
                        Constants.getSuccMsg(result, map);
                        return result;
                    }
                    if( name.equals("??????")) { //1
                          map = batchKechuang(sheet,membertype.getCode());
                        Constants.getSuccMsg(result, map);

                       }else if(name.equals("GP")){ //2
                        map = batchGP(sheet,membertype.getCode());
                        Constants.getSuccMsg(result, map);
                       }
                       else if(name.equals("????????????")){ //3
                        map = batchchuangye(sheet,membertype.getCode());
                       // map = batchlabel(sheet);
                        Constants.getSuccMsg(result, map);
                       }else if(name.equals("LP")){//4
                        map = batchLP(sheet,membertype.getCode());
                        Constants.getSuccMsg(result, map);
                       }else if(name.equals("?????????")){//5
                        map = batchgongyingshang(sheet,membertype.getCode());
                        Constants.getSuccMsg(result, map);
                        }
                       else{
                           map.put("code", "300");
                           map.put("msg", "??????????????????????????????????????????GP??????????????????LP????????????");
                           Constants.getSuccMsg(result, map);
                           return result;
                       }

                }else{
                    map.put("code", "207");
                    map.put("msg", "?????????????????????????????????");
                    Constants.getSuccMsg(result, map);
                    return result;
                }



            } catch (Exception e) {
                map.put("code", "500");
                map.put("msg", "????????????");
                Constants.getSuccMsg(result, map);
                e.printStackTrace();
                logger.error(e.getMessage());
            } finally {
                try {
                    workbook.close();
                } catch (IOException e) {
                    map.put("code", 500);
                    map.put("msg", "Excel??????????????????");
                    Constants.getErrMsg(result, map);
                    logger.error(e.getMessage());
                }
            }
        } else {
            map.put("code", "203");
            map.put("msg", "?????????xlsx???xls???????????????");
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
        //???????????????
        int rowTotal = sheet.getPhysicalNumberOfRows();
        //??????????????????
        if (rowTotal <= 2) {
            map.put("code", "201");
            map.put("msg", "????????????");
            // Constants.getSuccMsg(result, map);
            return map;
        }
        //??????????????????
        int lastRowNum = sheet.getLastRowNum() + 1;
        //?????????????????????
        if (rowTotal != lastRowNum) {
            map.put("code", "202");
            map.put("msg", "???????????????");
            // Constants.getSuccMsg(result, map);
            return map;
        }
        //
        //??????
        Member member = null;
        for (int i = 2; i < lastRowNum; i++) {
            member = new Member();
            Row row = sheet.getRow(i);



                if (row.getCell(14) != null && !row.getCell(14).getStringCellValue().isEmpty()) {
                    row.getCell(14).setCellType(CellType.STRING);
                    String labelname = row.getCell(14).getStringCellValue();
                    String[] label = labelname.split("???");
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
     * ??????Excel
     * @param
     * sheet  10???
     * @return
     */
    @Transactional
    public Map<String,Object> batchKechuang(Sheet sheet,String code){
        Map<String,Object> map = new HashMap<>();
        List<Member> insertMemberList = new ArrayList<>();
        List<Member> updateMemberList = new ArrayList<>();
        //???????????????
        int rowTotal = sheet.getPhysicalNumberOfRows();
        //??????????????????
        if (rowTotal <= 2) {
            map.put("code", "201");
            map.put("msg", "????????????");
           // Constants.getSuccMsg(result, map);
            return map;
        }
        //??????????????????
        int lastRowNum = sheet.getLastRowNum() + 1;
        //?????????????????????
        if (rowTotal != lastRowNum) {
            map.put("code", "202");
            map.put("msg", "???????????????");
           // Constants.getSuccMsg(result, map);
            return map;
        }

        //
        //??????
        Member member = null;
        for (int i = 2; i < lastRowNum; i++) {
            member = new Member();
            Row row = sheet.getRow(i);
            if (row.getCell(0) == null || row.getCell(1) == null ||
                    row.getCell(3) == null || row.getCell(4) == null || row.getCell(5) == null ||
                    row.getCell(6) == null || row.getCell(7) == null||
                    row.getCell(8) == null || row.getCell(9) == null) {
                map.put("code", "207");
                map.put("msg", "??????????????????");
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
                map.put("msg", "?????????" + i + "???????????????????????????????????????");
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
                map.put("msg", "?????????" + i + "??????????????????????????????????????????");
                //Constants.getSuccMsg(result, map);
                return map;
            }
            //row.getCell(10).setCellType(CellType.STRING);

            if (row.getCell(0).getStringCellValue().isEmpty() ||
                    row.getCell(1).getStringCellValue().isEmpty() ||
                    //row.getCell(2).getStringCellValue().isEmpty() ||  //????????????  ?????????
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
                    str.append("?????????" + i + "????????????????????????");
                if (row.getCell(1).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "????????????????????????");
                /*if (row.getCell(2).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "????????????????????????");*/
                if (row.getCell(3).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "???????????????????????????");
                if (row.getCell(4).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "??????????????????????????????");
                if (row.getCell(5).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "????????????????????????");
                if (row.getCell(6).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "????????????????????????");
                if (row.getCell(7).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "??????????????????????????????");
                if (row.getCell(8).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "????????????????????????????????????");
                if (row.getCell(9).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "????????????????????????????????????");



                map.put("code", "207");
                map.put("msg", str);
               // Constants.getSuccMsg(result, map);
                return map;
            } else {
                if(row.getCell(1).getStringCellValue().length()>10){
                    map.put("code", "205");
                    map.put("msg", "?????????" + i + "???????????????????????????10");
                    return map;
                }
               /* if (!Pattern.matches("2\\d{9}", row.getCell(1).getStringCellValue())) {
                    map.put("code", "205");
                    map.put("msg", "?????????" + i + "?????????????????????2?????????10???????????????");
                   // Constants.getSuccMsg(result, map);
                    return map;
                }*/

               /* String pwd = row.getCell(1).getStringCellValue();
                if (!Pattern.matches(".{6,20}", pwd) || Pattern.matches("\\d+", pwd) || Pattern.matches("[a-zA-Z]+", pwd) || Pattern.matches("[\\W_]+", pwd)) {
                    map.put("code", "205");
                    map.put("msg", "?????????" + i + "???????????????6-20?????????????????????????????????????????????????????????");
                    Constants.getSuccMsg(result, map);
                    return result;
                }*/


               /* Dictionary dictionary = memberMapper.selectDictionaryCodeByName(row.getCell(6).getStringCellValue());
                if (dictionary == null || "".equals(dictionary.getCode())) {
                    map.put("code", "205");
                    map.put("msg", "?????????" + i + "???????????????????????????");
                    Constants.getSuccMsg(result, map);
                    return result;
                }*/

                User user = new User();
                user.setRealName(row.getCell(8).getStringCellValue());
                user.setId(row.getCell(9).getStringCellValue());

                List<User> userList = memberMapper.selectUserInfoByIdAndRealName(user);
                if (userList == null || userList.size() == 0) {
                    map.put("code", "204");
                    map.put("msg", "?????????" + i + "??????????????????????????????????????????????????????");
                    //Constants.getSuccMsg(result, map);
                    return map;
                } else if (userList != null && !userList.get(0).getRealName().equals(user.getRealName())) {
                    map.put("code", "204");
                    map.put("msg", "?????????" + i + "????????????????????????????????????????????????????????????");
                   // Constants.getSuccMsg(result, map);
                    return map;
                }


                member.setEnterpriseName(row.getCell(0).getStringCellValue());//????????????
                member.setEnterpriseCode(row.getCell(1).getStringCellValue());//????????????
                member.setAbbreviation(row.getCell(2).getStringCellValue());//????????????
                member.setContact(row.getCell(3).getStringCellValue());//?????????
                member.setPhone(row.getCell(4).getStringCellValue());//????????????
                member.setEmail(row.getCell(5).getStringCellValue());//??????
                member.setJob(row.getCell(6).getStringCellValue());//??????
                member.setAccountManager(user.getId());//????????????
                member.setAddress(row.getCell(7).getStringCellValue());//????????????
                if (row.getCell(10) != null && !row.getCell(10).getStringCellValue().isEmpty()) {
                    row.getCell(10).setCellType(CellType.STRING);
                    member.setRemark(row.getCell(10).getStringCellValue());//??????
                }
                //member.setRemark(row.getCell(10).getStringCellValue());//??????
                member.setDelFlag(1);//????????????
                member.setMemberType(code);//????????????

                Member memberByEnterpriseCode = memberMapper.getMemberByEnterpriseCode(member.getEnterpriseCode());
                // Member memberByEnterpriseName = memberMapper.getMemberByEnterpriseName(member.getEnterpriseName());
                //????????????????????????????????????????????????????????????????????????????????????????????????  && (memberByEnterpriseName == null || "".equals(memberByEnterpriseName.getId()))
                if (memberByEnterpriseCode == null || "".equals(memberByEnterpriseCode.getId()) ) {
                    super.insertBaseInfo(member); //????????????????????????????????????
                    //setMemberDictionaryList(member, dictionaryList);//?????????????????????????????????????????????????????????
                    String pwd = getRandomNumAndChacters(6);
                    member.setPwd(pwd);
                    insertMemberList.add(member);
                } else {
                    if (memberByEnterpriseCode != null && !"".equals(memberByEnterpriseCode.getId()) &&
                            // memberByEnterpriseName != null && !"".equals(memberByEnterpriseName.getId()) &&
                            // memberByEnterpriseCode.getId().equals(memberByEnterpriseName.getId())
                            memberByEnterpriseCode.getEnterpriseName().equals(member.getEnterpriseName())) {
                        super.updateBaseInfo(member, memberByEnterpriseCode.getId());//????????????????????????????????????
                        // setMemberDictionaryList(member, dictionaryList);//?????????????????????????????????????????????????????????????????????
                        updateMemberList.add(member);
                    } else {
                        map.put("code", "204");
                        map.put("msg", "???" + i + "?????????????????????????????????????????????????????????????????????????????????????????????");
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
            map.put("msg", "????????????");
           // Constants.getSuccMsg(result, map);
        } else {
            map.put("code", "200");
            map.put("msg", "???????????????????????????0");
           // Constants.getSuccMsg(result, map);
        }
        return map;
    }

    /**
     * GP  Excel
     * @param
     * sheet  16???
     * @return
     */
    @Transactional
    public Map<String,Object> batchGP(Sheet sheet,String code){
        Map<String,Object> map = new HashMap<>();
        List<Member> insertMemberList = new ArrayList<>();
        List<Member> updateMemberList = new ArrayList<>();
        //???????????????
        int rowTotal = sheet.getPhysicalNumberOfRows();
        //??????????????????
        if (rowTotal <= 2) {
            map.put("code", "201");
            map.put("msg", "????????????");
            // Constants.getSuccMsg(result, map);
            return map;
        }
        //??????????????????
        int lastRowNum = sheet.getLastRowNum() + 1;
        //?????????????????????
        if (rowTotal != lastRowNum) {
            map.put("code", "202");
            map.put("msg", "???????????????");
            // Constants.getSuccMsg(result, map);
            return map;
        }

        //
        //??????
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
                map.put("msg", "??????????????????");
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
                map.put("msg", "?????????" + i + "???????????????????????????????????????");
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
                map.put("msg", "?????????" + i + "??????????????????????????????????????????");
                //Constants.getSuccMsg(result, map);
                return map;
            }*/
           // row.getCell(9).setCellType(CellType.STRING);
           // row.getCell(10).setCellType(CellType.STRING);
           // row.getCell(11).setCellType(CellType.STRING);
            //row.getCell(12).setCellType(CellType.STRING);
           // row.getCell(13).setCellType(CellType.STRING);
            //row.getCell(14).setCellType(CellType.STRING);  //?????????????????????
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
                    str.append("?????????" + i + "????????????????????????");
                if (row.getCell(1).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "????????????????????????");
                if (row.getCell(2).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "????????????????????????");
               /* if (row.getCell(3).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "?????????????????????????????????");*/
               /* if (row.getCell(4).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "??????????????????????????????");
                if (row.getCell(5).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "????????????????????????");
                if (row.getCell(6).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "????????????????????????");

                if (row.getCell(7).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "????????????????????????????????????");
                if (row.getCell(8).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "????????????????????????????????????");*/
               /* if (row.getCell(9).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "??????????????????????????????????????????");*/
               /* if (row.getCell(10).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "??????????????????????????????????????????");
                if (row.getCell(11).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "??????????????????????????????");*/
               /* if (row.getCell(12).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "?????????????????????????????????");
                if (row.getCell(13).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "??????????????????????????????????????????");*/




                map.put("code", "207");
                map.put("msg", str);
                // Constants.getSuccMsg(result, map);
                return map;
            } else {
                if(row.getCell(1).getStringCellValue().length()>10){
                    map.put("code", "205");
                    map.put("msg", "?????????" + i + "???????????????????????????10");
                    return map;
                }
               /* if (!Pattern.matches("2\\d{9}", row.getCell(0).getStringCellValue())) {
                    map.put("code", "205");
                    map.put("msg", "?????????" + i + "?????????????????????2?????????10???????????????");
                    Constants.getSuccMsg(result, map);
                    return result;
                }*/

               /* String pwd = row.getCell(1).getStringCellValue();
                if (!Pattern.matches(".{6,20}", pwd) || Pattern.matches("\\d+", pwd) || Pattern.matches("[a-zA-Z]+", pwd) || Pattern.matches("[\\W_]+", pwd)) {
                    map.put("code", "205");
                    map.put("msg", "?????????" + i + "???????????????6-20?????????????????????????????????????????????????????????");
                    Constants.getSuccMsg(result, map);
                    return result;
                }*/


               /* Dictionary dictionary = memberMapper.selectDictionaryCodeByName(row.getCell(6).getStringCellValue());
                if (dictionary == null || "".equals(dictionary.getCode())) {
                    map.put("code", "205");
                    map.put("msg", "?????????" + i + "???????????????????????????");
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
                        map.put("msg", "?????????" + i + "??????????????????????????????????????????");
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
                        map.put("msg", "?????????" + i + "??????????????????????????????????????????????????????");
                        //Constants.getSuccMsg(result, map);
                        return map;
                    } else if (userList != null && !userList.get(0).getRealName().equals(user.getRealName())) {
                        map.put("code", "204");
                        map.put("msg", "?????????" + i + "????????????????????????????????????????????????????????????");
                        // Constants.getSuccMsg(result, map);
                        return map;
                    }
                    member.setAccountManager(user.getId());//????????????
                }

                member.setEnterpriseName(row.getCell(0).getStringCellValue());//??????????????????
                member.setEnterpriseCode(row.getCell(1).getStringCellValue());//????????????
                member.setAbbreviation(row.getCell(2).getStringCellValue());//????????????
                if (row.getCell(3) != null && !row.getCell(3).getStringCellValue().isEmpty()) {
                    row.getCell(3).setCellType(CellType.STRING);
                    member.setContact(row.getCell(3).getStringCellValue());//?????????
                }
                if (row.getCell(4) != null && !row.getCell(4).getStringCellValue().isEmpty()) {
                    try {
                        row.getCell(4).setCellType(CellType.STRING);
                        member.setPhone(row.getCell(4).getStringCellValue());//????????????
                    } catch (Exception e) {
                        e.printStackTrace();
                        map.put("code", "204");
                        map.put("msg", "?????????" + i + "???????????????????????????????????????");
                        //Constants.getSuccMsg(result, map);
                        return map;
                    }
                }
                if (row.getCell(5) != null && !row.getCell(5).getStringCellValue().isEmpty()) {
                    row.getCell(5).setCellType(CellType.STRING);
                    member.setEmail(row.getCell(5).getStringCellValue());//??????
                }
                if (row.getCell(6) != null && !row.getCell(6).getStringCellValue().isEmpty()) {
                    row.getCell(6).setCellType(CellType.STRING);
                    member.setJob(row.getCell(6).getStringCellValue());//??????
                }
                if (row.getCell(9) != null && !row.getCell(9).getStringCellValue().isEmpty()) {
                    row.getCell(9).setCellType(CellType.STRING);
                    member.setRegistrationProvince(row.getCell(9).getStringCellValue());//??????????????????
                }
                if (row.getCell(10) != null && !row.getCell(10).getStringCellValue().isEmpty()) {
                    row.getCell(10).setCellType(CellType.STRING);
                    member.setAddress(row.getCell(10).getStringCellValue());//??????????????????
                }
                if (row.getCell(11) != null && !row.getCell(11).getStringCellValue().isEmpty()) {
                    row.getCell(11).setCellType(CellType.STRING);
                    if(row.getCell(11).getStringCellValue().equals("???")){
                        member.setSHOffice(1);
                    }else {
                        member.setSHOffice(0);
                    }

                }
                if (row.getCell(12) != null && !row.getCell(12).getStringCellValue().isEmpty()) {
                    row.getCell(12).setCellType(CellType.STRING);
                    String attentionIndustryname = row.getCell(12).getStringCellValue();
                    String[] attentionIndustry = attentionIndustryname.split("???");
                    // Dictionary rhinocerosLabel = memberMapper.getDictionaryCodeByName("rhinocerosLabel",row.getCell(14).getStringCellValue());

                    String[] attentionIndustrys = memberMapper.getDictionaryLabels("attentionIndustry",attentionIndustry);
                    if(attentionIndustrys == null || attentionIndustrys.length<=0){
                        map.put("code", "204");
                        map.put("msg", "?????????" + i + "?????????????????????????????????,???????????????????????????????????????");
                        return map;
                    }
                    String attentionIndustrys1 = JSONArray.toJSON(attentionIndustrys).toString();

                    member.setAttentionIndustryId(attentionIndustrys1);//????????????
                }
                //Dictionary attentionIndustry = memberMapper.getDictionaryCodeByName("attentionIndustry",row.getCell(11).getStringCellValue());
                /*if(attentionIndustry == null || attentionIndustry.equals("") || attentionIndustry.getName() == null ||attentionIndustry.getName().equals("")){
                    map.put("code", "204");
                    map.put("msg", "?????????" + i + "?????????????????????????????????");
                    // Constants.getSuccMsg(result, map);
                    return map;
                }
                member.setAttentionIndustryId(attentionIndustry.getCode());//????????????*/

                if (row.getCell(13) != null && !row.getCell(13).getStringCellValue().isEmpty()) {
                    row.getCell(13).setCellType(CellType.STRING);
                    String attentionStagename = row.getCell(13).getStringCellValue();
                    String[] attentionStage = attentionStagename.split("???");
                    // Dictionary rhinocerosLabel = memberMapper.getDictionaryCodeByName("rhinocerosLabel",row.getCell(14).getStringCellValue());

                    String[] attentionStages = memberMapper.getDictionaryLabels("attentionStage",attentionStage);
                    if(attentionStages == null || attentionStages.length<=0){
                        map.put("code", "204");
                        map.put("msg", "?????????" + i + "?????????????????????????????????,???????????????????????????????????????");
                        return map;
                    }
                    String attentionStages1 = JSONArray.toJSON(attentionStages).toString();

                    member.setAttentionStageId(attentionStages1);//????????????
                }
                /*Dictionary attentionStage = memberMapper.getDictionaryCodeByName("attentionStage",row.getCell(12).getStringCellValue());
                if(attentionStage == null || attentionStage.equals("") || attentionStage.getName() == null ||attentionStage.getName().equals("")){
                    map.put("code", "204");
                    map.put("msg", "?????????" + i + "?????????????????????????????????");
                    // Constants.getSuccMsg(result, map);
                    return map;
                }
                member.setAttentionStageId(attentionStage.getCode());*/


                if (row.getCell(14) != null && !row.getCell(14).getStringCellValue().isEmpty()) {
                    row.getCell(14).setCellType(CellType.STRING);
                    member.setInvestmentManager(row.getCell(14).getStringCellValue()); //??????????????????
                }

                if (row.getCell(15) != null && !row.getCell(15).getStringCellValue().isEmpty()) {
                    row.getCell(15).setCellType(CellType.STRING);
                    member.setInvestmentFund(row.getCell(15).getStringCellValue()); //??????????????????
                }
                if (row.getCell(16) != null && !row.getCell(16).getStringCellValue().isEmpty()) {
                    row.getCell(16).setCellType(CellType.STRING);
                    member.setRemark(row.getCell(16).getStringCellValue());//??????
                }

                member.setDelFlag(1);//????????????
                member.setMemberType(code);//????????????
                Member memberByEnterpriseCode = memberMapper.getMemberByEnterpriseCode(member.getEnterpriseCode());
                // Member memberByEnterpriseName = memberMapper.getMemberByEnterpriseName(member.getEnterpriseName());
                //????????????????????????????????????????????????????????????????????????????????????????????????  && (memberByEnterpriseName == null || "".equals(memberByEnterpriseName.getId()))
                if (memberByEnterpriseCode == null || "".equals(memberByEnterpriseCode.getId()) ) {
                    super.insertBaseInfo(member); //????????????????????????????????????
                    //setMemberDictionaryList(member, dictionaryList);//?????????????????????????????????????????????????????????
                    String pwd = getRandomNumAndChacters(6);
                    member.setPwd(pwd);
                    insertMemberList.add(member);
                } else {
                    if (memberByEnterpriseCode != null && !"".equals(memberByEnterpriseCode.getId()) &&
                            // memberByEnterpriseName != null && !"".equals(memberByEnterpriseName.getId()) &&
                            // memberByEnterpriseCode.getId().equals(memberByEnterpriseName.getId())
                            memberByEnterpriseCode.getEnterpriseName().equals(member.getEnterpriseName())) {
                        super.updateBaseInfo(member, memberByEnterpriseCode.getId());//????????????????????????????????????
                        // setMemberDictionaryList(member, dictionaryList);//?????????????????????????????????????????????????????????????????????
                        updateMemberList.add(member);
                    } else {
                        map.put("code", "204");
                        map.put("msg", "???" + i + "?????????????????????????????????????????????????????????????????????????????????????????????");
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
            map.put("msg", "????????????");
            // Constants.getSuccMsg(result, map);
        } else {
            map.put("code", "200");
            map.put("msg", "???????????????????????????0");
            // Constants.getSuccMsg(result, map);
        }
        return map;
    }
    /**
     * ????????????Excel
     * @param
     * sheet  19???
     * @return
     */
    @Transactional
    public Map<String,Object> batchchuangye(Sheet sheet,String code){
        Map<String,Object> map = new HashMap<>();
        List<Member> insertMemberList = new ArrayList<>();
        List<Member> updateMemberList = new ArrayList<>();
        //???????????????
        int rowTotal = sheet.getPhysicalNumberOfRows();
        //??????????????????
        if (rowTotal <= 2) {
            map.put("code", "201");
            map.put("msg", "????????????");
            // Constants.getSuccMsg(result, map);
            return map;
        }
        //??????????????????
        int lastRowNum = sheet.getLastRowNum() + 1;
        //?????????????????????
        if (rowTotal != lastRowNum) {
            map.put("code", "202");
            map.put("msg", "???????????????");
            // Constants.getSuccMsg(result, map);
            return map;
        }

        //
        //??????
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
                map.put("msg", "??????????????????");
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
                map.put("msg", "?????????" + i + "???????????????????????????????????????");
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
                map.put("msg", "?????????" + i + "??????????????????????????????????????????");
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
                    //row.getCell(2).getStringCellValue().isEmpty() ||  //????????????  ?????????
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
                    str.append("?????????" + i + "??????????????????????????????");
                if (row.getCell(1).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "????????????????????????");
                if (row.getCell(2).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "????????????????????????");
               /* if (row.getCell(3).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "???????????????????????????");
                if (row.getCell(4).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "??????????????????????????????");
                if (row.getCell(5).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "????????????????????????");
                if (row.getCell(6).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "????????????????????????");

                if (row.getCell(7).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "????????????????????????????????????");
                if (row.getCell(8).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "????????????????????????????????????");*/
               /* if (row.getCell(9).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "?????????????????????????????????");*/
               /* if (row.getCell(10).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "?????????????????????????????????");*/
                /*if (row.getCell(11).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "??????????????????????????????");*/
                if (row.getCell(12).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "????????????????????????????????????");
                /*if (row.getCell(13).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "???????????????????????????????????????");*/
               /* if (row.getCell(14).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "?????????????????????????????????");*/
                if (row.getCell(15).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "????????????????????????????????????");
                if (row.getCell(16).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "????????????????????????????????????");
               /* if (row.getCell(17).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "????????????????????????????????????");*/



                map.put("code", "207");
                map.put("msg", str);
                // Constants.getSuccMsg(result, map);
                return map;
            } else {
                if(row.getCell(1).getStringCellValue().length()>10){
                    map.put("code", "205");
                    map.put("msg", "?????????" + i + "???????????????????????????10");
                    return map;
                }
               /* if (!Pattern.matches("2\\d{9}", row.getCell(0).getStringCellValue())) {
                    map.put("code", "205");
                    map.put("msg", "?????????" + i + "?????????????????????2?????????10???????????????");
                    Constants.getSuccMsg(result, map);
                    return result;
                }*/

               /* String pwd = row.getCell(1).getStringCellValue();
                if (!Pattern.matches(".{6,20}", pwd) || Pattern.matches("\\d+", pwd) || Pattern.matches("[a-zA-Z]+", pwd) || Pattern.matches("[\\W_]+", pwd)) {
                    map.put("code", "205");
                    map.put("msg", "?????????" + i + "???????????????6-20?????????????????????????????????????????????????????????");
                    Constants.getSuccMsg(result, map);
                    return result;
                }*/


               /* Dictionary dictionary = memberMapper.selectDictionaryCodeByName(row.getCell(6).getStringCellValue());
                if (dictionary == null || "".equals(dictionary.getCode())) {
                    map.put("code", "205");
                    map.put("msg", "?????????" + i + "???????????????????????????");
                    Constants.getSuccMsg(result, map);
                    return result;
                }*/
               //String d = row.getCell(15).getStringCellValue();
                String time1 = null;
                try {
                    Date setupTime = HSSFDateUtil.getJavaDate(Double.valueOf(row.getCell(15).getStringCellValue()));
                    //boolean vdate =  isValidDate(row.getCell(15).getStringCellValue());
                    SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                     time1 = format0.format(setupTime.getTime());//???????????????????????????????????????????????????????????????
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    map.put("code", "204");
                    map.put("msg", "?????????" + i + "???????????????????????????????????????");
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
                        map.put("msg", "?????????" + i + "??????????????????????????????????????????");
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
                        map.put("msg", "?????????" + i + "??????????????????????????????????????????????????????");
                        //Constants.getSuccMsg(result, map);
                        return map;
                    } else if (userList != null && !userList.get(0).getRealName().equals(user.getRealName())) {
                        map.put("code", "204");
                        map.put("msg", "?????????" + i + "????????????????????????????????????????????????????????????");
                        // Constants.getSuccMsg(result, map);
                        return map;
                    }
                    member.setAccountManager(user.getId());//????????????
                }


                member.setEnterpriseName(row.getCell(0).getStringCellValue());//??????????????????
                member.setEnterpriseCode(row.getCell(1).getStringCellValue());//????????????
                member.setAbbreviation(row.getCell(2).getStringCellValue());//????????????
                if (row.getCell(3) != null && !row.getCell(3).getStringCellValue().isEmpty()) {
                    row.getCell(3).setCellType(CellType.STRING);
                    member.setContact(row.getCell(3).getStringCellValue());//?????????
                }
                if (row.getCell(4) != null && !row.getCell(4).getStringCellValue().isEmpty()) {
                    try {
                        row.getCell(4).setCellType(CellType.STRING);
                        member.setPhone(row.getCell(4).getStringCellValue());//????????????
                    } catch (Exception e) {
                        e.printStackTrace();
                        map.put("code", "204");
                        map.put("msg", "?????????" + i + "???????????????????????????????????????");
                        //Constants.getSuccMsg(result, map);
                        return map;
                    }
                }
                if (row.getCell(5) != null && !row.getCell(5).getStringCellValue().isEmpty()) {
                    row.getCell(5).setCellType(CellType.STRING);
                    member.setEmail(row.getCell(5).getStringCellValue());//??????
                }
                if (row.getCell(6) != null && !row.getCell(6).getStringCellValue().isEmpty()) {
                    row.getCell(6).setCellType(CellType.STRING);
                    member.setJob(row.getCell(6).getStringCellValue());//??????
                }


                if (row.getCell(9) != null && !row.getCell(9).getStringCellValue().isEmpty()) {
                    row.getCell(9).setCellType(CellType.STRING);
                    member.setAddress1(row.getCell(9).getStringCellValue());//???????????????
                }

                if (row.getCell(10) != null && !row.getCell(10).getStringCellValue().isEmpty()) {
                    row.getCell(10).setCellType(CellType.STRING);
                    member.setAddress(row.getCell(10).getStringCellValue());//???????????????
                }

                if (row.getCell(11) != null && !row.getCell(11).getStringCellValue().isEmpty()) {
                    row.getCell(11).setCellType(CellType.STRING);
                    //??????GPid
                    String gpId = memberMapper.getGPIdbyFund(row.getCell(11).getStringCellValue(),"1");
                    if(gpId != null && !gpId.equals("")){
                        member.setInvestmentFund(gpId);
                    }else{
                        member.setInvestmentFund(row.getCell(11).getStringCellValue());//????????????
                    }
                }
                Dictionary industryClassification = memberMapper.getDictionaryCodeByName("industryClassification",row.getCell(12).getStringCellValue());
                if(industryClassification == null || industryClassification.equals("") || industryClassification.getName() == null ||industryClassification.getName().equals("")){
                    map.put("code", "204");
                    map.put("msg", "?????????" + i + "???????????????????????????????????????");
                    // Constants.getSuccMsg(result, map);
                    return map;
                }
                member.setIndustryClassificationId(industryClassification.getCode());//??????????????????
                if (row.getCell(13) != null && !row.getCell(13).getStringCellValue().isEmpty()) {
                    row.getCell(13).setCellType(CellType.STRING);
                    Dictionary directInvestmentClassification = memberMapper.getDictionaryCodeByName("directInvestmentClassification",row.getCell(13).getStringCellValue());
                    if(directInvestmentClassification == null || directInvestmentClassification.equals("") || directInvestmentClassification.getName() == null ||directInvestmentClassification.getName().equals("")){
                        map.put("code", "204");
                        map.put("msg", "?????????" + i + "???????????????????????????????????????");
                        // Constants.getSuccMsg(result, map);
                        return map;
                    }
                    member.setDirectInvestmentClassificationId(directInvestmentClassification.getCode());//??????????????????
                }
                if (row.getCell(14) != null && !row.getCell(14).getStringCellValue().isEmpty()) {
                    row.getCell(14).setCellType(CellType.STRING);
                    String labelname = row.getCell(14).getStringCellValue();
                    String[] label = labelname.split("???");
                   // Dictionary rhinocerosLabel = memberMapper.getDictionaryCodeByName("rhinocerosLabel",row.getCell(14).getStringCellValue());
                    String[] rhinocerosLabel = memberMapper.getDictionaryLabels("rhinocerosLabel",label);
                    if(rhinocerosLabel == null || rhinocerosLabel.length<=0){
                        map.put("code", "204");
                        map.put("msg", "?????????" + i + "?????????????????????????????????,???????????????????????????????????????");
                        // Constants.getSuccMsg(result, map);
                        return map;
                    }
                    String rhinocerosLabel1 = JSONArray.toJSON(rhinocerosLabel).toString();
                   // member.setRhinocerosLabelId(Arrays.toString(rhinocerosLabel));//????????????
                    member.setRhinocerosLabelId(rhinocerosLabel1);//????????????
                }

                member.setFirstInvestmentTime(time1);//??????????????????
                Dictionary firstInvestmentStage = memberMapper.getDictionaryCodeByName("firstInvestmentStage",row.getCell(16).getStringCellValue());
                if(firstInvestmentStage == null || firstInvestmentStage.equals("") || firstInvestmentStage.getName() == null ||firstInvestmentStage.getName().equals("")){
                    map.put("code", "204");
                    map.put("msg", "?????????" + i + "???????????????????????????????????????");
                    // Constants.getSuccMsg(result, map);
                    return map;
                }
                member.setFirstInvestmentStageId(firstInvestmentStage.getCode());//??????????????????
                if (row.getCell(17) != null && !row.getCell(17).getStringCellValue().isEmpty()) {
                    row.getCell(17).setCellType(CellType.STRING);
                    if(row.getCell(17).getStringCellValue().equals("???")){
                        member.setLeadInvestment(1);
                    }else {
                        member.setLeadInvestment(0);
                    }//????????????
                }
                if (row.getCell(18) != null && !row.getCell(18).getStringCellValue().isEmpty()) {
                    row.getCell(18).setCellType(CellType.STRING);
                    member.setRegistrationProvince(row.getCell(18).getStringCellValue());//????????????
                }
                if (row.getCell(19) != null && !row.getCell(19).getStringCellValue().isEmpty()) {
                    row.getCell(19).setCellType(CellType.STRING);
                    member.setRemark(row.getCell(19).getStringCellValue());//??????
                }
                //member.setRemark(row.getCell(18).getStringCellValue());//??????
                member.setDelFlag(1);//????????????
                member.setMemberType(code);//????????????

                Member memberByEnterpriseCode = memberMapper.getMemberByEnterpriseCode(member.getEnterpriseCode());
               // Member memberByEnterpriseName = memberMapper.getMemberByEnterpriseName(member.getEnterpriseName());
                //????????????????????????????????????????????????????????????????????????????????????????????????  && (memberByEnterpriseName == null || "".equals(memberByEnterpriseName.getId()))
                if (memberByEnterpriseCode == null || "".equals(memberByEnterpriseCode.getId()) ) {
                    super.insertBaseInfo(member); //????????????????????????????????????
                    //setMemberDictionaryList(member, dictionaryList);//?????????????????????????????????????????????????????????
                    String pwd = getRandomNumAndChacters(6);
                    member.setPwd(pwd);
                    insertMemberList.add(member);
                } else {
                    if (memberByEnterpriseCode != null && !"".equals(memberByEnterpriseCode.getId()) &&
                           // memberByEnterpriseName != null && !"".equals(memberByEnterpriseName.getId()) &&
                           // memberByEnterpriseCode.getId().equals(memberByEnterpriseName.getId())
                            memberByEnterpriseCode.getEnterpriseName().equals(member.getEnterpriseName())) {
                        super.updateBaseInfo(member, memberByEnterpriseCode.getId());//????????????????????????????????????
                        // setMemberDictionaryList(member, dictionaryList);//?????????????????????????????????????????????????????????????????????
                        updateMemberList.add(member);
                    } else {
                        map.put("code", "204");
                        map.put("msg", "???" + i + "?????????????????????????????????????????????????????????????????????????????????????????????");
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
            map.put("msg", "????????????");
            // Constants.getSuccMsg(result, map);
        } else {
            map.put("code", "200");
            map.put("msg", "???????????????????????????0");
            // Constants.getSuccMsg(result, map);
        }
        return map;
    }

    /**
     * LP  Excel
     * @param
     * sheet  10???
     * @return
     */
    @Transactional
    public Map<String,Object> batchLP(Sheet sheet,String code){
        Map<String,Object> map = new HashMap<>();
        List<Member> insertMemberList = new ArrayList<>();
        List<Member> updateMemberList = new ArrayList<>();
        //???????????????
        int rowTotal = sheet.getPhysicalNumberOfRows();
        //??????????????????
        if (rowTotal <= 2) {
            map.put("code", "201");
            map.put("msg", "????????????");
            // Constants.getSuccMsg(result, map);
            return map;
        }
        //??????????????????
        int lastRowNum = sheet.getLastRowNum() + 1;
        //?????????????????????
        if (rowTotal != lastRowNum) {
            map.put("code", "202");
            map.put("msg", "???????????????");
            // Constants.getSuccMsg(result, map);
            return map;
        }

        //
        //??????
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
                map.put("msg", "??????????????????");
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
                map.put("msg", "?????????" + i + "???????????????????????????????????????");
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
                map.put("msg", "?????????" + i + "??????????????????????????????????????????");
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
                    str.append("?????????" + i + "LP??????????????????");
                if (row.getCell(1).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "????????????????????????");
                if (row.getCell(2).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "LP??????????????????");
              /*  if (row.getCell(3).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "???????????????????????????");
                if (row.getCell(4).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "??????????????????????????????");
                if (row.getCell(5).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "????????????????????????");
                if (row.getCell(6).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "????????????????????????");
                if (row.getCell(7).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "????????????????????????????????????");
                if (row.getCell(8).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "????????????????????????????????????");*/

                map.put("code", "207");
                map.put("msg", str);
                // Constants.getSuccMsg(result, map);
                return map;
            } else {
                if(row.getCell(1).getStringCellValue().length()>10){
                    map.put("code", "205");
                    map.put("msg", "?????????" + i + "???????????????????????????10");
                    return map;
                }
               /* if (!Pattern.matches("2\\d{9}", row.getCell(0).getStringCellValue())) {
                    map.put("code", "205");
                    map.put("msg", "?????????" + i + "?????????????????????2?????????10???????????????");
                    Constants.getSuccMsg(result, map);
                    return result;
                }*/

               /* String pwd = row.getCell(1).getStringCellValue();
                if (!Pattern.matches(".{6,20}", pwd) || Pattern.matches("\\d+", pwd) || Pattern.matches("[a-zA-Z]+", pwd) || Pattern.matches("[\\W_]+", pwd)) {
                    map.put("code", "205");
                    map.put("msg", "?????????" + i + "???????????????6-20?????????????????????????????????????????????????????????");
                    Constants.getSuccMsg(result, map);
                    return result;
                }*/


               /* Dictionary dictionary = memberMapper.selectDictionaryCodeByName(row.getCell(6).getStringCellValue());
                if (dictionary == null || "".equals(dictionary.getCode())) {
                    map.put("code", "205");
                    map.put("msg", "?????????" + i + "???????????????????????????");
                    Constants.getSuccMsg(result, map);
                    return result;
                }*/


                member.setEnterpriseName(row.getCell(0).getStringCellValue());//lp??????
                member.setEnterpriseCode(row.getCell(1).getStringCellValue());//????????????
                member.setAbbreviation(row.getCell(2).getStringCellValue());//lp??????
                if (row.getCell(3) != null && !row.getCell(3).getStringCellValue().isEmpty()) {
                    row.getCell(3).setCellType(CellType.STRING);
                    member.setContact(row.getCell(3).getStringCellValue());//?????????
                }
                if (row.getCell(4) != null && !row.getCell(4).getStringCellValue().isEmpty()) {
                    try {
                        row.getCell(4).setCellType(CellType.STRING);
                        member.setPhone(row.getCell(4).getStringCellValue());//????????????
                    } catch (Exception e) {
                        e.printStackTrace();
                        map.put("code", "204");
                        map.put("msg", "?????????" + i + "???????????????????????????????????????");
                        //Constants.getSuccMsg(result, map);
                        return map;
                    }
                }
                if (row.getCell(5) != null && !row.getCell(5).getStringCellValue().isEmpty()) {
                    row.getCell(5).setCellType(CellType.STRING);
                    member.setEmail(row.getCell(5).getStringCellValue());//??????
                }
                if (row.getCell(6) != null && !row.getCell(6).getStringCellValue().isEmpty()) {
                    row.getCell(6).setCellType(CellType.STRING);
                    member.setJob(row.getCell(6).getStringCellValue());//??????
                }
                if (row.getCell(7) != null && !row.getCell(7).getStringCellValue().isEmpty()) {
                    row.getCell(7).setCellType(CellType.STRING);
                    String participationFundname = row.getCell(7).getStringCellValue();
                    String[] participationFund = participationFundname.split("???");
                    String[] participationFundIds = memberMapper.getDictionaryLabels("participationFund",participationFund);
                    if(participationFundIds == null || participationFundIds.length<=0){
                        map.put("code", "204");
                        map.put("msg", "?????????" + i + "???????????????????????????????????????,?????????????????????????????????????????????");
                        // Constants.getSuccMsg(result, map);
                        return map;
                    }
                    String participationFundIds1 = JSONArray.toJSON(participationFundIds).toString();
                    member.setParticipationFundId(participationFundIds1);//??????????????????
                }

               // member.setAddress(row.getCell(7).getStringCellValue());//????????????
                if (row.getCell(8) != null && !row.getCell(8).getStringCellValue().isEmpty()) {
                    row.getCell(8).setCellType(CellType.STRING);
                    member.setRemark(row.getCell(8).getStringCellValue());//??????
                }

                member.setDelFlag(1);//????????????
                member.setMemberType(code);//????????????
                Member memberByEnterpriseCode = memberMapper.getMemberByEnterpriseCode(member.getEnterpriseCode());
                Member memberByEnterpriseName = memberMapper.getMemberByEnterpriseName(member.getEnterpriseName());
                //????????????????????????????????????????????????????????????????????????????????????????????????  && (memberByEnterpriseName == null || "".equals(memberByEnterpriseName.getId()))
                if (memberByEnterpriseCode == null || "".equals(memberByEnterpriseCode.getId()) ) {
                    super.insertBaseInfo(member); //????????????????????????????????????
                    //setMemberDictionaryList(member, dictionaryList);//?????????????????????????????????????????????????????????
                    String pwd = getRandomNumAndChacters(6);
                    member.setPwd(pwd);
                    insertMemberList.add(member);
                } else {
                    if (memberByEnterpriseCode != null && !"".equals(memberByEnterpriseCode.getId()) &&
                            memberByEnterpriseName != null && !"".equals(memberByEnterpriseName.getId()) &&
                            memberByEnterpriseCode.getId().equals(memberByEnterpriseName.getId())) {
                        super.updateBaseInfo(member, memberByEnterpriseCode.getId());//????????????????????????????????????
                        // setMemberDictionaryList(member, dictionaryList);//?????????????????????????????????????????????????????????????????????
                        updateMemberList.add(member);
                    } else {
                        map.put("code", "204");
                        map.put("msg", "???" + i + "?????????????????????????????????????????????????????????????????????????????????????????????");
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
            map.put("msg", "????????????");
            // Constants.getSuccMsg(result, map);
        } else {
            map.put("code", "200");
            map.put("msg", "???????????????????????????0");
            // Constants.getSuccMsg(result, map);
        }
        return map;
    }

    /**
     * ?????????  Excel
     * @param
     * sheet  10???
     * @return
     */
    @Transactional
    public Map<String,Object> batchgongyingshang(Sheet sheet,String code){
        Map<String,Object> map = new HashMap<>();
        List<Member> insertMemberList = new ArrayList<>();
        List<Member> updateMemberList = new ArrayList<>();
        //??????department???
        List<Department> insertDeptList = new ArrayList<>();
        //???????????????
        int rowTotal = sheet.getPhysicalNumberOfRows();
        //??????????????????
        if (rowTotal <= 2) {
            map.put("code", "201");
            map.put("msg", "????????????");
            // Constants.getSuccMsg(result, map);
            return map;
        }
        //??????????????????
        int lastRowNum = sheet.getLastRowNum() + 1;
        //?????????????????????
        if (rowTotal != lastRowNum) {
            map.put("code", "202");
            map.put("msg", "???????????????");
            // Constants.getSuccMsg(result, map);
            return map;
        }

        //
        //??????
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
                map.put("msg", "??????????????????");
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
                map.put("msg", "?????????" + i + "???????????????????????????????????????");
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
                map.put("msg", "?????????" + i + "??????????????????????????????????????????");
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
                    str.append("?????????" + i + "?????????????????????????????????");
                if (row.getCell(1).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "??????????????????????????????");
                if (row.getCell(2).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "?????????????????????????????????");
                /*if (row.getCell(3).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "???????????????????????????");
                if (row.getCell(4).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "??????????????????????????????");
                if (row.getCell(5).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "????????????????????????");
                if (row.getCell(6).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "????????????????????????");
                if (row.getCell(7).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "????????????????????????????????????");
                if (row.getCell(8).getStringCellValue().isEmpty())
                    str.append("?????????" + i + "????????????????????????????????????");*/

                map.put("code", "207");
                map.put("msg", str);
                // Constants.getSuccMsg(result, map);
                return map;
            } else {
                if(row.getCell(1).getStringCellValue().length()>10){
                    map.put("code", "205");
                    map.put("msg", "?????????" + i + "???????????????????????????10");
                    return map;
                }
               /* if (!Pattern.matches("2\\d{9}", row.getCell(0).getStringCellValue())) {
                    map.put("code", "205");
                    map.put("msg", "?????????" + i + "?????????????????????2?????????10???????????????");
                    Constants.getSuccMsg(result, map);
                    return result;
                }*/

               /* String pwd = row.getCell(1).getStringCellValue();
                if (!Pattern.matches(".{6,20}", pwd) || Pattern.matches("\\d+", pwd) || Pattern.matches("[a-zA-Z]+", pwd) || Pattern.matches("[\\W_]+", pwd)) {
                    map.put("code", "205");
                    map.put("msg", "?????????" + i + "???????????????6-20?????????????????????????????????????????????????????????");
                    Constants.getSuccMsg(result, map);
                    return result;
                }*/


               /* Dictionary dictionary = memberMapper.selectDictionaryCodeByName(row.getCell(6).getStringCellValue());
                if (dictionary == null || "".equals(dictionary.getCode())) {
                    map.put("code", "205");
                    map.put("msg", "?????????" + i + "???????????????????????????");
                    Constants.getSuccMsg(result, map);
                    return result;
                }*/

                /*User user = new User();
                user.setRealName(row.getCell(7).getStringCellValue());
                user.setId(row.getCell(8).getStringCellValue());*/

               /* List<User> userList = memberMapper.selectUserInfoByIdAndRealName(user);
                if (userList == null || userList.size() == 0) {
                    map.put("code", "204");
                    map.put("msg", "?????????" + i + "??????????????????????????????????????????????????????");
                    //Constants.getSuccMsg(result, map);
                    return map;
                } else if (userList != null && !userList.get(0).getRealName().equals(user.getRealName())) {
                    map.put("code", "204");
                    map.put("msg", "?????????" + i + "????????????????????????????????????????????????????????????");
                    // Constants.getSuccMsg(result, map);
                    return map;
                }*/


                member.setEnterpriseName(row.getCell(0).getStringCellValue());//lp??????
                member.setEnterpriseCode(row.getCell(1).getStringCellValue());//????????????
                member.setAbbreviation(row.getCell(2).getStringCellValue());//lp??????
                if (row.getCell(3) != null && !row.getCell(3).getStringCellValue().isEmpty()) {
                    row.getCell(3).setCellType(CellType.STRING);
                    member.setContact(row.getCell(3).getStringCellValue());//?????????
                }
                if (row.getCell(4) != null && !row.getCell(4).getStringCellValue().isEmpty()) {
                    try {
                        row.getCell(4).setCellType(CellType.STRING);
                        member.setPhone(row.getCell(4).getStringCellValue());//????????????
                    } catch (Exception e) {
                        e.printStackTrace();
                        map.put("code", "204");
                        map.put("msg", "?????????" + i + "???????????????????????????????????????");
                        //Constants.getSuccMsg(result, map);
                        return map;
                    }
                }
                if (row.getCell(5) != null && !row.getCell(5).getStringCellValue().isEmpty()) {
                    row.getCell(5).setCellType(CellType.STRING);
                    member.setEmail(row.getCell(5).getStringCellValue());//??????
                }
                if (row.getCell(6) != null && !row.getCell(6).getStringCellValue().isEmpty()) {
                    row.getCell(6).setCellType(CellType.STRING);
                    member.setJob(row.getCell(6).getStringCellValue());//??????
                }
                if (row.getCell(7) != null && !row.getCell(7).getStringCellValue().isEmpty()) {
                    row.getCell(7).setCellType(CellType.STRING);
                    member.setProductSection(row.getCell(7).getStringCellValue());//??????????????????
                }
                if (row.getCell(8) != null && !row.getCell(8).getStringCellValue().isEmpty()) {
                    row.getCell(8).setCellType(CellType.STRING);
                    member.setProductName(row.getCell(8).getStringCellValue());//??????????????????
                }

                if (row.getCell(9) != null && !row.getCell(9).getStringCellValue().isEmpty()) {
                    row.getCell(9).setCellType(CellType.STRING);
                    member.setRemark(row.getCell(9).getStringCellValue());//??????
                }

                member.setDelFlag(1);//????????????
                member.setMemberType(code);//????????????
                String pwd = getRandomNumAndChacters(6);
                member.setPwd(pwd);
                Member memberByEnterpriseCode = memberMapper.getMemberByEnterpriseCode(member.getEnterpriseCode());
                Member memberByEnterpriseName = memberMapper.getMemberByEnterpriseName(member.getEnterpriseName());
                //????????????????????????????????????????????????????????????????????????????????????????????????
                if ((memberByEnterpriseCode == null || "".equals(memberByEnterpriseCode.getId())) && (memberByEnterpriseName == null || "".equals(memberByEnterpriseName.getId()))) {
                    super.insertBaseInfo(member); //????????????????????????????????????
                    //setMemberDictionaryList(member, dictionaryList);//?????????????????????????????????????????????????????????
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
                        super.updateBaseInfo(member, memberByEnterpriseCode.getId());//????????????????????????????????????
                        // setMemberDictionaryList(member, dictionaryList);//?????????????????????????????????????????????????????????????????????
                        updateMemberList.add(member);
                    } else {
                        map.put("code", "204");
                        map.put("msg", "???" + i + "?????????????????????????????????????????????????????????????????????????????????????????????");
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
            map.put("msg", "????????????");
            // Constants.getSuccMsg(result, map);
        } else {
            map.put("code", "200");
            map.put("msg", "???????????????????????????0");
            // Constants.getSuccMsg(result, map);
        }
        return map;
    }

    //?????????????????????????????????????????????????????????
    private Map<String, Object> selectUserByDepartmentIdAndUroleEqZeroList(String param) {
        Map<String, Object> result = new HashMap<>();
        String departmentId = JSONObject.parseObject(param).getString("departmentId");
        List<User> userList = memberMapper.selectUserByDepartmentIdAndUroleEqZeroList(departmentId);
        Constants.getSuccMsg(result, userList);
        return result;
    }

    //???????????????????????????
    private Map<String, Object> selectDictionaryByPropertyList(String param) {
        Map<String, Object> result = new HashMap<>();
        List<Dictionary> dictionaryList = memberMapper.selectDictionaryByPropertyList();
        Constants.getSuccMsg(result, dictionaryList);
        return result;
    }

    //???????????????????????????????????????
    private Map<String, Object> selectUserByUroleEqOneList(String param) {
        Map<String, Object> result = new HashMap<>();
        List<User> userList = memberMapper.selectUserByUroleEqOneList();
        Constants.getSuccMsg(result, userList);
        return result;
    }

    /*
     * ????????????????????????????????????(?????????????????????????????????????????????????????????)
     * param ---> {"userId":"xxx"}
     * userId ???????????????????????????
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

    //?????????????????????????????????????????????
    private Map<String, Object> selectDepartmentOfCodeAndNameList(String param) {
        Map<String, Object> result = new HashMap<>();
        List<Department> departmentList = memberMapper.selectDepartmentOfCodeAndNameList();
        Constants.getSuccMsg(result, departmentList);
        return result;
    }


    /**
     * ????????????????????????????????????????????????
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
     * ????????????????????????????????????
     * @param param
     * @return
     */
    private Map<String, Object> getMemberById(String param) {
        Map<String, Object> result = new HashMap<>();
        try {
            String memberId = JSONObject.parseObject(param).getString("memberId");
            if(memberId != null && !memberId.equals("")){
                Member member = memberMapper.getMemberById(memberId);

                //????????????  ????????????
                String memberType = member.getMemberType();
                if(memberType.equals("5")){
                    //???????????????????????????
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
                   /* //??????Gp?????????????????????
                    String fundname = null;
                    if(member.getInvestmentFund()!=null && !member.getInvestmentFund().equals("")){
                        fundname = memberMapper.getFundbyId(member.getInvestmentFund());
                    }
                    member.setInvestmentFundName(fundname);*/
                }
                //GP????????????
                String attentionIndustry = member.getAttentionIndustryId();
                if(attentionIndustry != null && !attentionIndustry.equals("")){
                    JSONArray jsonArray = JSONArray.parseArray(attentionIndustry);
                    String[] attentionIndustrys = jsonArray.toArray(new String[]{});
                    String[] attentionIndustrysName = memberMapper.getDictionaryLabelsbyCode("attentionIndustry",attentionIndustrys);
                    String attentionIndustryName = Arrays.toString(attentionIndustrysName);
                    String attentionIndustryName1 = attentionIndustryName.substring(attentionIndustryName.indexOf("[")+1, attentionIndustryName.lastIndexOf("]"));
                    member.setAttentionIndustry(attentionIndustryName1);
                }
                //GP????????????
                String attentionStage = member.getAttentionStageId();
                if(attentionStage != null && !attentionStage.equals("")){
                    JSONArray jsonArray = JSONArray.parseArray(attentionStage);
                    String[] attentionStages = jsonArray.toArray(new String[]{});
                    String[] attentionStagesname = memberMapper.getDictionaryLabelsbyCode("attentionStage",attentionStages);
                    String attentionStagename = Arrays.toString(attentionStagesname);
                    String attentionStagename1 = attentionStagename.substring(attentionStagename.indexOf("[")+1, attentionStagename.lastIndexOf("]"));
                    member.setAttentionStage(attentionStagename1);
                }
                //lp ??????????????????
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
                Constants.getErrMsg(result, "??????????????????");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Constants.getErrMsg(result, "??????????????????");
        }
        return result;
    }

    //??????????????????????????????
    private Map<String, Object> selectUserByUroleEqZeroList(String param) {
        Map<String, Object> result = new HashMap<>();
        List<User> mapList = memberMapper.selectUserByUroleEqZeroList();
        Constants.getSuccMsg(result, mapList);
        return result;
    }

    //??????????????????????????????????????????
    private Map<String, Object> selectComboOfCodeAndNameList(String param) {
        Map<String, Object> result = new HashMap<>();
        List<Dictionary> mapList = memberMapper.selectComboOfCodeAndNameList();
        Constants.getSuccMsg(result, mapList);
        return result;
    }

    //??????????????????
   /* private Map<String, Object> memberReviewSuccess(String param) {
        Map<String, Object> result = new HashMap<>();

        JSONObject jsonObject = JSONObject.parseObject(param);
        String userId = jsonObject.getString("userId");//??????????????????
        String memberId = jsonObject.getString("memberId");//??????????????????

        MemberDetail memberDetail = new MemberDetail();
        super.insertBaseInfo(memberDetail);//????????????????????????????????????
        memberDetail.setMemberId(memberId);
        memberDetail.setUserId(userId);
        memberDetail.setDelFlag(Integer.valueOf(1));//????????????

        int update = memberMapper.memberReviewSuccess(memberDetail);
        Constants.getSuccMsg(result, update > 0);
        return result;
    }*/

    //?????????????????????
    private Map<String, Object> memberReviewFaild(String param) {
        Map<String, Object> result = new HashMap<>();

        String userId = JSONObject.parseObject(param).getString("userId");
        User user = new User();
        super.updateBaseInfo(user, userId);//????????????????????????????????????
        user.setCertificationMark("2"); //2??????????????????

        int update = userInfoMapper.updateById(user);
        Constants.getSuccMsg(result, update > 0);
        return result;
    }

    //??????????????????
    private Map<String, Object> deleteMember(String param) {
        Map<String, Object> result = new HashMap<>();

        String memberId = JSONObject.parseObject(param).getString("memberId");
        Member member = new Member();
        super.updateBaseInfo(member, memberId);//????????????????????????????????????
        member.setDelFlag(Integer.valueOf(0));//????????????0???????????????
        Member m = memberMapper.getMemberById(memberId);
        //??????????????????
        //???????????????department??????
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
     * ??????????????????
     * @param param
     * @return
     */
    private Map<String, Object> updateMember(String param) {
        Map<String, Object> result = new HashMap<>();

        Member member = JSONObject.parseObject(param, Member.class);
        super.updateBaseInfo(member, member.getId());//????????????????????????????????????

       /* if (null != member.getReportKinds() && member.getReportKinds().size() > 0) {
            List<Dictionary> dictionaryList = memberMapper.selectDictionaryById(member.getReportKinds());
            setMemberDictionaryList(member, dictionaryList);//???????????????????????????????????????
        }*/
        Member memberByEnterpriseCode = memberMapper.getMemberByEnterpriseCode(member.getEnterpriseCode());
        if(memberByEnterpriseCode != null && !"".equals(memberByEnterpriseCode.getId())){
            if(!memberByEnterpriseCode.getId().equals(member.getId())){
                Constants.getSuccMsg(result, "?????????????????????");
                return result;
            }
        }
        //????????????????????????department???
        if(member.getMemberType().equals("4")){
            //??????Id????????????department
            List<Department> depts = memberMapper.getDepartmentById(member.getId());
            if(depts!=null && depts.size()>0){
                if(!depts.get(0).getCode().equals(member.getEnterpriseCode()) || !depts.get(0).getName().equals(member.getEnterpriseName())){
                    //????????????
                    int ud = memberMapper.updateDepartment(member.getEnterpriseName(),member.getEnterpriseCode(),depts.get(0).getId());
                }
            }else{
                //???????????????
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
     * ??????????????????
     * @param param
     * @return
     */
    private Map<String, Object> insertMember(String param) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> map = new HashMap<>();

        Member member = JSONObject.parseObject(param, Member.class);
        if(member == null || member.equals("")){
            Constants.getErrMsg(result, "??????????????????");
            return result;
        }
        Member memberByEnterpriseCode = memberMapper.getMemberByEnterpriseCode(member.getEnterpriseCode());
        if(memberByEnterpriseCode != null && !"".equals(memberByEnterpriseCode.getId())){
            Constants.getErrMsg(result, "????????????????????????");
            return result;
        }

        super.insertBaseInfo(member);//????????????????????????????????????
        //?????????????????????
        String pwd = getRandomNumAndChacters(6);
        member.setPwd(pwd);
        //???????????????????????????????????????department
        if(member.getMemberType().equals("4")){
            //???????????????
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
     * ???????????????????????????
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
            Constants.getErrMsg(result, "??????????????????");
        }
        return result;
    }

  /*  //???????????????????????????????????????
    private void setMemberDictionaryList(Member member, List<Dictionary> dictionaryList) {
        //?????????????????????????????????????????????
        List<MemberDictionary> memberDictionaryList = new ArrayList<>();
        //??????????????????????????????
        if (dictionaryList != null && dictionaryList.size() > 0) {
            for (Dictionary dictionary : dictionaryList) {
                MemberDictionary memberDictionary = new MemberDictionary();
                super.insertBaseInfo(memberDictionary);//??????????????????????????????????????????
                memberDictionary.setMemberId(member.getId());
                memberDictionary.setReportKind(dictionary.getCode());
                memberDictionary.setPropertyKind(dictionary.getKind());

                memberDictionaryList.add(memberDictionary);
            }
        }
        //member.setMemberDictionaryList(memberDictionaryList);
    }*/

    /**
     * ??????????????????list??????
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
            //??????????????????
            User u1 = userInfoMapper.getuRolebyId(u.getId());
            Integer count =  userInfoMapper.getCountByUser(u.getId(),"memberapproval");
            if(count <=0 && !StringUtils.equalsIgnoreCase("IR",u1.getJob())){
                Constants.getSuccMsg(result, null);
                return result;
            }
            if(u1==null||u1.equals("")||u1.getURole()== null ||u1.getURole().equals("")){
                Constants.getErrMsg(result, "????????????????????????");
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
                Constants.getErrMsg(result, "????????????????????????");
                return result;
            }
            //GP??????????????????????????????
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

            //???????????????
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
            Constants.getErrMsg(result, "??????????????????");
        }
            return result;
    }

    /**
     * ????????????list
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
                //??????dictionary??????
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
     * ????????????????????????byid
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
            Constants.getErrMsg(result, "??????????????????");
        }
        return result;
    }

    private int setchuangyeApprovalstatus(String memberuserId){
        //??????????????????????????????
        MemberUser memberUser = memberMapper.getMemberDetailById2(memberuserId);
        BaseUserInfo u = super.getUserInfo();
        //??????????????????????????????
        User u1 = memberMapper.getUserInfo(u.getId());
        //??????????????????????????????????????????????????????  1GP, 2??????
        if(memberUser.getURole().equals("5") && u1.getURole().equals("1")){

            //?????????????????????3???????????????????????????gp,???????????????????????????????????????
            if(memberUser.getApprovalstatus().equals("3")){

                if(u1.getMemberId().equals(memberUser.getModifymemberId())){
                    return 0;
                }else{
                    return 2;
                }
            }//?????????????????????0????????????????????????GP??????????????????>1 ????????????3???<=1 ??????2
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
     * ??????????????????
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
            Constants.getErrMsg(result, "??????????????????");
            return  result;
        }
        super.updateBaseInfo(md,md.getId());
        int u1 = 0;
        //??????????????????
        String status = memberMapper.getMemberDetailstatus(md);
        if(md.getMemberType().equals("4")){
            md.setDepartmentId(md.getMemberId());
        }else{
            md.setDepartmentId("001");
        }
        // 0  ?????????
        if(status.equals("0")){
            u1 = memberMapper.updateMemberUsernoUrole(md);
        }else if(status.equals("1")){
            //??????member_user??????user????????? ?????????????????????
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
                        Constants.getErrMsg(result, "?????????????????????");
                        return result;
                    }
                }
               // BaseUserInfo u = super.getUserInfo();
               // String status = memberMapper.getMemberDetailstatus(md);
                //if(status.equals("0")){
                    //super.updateBaseInfo(md, md.getId());//????????????????????????????????????
                    Integer i = memberMapper.updateApprovalstatus(md);
                    //?????????2?????????????????????????????????notes
                    if(i>0 && md.getApprovalstatus().equals("2")){
                        int i2 = memberMapper.updateUserCherk(md.getId());
                        MemberUser memberUser = memberMapper.getMemberDetailById(md.getId());
                        Integer i1 = setNotes("001","001","001",memberUser.getUserId());
                    }
                    if(i>0 && md.getApprovalstatus().equals("1")){
                        //??????user??? uRole
                      int i2 =  memberMapper.updateUseruRole(md.getId());
                        MemberUser memberUser = memberMapper.getMemberDetailById(md.getId());
                        Integer i1 = setNotes("003","003","003",memberUser.getUserId());
                  }
                    Constants.getSuccMsg(result,i>0);
                /*}else{
                    Constants.getErrMsg(result,"??????????????????????????????");
                }*/
            }else{
                Constants.getErrMsg(result, "??????????????????");
            }

        return  result;
    }

    /**
     * ????????????
     * ????????????
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
        //????????????id
        Dictionary dnotes = memberMapper.selectDictionaryByCode(notesid,"notesid");
        //Notes notes1 = new Notes();
        if(dnotes == null || dnotes.equals("") || dnotes.getName() == null ||dnotes.getName().equals("")){
            //???????????????title
            Dictionary dtitle = memberMapper.selectDictionaryByCode(title,"title");
            Dictionary dcontent = memberMapper.selectDictionaryByCode(content,"content");
            if(dcontent == null || dcontent.equals("") || dcontent.getName() == null ||dcontent.getName().equals("")){
                if(content.equals("003")){
                    notes.setContent("????????????????????????????????????????????????????????????????????????????????????????????????????????????");
                }else if(content.equals("001")){
                    notes.setContent("????????????????????????????????????????????????????????????");
                }
            }else{
                notes.setContent(dcontent.getName());
            }
            if(dtitle == null || dtitle.equals("") || dtitle.getName() == null ||dtitle.getName().equals("")){
                if(title.equals("003")){
                    notes.setTitle("????????????");
                }else if(title.equals("001")){
                    notes.setTitle("????????????");
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
            //????????????
            List<Notes> n = notesMapper.getNoteById(dnotes.getName());
            if(n == null || n.size() <= 0){
                //????????????
                //???????????????title
                Dictionary dtitle = memberMapper.selectDictionaryByCode(title,"title");
                Dictionary dcontent = memberMapper.selectDictionaryByCode(content,"content");
                if(dcontent == null || dcontent.equals("") || dcontent.getName() == null ||dcontent.getName().equals("")){
                    if(content.equals("003")){
                        notes.setContent("????????????????????????????????????????????????????????????????????????????????????????????????????????????");
                    }else if(content.equals("001")){
                        notes.setContent("????????????????????????????????????????????????????????????");
                    }
                }else{
                    notes.setContent(dcontent.getName());
                }
                if(dtitle == null || dtitle.equals("") || dtitle.getName() == null ||dtitle.getName().equals("")){
                    if(title.equals("003")){
                        notes.setTitle("????????????");
                    }else if(title.equals("001")){
                        notes.setTitle("????????????");
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
        //??????????????????
        String templateId = "2WYFptTPrbIExm97CLGZ-iaveFA19WQGxwi5q7yyeRI";
        notes.setTemplateId(templateId);
        this.sendweixin(notes);
        this.sendEmaillist(notes);

        return i1;

    }

   /* *//**
     * ?????????????????????
     * @param notes
     *//*
    public void sendweixin(Notes notes){
        String templateId="Slxjlr_FPAdcwrIFuR4KIUoA53KggalBxkS2ICbQUik";
        List<String> userIds = notes.getUserIds();
        //??????userId,??????user??????unionid????????????
        List<User> users = notesMapper.getUserUnionId(userIds);
        if(users!=null && users.size()>0){
            //??????????????????
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
                        //unionid?????????????????????
                        //unionid????????????openId??????????????????list
                        if (users.get(i).getUnionid() != null && !users.get(i).getUnionid().equals("")) {
                            unusers.add(users.get(i).getId());
                        }
                    }
                }
            }
                //?????????unionud????????????openId?????????????????????????????????????????????????????????
                if(unusers!=null && unusers.size()>0){
                    String accessToken = (String) redisTemplate.opsForValue().get("access_token");
                    //????????????????????????openId
                    List<String> nextopenId = notesMapper.getNewOpenId();
                    List<String> list = wechatpushUtil.GetOpenIdList(nextopenId.get(0),accessToken);
                    //???openId??????
                    int e = pushMapper.insertweixinIfo(list);
                    //??????weixin_info??????unionid???????????????
                    List<WeixinInfo> unionidemptys = notesMapper.getUnionidempty();
                    for (int j=0;j<unionidemptys.size();j++){
                        String unionid = wechatpushUtil.getUnionId(unionidemptys.get(j).getOpenId(),accessToken);
                        unionidemptys.get(j).setUnionid(unionid);
                    }
                    //???unionid??????
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
                //??????appid /url
            //??????kind???????????????list
            List<Dictionary> appids = memberMapper.getMemberTypelist("weixinappId");
            List<Dictionary> urls = memberMapper.getMemberTypelist("weixinurl");
            pushEntity.setAppId(appids.get(0).getName());
            pushEntity.setUrl(urls.get(0).getName());
            //????????????
            wechatpushUtil.sendMsg(appId,secret,pushEntity,templateId,openIds);
        }
    }*/
    /**
     * ?????????????????????
     * @param notes
     */
    public void sendweixin(Notes notes){
        //???????????????
        String userName = notesMapper.getRealName(notes.getUserIds().get(0));
        notes.setUserName(userName);
        Thread t =  new SendWechatThread(sendwechat,notes);
        t.start();

    }
    public void sendEmaillist(Notes notes){
        List<String> emailList = null;

        if(notes.getUserIds()!=null && notes.getUserIds().size()>0) {
            //??????nodes_detail???????????????????????????
            emailList = notesMapper.getUserEmails(notes.getUserIds());
        }
        if(emailList != null && emailList.size() > 0){
            //??????kind???????????????list
            List<Dictionary> d = memberMapper.getEmail("email");
            if(d != null && d.size() > 0) {
                //??????????????????flag???2???3????????????.???????????? all
                ExecutorService es = Executors.newFixedThreadPool(10);
                for (String email : emailList) {
                    es.execute(new SendMailThread(email, notes.getTitle(), notes.getContent(), d.get(0).getName(), d.get(0).getCode(), d.get(0).getPicurl()));
                }
                es.shutdown();
            }
        }
    }

    /**
     * ????????????
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
            Constants.getErrMsg(result, "??????????????????");
            return result;
        }
        if(lists==null||lists.size()<=0){
            Constants.getErrMsg(result, "??????????????????");
        }else {
            super.updateBaseInfo(md, md.getId());//????????????????????????????????????
            Integer i = memberMapper.batchupdateApprovalstatus(lists);
            if(i>0 && lists.get(0).getApprovalstatus().equals("2")){
                //????????????id
                Dictionary dnotes = memberMapper.selectDictionaryByCode("001","notesid");
                if(dnotes == null || dnotes.equals("") || dnotes.getName() == null ||dnotes.getName().equals("")){
                    Notes notes = new Notes();
                    //???????????????title
                    Dictionary dtitle = memberMapper.selectDictionaryByCode("001","title");
                    Dictionary dcontent = memberMapper.selectDictionaryByCode("001","content");
                    if(dcontent == null || dcontent.equals("") || dcontent.getName() == null ||dcontent.getName().equals("")){
                        notes.setContent("????????????????????????????????????????????????????????????");
                    }else{
                        notes.setContent(dcontent.getName());
                    }
                    if(dtitle == null || dtitle.equals("") || dtitle.getName() == null ||dtitle.getName().equals("")){
                        notes.setTitle("????????????????????????");
                    }else{
                        notes.setTitle(dtitle.getName());
                    }
                }else{

                }
            }
        }

    }*/

    /**
     * ??????????????????list
     * ????????????????????????
     * @return
     */
    private Map<String,Object> getMemberTypeList(){
        Map<String,Object> result = new HashMap<>();
        //??????kind???membertype??????????????????
        List<Dictionary> d = memberMapper.getMemberTypelist("membertype");
        Constants.getSuccMsg(result,d);
        return result;
    }


    /**
     * ???????????????
     *  @param //kind
     * membertype  ????????????
     *  attentionIndustry  ????????????
     *  attentionStage ????????????
     * industryClassification ??????????????????
     * directInvestmentClassification ??????????????????
     * rhinocerosLabel ????????????
     * firstInvestmentStage ??????????????????
     * participationFund ??????????????????
     * ????????????  productlabel
     * @return
     */
    private Map<String,Object> getDictionaryList(String param){
        Map<String,Object> result = new HashMap<>();
        String kind = JSONObject.parseObject(param).getString("kind");
        //??????kind???????????????list
        List<Dictionary> d = memberMapper.getMemberTypelist(kind);
        Constants.getSuccMsg(result,d);
        return result;
    }

    /**
     * ??????????????????list
     * ?????????
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
     * ??????????????????list
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
                //??????dictionary??????
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
     * ????????????????????????????????????,?????????length
     * @param length
     * @return ????????????????????????????????????String?????????
     */
    public static String getRandomNumAndChacters(int length){
        Random random = new Random();
        String str = null;
        do {
            str = new String();
            for (int i = 0; i < length; i++) {
                boolean b = random.nextBoolean();
                if(b){
                    int choice = random.nextBoolean() ? 65 : 97;//?????????65???????????????  97???????????????
                    str += (char)(choice+random.nextInt(26));
                }else{
                    str += String.valueOf(random.nextInt(10));
                }
            }
        } while (validate(str));//???????????????????????????????????????
        return str;
    }
    /**
     * ????????????????????????????????????????????????????????????????????????
     * @param str
     * @return   true:?????????????????????????????????false???????????????????????????????????????
     */
    public static boolean validate(String str){
        Pattern pattern = Pattern.compile("^([0-9]+)|([A-Za-z]+)$");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * ??????????????????
     * ????????????id ??? name
     * @param param
     * @return
     */
   private Map<String,Object> getMemberByType(String param){

       Map<String,Object> result = new HashMap<>();
       try {
           String memberType = JSONObject.parseObject(param).getString("memberType");
           if(memberType == null || memberType.equals("")){
               Constants.getErrMsg(result, "????????????");
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
     * ???????????????????????????
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
           Constants.getSuccMsg(result,"??????????????????");
       }
       return result;
   }

    /**
     * ??????????????????
     * @param param
     * @return
     */
    private Map<String,Object> delMemberUserById(String param){
       Map<String,Object> result = new HashMap<>();
        //?????????????????????id
        String id = JSONObject.parseObject(param).getString("id");
        String userId = JSONObject.parseObject(param).getString("userId");
        MemberUser mu = new MemberUser();
        mu.setUserId(userId);
        super.updateBaseInfo(mu, id);//????????????????????????????????????
        mu.setDelFlag(0);
        int delmember = memberMapper.delMemberuserById(mu);
        Constants.getSuccMsg(result, delmember > 0);
        return result;
    }

    /**
     * ??????????????????list
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
     * ??????????????????
     * @param param
     * @return
     */
    private Map<String, Object> updateMemberUser(String param){
        Map<String, Object> result = new HashMap<>();
        MemberUser md = JSONObject.parseObject(param,MemberUser.class);
        //???????????????????????????urole????????????
        int i = memberMapper.selectmember(md.getMemberType(),md.getMemberId());
        if(i <= 0){
            Constants.getErrMsg(result,"?????????????????????");
            return result;
        }else{
            super.updateBaseInfo(md,md.getId());
            int u1 = 0;
            //??????????????????
            String status = memberMapper.getMemberDetailstatus(md);
            if(md.getMemberType().equals("4")){
                md.setDepartmentId(md.getMemberId());
            }else{
                md.setDepartmentId("001");
            }
            // 0  ?????????
            if(status.equals("0")){
                u1 = memberMapper.updateMemberUsernoUrole(md);
            }else if(status.equals("1")){
                //??????member_user??????user????????? ?????????????????????
                u1 = memberMapper.updateMemberUser(md);
            }else if(status.equals("2")){
                u1 = memberMapper.updateMemberUsernoUrole2(md);
            }
            Constants.getSuccMsg(result, u1>0);
            return result;
        }
    }
   //??????????????????????????????
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
     * ????????????
     *//*
    private Map<String,Object> exportExcel(String param){
        Map<String,Object> result = new HashMap<>();

    }*/
    @RequestMapping(value = {"/Memberload"}, method = RequestMethod.POST, produces = "application/octet-stream;charset=utf-8")
    public String ExportExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String memberId = request.getParameter("memberId");// ??????????????????????????????????????????????????????????????????
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYYMMDDhhmmss");
        String now = dateFormat.format(new Date());
        String exportFileName = "??????" + now + ".xlsx";
        String[] headers = {"????????????", "????????????", "????????????", "????????????", "??????????????????", "????????????", "?????????", "????????????",
                "??????", "????????????", "?????????"};//?????????Excel?????????????????????????????????????????????
        List<MemberUserExport> interfacesVOPageData = memberMapper.filterQuery(memberId);

        //?????????????????????????????????Excel???????????????????????????
        // ?????????????????????
        XSSFWorkbook workbook = new XSSFWorkbook();
        // ??????????????????
        XSSFSheet sheet = workbook.createSheet();
        // ??????????????????????????????15?????????
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
        //????????????????????????????????????
        Iterator it = interfacesVOPageData.iterator();
        int index = 0;
        while (it.hasNext()) {
            index++;
            row = sheet.createRow(index);
            MemberUserExport objectValue = (MemberUserExport) it.next();
            //?????????????????????javabean????????????????????????????????????getXxx()?????????????????????
            Field[] fields = objectValue.getClass().getDeclaredFields();
            for (short i = 0; i < fields.length; i++) { // ????????????fields ????????????
                XSSFCell cell = row.createCell(i);
                Field field = fields[i];
                String fieldName = field.getName();
                String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                // ??????getName() ????????????
                try {
                    Class tCls = objectValue.getClass();
                    Method getMethod = tCls.getMethod(getMethodName,
                            new Class[]{});
                    Object value = getMethod.invoke(objectValue, new Object[]{});
                    // ????????????invoke ??????bug?????????
                    String textValue = null;
                    // ?????????????????? ???????????? ????????????????????????????????????
                    if (value instanceof Date) {
                        Date date = (Date) value;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        textValue = sdf.format(date);
                    } else {
                        //????????????????????????????????????????????????
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
        response.setContentType("application/vnd.ms-excel;charset=utf-8");// ??????contentType???excel??????
        response.setHeader("Content-disposition", "attachment;filename=" + exportFileName);//??????Excel??????
        response.flushBuffer();
        workbook.write(response.getOutputStream());
        return "succ";
    }

    /**
     * ??????GP??????????????????ID,NAME
     *
     */
    private Map<String, Object> getGPMemberlist(){
        Map<String, Object> result = new HashMap<>();
        //List<Member> members = memberMapper.getMemberByType("1");
        List<Member> members = memberMapper.getinvestmentFundlist("1");
        Constants.getSuccMsg(result,members);
        return result;
    }

    //???????????????id name
    private Map<String, Object> getGYList(){
        Map<String, Object> result = new HashMap<>();
        List<Department> departments = memberMapper.getDeptList("4");
        Constants.getSuccMsg(result, departments);
        return result;
    }
}
