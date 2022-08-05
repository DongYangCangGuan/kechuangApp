package com.cloud.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * gateway网关服务启动类
 */
@EnableZuulProxy
@SpringBootApplication
public class BaseGatewayZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaseGatewayZuulApplication.class, args);
    }
}
