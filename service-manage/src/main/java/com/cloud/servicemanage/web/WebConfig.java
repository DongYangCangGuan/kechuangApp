package com.cloud.servicemanage.web;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig  extends WebMvcConfigurerAdapter {
    @Value("${picture.path}")
    private String picturepath;
    @Value("${product.path}")
    private String productpath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/pictureloads/**").addResourceLocations("file:" + picturepath);
        registry.addResourceHandler("/productfile/**").addResourceLocations("file:" + productpath);
        super.addResourceHandlers(registry);
    }


   /* @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
        System.out.println("-----"+picturepath);
        registry.addResourceHandler("/pictureloads/**").addResourceLocations("file:" + picturepath);
    }*/
}
