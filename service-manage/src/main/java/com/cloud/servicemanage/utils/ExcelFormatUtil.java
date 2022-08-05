package com.cloud.servicemanage.utils;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.xml.crypto.Data;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ExcelFormatUtil {

    // 显示的导出表的标题
    private String title;
    // 导出表的列名
    private String[] rowName;
    private List<Object[]> dataList = new ArrayList<Object[]>();
    private String filename;//文件名

    // 构造函数，传入要导出的数据
    public ExcelFormatUtil(String title, String[] rowName, List<Object[]> dataList,String filename) {
        this.dataList = dataList;
        this.rowName = rowName;
        this.title = title;
        this.filename = filename;
    }
    // 导出数据
    public void  export(OutputStream out)  {
        //ByteArrayOutputStream baos = null;
        //HttpHeaders headers = null;
        try {
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet(title);

            // 产生表格标题行
            HSSFRow rowm = sheet.createRow(0);
            HSSFCell cellTitle = rowm.createCell(0);


            //sheet样式定义【】
            HSSFCellStyle columnTopStyle=this.getColumnTopStyle(workbook);
            HSSFCellStyle style=this.getStyle(workbook);
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, (rowName.length - 1)));
            cellTitle.setCellStyle(columnTopStyle);
            cellTitle.setCellValue(title);

            // 定义所需列数
            int columnNum = rowName.length;
            HSSFRow rowRowName = sheet.createRow(2);

            // 将列头设置到sheet的单元格中
            for (int n = 0; n < columnNum; n++) {
                HSSFCell cellRowName = rowRowName.createCell(n);
                //cellRowName.setCellType(HSSFCell.CELL_TYPE_STRING);
                cellRowName.setCellType(CellType.STRING);
                HSSFRichTextString text = new HSSFRichTextString(rowName[n]);
                cellRowName.setCellValue(text);
                cellRowName.setCellStyle(columnTopStyle);

            }
            HSSFCellStyle style_date = workbook.createCellStyle();
            HSSFDataFormat df = workbook.createDataFormat();
            // 将查询到的数据设置到sheet对应的单元格中
            for (int i = 0; i < dataList.size(); i++) {
                Object[] obj = dataList.get(i);// 遍历每个对象
                HSSFRow row = sheet.createRow(i + 3);// 创建所需的行数

                for (int j = 0; j < obj.length; j++) {
                    HSSFCell cell = null;
                    if(j == obj.length-1){
                        //HSSFCellStyle style_date = workbook.createCellStyle();
                        style_date.setAlignment(HorizontalAlignment.CENTER);
                        style_date.setDataFormat(df.getFormat("yyyy-MM-dd"));
                        //style_date.setFont(font_content);


                        cell = row.createCell(j);
                        if (!"".equals(obj[j]) && obj[j] != null) {
                            cell.setCellStyle(style_date);
                            cell.setCellValue(obj[j].toString());
                           /* SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                            Data d = (Data) obj[j];
                            String dateString = formatter.format(d);
                            cell.setCellValue(dateString);*/
                        }else{
                            cell.setCellValue("");
                        }


                    }
                    if (j == 0) {
                        cell = row.createCell(j, CellType.NUMERIC);
                        cell.setCellValue(i + 1);
                    }
                    else {
                        cell = row.createCell(j, CellType.STRING);
                        if (!"".equals(obj[j]) && obj[j] != null) {
                            cell.setCellValue(obj[j].toString());
                        }else{
                            cell.setCellValue("");
                        }
                    }
                    cell.setCellStyle(style);

                }

            }

            // 让列宽随着导出的列长自动适应
            for (int colNum = 0; colNum < columnNum; colNum++) {
                int columnWidth = sheet.getColumnWidth(colNum) / 256;
                for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
                    HSSFRow currentRow;
                    if (sheet.getRow(rowNum) == null || sheet.getRow(rowNum).equals("")) {
                        currentRow = sheet.createRow(rowNum);
                    } else {
                        currentRow = sheet.getRow(rowNum);
                    }
                    if (currentRow.getCell(colNum) != null && !currentRow.getCell(colNum).equals("")) {
                        HSSFCell currentCell = currentRow.getCell(colNum);
                        if (currentCell.getCellType() == CellType.STRING) {

                            int length = currentCell.getStringCellValue().getBytes().length;
                            if (columnWidth < length) {
                                columnWidth = length;
                            }
                        }
                    }
                }
                if (colNum == 0) {
                    sheet.setColumnWidth(colNum, (columnWidth - 2) * 256);
                } else {
                    //sheet.setColumnWidth(colNum, (columnWidth + 4) * 256);
                    if(columnWidth<255*256){
                        sheet.setColumnWidth(colNum, columnWidth < 3000 ? 3000 : columnWidth);
                    }else{
                        sheet.setColumnWidth(colNum,6000 );
                    }
                }
            }

            if (workbook != null) {
                try {
                   // headers = new HttpHeaders();
                   // headers.setContentDispositionFormData("attachment",new String(filename.getBytes("UTF-8")));
                   // headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                  //  baos = new ByteArrayOutputStream();
                    workbook.write(out);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
       // ResponseEntity<byte[]> e =  new ResponseEntity<byte[]>(baos.toByteArray(),headers, HttpStatus.CREATED);
        return ;
    }

    /*
     * 列头单元格样式
     */
    public HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {
        // 设置字体
        HSSFFont font = workbook.createFont();

        // 设置字体大小
        font.setFontHeightInPoints((short) 11);
        // 字体加粗
       // font.setBold(true);
        // 设置字体名字
        font.setFontName("Courier New");
        // 设置样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置低边框
        style.setBorderBottom(BorderStyle.THIN);
        // 设置低边框颜色
        style.setBottomBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        // 设置右边框
        style.setBorderRight(BorderStyle.THIN);
        // 设置顶边框
        style.setTopBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        // 设置顶边框颜色
        style.setTopBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        // 在样式中应用设置的字体
        style.setFont(font);
        // 设置自动换行
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐；
        //style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setAlignment(HorizontalAlignment.CENTER);
        //style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;

    }

    public HSSFCellStyle getStyle(HSSFWorkbook workbook) {
        // 设置字体
        HSSFFont font = workbook.createFont();
        // 设置字体大小
        font.setFontHeightInPoints((short) 10);
        // 字体加粗
        //        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setBold(true);


        // 设置字体名字
        font.setFontName("Courier New");
        // 设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置底边框;
        style.setBorderBottom(BorderStyle.THIN);
        // 设置底边框颜色;
        style.setBottomBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        // 设置左边框;
        style.setBorderLeft(BorderStyle.THIN);
        // 设置左边框颜色;
        style.setLeftBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        // 设置右边框;
        style.setBorderRight(BorderStyle.THIN);
        // 设置右边框颜色;
        style.setRightBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        // 设置顶边框;
        style.setBorderTop(BorderStyle.THIN);
        // 设置顶边框颜色;
        style.setTopBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        // 在样式用应用设置的字体;
        style.setFont(font);
        // 设置自动换行;
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

}
