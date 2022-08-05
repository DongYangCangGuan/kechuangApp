package com.cloud.servicewechat.web;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    @Value("${objStorage.aws.accessKeyId}")
    private String accessKeyId; //用户名

    @Value("${objStorage.aws.secretAccessKey}")
    private String secretAccessKey; //密码

    @Value("${objStorage.aws.endpoint}")
    private String endpoint; //终端

    @Bean
    public AmazonS3 amazonS3() {
        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);
        AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);

        String region = Regions.CN_NORTH_1.getName();//地区
        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(endpoint, region);

        ClientConfiguration config = new ClientConfiguration();
        config.setConnectionTimeout(4000);
        config.setMaxErrorRetry(4);

        AmazonS3ClientBuilder builder = AmazonS3Client.builder();
        builder.withClientConfiguration(config);
        builder.withEndpointConfiguration(endpointConfiguration);
        builder.withCredentials(awsCredentialsProvider);
        //使用域名时，需要如下设置
        builder.setPathStyleAccessEnabled(true);

        AmazonS3 amazonS3 = builder.build();
        return amazonS3;
    }
}
