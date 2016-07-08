package com.jackie0.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableAutoConfiguration(excludeName = {
        /*没有用到SpringSecurity功能但自动配置里有，会打出一个错误日志，但不影响应用，有强迫症排除该配置。*/
        "org.springframework.boot.autoconfigure.security.FallbackWebSecurityAutoConfiguration"})
public class PlatformApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(PlatformApplication.class, args);
    }
}
