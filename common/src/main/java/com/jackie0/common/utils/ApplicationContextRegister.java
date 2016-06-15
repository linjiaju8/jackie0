package com.jackie0.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

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
public class ApplicationContextRegister implements ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationContextRegister.class);

    private static ApplicationContext APPLICATION_CONTEXT;

    /**
     * 设置spring上下文
     *
     * @param applicationContext spring上下文
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        LOGGER.debug("ApplicationContext registed-->{}", applicationContext);
        APPLICATION_CONTEXT = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return APPLICATION_CONTEXT;
    }
}


