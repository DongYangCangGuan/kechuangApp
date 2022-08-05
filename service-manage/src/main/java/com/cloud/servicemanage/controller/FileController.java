package com.cloud.servicemanage.controller;

import cn.hutool.core.io.FileTypeUtil;
import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.activiti.ActivitiUploadBPMNUtil;
import com.cloud.commonsmng.activiti.model.ResponseModel;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.security.AESUtil;
import com.cloud.commonsmng.security.JwtUtil;
import com.cloud.commonsmng.userConfig.GetUserInfo;
import com.cloud.servicemanage.common.FileType;
import com.cloud.servicemanage.common.DataProcessing;
import com.cloud.servicemanage.mapper.MemberMapper;
import com.cloud.servicemanage.service.FileUploadService;

import com.cloud.servicemanage.service.S3Service;
import com.cloud.servicemanage.utils.ExcelFormatUtil;
import com.google.common.base.Joiner;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/manage/api/file")
//@RequestMapping("/api/file")
public class FileController {
    @Value("${file.upload.path}")
    private String fileUploadpath;

    @Value("${product.path}")
    private String productpath;

    @Value("${file.addressable.path}")
    private String fileAddressablepath;

    @Value("${picture.path}")
    private String picturepath;

    @Value("${deployment.scene.environment}")
    private String scene;

    @Autowired
    private S3Service s3Service;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    GetUserInfo getUserInfo;

    @Autowired
    AESUtil aesUtil;
    @Autowired
    private FileUploadService fileService;

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    @RequestMapping(value = {"/fileupload"}, method = {RequestMethod.POST})
    @ResponseBody
    public String getInfo(@RequestBody MultipartFile file) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (file.isEmpty()) {
            result.put("code", 501);
            result.put("message", "文件名为空");
            String resultEncrypt = aesUtil.aesEncryptToBytes(result, AESUtil.MANAGE_KEY);
            return resultEncrypt;
        }
        try {
            // 获取文件名
            String fileName = file.getOriginalFilename();
            // 获取文件的后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            // 设置文件存储路径
//            String filePath =new File(this.getClass().getResource(File.separator).getPath())
//                    .getParentFile().getParentFile().getParentFile().getParentFile().getAbsoluteFile() +File.separator+"file"+File.separator;
            //String filePath = uploadFilesUrl+"/FileManagerFile/";
            String filePath = fileUploadpath;
            File picFile = new File(filePath);
            if (!picFile.exists()) {
                picFile.mkdirs();
            }
            filePath = java.net.URLDecoder.decode(filePath, "UTF-8");
            UUID uuid = UUID.randomUUID();
            String path = filePath + uuid + suffixName;
            File dest = new File(path);
            // 检测是否存在目录
            dest.deleteOnExit();
            file.transferTo(dest);// 文件写入
            result.put("code", 200);
            result.put("message", "succ");
            result.put("data", path);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e);
        }
        String resultEncrypt = aesUtil.aesEncryptToBytes(result, AESUtil.MANAGE_KEY);
        return resultEncrypt;
    }

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    @RequestMapping(value = {"/imgupload"}, method = {RequestMethod.POST})
    @ResponseBody
    public String imgupload(@RequestBody MultipartFile file) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (file.isEmpty()) {
            result.put("code", 501);
            result.put("message", "文件名为空");
            String resultEncrypt = aesUtil.aesEncryptToBytes(result, AESUtil.MANAGE_KEY);
            return resultEncrypt;
        }
        try {
            // 获取文件名
            String fileName = file.getOriginalFilename();
            // 获取文件的后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            // 设置文件存储路径
//            String filePath =new File(this.getClass().getResource(File.separator).getPath())
//                    .getParentFile().getParentFile().getParentFile().getParentFile().getAbsoluteFile() +File.separator+"file"+File.separator;
            //String filePath = uploadFilesUrl+"/FileManagerFile/";
            String filePath = picturepath;
            File picFile = new File(filePath);
            if (!picFile.exists()) {
                picFile.mkdirs();
            }
            filePath = java.net.URLDecoder.decode(filePath, "UTF-8");
            UUID uuid = UUID.randomUUID();
            String picName = uuid + suffixName;
            String path = filePath + picName;
            File dest = new File(path);
            // 检测是否存在目录
            dest.deleteOnExit();
            file.transferTo(dest);// 文件写入
            String uploadpath = fileService.getAddress("pictureaddress");
            String u = uploadpath + picName;
            result.put("code", 200);
            result.put("message", "succ");
            result.put("data", u);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e);
        }
        String resultEncrypt = aesUtil.aesEncryptToBytes(result, AESUtil.MANAGE_KEY);
        return resultEncrypt;
    }

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    @RequestMapping(value = {"/productupload"}, method = {RequestMethod.POST})
    @ResponseBody
    public String productupload(@RequestBody MultipartFile file) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (file.isEmpty()) {
            result.put("code", 501);
            result.put("message", "文件名为空");
            String resultEncrypt = aesUtil.aesEncryptToBytes(result, AESUtil.MANAGE_KEY);
            return resultEncrypt;
        }
        try {
            // 获取文件名
            String fileName = file.getOriginalFilename();
            // 获取文件的后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            // 设置文件存储路径
//            String filePath =new File(this.getClass().getResource(File.separator).getPath())
//                    .getParentFile().getParentFile().getParentFile().getParentFile().getAbsoluteFile() +File.separator+"file"+File.separator;
            //String filePath = uploadFilesUrl+"/FileManagerFile/";
            String filePath = productpath;
            File picFile = new File(filePath);
            if (!picFile.exists()) {
                picFile.mkdirs();
            }
            filePath = java.net.URLDecoder.decode(filePath, "UTF-8");
            UUID uuid = UUID.randomUUID();
            String picName = uuid + suffixName;
            String path = filePath + picName;
            File dest = new File(path);
            // 检测是否存在目录
            dest.deleteOnExit();
            file.transferTo(dest);// 文件写入
            String uploadpath = fileService.getAddress("productaddress");
            String u = uploadpath + picName;
            result.put("code", 200);
            result.put("message", "succ");
            result.put("data", u);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e);
        }
        String resultEncrypt = aesUtil.aesEncryptToBytes(result, AESUtil.MANAGE_KEY);
        return resultEncrypt;
    }

    /**
     * 图片文件上传
     *
     * @param file
     * @return
     */
    @RequestMapping(value = {"/imgFileUpload"}, method = {RequestMethod.POST})
    @ResponseBody
    public String imgFileUpload(@RequestBody MultipartFile file) {
        List<String> types = Arrays.asList(FileType.JPEG, FileType.PNG, FileType.GIF);
        long size = 2 * 1024 * 1024;//设置文件最大上传大小
        Map<String, Object> map = fileUploadPortionCode(file, types, size);
        String resultEncrypt = aesUtil.aesEncryptToBytes(map, AESUtil.MANAGE_KEY);
        return resultEncrypt;
    }

    /**
     * pdf 文件上传
     *
     * @param file
     * @return
     */
    @RequestMapping(value = {"/pdfFileUpload"}, method = {RequestMethod.POST})
    @ResponseBody
    public String pdfFileUpload(@RequestBody MultipartFile file) {
        List<String> types = Arrays.asList(FileType.PDF);
        long size = 10 * 1024 * 1024;//设置文件最大上传大小
        Map<String, Object> map = fileUploadPortionCode(file, types, size);
        String resultEncrypt = aesUtil.aesEncryptToBytes(map, AESUtil.MANAGE_KEY);
        return resultEncrypt;
    }

    /**
     * excel文件上传
     *
     * @param file
     * @return
     */
    @RequestMapping(value = {"/excelFileUpload"}, method = {RequestMethod.POST})
    @ResponseBody
    public String excelFileUpload(@RequestBody MultipartFile file) {
        List<String> types = Arrays.asList(FileType.XLS_DOC, FileType.XLSX_DOCX);
        long size = 10 * 1024 * 1024;//设置文件最大上传大小
        Map<String, Object> map = fileUploadPortionCode(file, types, size);
        String resultEncrypt = aesUtil.aesEncryptToBytes(map, AESUtil.MANAGE_KEY);
        return resultEncrypt;
    }

    /**
     * base64图片上传
     *
     * @param imgStr
     * @return
     */
    @RequestMapping(value = {"/Base64FileUpload"}, method = {RequestMethod.POST})
    @ResponseBody
    public String Base64FileUpload(@RequestBody String imgStr) {
        Map<String, Object> result = new HashMap<String, Object>();

        imgStr = imgStr.replace("data:image/png;base64,", "");
        imgStr = imgStr.replace(" ", "+");

        if (imgStr == null || imgStr == "") {
            result.put("code", 501);
            result.put("message", "文件名为空");
            String resultEncrypt = aesUtil.aesEncryptToBytes(result, AESUtil.MANAGE_KEY);
            return resultEncrypt;
        }
        try {
            BASE64Decoder decoder = new sun.misc.BASE64Decoder();
            byte[] bytes1 = decoder.decodeBuffer(imgStr);

            String filePath = new File(this.getClass().getResource("/").getPath())
                    .getParentFile().getParentFile().getParentFile().getParentFile().getAbsoluteFile() + "/file/";


            filePath = java.net.URLDecoder.decode(filePath, "UTF-8");
            UUID uuid = UUID.randomUUID();
            String path = filePath + uuid + ".png";


            ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);


            ImageIO.setUseCache(false);

            BufferedImage image = ImageIO.read(bais);


            File w2 = new File(path);// 可以是jpg,png,gif格式

            ImageIO.write(image, "jpg", w2);// 不管输出什么格式图片，此处不需改动


            bais.close();


            result.put("code", 200);
            result.put("message", "succ");
            result.put("data", path);

            String resultEncrypt = aesUtil.aesEncryptToBytes(result, AESUtil.MANAGE_KEY);
            return resultEncrypt;

        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e);
            String resultEncrypt = aesUtil.aesEncryptToBytes(result, AESUtil.MANAGE_KEY);
            return resultEncrypt;
        }
    }

    /**
     * 下载文件 (获取base64文件信息)
     *
     * @param params
     * @param resp
     * @return
     * @throws IOException
     */
    @RequestMapping(value = {"/downloadfile"}, method = {RequestMethod.POST})
    public String download(@RequestBody(required = false) String params, HttpServletResponse resp) throws IOException {
        String decodeParam = DataProcessing.urlDecode(params);
        String param1 = aesUtil.desEncryt(decodeParam, AESUtil.MANAGE_KEY);//解密
        String path = JSONObject.parseObject(param1).getString("filePath");
        Map<String, Object> result = new HashMap<String, Object>();
        if (path != null && !"".equals(path) && path.startsWith(fileAddressablepath)) {//限制下载的文件路径只能是上传文件的目录地址
            List<String> typeList = Arrays.asList(FileType.JPEG, FileType.PNG, FileType.GIF, FileType.PDF, FileType.XLS_DOC, FileType.XLSX_DOCX);
            if (typeList.contains(path.substring(path.lastIndexOf(".")))) {
                try {
                    if (StringUtils.isEmpty(path)) {
                        result.put("code", 201);
                        result.put("msg", "文件不能为空");
                        String resultEncrypt = aesUtil.aesEncryptToBytes(result, AESUtil.MANAGE_KEY);
                        return resultEncrypt;
                    }

                    if ("extranet".equals(scene)) {//行外
                        File file1 = new File(path);
                        String canonicalPath = file1.getCanonicalPath();
                        if (path.equals(canonicalPath)) { //如果文件的绝对路径与传递路径不一致则判定为路径被返回上一级,抛出错误
                            if (!file1.exists()) {
                                result.put("code", 201);
                                result.put("msg", "文件不存在");
                                String resultEncrypt = aesUtil.aesEncryptToBytes(result, AESUtil.MANAGE_KEY);
                                return resultEncrypt;
                            }

                            path = "/".equals(path.substring(0, 1)) ? path.substring(1) : path;
                            byte[] b = Files.readAllBytes(Paths.get(path)); //使用服务器中的数据
                            final BASE64Encoder encoder = new BASE64Encoder();
                            String base = encoder.encode(b);

                            result.put("code", 200);
                            result.put("msg", "succ");
                            result.put("data", base);
                        } else {
                            result.put("code", 500);
                            result.put("msg", "该目录中不允许返回上一级，请勿修改下载路径");
                        }
                    } else if ("intranet".equals(scene)) { //行内
                        String key = path;
                        boolean existsObject = s3Service.existsObject(key);
                        if (!existsObject) {
                            result.put("code", 201);
                            result.put("msg", "文件不存在");
                            String resultEncrypt = aesUtil.aesEncryptToBytes(result, AESUtil.MANAGE_KEY);
                            return resultEncrypt;
                        }

                        String content = s3Service.getObjectBaseb4(key); //使用对象存储中的数据
                        result.put("code", 200);
                        result.put("msg", "succ");
                        result.put("data", content);

                    } else {
                        result.put("code", 203);
                        result.put("msg", "未定义系统场景");
                    }
                    String resultEncrypt = aesUtil.aesEncryptToBytes(result, AESUtil.MANAGE_KEY);
                    return resultEncrypt;
                } catch (Exception e) {
                    result.put("code", 500);
                    result.put("msg", e.getMessage());
                    e.printStackTrace();
                }
                String resultEncrypt = aesUtil.aesEncryptToBytes(result, AESUtil.MANAGE_KEY);
                return resultEncrypt;
            } else {
                result.put("code", 500);
                result.put("msg", "该文件不允许访问，请勿修改下载路径");
                String resultEncrypt = aesUtil.aesEncryptToBytes(result, AESUtil.MANAGE_KEY);
                return resultEncrypt;
            }
        } else {
//            result.put("code", 500);
//            result.put("msg", "该目录不允许访问，请勿修改下载路径");
            String resultEncrypt = aesUtil.aesEncryptToBytes(result, AESUtil.MANAGE_KEY);
            return resultEncrypt;
        }
    }

    /**
     * 下载文件 (获取base64文件信息)
     *
     * @param params
     * @param resp
     * @return
     * @throws IOException
     */
    @RequestMapping(value = {"/downloadfile1"}, method = {RequestMethod.POST})
    public String download1(@RequestBody(required = false) String params, HttpServletResponse resp) throws IOException {
        System.out.println("-----"+params);
        String decodeParam = DataProcessing.urlDecode(params);
        String param1 = aesUtil.desEncryt(decodeParam, AESUtil.MANAGE_KEY);//解密
        System.out.println("-----"+param1);
        String path = JSONObject.parseObject(param1).getString("filePath");
        System.out.println("-----"+path);
        Map<String, Object> result = new HashMap<String, Object>();
        if (path != null && !"".equals(path)) {//限制下载的文件路径只能是上传文件的目录地址
            List<String> typeList = Arrays.asList(FileType.JPEG, FileType.PNG, FileType.GIF, FileType.PDF, FileType.XLS_DOC, FileType.XLSX_DOCX,".docx",".doc");
            if (typeList.contains(path.substring(path.lastIndexOf(".")))) {
                try {
                    if (StringUtils.isEmpty(path)) {
                        result.put("code", 201);
                        result.put("msg", "文件不能为空");
                        String resultEncrypt = aesUtil.aesEncryptToBytes(result, AESUtil.MANAGE_KEY);
                        return resultEncrypt;
                    }

                        File file1 = new File(path);
                        String canonicalPath = file1.getCanonicalPath();
                        //if (path.equals(canonicalPath)) { //如果文件的绝对路径与传递路径不一致则判定为路径被返回上一级,抛出错误
                            if (!file1.exists()) {
                                result.put("code", 201);
                                result.put("msg", "文件不存在");
                                String resultEncrypt = aesUtil.aesEncryptToBytes(result, AESUtil.MANAGE_KEY);
                                return resultEncrypt;
                            }

                            path = "/".equals(path.substring(0, 1)) ? path.substring(1) : path;
                            byte[] b = Files.readAllBytes(Paths.get(path)); //使用服务器中的数据
                            final BASE64Encoder encoder = new BASE64Encoder();
                            String base = encoder.encode(b);

                            result.put("code", 200);
                            result.put("msg", "succ");
                            result.put("data", base);
                        /*} else {
                            result.put("code", 500);
                            result.put("msg", "该目录中不允许返回上一级，请勿修改下载路径");
                        }*/

                    String resultEncrypt = aesUtil.aesEncryptToBytes(result, AESUtil.MANAGE_KEY);
                    return resultEncrypt;
                } catch (Exception e) {
                    result.put("code", 500);
                    result.put("msg", e.getMessage());
                    e.printStackTrace();
                }
                String resultEncrypt = aesUtil.aesEncryptToBytes(result, AESUtil.MANAGE_KEY);
                return resultEncrypt;
            } else {
                result.put("code", 500);
                result.put("msg", "该文件不允许访问，请勿修改下载路径");
                String resultEncrypt = aesUtil.aesEncryptToBytes(result, AESUtil.MANAGE_KEY);
                return resultEncrypt;
            }
        } else {
//            result.put("code", 500);
//            result.put("msg", "该目录不允许访问，请勿修改下载路径");
            String resultEncrypt = aesUtil.aesEncryptToBytes(result, AESUtil.MANAGE_KEY);
            return resultEncrypt;
        }
    }


    /**
     * 下载文件 (获取base64文件信息)
     *
     * @param size
     * @param resp
     * @return
     * @throws IOException
     */
    @RequestMapping(value = {"/downloadContent"}, method = {RequestMethod.GET})
    public byte[] downloadContent(String size, HttpServletResponse resp) throws IOException {
        File file = new File("C://Users//actort//Desktop//2021_05_12-新能源汽车行业分析月报//html//新能源汽车行业分析月报（2021年4月）.html");
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        ByteArrayOutputStream baos = null;
        try {
            if (file.exists()) {
                fis = new FileInputStream(file);
                baos = new ByteArrayOutputStream();
                //isr = new InputStreamReader(fis);
                //br = new BufferedReader(isr);
                //StringBuffer sb = new StringBuffer();
                //String len = null;
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = fis.read(bytes)) != -1) {
                    baos.write(bytes, 0, len);
                }
                String s = baos.toString();
                String label = "</head>";
                String appendLabel = "<style>\n\tbody,div,p,table,tr,td,th,ul,li,ol,span,a,h1,h2,h3,h4,h5,h6 {\n\t\tfont-size: "
                        + size + "px !important;\n\t\tline-height: "
                        + (Integer.valueOf(size) + 30) + "px !important;\n\t}\n</style>\n</head>";
                String conversionStr = s.replace(label, appendLabel);
                baos.flush();
                baos.write(conversionStr.getBytes(StandardCharsets.US_ASCII));
                return baos.toByteArray();
            }
        } finally {
            if (br != null) br.close();
            if (isr != null) isr.close();
            if (fis != null) fis.close();
            if (baos != null) baos.close();
        }
        return null;
    }


    /**
     * @param file
     * @return
     */
    @RequestMapping(value = {"/activitiUpload"}, method = {RequestMethod.POST})
    @ResponseBody
    public String upload(@RequestParam(value = "file", required = true) MultipartFile file) {
        Map<String, Object> result = new HashMap<>();

        // 获取上传的文件名
        String fileName = file.getOriginalFilename();
        // 限定上传的文件类型
        if (fileName.contains(".zip") || fileName.contains(".bpmn20.xml") || fileName.contains(".bpmn")) {
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("file", multipartFileToFile(file));
            ResponseModel responseModel = ActivitiUploadBPMNUtil.uploadBpmnFile(hashMap);

            if (responseModel.isSuccess()) {
                result.put("code", 200);
                result.put("msg", "succ");
                result.put("data", JSONObject.parseObject(responseModel.getRestBodyString()));
            }
        }

        String resultEncrypt = aesUtil.aesEncryptToBytes(result, AESUtil.MANAGE_KEY);
        return resultEncrypt;
    }

    /**
     * @param req
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping(value = {"/download"}, method = RequestMethod.POST, produces = "application/octet-stream;charset=utf-8")
    public String downloadOne(HttpServletRequest req, HttpServletResponse response) throws IOException {
//        HttpServletRequest req = (HttpServletRequest) request;
//
        //Map<String, Object> result = new HashMap<String, Object>();

        String fileName = req.getParameter("fileName");// 设置文件名，根据业务需要替换成要下载的文件名
        String fileUrl = req.getParameter("fileUrl");
//        String downloadDir = req.getSession().getServletContext().getRealPath("/") +"upload/";
//        downloadDir=downloadDir.substring(0,downloadDir.length()-1);
//        downloadDir=env.getProperty("file.upload.path");//下载目录

        //String downloadDir = "C:/test/file/";//测试地址

//        String realPath=downloadDir+fileUrl;//
        if (fileUrl != null && !"".equals(fileUrl) && fileUrl.startsWith(fileAddressablepath)) {//限制下载的文件路径只能是上传文件的目录地址
            File file = new File(fileUrl);//下载目录加文件名拼接成realpath
            String canonicalPath = file.getCanonicalPath();
            if (fileUrl.equals(canonicalPath)) { //如果文件的绝对路径与传递路径不一致则判定为路径被返回上一级,抛出错误
                if (file.exists()) { //判断文件父目录是否存在
                    response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
                    int rl = 1024;
                    byte[] buffer = new byte[rl];
                    FileInputStream fis = null; //文件输入流
                    BufferedInputStream bis = null;
                    OutputStream os = null; //输出流
                    try {
                        os = response.getOutputStream();
                        fis = new FileInputStream(file);
                        bis = new BufferedInputStream(fis);
                        int i = bis.read(buffer);
                        while (i >= 0) {
                            os.write(buffer);
                            i = bis.read(buffer);
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } finally {
                        if (bis != null) bis.close();
                        if (fis != null) fis.close();
                        if (os != null) os.close();
                    }
                }
                //Constants.getSuccMsg(result,"fail"+realPath+fileName);

                return "status:succ";
            } else {
                return "status:err";
            }
        } else {
            return "status:err";
        }
    }

    /**
     * MultipartFile 转 File
     *
     * @param multiFile
     * @return File
     */
    private File multipartFileToFile(@RequestParam MultipartFile multiFile) {
        try {
            // 获取文件名
            String fileName = multiFile.getOriginalFilename();
            // 获取文件的后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            // 设置文件存储路径
            String filePath = new File(this.getClass().getResource("/").getPath())
                    .getParentFile().getParentFile().getParentFile().getParentFile().getAbsoluteFile() + "/file/";
            File dir = new File(filePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            filePath = java.net.URLDecoder.decode(filePath, "UTF-8");
            UUID uuid = UUID.randomUUID();
            String path = filePath + uuid + suffixName;
            File file = new File(path);
            // 检测是否存在目录
            file.deleteOnExit();
            multiFile.transferTo(file);// 文件写入

            return file;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 通用文件上传部分代码
     *
     * @param file
     * @param types
     * @param size
     * @return
     */
    private Map<String, Object> fileUploadPortionCode(MultipartFile file, List<String> types, long size) {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> map = new HashMap<>();
        try {
            Map<String, String> map1 = fileRelevantValidation(file, types, size);//验证文件格式、大小
            if ("200".equalsIgnoreCase(map1.get("code"))) {
                String suffixName = map1.get("data");
                if ("extranet".equals(scene)) {//行内
                    String path = fileUploadByCloudServer(file, suffixName);//上传至服务器
                    map.put("code", 200);
                    map.put("message", "succ");
                    map.put("data", path);
                    Constants.getSuccMsg(result, map);
                    return result;
                } else if ("intranet".equals(scene)) { //行外
                    String path = fileUploadByObjectStorage(file, suffixName);
                    map.put("code", 200);
                    map.put("message", "succ");
                    map.put("data", path);
                    Constants.getSuccMsg(result, map);
                    return result;
                } else {
                    map.put("code", 203);
                    map.put("msg", "未定义系统场景");
                    Constants.getSuccMsg(result, map);
                    return result;
                }
            } else {
                map = (Map) map1;
                Constants.getSuccMsg(result, map);
                return result;
            }
        } catch (Exception e) {
            map.put("code", "500");
            map.put("msg", e.getMessage());
            Constants.getErrMsg(result, map);
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 文件的相关验证
     *
     * @param file
     * @param types
     * @param size
     * @return
     */
    private Map<String, String> fileRelevantValidation(MultipartFile file, List<String> types, long size) {
        Map<String, String> map = new HashMap<String, String>();
        if (file == null || file.isEmpty()) {
            map.put("code", "201");
            map.put("msg", "文件不能为空");
            return map;
        }

        if (file.getSize() > size) {
            map.put("code", "203");
            map.put("msg", "文件大小不能超过" + (size / 1024 / 1024) + "MB");
            return map;
        }

        try {
            // 获取文件名
            String fileName = file.getOriginalFilename();
            // 获取文件的后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));

            if (types != null && types.size() > 0) {
                // on           设置分割符
                // skipNulls    跳过null
                // join         拼接
                String typeFull = Joiner.on("、").skipNulls().join(types); //将集合按指定分隔符拼接成字符串

                String currentType = FileTypeUtil.getType(file.getInputStream());
                boolean typeResult = false;
                boolean formatResult = false;
                for (String type : types) {
                    if (type != null && !type.isEmpty()) {
                        if (type.equalsIgnoreCase(suffixName))
                            typeResult = true;

                        if (currentType == null || type.contains(currentType) || currentType.contains("zip"))
                            formatResult = true;

                        if (typeResult && formatResult) break;
                    } else {
                        map.put("code", "204");
                        map.put("msg", "传递类型不能为空！");
                        return map;
                    }
                }

                if (!typeResult) {
                    map.put("code", "203");
                    map.put("msg", "文件类型错误，请选择" + typeFull + "文件");
                    return map;
                }

                if (!formatResult) {
                    map.put("code", "203");
                    map.put("msg", "上传失败，文件内部格式错误！");
                    return map;
                }
            }
            map.put("code", "200");
            map.put("msg", "succ");
            map.put("data", suffixName);
            return map;
        } catch (Exception e) {
            map.put("code", "500");
            map.put("msg", e.getMessage());
        }
        return map;
    }

    /**
     * 文件上传至云服务器上
     *
     * @param file
     * @return
     */
    private String fileUploadByCloudServer(MultipartFile file, String suffixName) throws Exception {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String filePathPrefix = fileUploadpath + sdf.format(date) + "/";
        String filePath = java.net.URLDecoder.decode(filePathPrefix, "UTF-8");
        File picFile = new File(filePath);
        if (!picFile.exists()) {
            picFile.mkdirs();
        }
        String path = filePath + UUID.randomUUID() + suffixName;
        File dest = new File(path);
        // 检测是否存在目录
        dest.deleteOnExit();
        file.transferTo(dest);// 文件写入
        return path;
    }

    /**
     * 上传文件至对象存储中
     *
     * @param file
     * @return
     * @throws Exception
     */
    private String fileUploadByObjectStorage(MultipartFile file, String suffixName) throws Exception {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String filePathPrefix = fileUploadpath + sdf.format(date) + "/";
        String filePath = java.net.URLDecoder.decode(filePathPrefix, "UTF-8");
        String path = filePath + UUID.randomUUID() + suffixName;
        s3Service.uploadObject(file, path);
        return path;
    }

    /**
     * 将返回结果加密
     *
     * @param result
     * @return
     */
    private String resultByStringAndEncrypt(Map<String, Object> result) {
        String resultJson = JSONObject.toJSONString(result);
        String resultEncrypt = aesUtil.aesEncryptToBytes(resultJson, AESUtil.MANAGE_KEY);
        return resultEncrypt;
    }



    /**
     * 下载附件
     * @param param
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = {"/netDownLoadNet"}, method = {RequestMethod.POST}, produces = "application/octet-stream;charset=utf-8")
    public void netDownLoadNet(@RequestBody String param,HttpServletResponse response) throws Exception {
        String decodeParam = DataProcessing.urlDecode(param);
        String param1 = aesUtil.desEncryt(decodeParam, AESUtil.MANAGE_KEY);//解密
        JSONObject jsonObject=JSONObject.parseObject(param1);
        String netAddress=jsonObject.getString("netAddress");
        String filename=jsonObject.getString("filename");
        URL url = new URL(netAddress);
        URLConnection conn = url.openConnection();
        InputStream inputStream = conn.getInputStream();

        response.reset();
        response.setContentType(conn.getContentType());
        //下载 文件名编码成UTF-8
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(filename, "UTF-8"));

        byte[] buffer = new byte[1024];
        int len;
        OutputStream outputStream = response.getOutputStream();
        while ((len = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, len);
        }
        inputStream.close();
    }

    /**
     * excel导出
     * ResponseEntity<byte[]>
     */
 /*   @RequestMapping(value = {"/exportexcel"}, method = {RequestMethod.POST,RequestMethod.GET}, produces = "application/octet-stream;charset=utf-8")
    public void exportexcel( @RequestBody String param,HttpServletResponse response)throws Exception{

        //
//        String param = "{}";
       // String decodeParam = DataProcessing.urlDecode(param);
       // String param1 = aesUtil.desEncryt(decodeParam, AESUtil.MANAGE_KEY);//解密
        Map<String,Object> map = fileService.exportExcel(param);
        String title = map.get("title").toString();
        String[] rowName = (String[]) map.get("rowName");
        List<Object[]> datalist = (List<Object[]>) map.get("data");
        String fileName = "会员基本信息"  + ".xls";
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
*/



}

