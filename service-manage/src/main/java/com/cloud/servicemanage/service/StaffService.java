package com.cloud.servicemanage.service;

import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.aop.OperationLogDetail;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.factory.BaseService;
import com.cloud.servicemanage.common.ConstantUtil;
import com.cloud.servicemanage.common.PageUtil;
import com.cloud.servicemanage.common.PageVo;
import com.cloud.servicemanage.common.UUIDGenerator;
import com.cloud.servicemanage.entity.*;
import com.cloud.servicemanage.entity.Dictionary;
import com.cloud.servicemanage.mapper.StaffMapper;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Service("Staff")
public class StaffService extends BaseService {

    @Autowired
    public StaffMapper staffMapper;

    public Map<String, Object> exec(String method, String param) {
        Map<String, Object> obj = new HashMap<String, Object>();
        switch (method) {
            case ConstantUtil.GET_STAFF_BY_ID: //getStaffById
                obj = getStaffById(param);
                break;
            case ConstantUtil.GET_STAFF_SUM: //getStaffSum
                obj = getStaffSum(param);
                break;
            case ConstantUtil.INSERT_STAFF: //insertStaff
                obj = insertStaff(param);
                break;
            case ConstantUtil.SELECT_BY_NAME: //selectByName
                obj = selectByName(param);
                break;
            case ConstantUtil.DELETE_STAFF_INFO: //deleteStaffInfo
                obj = deleteStaffInfo(param);
                break;
            case ConstantUtil.SELECT_ROLE: //selectRole
                obj = selectRole();
                break;
            case ConstantUtil.SELECT_EDUCATION_OR_TYPE: //selectEducationOrType
                obj = selectEducationOrType(param);
                break;
            case ConstantUtil.IS_STAFFID_EXISTED: //isStaffIdExisted
                obj = isStaffIdExisted(param);
                break;
            case ConstantUtil.IS_LOGINNAME_EXISTED: //isLoginnameExisted
                obj = isStaffLoginNameExisted(param);
                break;
            case ConstantUtil.SELECT_USER_EDUCATION: //selectUserEducation
                obj = selectUserEdu(param);
                break;
            case ConstantUtil.SELECT_USER_DEPARTMENT: //selectUserDepartMent
                obj = selectUserDepart(param);
                break;
            case ConstantUtil.SELECT_USER_ROLE_ID: //selectUserRoleId
                obj = selectUserRoleId(param);
                break;
            case ConstantUtil.SELECT_USER_ROLE_NAME: //selectUserRoleName
                obj = selectUserRoleName(param);
                break;
            case ConstantUtil.UPDATE_STAFF: //updateStaff
                obj = updateStaff(param);
                break;
            case ConstantUtil.IMPORT_STAFF_BEFORE: //importStaffBefore
                obj = importStaffBefore(param);
                break;
            case ConstantUtil.INSERT_FILE_IMPORT: //insertFileImport
                obj = insertFileImport(param);
                break;
            case ConstantUtil.SHOW_STAFF_DATA: //showStaffData
                obj = showStaffData(param);
                break;
            case ConstantUtil.INSERT_USER_DATA: //insertUserData
                obj = insertUserData(param);
                break;
            case ConstantUtil.IS_EMPTY_ERRORMSG: //isEmptyErrorMsg
                obj = isEmptyErrorMsg(param);
                break;
            case ConstantUtil.SELECT_ERROR_MSG: //selectErrorMsg
                obj = selectErrorMsg(param);
                break;
            case ConstantUtil.UPDATE_PASSWORD:
                obj = updatePassword(param);
                break;
            default:
                break;
        }
        return obj;
    }

    List<FileImportDetail> FileImportDetailList = null;

    /**
     * ????????????????????????????????????Excel
     *
     * @param filepath
     * @return
     */
    @OperationLogDetail(detail = "????????????-??????Excel")
    private Map<String, Object> importStaffBefore(String filepath) {
        Map<String, Object> result = new HashMap<String, Object>();

        XSSFWorkbook workbook = null;
        FileImportDetailList = new ArrayList<FileImportDetail>();
//        JSONObject jsonObject = JSON.parseObject(filepath);
        try {
            File in = new File(filepath);
            //?????????????????????
            //InputStream file = new FileInputStream(in);
            //??????excel???????????????
            workbook = new XSSFWorkbook(new FileInputStream(in));
            //1.??????sheet??????
//            int sheetNum = workbook.getNumberOfSheets();
            //???????????????sheet
            XSSFSheet sheet = workbook.getSheetAt(0);
            //???????????????
            int rowTotal = sheet.getPhysicalNumberOfRows();
            //??????????????????
            if (rowTotal <= 1) {
                Constants.getSuccMsg(result, new HashMap<String, String>() {
                    {
                        put("code", "201");
                        put("msg", "????????????");
                    }
                });
                return result;
            }
            //??????????????????
            int lastRowNum = sheet.getLastRowNum() + 1;
            //?????????????????????
            if (rowTotal != lastRowNum) {
                Constants.getSuccMsg(result, new HashMap<String, String>() {
                    {
                        put("code", "202");
                        put("msg", "???????????????");
                    }
                });
                return result;
            }

            //??????Cell
            FileImportDetail fileImportDetail = null;
            flag:
            for (int i = 1; i < lastRowNum; i++) {
                fileImportDetail = new FileImportDetail();
                //???????????????i??????
                Row row = sheet.getRow(i);
                //fileImportDetail.setId(UUIDGenerator.getUUID());
                row.getCell(0).setCellType(CellType.STRING);
                if (row.getCell(0).getStringCellValue().isEmpty()) {
                    break flag;
                } else {
                    fileImportDetail.setUser_id(row.getCell(0).getStringCellValue());
                    row.getCell(1).setCellType(CellType.STRING);
                    fileImportDetail.setUser_name(row.getCell(1).getStringCellValue());
                    row.getCell(2).setCellType(CellType.STRING);

                    if (fileImportDetail.getUser_id() == null && fileImportDetail.getUser_name() == null) {
                        break;
                    }
                    fileImportDetail.setUser_sex(row.getCell(2).getStringCellValue());
                    row.getCell(3).setCellType(CellType.STRING);
                    fileImportDetail.setUser_loginname(row.getCell(3).getStringCellValue());
                    row.getCell(4).setCellType(CellType.STRING);
                    fileImportDetail.setUser_dept(row.getCell(4).getStringCellValue());
                    row.getCell(5).setCellType(CellType.STRING);
                    fileImportDetail.setUser_birth(HSSFDateUtil.getJavaDate(Double.valueOf(row.getCell(5).getStringCellValue())));
                    row.getCell(6).setCellType(CellType.STRING);
                    fileImportDetail.setUser_role(row.getCell(6).getStringCellValue());
                    row.getCell(7).setCellType(CellType.STRING);
                    fileImportDetail.setUser_phone(row.getCell(7).getStringCellValue());
                    row.getCell(8).setCellType(CellType.STRING);
                    fileImportDetail.setUser_email(row.getCell(8).getStringCellValue());
                    fileImportDetail.setErrorCode("");
                    fileImportDetail.setErrorMsg("");

//                    String userRole=fileImportDetail.getUser_role();
//                    String userRoleArray[]=userRole.split(",");
//                    List<String> list = Arrays.asList(userRoleArray);
//                    fileImportDetail.setList(list);

                    //System.out.print(list);
                }

                FileImportDetailList.add(fileImportDetail);

            }
            result.put("FileImportDetailList", FileImportDetailList);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private Map<String, Object> insertFileImport(String param) {
        Map<String, Object> result = new HashMap<String, Object>();

        //String a ="C:/Users/dell1/Desktop/??????????????????(1).xlsx";
        //FileImport fileImport =JSONObject.parseObject(param,FileImport.class);
        FileImport fileImport = new FileImport();
        //fileImport.setFilepath(a);
        JSONObject jsonObject = JSONObject.parseObject(param);

        fileImport.setId(UUIDGenerator.getUUID());
        String id = fileImport.getId();

        String filepath = jsonObject.getString("filePath");
        fileImport.setFilepath(filepath);
        //String filename=filepath.substring(filepath.indexOf("D:\\Program Files\\ideaWorkSpace\\topcheer/file/"),filepath.indexOf(".xlsx"));

        String batchidtemp = filepath.substring(filepath.lastIndexOf(File.separator) + 1);
        String batchid = batchidtemp.substring(0, batchidtemp.indexOf("."));
        fileImport.setBatchid(batchid);
        String creatorid = this.usr.getId();
        fileImport.setCreatorid(creatorid);
        String modifierid = this.usr.getId();
        fileImport.setModifierid(modifierid);

        //??????excel
        result = this.importStaffBefore(filepath);
        if (result.size() != 1) { //1 List
            return result;
        } else {
            List<FileImportDetail> fileImportDetailList = (List<FileImportDetail>) result.get("FileImportDetailList");
            fileImport.setFileImportDetailList(FileImportDetailList);

            Integer insertFileImport = staffMapper.insertFileImport(fileImport);

            //?????????????????????
            //Integer check=staffMapper.checkFileImportDetail(batchid,fileImportDetailList);
            // staffMapper.checkFileImportDetail(batchid);

            if (insertFileImport <= 0) {
                Constants.getSuccMsg(result, Constants.RESULT_NO_DATA); //""
            } else {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("code", "200");
                hashMap.put("batchid", batchid);
                Constants.getSuccMsg(result, hashMap);
            }
            return result;
        }
    }

    /**
     * ????????????
     *
     * @param param
     * @return
     */
    private Map<String, Object> showStaffData(String param) {
        Map<String, Object> result = new HashMap<String, Object>();
        //????????????????????????????????????Object??????
        PageVo pageVo = JSONObject.parseObject(param, PageVo.class);

        //???????????????????????????
        PageUtil pageUtil = pageVo.getPage();
        try {
            //??????????????????
            int totalNum = staffMapper.getStaffDataTotal(pageVo);
            if (totalNum > 0) {
                List<FileImportDetail> list = staffMapper.showStaffData(pageVo);
                //???????????? ?????????pageVo????????????dataList???????????????????????????
                PageVo<FileImportDetail> pageVo1 = new PageVo<>();
                pageVo1.setPage(new PageUtil(pageUtil.getPageIndex(), pageUtil.getPageSize(), totalNum));
                pageVo1.setDataList(list);
                Constants.getSuccMsg(result, pageVo1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * ??????User???
     *
     * @param param
     * @return
     */
    private Map<String, Object> insertUserData(String param) {
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject jsonObject = JSONObject.parseObject(param);
        String batchid = jsonObject.getString("batchid");

        int insertUserData = staffMapper.insertUserData(batchid, FileImportDetailList);
        Constants.getSuccMsg(result, insertUserData);
        return result;
    }

    /**
     * ??????ErrorMsg????????????
     *
     * @param param
     * @return
     */
    private Map<String, Object> isEmptyErrorMsg(String param) {
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject jsonObject = JSONObject.parseObject(param);
        String batchid = jsonObject.getString("batchid");
        int isEmptyErrorMsg = staffMapper.isEmptyErrorMsg(batchid);
        if (isEmptyErrorMsg == 0) {
            Constants.getSuccMsg(result, new HashMap<String, String>() {
                {
                    put("code", "200");
                    put("msg", "????????????");
                }
            });
        } else {
            Constants.getSuccMsg(result, new HashMap<String, String>() {
                {
                    put("code", "205");
                    put("msg", "????????????????????????????????????");
                }
            });
        }
        return result;
    }

    /**
     * ??????????????????
     *
     * @param param
     * @return
     */
    private Map<String, Object> selectErrorMsg(String param) {
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject jsonObject = JSONObject.parseObject(param);
        String batchid = jsonObject.getString("batchid");
        List<FileImportDetail> msglist = staffMapper.selectErrorMsg(batchid);
        Constants.getSuccMsg(result, msglist);
        return result;
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param param
     * @return
     */
    private Map<String, Object> getStaffById(String param) {
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject jsonObject = JSONObject.parseObject(param);

        result.put("departmentid", jsonObject.getString("departmentid"));
        result.put("CurrentPage1", jsonObject.getString("currentPage1"));
        result.put("PageSize1", jsonObject.getString("pageSize1"));
        result.put("switch1", jsonObject.getBoolean("switch1"));
        result.put("orderKind", jsonObject.getString("orderKind"));
        result.put("orderName", jsonObject.getString("orderName"));

        List<StaffInfo> stafflist1 = staffMapper.getStaffById(result);

        if (stafflist1.size() <= 0) {
            Constants.getSuccMsg(result, Constants.RESULT_NO_DATA);
        } else {
            Constants.getSuccMsg(result, stafflist1);
        }

        return result;
    }

    /**
     * ????????????????????????????????????
     *
     * @param param
     * @return
     */
    private Map<String, Object> getStaffSum(String param) {
        Map<String, Object> resultSum = new HashMap<String, Object>();
        JSONObject jsonObject = JSONObject.parseObject(param);
        String departmentid = jsonObject.getString("departmentid");
        Boolean switch1 = jsonObject.getBoolean("switch1");
        String isused = null;

        Integer SUM = staffMapper.getStaffSum(departmentid, isused, switch1);

        Constants.getSuccMsg(resultSum, SUM);

        return resultSum;
    }

    /**
     * ????????????????????????????????????
     *
     * @param param
     * @return
     */
    private Map<String, Object> selectByName(String param) {
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject jsonObject = JSONObject.parseObject(param);
        String username = jsonObject.getString("name");
        String departmentid = jsonObject.getString("departmentId");

        List<StaffInfo> stafflist = staffMapper.selectByName(username, departmentid);

        if (stafflist.size() <= 0) {
            Constants.getSuccMsg(result, Constants.RESULT_NO_DATA);
        } else {
            Constants.getSuccMsg(result, stafflist);
        }
        return result;
    }

    /**
     * ??????????????????
     *
     * @param param
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Map<String, Object> insertStaff(String param) {
        Map<String, Object> result = new HashMap<String, Object>();
        StaffInfo staffInfo = JSONObject.parseObject(param, StaffInfo.class);
        staffInfo.setId(UUIDGenerator.getUUID());
        staffInfo.setUserId(this.usr.getId());
        try {
            Integer insertStaff = staffMapper.insertStaff(staffInfo);

            if (insertStaff <= 0) {
                Constants.getErrMsg(result, Constants.RESULT_NO_DATA);
            } else {
                Constants.getSuccMsg(result, Constants.RESULTCODE_SUCC);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Constants.getErrMsg(result, Constants.RESULT_NO_DATA);
        }


        return result;
    }

    /**
     * ????????????
     *
     * @param param
     * @return
     */
    private Map<String, Object> deleteStaffInfo(String param) {
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject jsonObject = JSONObject.parseObject(param);
        String id = jsonObject.getString("id");
        String isused = jsonObject.getString("switchChange");

        Integer deleteStaff = staffMapper.deleteStaff(isused, id);

        Constants.getSuccMsg(result, deleteStaff);

        return result;
    }

    /**
     * ?????????????????????
     *
     * @param
     * @return
     */
    private Map<String, Object> selectRole() {
        Map<String, Object> resultSum = new HashMap<String, Object>();

        List<Role> SUM = staffMapper.selectRole();

        Constants.getSuccMsg(resultSum, SUM);

        return resultSum;
    }

    /**
     * ?????????????????????????????????
     *
     * @param param
     * @return
     */
    private Map<String, Object> selectEducationOrType(String param) {
        Map<String, Object> resultSum = new HashMap<String, Object>();
        JSONObject jsonObject = JSONObject.parseObject(param);
        String kind = jsonObject.getString("kind");
        List<Dictionary> SUM = staffMapper.selectEducationOrType(kind);

        Constants.getSuccMsg(resultSum, SUM);

        return resultSum;
    }

    /**
     * ??????????????????
     *
     * @param param
     * @return
     */
    private Map<String, Object> selectUserEdu(String param) {
        Map<String, Object> resultSum = new HashMap<String, Object>();
        JSONObject jsonObject = JSONObject.parseObject(param);
        List<UserEducation> edu = staffMapper.selectUserEdu(jsonObject.getString("id"));
        Constants.getSuccMsg(resultSum, edu);
        return resultSum;
    }

    /**
     * ????????????????????????
     *
     * @param param
     * @return
     */
    private Map<String, Object> selectUserDepart(String param) {
        Map<String, Object> resultSum = new HashMap<String, Object>();
        JSONObject jsonObject = JSONObject.parseObject(param);
        List<UserDepart> depart = staffMapper.selectUserDepart(jsonObject.getString("id"));
        Constants.getSuccMsg(resultSum, depart);
        return resultSum;
    }

    /**
     * ??????????????????ID
     *
     * @param param
     * @return
     */
    private Map<String, Object> selectUserRoleId(String param) {
        Map<String, Object> resultSum = new HashMap<String, Object>();
        JSONObject jsonObject = JSONObject.parseObject(param);
        List<String> roleId = staffMapper.selectUserRoleId(jsonObject.getString("id"));
        Constants.getSuccMsg(resultSum, roleId);
        return resultSum;
    }

    /**
     * ??????????????????NAME
     *
     * @param param
     * @return
     */
    private Map<String, Object> selectUserRoleName(String param) {
        Map<String, Object> resultSum = new HashMap<String, Object>();
        JSONObject jsonObject = JSONObject.parseObject(param);
        List<String> roleName = staffMapper.selectUserRoleName(jsonObject.getString("id"));
        Constants.getSuccMsg(resultSum, roleName);
        return resultSum;
    }

    /**
     * ??????????????????????????????
     *
     * @param param
     * @return
     */
    private Map<String, Object> isStaffIdExisted(String param) {
        Map<String, Object> result = new HashMap<String, Object>();

        // ????????????json??????sql????????????
        JSONObject jsonObject = JSONObject.parseObject(param);
        String id = jsonObject.getString("id");
        String empid = jsonObject.getString("empid");

        boolean isExisted = staffMapper.isStaffIdExisted(id, empid);
        if (isExisted) {
            Constants.getSuccMsg(result, Constants.RESULTCODE_REPEAT);
        } else {
            Constants.getSuccMsg(result, Constants.RESULT_NO_DATA);
        }

        return result;
    }

    /**
     * ?????????????????????????????????
     *
     * @param param
     * @return
     */
    private Map<String, Object> isStaffLoginNameExisted(String param) {
        Map<String, Object> result = new HashMap<String, Object>();

        // ????????????json??????sql????????????
        JSONObject jsonObject = JSONObject.parseObject(param);
        String id = jsonObject.getString("id");
        String loginname = jsonObject.getString("loginname");

        boolean isExisted = staffMapper.isStaffLoginNameExisted(id, loginname);
        if (isExisted) {
            Constants.getSuccMsg(result, Constants.RESULTCODE_REPEAT);
        } else {
            Constants.getSuccMsg(result, Constants.RESULT_NO_DATA);
        }

        return result;
    }

    /**
     * ??????????????????
     *
     * @param param
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Map<String, Object> updateStaff(String param) {
        Map<String, Object> result = new HashMap<String, Object>();
        StaffInfo staffInfo = JSONObject.parseObject(param, StaffInfo.class);

        staffInfo.setUserId(this.usr.getId());
        try {

            int updateStaff = staffMapper.updateStaff(staffInfo);

            if (updateStaff <= 0) {
                Constants.getSuccMsg(result, Constants.RESULTCODE_REPEAT);
            } else {
                Constants.getSuccMsg(result, Constants.RESULTCODE_SUCC);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Constants.getSuccMsg(result, Constants.RESULTCODE_REPEAT);
        }
        return result;
    }

    /**
     * ????????????
     *
     * @param param
     * @return
     */
    private Map<String, Object> updatePassword(String param) {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        JSONObject jsonObject = JSONObject.parseObject(param);
        String password = jsonObject.getString("oldPwd");
        String newPwd = jsonObject.getString("newPwd");
        String userid = this.usr.getId();
        String pwd = staffMapper.selectPwdByUserId(super.getUserInfo().getId());
        if (password.equals(pwd)) {
            Integer i = staffMapper.updatePassword(userid, newPwd);
            if (i > 0) {
                map.put("code", 200);
                map.put("data", "????????????");
            } else {
                map.put("code", 202);
                map.put("data", "?????????????????????????????????");
            }
        } else {
            map.put("code", 201);
            map.put("data", "???????????????????????????");
        }
        Constants.getSuccMsg(result, map);
        return result;
    }

}
