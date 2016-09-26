package com.jackie0.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * spring应用启动及配置类
 *
 * @author jackie0
 * @since Java8
 * date 2016-06-15 16:17
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration(excludeName = {
        /*没有用到SpringSecurity功能但自动配置里有，会打出一个错误日志，但不影响应用，有强迫症排除该配置。*/
        "org.springframework.boot.autoconfigure.security.FallbackWebSecurityAutoConfiguration"})
@EnableCaching
public class CommonApplication {
    private CommonApplication() {
    }

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(CommonApplication.class);
        springApplication.setWebEnvironment(false);
        springApplication.run(args);
    }
}
