package com.cloud.commonsmng.factory;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextHolder implements ApplicationContextAware {


    private static ApplicationContext applicationContext = null;

    //spring 启动时候 把spring容器作为参数传入
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextHolder.applicationContext = applicationContext;
    }

    //拿到spring容器， 可以传入class,string;传入 class service 对象  ,string 名称拿到spring托管service对象
    public static <T> T getBean(String type){
        return (T)applicationContext.getBean(type);
    }


}
