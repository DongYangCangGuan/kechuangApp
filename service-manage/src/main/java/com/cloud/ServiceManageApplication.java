package com.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import javax.servlet.MultipartConfigElement;

@SpringBootApplication(scanBasePackages ={"com.cloud"})
@EnableScheduling
@ServletComponentScan
//@PropertySource(value ={"file:/usr/local/javaApplication/properties/service-manage/application.properties"})
public class ServiceManageApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceManageApplication.class, args);
    }

    /**
     * 文件上传配置
     *
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //  单个数据大小
        factory.setMaxFileSize("20480KB"); // KB,MB
        /// 总上传数据大小
        factory.setMaxRequestSize("102400KB");
        factory.setLocation("/data/tmp");
        return factory.createMultipartConfig();
    }
}
