package com.jackie0.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
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
@EnableAutoConfiguration
public class CommonApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommonApplication.class, args);
    }
}
