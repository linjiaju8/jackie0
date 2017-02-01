package com.jackie0.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.WebMvcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * spring-boot为了使用jar方式及内嵌tomcat或jetty运行web应用，默认放弃了以前springMVC
 * 在WEB-INF下存放视图的方式，为使用以前的方式以war包运行web应用，需做如下配置，如果
 * 仅仅是访问WEB-INF下的jsp此类不是必须的，但要访问html则必须如下配置。
 *
 * @author jackie0
 * @since Java8
 * date 2016-10-20 10:28
 */
@Configuration
@EnableWebMvc
@EnableConfigurationProperties({WebMvcProperties.class})
public class MvcConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    private WebMvcProperties mvcProperties = new WebMvcProperties();

    @Bean
    public ViewResolver getViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix(this.mvcProperties.getView().getPrefix());
        resolver.setSuffix(this.mvcProperties.getView().getSuffix());
        return resolver;
    }


    @Override
    public void configureDefaultServletHandling(
            DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
}
