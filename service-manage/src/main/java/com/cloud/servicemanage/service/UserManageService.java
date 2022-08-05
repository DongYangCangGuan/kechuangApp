package com.cloud.servicemanage.service;

import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.entity.BaseUserInfo;
import com.cloud.commonsmng.entity.appletEntity.MemberUser;
import com.cloud.commonsmng.entity.appletEntity.User;
import com.cloud.commonsmng.factory.BaseService;
import com.cloud.servicemanage.common.ConstantUtil;
import com.cloud.servicemanage.common.PageUtil;
import com.cloud.servicemanage.common.PageVo;
import com.cloud.servicemanage.mapper.UsersMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("userMange")
public class UserManageService extends BaseService {


    @Autowired
    UsersMapper userMapper;

    @Override
    public Map<String, Object> exec(String method, String param) {
        Map<String, Object> obj = new HashMap<>();
        switch (method) {
            case ConstantUtil.GET_USER_LIST: //
                obj = getUserList(param);
                break;
            case ConstantUtil.INSERT_USER: //
                obj = insertUser(param);
                break;
            case ConstantUtil.UPDATE_USER: //
                obj = updateUser(param);
                break;
            case ConstantUtil.DELETE_USER: //
                obj = deleteUser(param);
                break;
            case ConstantUtil.EXPORT_USER: //
                obj = createDiff(param);
                break;
            default:
                break;
        }
        return obj;
    }


    /**
     * 获取用户
     *
     */
    private Map<String,Object> getUserList(String param){
        Map<String, Object> result = new HashMap<>();
        try {
            PageVo pagevo = JSONObject.parseObject(param,PageVo.class);
            PageUtil page = pagevo.getPage();//获取前端的页面分页信息
            BaseUserInfo u = super.getUserInfo();
            int pageTotal = userMapper.getUserCount(pagevo);
            List<User> list = userMapper.getUserList(pagevo);
            list.forEach((User user) -> {
                 String enterpriseName    =  userMapper.getEnterpriseName(user.getId());
                 user.setEnterpriseName(enterpriseName);
            });
            PageVo<User> pageVo = new PageVo<>();
            pageVo.setPage(new PageUtil(page.getPageIndex(), page.getPageSize(), pageTotal));
            pageVo.setDataList(list);
            Constants.getSuccMsg(result, pageVo);
        }catch (Exception e) {
            e.printStackTrace();
            Constants.getErrMsg(result, Constants.RESULT_NO_DATA);
        }

        return result;
    }

    /**
     * 新增用户
     *
     */
    private Map<String,Object> insertUser(String param){
        Map<String, Object> result = new HashMap<>();
        try {
            User user = JSONObject.parseObject(param,User.class);
            int count = userMapper.insertUser(user);
            Constants.getSuccMsg(result, count>0);
        }catch (Exception e) {
            e.printStackTrace();
            Constants.getErrMsg(result, Constants.RESULT_NO_DATA);
        }

        return result;
    }


    /**
     * 删除用户
     *
     */
    private Map<String,Object> deleteUser(String param){
        Map<String, Object> result = new HashMap<>();
        try {
            User user =  JSONObject.parseObject(param,User.class);
            int count = userMapper.deleteUser(user.getId());
            Constants.getSuccMsg(result, count>0);
        }catch (Exception e) {
            e.printStackTrace();
            Constants.getErrMsg(result, Constants.RESULT_NO_DATA);
        }

        return result;
    }

    /**
     * 修改用户
     *
     */
    private Map<String,Object> updateUser(String param){
        Map<String, Object> result = new HashMap<>();
        try {
            User user = JSONObject.parseObject(param,User.class);
            int count = userMapper.updateUser(user);
            Constants.getSuccMsg(result, count>0);
        }catch (Exception e) {
            e.printStackTrace();
            Constants.getErrMsg(result, Constants.RESULT_NO_DATA);
        }

        return result;
    }

//    //导出excel数据
//    private Map<String, Object> exportTakeStock(String param) {
//        Map<String,Object>result = new HashMap<>();
//        JSONObject req = JSONObject.parseObject(param);
//        List<StockInfo>list=statisticalMapper.exportStock((String)req.get("departId"),(String)req.get("gName"));
//        Constants.getSuccMsg(result,list);
//        return result;
//    }

    @Transactional
    public Map<String, Object> createDiff(String param) {
        Map<String, Object> result = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(param);
        Map map = new HashMap();
        String filepath = jsonObject.getString("filePath");
        if(filepath.endsWith(".xlsx") || filepath.endsWith(".xls")){
            Workbook workbook = null;
            Sheet sheet = null;
            BaseUserInfo u = super.getUserInfo();
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
                //物理总行数
                int rowTotal = sheet.getPhysicalNumberOfRows();
                //判断表格非空
                if (rowTotal <= 1) {
                    map.put("code", "201");
                    map.put("msg", "表格为空");
                    Constants.getSuccMsg(result, map);
                    return result;
                }
                if(rowTotal>100){
                    map.put("code","201");
                    map.put("msg","一次最多只能导入100条数据");
                    Constants.getSuccMsg(result,map);
                    return result;
                }
                //获取最后一行
                int lastRowNum = sheet.getLastRowNum() + 1;
                //判断是否有空行
                if (rowTotal != lastRowNum) {
                    map.put("code", "202");
                    map.put("msg", "表格有空行");
                    Constants.getSuccMsg(result, map);
                    return result;
                }
                //excel导入数据解析
                User user = null;//盘点表信息
                for (int i = 1; i < lastRowNum; i++) {
                    user = new User();
                    Row row = sheet.getRow(i);
                    for (int lineNum =0 ;lineNum <= 5;lineNum ++){
                        if(row.getCell(lineNum) == null ){
                            map.put("code", "207");
                            map.put("msg", "文件中有空格");
                            Constants.getSuccMsg(result, map);
                            return result;
                        }
                        row.getCell(lineNum).setCellType(CellType.STRING);
                        if (getCellValueByCell(row.getCell(i)).isEmpty()){
                            map.put("code", "207");
                            map.put("msg", "文件中有空格");
                            Constants.getSuccMsg(result, map);
                            return result;
                        }
                    }
// userName 用户名passWord  密码 realName 姓名 gender  性别 birthdate 出生日期 identityCard 身份证号 phone 手机号 email 邮箱 uRole 人员类型 contactAddress 地址
                    user.setUserName(getCellValueByCell(row.getCell(0)));
                    user.setPassWord(getCellValueByCell(row.getCell(1)));
                    user.setRealName(getCellValueByCell(row.getCell(2)));
                    user.setGender(Integer.parseInt(getCellValueByCell(row.getCell(3))));
                    Date date = HSSFDateUtil.getJavaDate(Double.parseDouble(getCellValueByCell(row.getCell(4))));
                    user.setBirthdate(date);
                    user.setIdentityCard(getCellValueByCell(row.getCell(5)));
                    user.setPhone(getCellValueByCell(row.getCell(6)));
                    user.setEmail(getCellValueByCell(row.getCell(7)));
                    user.setURole(getCellValueByCell(row.getCell(8)));
                    user.setContactAddress(getCellValueByCell(row.getCell(9)));
                    user.setCertificationMark("1");
                    user.setDelFlag("1");
                    user.setCreatorId(u.getId());
                    user.setModifierId(u.getId());
                    user.setCreateTime(new Date());
                    user.setModifyTime(new Date());

                    int count = userMapper.insertUser(user);
                }

                map.put("code","200");
                map.put("msg","导入完成");
                Constants.getSuccMsg(result,map);
            }catch (Exception e){
                map.put("code","500");
                map.put("msg","导入失败");
                Constants.getSuccMsg(result,map);
                e.printStackTrace();
            }finally {
                try {
                    workbook.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }else {
            map.put("code","203");
            map.put("msg","只支持xlsx和xls结尾的文件");
            Constants.getSuccMsg(result,map);
        }
        return result;
    }


    //获取单元格各类型值，返回字符串类型
    private static String getCellValueByCell(Cell cell) {
        if (cell==null || cell.toString().trim().equals("")) {
            return "";
        }
        String cellValue = "";

        switch (cell.getCellType()) {
            case STRING:
                cellValue= cell.getStringCellValue().trim();
                cellValue=StringUtils.isEmpty(cellValue) ? "" : cellValue;
                break;
            case BOOLEAN:
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
                    cellValue = sim.format(cell.getDateCellValue());
                } else {
                    cellValue = cell.getNumericCellValue()+"";
                }
                break;
            default:
                cellValue = "";
                break;
        }
        return cellValue;
    }
}

