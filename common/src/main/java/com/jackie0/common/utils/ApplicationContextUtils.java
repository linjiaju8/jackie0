package com.jackie0.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * spring上下文注册类，方便在非spring容器管理的类中调用spring容器相关方法
 * ClassName:ApplicationContextRegister <br/>
 * Date:     2015年09月05日 17:31 <br/>
 *
 * @author jackie0
 * @since JDK 1.8
 */
@Component
@Lazy(false)
public class ApplicationContextUtils {

    private static ApplicationContextUtils instance;

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void registerInstance() {
        instance = this;
    }

    public static ApplicationContext getApplicationContext() {
        return instance.applicationContext;
    }
}


