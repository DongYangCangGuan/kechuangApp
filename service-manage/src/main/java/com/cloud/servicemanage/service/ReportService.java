//package com.cloud.servicemanage.service;
//
//import ch.ethz.ssh2.ChannelCondition;
//import ch.ethz.ssh2.Connection;
//import ch.ethz.ssh2.Session;
//import com.alibaba.fastjson.JSONObject;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.model.S3Object;
//import com.amazonaws.services.s3.model.S3ObjectInputStream;
//import com.cloud.commonsmng.constants.Constants;
//import com.cloud.commonsmng.entity.appletEntity.Report;
//import com.cloud.commonsmng.entity.appletEntity.ReportKind;
//import com.cloud.commonsmng.factory.BaseService;
//import com.cloud.servicemanage.common.ConstantUtil;
//import com.cloud.servicemanage.common.FileType;
//import com.cloud.servicemanage.common.PageUtil;
//import com.cloud.servicemanage.common.PageVo;
//import com.cloud.servicemanage.mapper.DictionaryMapper;
//import com.cloud.servicemanage.mapper.ReportMapper;
//import jdk.nashorn.internal.ir.ForNode;
//import org.apache.commons.io.IOUtils;
//import org.apache.commons.lang.StringUtils;
//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.pdmodel.PDPage;
//import org.apache.pdfbox.printing.PDFPageable;
//import org.apache.pdfbox.rendering.PDFRenderer;
//import org.apache.pdfbox.text.PDFTextStripper;
//import org.icepdf.core.exceptions.PDFException;
//import org.icepdf.core.exceptions.PDFSecurityException;
//import org.icepdf.core.pobjects.Document;
//import org.icepdf.core.pobjects.Page;
//import org.icepdf.core.util.GraphicsRenderingHints;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import sun.misc.BASE64Encoder;
//
//import javax.imageio.IIOImage;
//import javax.imageio.ImageIO;
//import javax.imageio.ImageWriter;
//import javax.imageio.stream.ImageInputStream;
//import javax.imageio.stream.ImageOutputStream;
//import java.awt.image.BufferedImage;
//import java.io.*;
//import java.net.*;
//import java.nio.charset.StandardCharsets;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
///**
// * ???????????????????????????
// * author: tjs
// */
//@Service("Report")
//public class ReportService extends BaseService {
//
//    //??????????????????????????????jar???org.slf4j.Logger
//    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);
//
//    @Value("${file.upload.path}")
//    private String fileUploadpath;
//
//    @Value("{current.storage.server.path}")
//    private String serverPath;
//
//    @Value("${deployment.scene.environment}")
//    private String scene;
//
//    @Autowired
//    private S3Service s3Service;
//
//    @Autowired
//    private ReportMapper reportMapper;
//
//    @Autowired
//    private DictionaryMapper dictionaryMapper;
//
//    @Override
//    public Map<String, Object> exec(String method, String param) throws Exception {
//        Map<String, Object> obj = new HashMap<>();
//        switch (method) {
//            case ConstantUtil.GET_REPORT_PAGE_VO: //getReportPageVo
//                obj = getReportPageVo(param);
//                break;
//            case ConstantUtil.GET_REPORT_BY_ID: //getReportById
//                obj = getReportById(param);
//                break;
//            case ConstantUtil.GET_REPORT_INFO_BY_ID: //getReportInfoById
//                obj = getReportInfoById(param);
//                break;
//            case ConstantUtil.INSERT_REPORT: //insertReport
//                obj = insertReport(param);
//                break;
//            case ConstantUtil.UPDATE_REPORT: //updateReport
//                obj = updateReport(param);
//                break;
//            case ConstantUtil.DELETE_REPORT: //deleteReport
//                obj = deleteReport(param);
//                break;
//            case ConstantUtil.CHECK_REPORT_CODE_BASEMSG: //checkReportCode
//                obj = checkReportCode(param);
//                break;
//
//            default:
//                break;
//        }
//        return obj;
//    }
//
//    /**
//     * ????????????????????????????????????
//     *
//     * @param param
//     * @return
//     */
//    private Map<String, Object> checkReportCode(String param) {
//        Map<String, Object> result = new HashMap<>();
//        JSONObject jsonObject = JSONObject.parseObject(param);
//        String id = jsonObject.getString("id");
//        String code = jsonObject.getString("code");
//        Integer checkNum = reportMapper.checkReportCode((id == null || id.isEmpty()) ? "-99999" : id, code);
//        if (checkNum > 0)
//            Constants.getSuccMsg(result, Constants.RESULTCODE_REPEAT);
//        else
//            Constants.getSuccMsg(result, Constants.RESULT_NO_DATA);
//
//        Constants.getSuccMsg(result, checkNum > 0);
//        return result;
//    }
//
//    /**
//     * ??????????????????
//     *
//     * @param param
//     * @return
//     */
//    private Map<String, Object> deleteReport(String param) {
//        Map<String, Object> result = new HashMap<>();
//        JSONObject jsonObject = JSONObject.parseObject(param);
//        String id = jsonObject.getString("id");
//        Integer update = reportMapper.deleteReport(id);
//        Constants.getSuccMsg(result, update > 0);
//        return result;
//    }
//
//    /**
//     * ??????????????????
//     *
//     * @param param
//     * @return
//     */
//    private Map<String, Object> updateReport(String param) {
//
//        Map<String, Object> result = new HashMap<>();
//        Map<String, Object> map = new HashMap<>();
//        Report report = JSONObject.parseObject(param, Report.class);
//        report.setModifierId(super.getUserInfo().getId());
//        try {
//            if ("extranet".equals(scene)) {//??????
//                String filePath = report.getUrl();
//                if (!new File(filePath).exists()) {
//                    map.put("code", 201);
//                    map.put("msg", "???????????????");
//                    Constants.getSuccMsg(result, map);
//                    return result;
//                }
//                report.setPic(generateBookImage(filePath));
////                if (report.getReportKind().getKindId().indexOf("voiceReport") != -1) {
////                    String path = readPdf(filePath);
////                    String txtMsg = readTxt(path);
////                    //report.setVoice(txtToVoice(txtMsg, path));
////                    String voicePath = path.split(".") + ".wav";
////
////                    //RemoteInvokeShell("sh /espeak/txtToWav.sh " + txtMsg.replace(" ", "").replace("(", "").replace(")", "") + " test2.wav");
////                }
//            } else if ("intranet".equals(scene)) { //??????
//                String filePath = report.getUrl();
//                boolean existsObject = s3Service.existsObject(filePath);
//                if (!existsObject) {
//                    map.put("code", 201);
//                    map.put("msg", "???????????????");
//                    Constants.getSuccMsg(result, map);
//                    return result;
//                }
//
//                report.setPic(generateBookImage2(filePath));
////                if (report.getReportKind().getKindId().indexOf("voiceReport") != -1) {
////                    String path = readPdf2(filePath);
////                    String txtMsg = readTxt2(path);
////                    report.setVoice(txtToVoice(txtMsg, path));
////                    String voicePath = path.split(".") + ".wav";
////
////                    //RemoteInvokeShell("sh /espeak/txtToWav.sh " + txtMsg.replace(" ", "").replace("(", "").replace(")", "") + " test2.wav");
////                }
//            } else {
//                map.put("code", 203);
//                map.put("msg", "?????????????????????");
//                Constants.getSuccMsg(result, map);
//                return result;
//            }
//
//            int update = reportMapper.updateReport(report);
//            if (update > 0) {
//                map.put("code", 200);
//                map.put("msg", "succ");
//                Constants.getSuccMsg(result, map);
//            } else {
//                map.put("code", 204);
//                map.put("msg", "??????????????????!");
//                Constants.getSuccMsg(result, map);
//            }
//            return result;
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//            e.printStackTrace();
//            map.put("code", "203");
//            map.put("msg", e.getMessage());
//            Constants.getSuccMsg(result, map);
//        }
//        return result;
//    }
//
//    /**
//     * ??????????????????
//     *
//     * @param param
//     * @return
//     */
//    private Map<String, Object> insertReport(String param) {
//        Map<String, Object> result = new HashMap<>();
//        Map<String, Object> map = new HashMap<>();
//        try {
//            Report report = JSONObject.parseObject(param, Report.class);
//            super.insertBaseInfo(report);
//
//            if ("extranet".equals(scene)) {//??????
//                String filePath = report.getUrl();
//                File file1 = new File(filePath);
//                if (!file1.exists()) {
//                    result.put("code", 201);
//                    result.put("msg", "???????????????");
//                    return result;
//                }
//                report.setPic(generateBookImage(filePath));
////                if (report.getReportKind().getKindId().indexOf("voiceReport") != -1) {
////                    String path = readPdf(filePath);
////                    String txtMsg = readTxt(path);
////                    //report.setVoice(txtToVoice(txtMsg, path));
////                    String voicePath = path.split(".") + ".wav";
////
////                    //RemoteInvokeShell("sh /espeak/txtToWav.sh " + txtMsg.replace(" ", "").replace("(", "").replace(")", "") + " test2.wav");
////                }
//            } else if ("intranet".equals(scene)) { //??????
//                String filePath = report.getUrl();
//                boolean existsObject = s3Service.existsObject(filePath);
//                if (!existsObject) {
//                    map.put("code", 201);
//                    map.put("msg", "???????????????");
//                    Constants.getSuccMsg(result, map);
//                    return result;
//                }
//
//                report.setPic(generateBookImage2(filePath));
////                if (report.getReportKind().getKindId().indexOf("voiceReport") != -1) {
////                    String path = readPdf2(filePath);
////                    String txtMsg = readTxt2(path);
////                    report.setVoice(txtToVoice(txtMsg, path));
////                    String voicePath = path.split(".") + ".wav";
////
////                    //RemoteInvokeShell("sh /espeak/txtToWav.sh " + txtMsg.replace(" ", "").replace("(", "").replace(")", "") + " test2.wav");
////                }
//            } else {
//                map.put("code", 203);
//                map.put("msg", "?????????????????????");
//                Constants.getSuccMsg(result, map);
//                return result;
//            }
//
//            //???reportKind????????????
//            ReportKind reportKind = report.getReportKind();
//            super.insertBaseInfo(reportKind);
//            reportKind.setReportId(report.getId());
//            report.setReportKind(reportKind);
//            int insert = reportMapper.insertReport(report);
//            if (insert > 0) {
//                map.put("code", 200);
//                map.put("msg", "succ");
//                Constants.getSuccMsg(result, map);
//            } else {
//                map.put("code", 204);
//                map.put("msg", "??????????????????!");
//                Constants.getSuccMsg(result, map);
//            }
//            return result;
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//            e.printStackTrace();
//            map.put("code", "203");
//            map.put("msg", e.getMessage());
//            Constants.getSuccMsg(result, map);
//        }
//        return result;
//    }
//
//    /**
//     * ??????code????????????????????????
//     *
//     * @param param
//     * @return
//     */
//    private Map<String, Object> getReportById(String param) {
//        Map<String, Object> result = new HashMap<>();
//        JSONObject jsonObject = JSONObject.parseObject(param);
//        String code = jsonObject.getString("code");
//        Report data = reportMapper.getReportById(code);
//        Constants.getSuccMsg(result, data);
//        return result;
//    }
//
//    /**
//     * ??????id????????????????????????
//     *
//     * @param param
//     * @return
//     */
//    private Map<String, Object> getReportInfoById(String param) {
//        Map<String, Object> result = new HashMap<>();
//        JSONObject jsonObject = JSONObject.parseObject(param);
//        String id = jsonObject.getString("id");
//        Report data = reportMapper.getReportInfoById(id);
//        if (data != null && data.getReportKind() != null) {
//            if (data.getReportKind().getArea() != null && !"".equals(data.getReportKind().getArea()))
//                data.setAreaList(Arrays.asList(data.getReportKind().getArea().split(",")));
//
//            if (data.getReportKind().getIndustry() != null && !"".equals(data.getReportKind().getIndustry()))
//                data.setIndustryList(Arrays.asList(data.getReportKind().getIndustry().split(",")));
//
//            if (data.getReportKind().getTheme() != null && !"".equals(data.getReportKind().getTheme()))
//                data.setThemeList(Arrays.asList(data.getReportKind().getTheme().split(",")));
//
//            if (data.getReportKind().getKindId() != null && !"".equals(data.getReportKind().getKindId()))
//                data.setKindIdList(Arrays.asList(data.getReportKind().getKindId().split(",")));
//
//            if (data.getReportKind().getEconomy() != null && !"".equals(data.getReportKind().getEconomy()))
//                data.setEconomyList(Arrays.asList(data.getReportKind().getEconomy().split(",")));
//
//            if (data.getReportKind().getSpecial() != null && !"".equals(data.getReportKind().getSpecial()))
//                data.setSpecialList(Arrays.asList(data.getReportKind().getSpecial().split(",")));
//        }
//
//        Constants.getSuccMsg(result, data);
//        return result;
//    }
//
//    /**
//     * ??????????????????
//     *
//     * @return
//     */
//    private Map<String, Object> getReportPageVo(String param) {
//        Map<String, Object> result = new HashMap<>();
//        PageVo pageVo = JSONObject.parseObject(param, PageVo.class);
//        PageUtil pageUtil = pageVo.getPage();//?????????????????????????????????
//        try {
//            int totalNum = reportMapper.getPageTotal(pageVo);
//            List<Report> reportList = reportMapper.getReportPageVo(pageVo);
//            PageVo<Report> reportPageVo = new PageVo<>();
//            reportPageVo.setPage(new PageUtil(pageUtil.getPageIndex(), pageUtil.getPageSize(), totalNum));
//            reportPageVo.setDataList(reportList);
//            Constants.getSuccMsg(result, reportPageVo);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    /**
//     * ???????????????????????????????????????????????????
//     *
//     * @param path
//     * @return
//     * @throws PDFSecurityException
//     * @throws PDFException
//     * @throws IOException
//     */
//    public String generateBookImage(String path) throws Exception {
//        File file = new File(path);
//        // ??????????????????PDF Document
//        PDDocument document = PDDocument.load(file);
//        PDFRenderer renderer = new PDFRenderer(document);
//
//        BufferedImage image = renderer.renderImageWithDPI(0, 100);//?????????????????????
//        int imageWidth = image.getWidth() > 0 ? image.getWidth() : 100;
//        int imageHeight = image.getHeight() > 0 ? image.getHeight() : 100;
//        BufferedImage bufferedImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_3BYTE_BGR);
//        bufferedImage.getGraphics().drawImage(image, 0, 0, null);
//
//        String outputFile = getFilePath(file.getName(), FileType.JPEG);
//
//        FileOutputStream out = new FileOutputStream(new File(outputFile));
//        ImageIO.write(bufferedImage, FileType.JPEG.substring(1), out);
//        out.flush();
//        out.close();
//        return outputFile;
//    }
//
//    /**
//     * ??????????????????????????????????????????????????????
//     *
//     * @param path
//     * @return
//     * @throws PDFSecurityException
//     * @throws PDFException
//     * @throws IOException
//     */
//    public String generateBookImage2(String path) throws Exception {
//        // ??????????????????PDF Document
//        PDDocument document = null;
//        HttpURLConnection conn = null;
//        InputStream is = null;
//        ByteArrayOutputStream out = null;
//        InputStream istwo = null;
//        ByteArrayOutputStream outputStream = null;
//        File tempFile = File.createTempFile(UUID.randomUUID().toString(), ".pdf");
//        try {
//            //????????????????????????InputStream
//            conn = getUrlByInputStream(path);
//            if (conn != null) {
//                is = conn.getInputStream();
//                outputStream = new ByteArrayOutputStream();
//                FileOutputStream fos = new FileOutputStream(tempFile);
//                byte[] bytes = new byte[1024];
//                int n = 0;
//                while ((n = is.read(bytes)) != -1) {
//                    fos.write(bytes, 0, n);
//                }
//
//                // ??????????????????PDF Document
//                document = PDDocument.load(tempFile);
//                PDFRenderer renderer = new PDFRenderer(document);
//                logger.info(String.format("PDFRenderer: [%s]", renderer));
//
//                BufferedImage image = renderer.renderImageWithDPI(0, 100);//?????????????????????
//
//                int imageWidth = image.getWidth();
//                int imageHeight = image.getHeight();
//                logger.info(String.format("imageWidth: [%s]", imageWidth));
//                logger.info(String.format("imageHeight: [%s]", imageHeight));
//
//                BufferedImage bufferedImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_3BYTE_BGR);
//                bufferedImage.getGraphics().drawImage(image, 0, 0, null);
//
//                out = new ByteArrayOutputStream();//??????????????????????????????
//                String outputFile = getFilePath(path, FileType.JPEG);
//                ImageIO.write(bufferedImage, FileType.JPEG.substring(1), out);
//                istwo = new ByteArrayInputStream(out.toByteArray());//FileType.PNG ???????????????????????????
//                s3Service.uploadObject(istwo, outputFile);
//
//                return outputFile;
//            }
//        } finally {
//            if (is != null) is.close();
//            if (document != null) document.close();
//            if (out != null) out.close();
//            if (istwo != null) istwo.close();
//            if (outputStream != null) outputStream.close();
//            if (conn != null) conn.disconnect();
//            tempFile.delete();
//        }
//
//        return null;
//    }
//
//    /**
//     * ????????????????????????pdf????????????????????????????????????????????????
//     *
//     * @param path
//     * @return
//     * @throws IOException
//     */
//    public String readPdf(String path) throws Exception {
//        File file1 = new File(path);
//
//        //????????????pdf??????
//        PDDocument document = PDDocument.load(file1);
//        // ?????????PDF??????????????????????????????txt??????
//        String textFile = getFilePath(file1.getName(), FileType.TXT);
//        // ?????????????????????????????????textFile
//        Writer output = new OutputStreamWriter(new FileOutputStream(textFile), StandardCharsets.UTF_8);//????????????UTF_8
//        // PDFTextStripper???????????????
//        PDFTextStripper stripper = new PDFTextStripper();
//        // ??????????????????
//        stripper.setSortByPosition(false);
//        // ???????????????
//        stripper.setStartPage(1);
//        // ???????????????
//        int endPage = document.getNumberOfPages();
//        stripper.setEndPage(endPage);
//        // ??????PDFTextStripper???writeText?????????????????????
//        stripper.writeText(document, output);
//
//        if (document != null) document.close();// ??????PDF Document
//        if (output != null) output.close();// ???????????????
//        return textFile;
//    }
//
//    /**
//     * ????????????????????????pdf????????????????????????????????????????????????
//     *
//     * @param path
//     * @return
//     * @throws IOException
//     */
//    public String readPdf2(String path) throws Exception {
//        HttpURLConnection conn = null;
//        InputStream is = null;
//        PDDocument document = null;
//        InputStream istwo = null;
//        try {
//            //????????????????????????InputStream
//            conn = getUrlByInputStream(path);
//            if (conn != null) {
//                is = conn.getInputStream();
//                // ??????????????????PDF Document
//                document = PDDocument.load(is);
//
//                // ????????????????????????
//                String textFile = getFilePath(path, FileType.TXT);
//
//                // PDFTextStripper???????????????
//                PDFTextStripper stripper = new PDFTextStripper();
//                // ??????????????????
//                stripper.setSortByPosition(false);
//                // ???????????????
//                stripper.setStartPage(1);
//                // ???????????????
//                int endPage = document.getNumberOfPages();
//                stripper.setEndPage(endPage);
//                // ??????PDFTextStripper???getText?????????????????????
//                String text = stripper.getText(document);
//                logger.info("text????????????: {}", text);
//                istwo = IOUtils.toInputStream(text, StandardCharsets.UTF_8);//????????????UTF_8
//                s3Service.uploadObject(istwo, textFile);
//
//                return textFile;
//            }
//        } finally {
//            if (is != null) is.close();
//            if (document != null) document.close();// ??????PDF Document
//            if (istwo != null) istwo.close();//?????????
//            if (conn != null) conn.disconnect();
//        }
//        return null;
//    }
//
//    /**
//     * ?????????????????????txt???????????????????????????
//     *
//     * @param path
//     * @return
//     * @throws IOException
//     */
//    public String readTxt(String path) throws IOException { // ???????????????????????????????????????catch?????????????????????????????????throw
//        /* ??????TXT?????? */
//        File filename = new File(path); // ????????????????????????input???txt??????
//        // ????????????????????????????????????????????????????????????????????????
//        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
//        StringBuffer sb = new StringBuffer();
//        String line = null;
//        while ((line = br.readLine()) != null) {
//            sb.append(line);
//        }
//
//        if (br != null) br.close();
//        return sb.toString();
//    }
//
//    /**
//     * ????????????????????????txt???????????????????????????
//     *
//     * @param path
//     * @return
//     * @throws IOException
//     */
//    public String readTxt2(String path) throws Exception {
//        // ???????????????????????????????????????catch?????????????????????????????????throw
//        //????????????????????????InputStream
//        HttpURLConnection conn = null;
//        InputStream is = null;
//        BufferedReader br = null;
//        try {
//            conn = getUrlByInputStream(path);
//            if (conn != null) {
//                is = conn.getInputStream();
//                /* ??????TXT?????? */
//                br = new BufferedReader(new InputStreamReader(is)); // ????????????????????????????????????????????????????????????????????????
//                StringBuffer sb = new StringBuffer();
//                String line = null;
//                while ((line = br.readLine()) != null) {
//                    sb.append(line);
//                }
//                return sb.toString();
//            }
//        } finally {
//            if (is != null) is.close(); //?????????
//            if (br != null) br.close();
//            if (conn != null) conn.disconnect();
//        }
//        return null;
//    }
//
//    /**
//     * ??????txt????????????????????????
//     *
//     * @param txt
//     * @param path
//     * @return
//     */
//    public static String txtToVoice(String txt, String path) {
//        path = path.split("\\.")[0] + ".wav";
//        //??????????????????sh????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
//
//        String[] paths = new String[]{"sh", "/espeak/txtToWav.sh", txt, path};
//        try {
//            Runtime runtime = Runtime.getRuntime();
//            Process pro = runtime.exec(paths);
//            int status = pro.waitFor();
//            if (status != 0) {
//                System.out.println("Failed to call shell's command");
//            }
//
//            BufferedReader br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
//            StringBuffer strbr = new StringBuffer();
//            String line;
//            while ((line = br.readLine()) != null) {
//                strbr.append(line).append("\n");
//            }
//            String result = strbr.toString();
//            return path;
//        } catch (IOException ec) {
//            ec.printStackTrace();
//        } catch (InterruptedException ex) {
//            ex.printStackTrace();
//
//        }
//        return null;
//    }
//
//    /**
//     * ???????????????????????????shell??????
//     *
//     * @param cmds shell??????
//     */
//    public static void RemoteInvokeShell(String cmds) {
//
//        Connection conn = null;
//        try {
//            conn = new Connection("218.83.152.167", 10022);
//            conn.connect();
//            if (conn.authenticateWithPassword("root", "tz61276677")) {
//                // Open a new {@link Session} on this connection
//                Session session = conn.openSession();
//                // Execute a command on the remote machine.
//                session.execCommand(cmds);
////                session.execCommand("cd /espeak; ./txtToWav.sh ??????????????? test2.wav");
////                session.execCommand("sh /root/espeak/txtToWav.sh ??????????????? test3.wav ");
////                session.execCommand("/espeak/txtToWav.sh ??????????????? test4.wav ");
//                BufferedReader br = new BufferedReader(new InputStreamReader(session.getStdout()));
//                BufferedReader brErr = new BufferedReader(new InputStreamReader(session.getStderr()));
//
//                String line;
//                while ((line = br.readLine()) != null) {
//                    // .info("br={}", line);
//                }
//                while ((line = brErr.readLine()) != null) {
//                    //logger.info("brErr={}", line);
//                }
//                if (null != br) {
//                    br.close();
//                }
//                if (null != brErr) {
//                    brErr.close();
//                }
//                session.waitForCondition(ChannelCondition.EXIT_STATUS, 0);
//                int ret = session.getExitStatus();
//                //logger.info("getExitStatus:"+ ret);
//            } else {
//                //logger.info("????????????????????????" + ip);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (conn != null) {
//                conn.close();
//            }
//        }
//    }
//
//    /**
//     * ??????filePath,??????????????????????????????
//     *
//     * @param fileName
//     * @param type
//     * @return
//     * @throws Exception
//     */
//    private String getFilePath(String fileName, String type) throws Exception {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        String filePathPrefix = fileUploadpath + sdf.format(new Date()) + "/";
//        String filePath = URLDecoder.decode(filePathPrefix, "UTF-8");
//        if (fileName == null || "".equals(fileName)) {
//            String randomPath = filePath + UUID.randomUUID() + type;
//            return randomPath;
//        }
//
//        if (fileName.contains("/")) {
//            fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
//        } else if (fileName.contains("\\")) {
//            fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
//        }
//
//        if (fileName.lastIndexOf(".") != -1) {
//            fileName = fileName.substring(0, fileName.lastIndexOf("."));
//        }
//
//        String fileNamePath = filePath + fileName + type;
//        return fileNamePath;
//    }
//
//    /**
//     * ??????filePath,?????????????????? ????????????UUID?????????
//     *
//     * @param type
//     * @return
//     * @throws Exception
//     */
//    private String getFilePath(String type) throws Exception {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        String filePathPrefix = fileUploadpath + sdf.format(new Date()) + "/";
//        String filePath = URLDecoder.decode(filePathPrefix, "UTF-8");
//        String outputFile = filePath + UUID.randomUUID() + type;
//        return outputFile;
//    }
//
//    /**
//     * ??????url???InputStream
//     *
//     * @param filePath
//     * @return
//     * @throws Exception
//     */
//    private HttpURLConnection getUrlByInputStream(String filePath) throws Exception {
//        String preUrl = s3Service.GeneratePreSignedLinks(filePath);
//        if (preUrl != null && preUrl.contains("?")) {
//            URL url = new URL(preUrl);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            //conn.setDoOutput(true); //???????????????connection????????????????????????post????????????????????????http??????????????????????????????true,??????????????????false
//            //conn.setDoInput(true); //???????????????connection???????????????????????????true
//            conn.setRequestMethod("GET"); //?????????????????????GET,??????GET??????
//            //conn.setUseCaches(false); //post??????????????????????????????false
//            conn.setConnectTimeout(5000); //???????????????????????????
//            conn.setReadTimeout(5000); //????????????????????????????????????
//            //conn.setInstanceFollowRedirects(true); //?????????HttpURLConnection?????????????????????????????????
//            //conn.setRequestProperty("connection", "Keep-Alive"); //????????????
//            //conn.setRequestProperty("charset", "utf-8");
//
//            conn.connect(); //??????TCP??????,getOutputStream??????????????????connect,????????????????????????
//            int responseCode = conn.getResponseCode();
//            if (responseCode == HttpURLConnection.HTTP_OK)
//                return conn;
//        }
//        return null;
//    }
//}
