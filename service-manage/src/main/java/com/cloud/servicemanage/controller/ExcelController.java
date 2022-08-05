/*
package com.cloud.servicemanage.controller;


import com.cloud.servicemanage.service.FileUploadService;
import com.cloud.servicemanage.utils.ExcelFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/manage/api/excel")
public class ExcelController {

    @Autowired
    private FileUploadService fileService;

    */
/**
     * excel导出
     * ResponseEntity<byte[]>
     *     RequestMethod.POST,
     *     @RequestBody
     *//*

    @RequestMapping(value = {"/exportexcel"}, method = {RequestMethod.GET}, produces = "application/octet-stream;charset=utf-8")
    public void exportexcel( String memberType,String startTime,String endTime, String enterpriseName, HttpServletResponse response)throws Exception{

        //
//        String param = "{}";
        // String decodeParam = DataProcessing.urlDecode(param);
        // String param1 = aesUtil.desEncryt(decodeParam, AESUtil.MANAGE_KEY);//解密
        Map<String,Object> map = fileService.exportExcel(memberType,startTime,endTime,enterpriseName);

        String title = map.get("title").toString();
        String[] rowName = (String[]) map.get("rowName");
        List<Object[]> datalist = (List<Object[]>) map.get("data");
        String fileName = title  + ".xls";
        ExcelFormatUtil ex = new ExcelFormatUtil(title,rowName,datalist,fileName);
        //给文件命名。随机命名
        System.out.println(datalist);

        //String fileName = "会员基本信息" + String.valueOf(System.currentTimeMillis()).substring(4, 13) + ".xls";

        //告诉浏览器数据格式，将头和数据传到前台
        String headStr = "attachment; filename=\"" + URLEncoder.encode(fileName,"utf-8") + "\"";
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.setHeader("Content-Disposition", headStr);
        OutputStream out = response.getOutputStream();
        //ResponseEntity<byte[]> re = null;
        try {
            ex.export(out);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            out.flush();
            out.close();

        }
        return;
        //return re;
    }


}
*/
