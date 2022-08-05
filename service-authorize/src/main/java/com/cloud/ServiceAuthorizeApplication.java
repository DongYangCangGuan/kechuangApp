package com.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.cloud"})
@EnableScheduling
@ServletComponentScan(basePackages = {"com.cloud"})
public class ServiceAuthorizeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceAuthorizeApplication.class, args);
    }

}
