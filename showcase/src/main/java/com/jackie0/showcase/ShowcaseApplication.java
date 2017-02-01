package com.jackie0.showcase;

import com.jackie0.common.CommonApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * showcase应用启动程序
 *
 * @author jackie0
 * @since Java8
 * date 2016-07-29 16:00
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration(excludeName = {
        /*没有用到SpringSecurity功能但自动配置里有，会打出一个错误日志，但不影响应用，有强迫症排除该配置。*/
        "org.springframework.boot.autoconfigure.security.FallbackWebSecurityAutoConfiguration"})
public class ShowcaseApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CommonApplication.class, ShowcaseApplication.class);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication springApplication = new SpringApplication(CommonApplication.class, ShowcaseApplication.class);
        springApplication.run(args);
    }
}
