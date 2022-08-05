package com.cloud.servicewechat.service;

import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.entity.BaseUserInfo;
import com.cloud.commonsmng.entity.appletEntity.Report;
import com.cloud.commonsmng.entity.appletEntity.User;
import com.cloud.commonsmng.factory.BaseService;
import com.cloud.servicewechat.common.ConstantUtil;
import com.cloud.servicewechat.common.PageUtil;
import com.cloud.servicewechat.common.PageVo;
import com.cloud.servicewechat.mapper.ReportMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.naming.spi.DirectoryManager;
import java.io.*;
import java.util.*;

@Service("report")
public class ReportService extends BaseService {
    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private S3Service s3Service;

    @Value("${deployment.scene.environment}")
    private String scene;

    //实例化日志对象，引用jar包org.slf4j.Logger
    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

    private static final BASE64Encoder encoder = new BASE64Encoder();

    @Override
    public Map<String, Object> exec(String method, String param) {
        Map<String, Object> obj = new HashMap<>();
        switch (method) {
            case ConstantUtil.GET_REPORT_INFO: //getReportInfo;
                obj = getReportInfo(param);
                break;
            case ConstantUtil.ADD_ARRICLEVIEWS: //addArticleviews;
                obj = addArticleviews(param);
                break;
            case ConstantUtil.GET_PATH_AND_VERIFY_PERMISSION: //getPathAndVerifyPermission
                obj = getPathAndVerifyPermission(param);
                break;
            case ConstantUtil.GET_USER_NOTES_NO_READ_NUMBER: //getUserNotesNoReadNumber
                obj = getUserNotesNoReadNumber(param);
                break;
            default:
                break;
        }
        return obj;
    }

    //根据用户id获取用户未读消息个数
    private Map<String, Object> getUserNotesNoReadNumber(String param) {
        Map<String, Object> result = new HashMap<>();
        String userId = super.getUserInfo().getId();
        Integer number = reportMapper.getUserNotesNoReadNumber(userId);
        Constants.getSuccMsg(result, number);
        return result;
    };

    //验证权限，获取报告访问路径
    private Map<String, Object> getPathAndVerifyPermission(String param) {
        Map<String, Object> result = new HashMap<>();
        Map map = new HashMap();
        BaseUserInfo userInfo = super.getUserInfo();
        if (this.usr != null) {
            String id = JSONObject.parseObject(param).getString("id");
            Report reportPathAndProperty = reportMapper.getReportPathAndProperty(id);
            if (reportPathAndProperty != null) {
                if (reportPathAndProperty.getReportKind() != null) {
                    String label = reportPathAndProperty.getReportKind().getLabel();
                    System.out.println(label + "--------->");
                    if (label != null && !"".equals(label) && !Arrays.asList(label.split(",")).contains("freeLimitTime")) {
                        String area = reportPathAndProperty.getReportKind().getArea();
                        String industry = reportPathAndProperty.getReportKind().getIndustry();
                        String economy = reportPathAndProperty.getReportKind().getEconomy();
                        String special = reportPathAndProperty.getReportKind().getSpecial();
                        Report report = null;
                        if (StringUtils.hasText(area) || StringUtils.hasText(industry) || StringUtils.hasText(economy) || StringUtils.hasText(special)) {
                            report = new Report();
                            if (StringUtils.hasText(area))
                                report.setAreaList(Arrays.asList(area.split(",")));
                            if (StringUtils.hasText(industry))
                                report.setIndustryList(Arrays.asList(industry.split(",")));
                            if (StringUtils.hasText(economy))
                                report.setEconomyList(Arrays.asList(economy.split(",")));
                            if (StringUtils.hasText(special))
                                report.setSpecialList(Arrays.asList(special.split(",")));
                        }

                        logger.info(String.format("报告的相关信息：[%s]", report));
                        if (report != null) {
                            List<String> idByReportProperty = reportMapper.getIdByReportProperty(report);
                            if (idByReportProperty != null) {
                                String memberId = null;
                                User user = (User) super.getUserInfo();
                                if (user.getId() != null && !"".equals(user.getId())) {
                                    memberId = reportMapper.getMemberIdByUserId(user.getId());
                                } else if (user.getMember() != null && user.getMember().getEnterpriseCode() != null && !"".equals(user.getMember().getEnterpriseCode())) {
                                    memberId = reportMapper.getMemberIdByEnterpriseCode(user.getMember().getEnterpriseCode());
                                } else {
                                    memberId = null;
                                }
                                if (memberId != null) {
                                    List<String> idByMemberId = reportMapper.getIdByMemberId(memberId);
                                    for (String dicid : idByReportProperty) {
                                        if (idByMemberId != null && !idByMemberId.contains(dicid)) {
                                            map.put("code", 204);
                                            map.put("msg", "亲，你没有订制该报告哦");
                                            Constants.getSuccMsg(result, map);
                                            return result;
                                        }
                                    }
                                } else {
                                    map.put("code", 205);
                                    map.put("msg", "亲，该报告属于付费报告，你不是付费用户哦");
                                    Constants.getSuccMsg(result, map);
                                    return result;
                                }
                            }
                        }
                    }
                }

                if ("extranet".equals(scene)) {//行外
                    map.put("data", reportPathAndProperty.getUrl());
                } else if ("intranet".equals(scene)) {//行内
                    String path = reportPathAndProperty.getUrl();
                    boolean existsObject = s3Service.existsObject(path);
                    if (!existsObject) {
                        map.put("code", 201);
                        map.put("msg", "文件不存在");
                        Constants.getSuccMsg(result, map);
                        return result;
                    }
                    String newPath = s3Service.GeneratePreSignedLinks(path);
                    map.put("data", newPath);
                } else {
                    map.put("code", 202);
                    map.put("msg", "未定义系统场景");
                    Constants.getSuccMsg(result, map);
                    return result;
                }

                reportMapper.addArticleviews(id);//添加阅读次数
                map.put("code", 200);
                map.put("msg", "打开成功");
                Constants.getSuccMsg(result, map);
            } else {
                map.put("code", 500);
                map.put("msg", "报告不存在");
                Constants.getErrMsg(result, map);
                return result;
            }
        } else {
            map.put("code", 203);
            map.put("msg", "亲，没有授权登录哦，请登录");
            Constants.getSuccMsg(result, map);
            return result;
        }
        return result;
    }

    //修改浏览量，每次点击都加一个
    private Map<String, Object> addArticleviews(String param) {
        Map<String, Object> result = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(param);
        String id = jsonObject.getString("id");
        Integer addResult = reportMapper.addArticleviews(id);
        Constants.getSuccMsg(result, addResult > 0);
        return result;
    }

    //查询出所有可以展示的报告列表信息
    private Map<String, Object> getReportInfo(String param) {
        Map<String, Object> result = new HashMap<>();
        logger.info(String.format("获取报告列表传递参数: [%s]", param));
        PageVo pageVo = JSONObject.parseObject(param, PageVo.class);
        PageUtil pageUtil = pageVo.getPage();//获取前端的页面分页信息
        try {
            PageVo<Report> reportPageVo = new PageVo<>();
            String userId = null;
            String enterpriseCode = null;
            String memberId = null;
            boolean userBoo = false;//记载是否存在登录信息
            List<String> idByMemberId = new ArrayList<>();

            if (super.getUserInfo() != null) {
                User user = (User) super.getUserInfo();
                if (user.getId() != null && !"".equals(user.getId())) {
                    userId = user.getId();
                    userBoo = true;
                    memberId = reportMapper.getMemberIdByUserId(user.getId());
                    idByMemberId = reportMapper.getIdByMemberId(memberId);
                } else if (user.getMember() != null && user.getMember().getEnterpriseCode() != null && !"".equals(user.getMember().getEnterpriseCode())) {
                    enterpriseCode = user.getMember().getEnterpriseCode();
                    userBoo = true;
                    memberId = reportMapper.getMemberIdByEnterpriseCode(user.getMember().getEnterpriseCode());
                    idByMemberId = reportMapper.getIdByMemberId(memberId);
                }
            } else {
                userBoo = false;
                memberId = null;
                idByMemberId = null;
            }
            logger.info(String.format("当前用户的会员编号：[%s]", memberId));
            logger.info(String.format("当前用户会员所拥有的报告属性的名称: [%s]", idByMemberId));
            logger.info(String.format("获取收藏列表的条件: userId<-> [%s], enterpriseCode<-> [%s]", userId, enterpriseCode));
            List<Report> reportList = reportMapper.getReportList(pageVo, userId, enterpriseCode);

            List<Map<String, String>> getName = reportMapper.getNameByKind();
            for (Report data : reportList) {
                if (userBoo && idByMemberId != null && idByMemberId.size() > 0) {
                    Report reportPathAndProperty = reportMapper.getReportPathAndProperty(data.getId());
                    logger.info("根据编号查询报告的所有信息：id<-> [%s], report<-> [%s]", data.getId(), reportPathAndProperty);
                    if (reportPathAndProperty != null) {
                        if (reportPathAndProperty.getReportKind() != null) {
                            String label = reportPathAndProperty.getReportKind().getLabel();
                            if (label != null && !"".equals(label) && !Arrays.asList(label.split(",")).contains("freeLimitTime")) {
                                String area = reportPathAndProperty.getReportKind().getArea();
                                String industry = reportPathAndProperty.getReportKind().getIndustry();
                                String economy = reportPathAndProperty.getReportKind().getEconomy();
                                String special = reportPathAndProperty.getReportKind().getSpecial();
                                logger.info("报告的属性：area【地址】---> [%s]", area);
                                logger.info("报告的属性：industry【行业】---> [%s]", industry);
                                logger.info("报告的属性：economy---> [%s]", economy);
                                logger.info("报告的属性：special---> [%s]", special);

                                Report report = null;
                                if (StringUtils.hasText(area) || StringUtils.hasText(industry) || StringUtils.hasText(economy) || StringUtils.hasText(special)) {
                                    report = new Report();
                                    if (StringUtils.hasText(area))
                                        report.setAreaList(Arrays.asList(area.split(",")));
                                    if (StringUtils.hasText(industry))
                                        report.setIndustryList(Arrays.asList(industry.split(",")));
                                    if (StringUtils.hasText(economy))
                                        report.setEconomyList(Arrays.asList(economy.split(",")));
                                    if (StringUtils.hasText(special))
                                        report.setSpecialList(Arrays.asList(special.split(",")));
                                }

                                logger.info(String.format("报告的相关信息：[%s]", report));
                                if (report != null) {
                                    List<String> idByReportProperty = reportMapper.getIdByReportProperty(report);
                                    logger.info(String.format("报告的相关属性的编号集合：[%s]", idByReportProperty));
                                    if (idByReportProperty != null) {
                                        for (String dicid : idByReportProperty) {
                                            if (!idByMemberId.contains(dicid)) {
                                                data.setVisitStatus("204");
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (data.getVisitStatus() == null || "".equals(data.getVisitStatus()))
                            data.setVisitStatus("200");
                    } else {
                        data.setVisitStatus("500");
                    }
                } else {
                    data.setVisitStatus("203");
                }

                logger.info(String.format("当前用户的可访问状态：[%s]", data.getVisitStatus()));
                for (Map<String, String> item : getName) {
                    if (StringUtils.hasText(data.getReportKind().getIndustry())) {
                        List<String> industryList = Arrays.asList(data.getReportKind().getIndustry().split(","));
                        if (industryList != null && industryList.size() > 0) {
                            for (String s : industryList) {
                                if (s.equals(item.get("code"))) {
                                    data.getReportKind().setIndustry(data.getReportKind().getIndustry().replace(item.get("code"), item.get("name")));
                                }
                            }
                        }
                    }
//                    if (StringUtils.hasText(data.getReportKind().getLabel())) {
//                        if (data.getReportKind().getLabel().equals(item.get("code"))) {
//                            data.getReportKind().setLabel(data.getReportKind().getLabel().replace(item.get("code"), item.get("picurl")));
//                        }
//                    }

                    if (StringUtils.hasText(data.getReportKind().getKindId())) {
                        List<String> KindList = Arrays.asList(data.getReportKind().getKindId().split(","));
                        if (KindList != null && KindList.size() > 0) {
                            for (String s : KindList) {
                                if (s.equals(item.get("code"))) {
                                    data.getReportKind().setKindId(data.getReportKind().getKindId().replace(item.get("code"), item.get("name")));
                                }
                            }
                        }
                    }
                    if (StringUtils.hasText(data.getReportKind().getEconomy())) {
                        List<String> economyList = Arrays.asList(data.getReportKind().getEconomy().split(","));
                        if (economyList != null && economyList.size() > 0) {
                            for (String s : economyList) {
                                if (s.equals(item.get("code"))) {
                                    data.getReportKind().setEconomy(data.getReportKind().getEconomy().replace(item.get("code"), item.get("name")));
                                }
                            }
                        }
                    }
                    if (StringUtils.hasText(data.getReportKind().getSpecial())) {
                        List<String> speciaList = Arrays.asList(data.getReportKind().getSpecial().split(","));
                        if (speciaList != null && speciaList.size() > 0) {
                            for (String s : speciaList) {
                                if (s.equals(item.get("code"))) {
                                    data.getReportKind().setSpecial(data.getReportKind().getSpecial().replace(item.get("code"), item.get("name")));
                                }
                            }
                        }
                    }
                    if (StringUtils.hasText(data.getReportKind().getArea())) {
                        List<String> areaList = Arrays.asList(data.getReportKind().getArea().split(","));
                        if (areaList != null && areaList.size() > 0) {
                            for (String s : areaList) {
                                if (s.equals(item.get("code"))) {
                                    data.getReportKind().setArea(data.getReportKind().getArea().replace(item.get("code"), item.get("name")));
                                }
                            }
                        }
                    }
                }
                if (StringUtils.hasText(data.getReportKind().getIndustry())) {
                    data.setIndustryList(Arrays.asList(data.getReportKind().getIndustry().split(",")));
                }

                if (StringUtils.hasText(data.getReportKind().getKindId())) {
                    data.setKindIdList(Arrays.asList(data.getReportKind().getKindId().split(",")));
                }

                if (StringUtils.hasText(data.getReportKind().getEconomy())) {
                    data.setEconomyList(Arrays.asList(data.getReportKind().getEconomy().split(",")));
                }

                if (StringUtils.hasText(data.getReportKind().getSpecial())) {
                    data.setSpecialList(Arrays.asList(data.getReportKind().getSpecial().split(",")));
                }

                if (StringUtils.hasText(data.getReportKind().getArea())) {
                    data.setAreaList(Arrays.asList(data.getReportKind().getArea().split(",")));
                }

            }


            //将返回的结果进行base64加密
//            for (Report report : reportList) {
//                if(report.getUrl()!= null && report.getUrl()!= ""){
//                    String url = report.getUrl();
//
//                    File file = new File(url);
//                    String str = PDFToBase64(file);
//                    str = str.replaceAll("\r\n", "");
//                    str = str.replaceAll("\\+", "%2B");
//                    report.setUrl(str);
//                }
//
//            }

            reportPageVo.setDataList(reportList);
            Constants.getSuccMsg(result, reportPageVo);
            logger.info(String.format("当前获取报告列表的结果: [%s]", result));
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Description: 将pdf文件转换为Base64编码
     *
     * @param要转的的pdf文件
     * @Author fuyuwei
     * Create Date: 2015年8月3日 下午9:52:30
     */
    public String PDFToBase64(File file) {
        BASE64Encoder encoder = new BASE64Encoder();
        FileInputStream fin = null;
        BufferedInputStream bin = null;
        ByteArrayOutputStream baos = null;
        BufferedOutputStream bout = null;
        try {
            fin = new FileInputStream(file);
            bin = new BufferedInputStream(fin);
            baos = new ByteArrayOutputStream();
            bout = new BufferedOutputStream(baos);
            byte[] buffer = new byte[1024];
            int len = bin.read(buffer);
            while (len != -1) {
                bout.write(buffer, 0, len);
                len = bin.read(buffer);
            }
            //刷新此输出流并强制写出所有缓冲的输出字节
            bout.flush();
            byte[] bytes = baos.toByteArray();
            return encoder.encodeBuffer(bytes).trim();

        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            try {
                fin.close();
                bin.close();
                bout.close();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
        return null;
    }

    /**
     * 图片转base64字符串
     *
     * @param imgFile 图片路径
     * @return
     */
    public static String imageToBase64Str(String imgFile) {
        InputStream inputStream = null;
        byte[] data = null;
        try {
            inputStream = new FileInputStream(imgFile);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 加密
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }
}
