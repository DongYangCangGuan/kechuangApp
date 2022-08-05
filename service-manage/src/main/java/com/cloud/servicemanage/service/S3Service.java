package com.cloud.servicemanage.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.URL;
import java.util.*;

@Service
public class S3Service {

    //@Value("${objStorage.aws.bucketName}")
    private String bucketName = "actort"; //桶名称

    @Autowired
    private AmazonS3 amazonS3;

    //实例化日志对象，引用jar包org.slf4j.Logger
    private static final Logger logger = LoggerFactory.getLogger(S3Service.class);

    /**
     * 创建桶
     *
     * @param bucketName
     * @return
     */
    private boolean createBucket(String bucketName) {
        try {
            if (!amazonS3.doesBucketExistV2(bucketName)) {
                amazonS3.createBucket(new CreateBucketRequest(bucketName));
                String bucketLocation = amazonS3.getBucketLocation(new GetBucketLocationRequest(bucketName));
                System.out.println("bucketLocation------------>" + bucketLocation);
                return true;
            } else {
                throw new AmazonServiceException("桶已经存在，不能创建");
            }
        } catch (AmazonServiceException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        } catch (SdkClientException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除桶
     *
     * @param bucketName
     * @return
     */
    private boolean deleteBucket(String bucketName) {
        try {
            ObjectListing objectListing = amazonS3.listObjects(bucketName);
            while (true) {
                Iterator<S3ObjectSummary> objIter = objectListing.getObjectSummaries().iterator();
                while (objIter.hasNext()) {
                    amazonS3.deleteObject(bucketName, objIter.next().getKey());
                }

                if (objectListing.isTruncated()) {
                    objectListing = amazonS3.listNextBatchOfObjects(objectListing);
                } else {
                    break;
                }
            }

            if (amazonS3.doesBucketExistV2(bucketName)) {
                VersionListing versionListing = amazonS3.listVersions(new ListVersionsRequest().withBucketName(bucketName));
                while (true) {
                    Iterator<S3VersionSummary> versionIter = versionListing.getVersionSummaries().iterator();
                    while (versionIter.hasNext()) {
                        S3VersionSummary vs = versionIter.next();
                        amazonS3.deleteVersion(bucketName, vs.getKey(), vs.getVersionId());
                    }

                    if (versionListing.isTruncated()) {
                        versionListing = amazonS3.listNextBatchOfVersions(versionListing);
                    } else {
                        break;
                    }
                }

            }

            amazonS3.deleteBucket(bucketName);
            return true;
        } catch (AmazonServiceException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        } catch (SdkClientException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取桶列表
     *
     * @return
     */
    private List<Bucket> listBuckets() {
        List<Bucket> buckets = amazonS3.listBuckets();
        return buckets;
    }

    /**
     * 判断桶是否存在
     *
     * @param bucketName
     * @return
     */
    public boolean existsBucket(String bucketName) {
        return amazonS3.doesBucketExistV2(bucketName);
    }

    /**
     * 上传对象信息 (传递MultipartFile文件)
     *
     * @param file
     * @return
     */
    public boolean uploadObject(MultipartFile file, String key) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            amazonS3.putObject(bucketName, key, file.getInputStream(), metadata);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 上传对象信息 (传递流）
     *
     * @param is
     * @return
     */
    public boolean uploadObject(InputStream is, String key) {
        try {
            Integer available = is.available();
            long size = available.longValue();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(key.substring(key.lastIndexOf(".")));
            metadata.setContentLength(size);

            amazonS3.putObject(bucketName, key, is, metadata);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断对象是否存在
     *
     * @param key
     * @return
     */
    public boolean existsObject(String key) {
        ObjectListing objectListing = amazonS3.listObjects(bucketName, key);
        return objectListing.getObjectSummaries().size() > 0;
    }

    /**
     * 获取对象信息 (以base64位返回)
     *
     * @param key
     */
    public String getObjectBaseb4(String key) {
        try {
            S3Object object = amazonS3.getObject(bucketName, key);
            S3ObjectInputStream sis = object.getObjectContent();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] read_buf = new byte[1024];
            int read_len = 0;
            while ((read_len = sis.read(read_buf)) > 0) {
                bos.write(read_buf, 0, read_len);
            }

            byte[] bytes = bos.toByteArray();
            final BASE64Encoder encoder = new BASE64Encoder();
            String base = encoder.encode(bytes);
            if (sis != null) sis.close();
            if (bos != null) bos.close();
            return base;
        } catch (AmazonServiceException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除文件
     *
     * @param key
     * @return
     */
    private boolean deleteObject(String key) {
        try {
            amazonS3.deleteObject(bucketName, key);
            return true;
        } catch (AmazonServiceException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 批量删除文件
     *
     * @param keys
     * @return
     */
    private boolean deleteObject(String[] keys) {
        try {
            DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName).withKeys(keys);
            amazonS3.deleteObjects(deleteObjectsRequest);
            return true;
        } catch (AmazonServiceException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 生成预签名链接
     */
    public String GeneratePreSignedLinks(String key) {
        //设置过期时间
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 7);

        URL url = amazonS3.generatePresignedUrl(bucketName, key, calendar.getTime(), HttpMethod.GET);
        System.out.println("生成的签名的URL:" + url.toString());
        return url.toString();
    }

}
