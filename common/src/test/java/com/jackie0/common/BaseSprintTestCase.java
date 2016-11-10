package com.jackie0.common;

import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * 配置测试类 只需继承该类即可测试 方法加上 @Test
 *
 * @author jackie0
 * @since Jdk 1.8
 * date 2016-06-15 16:17
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration // common包虽然是jar包形式打包，但因开启了DefaultServletHandlerConfigurer所以也要使用web环境来单元测试
@SpringApplicationConfiguration(classes = CommonApplication.class)
@ActiveProfiles("development")
public class BaseSprintTestCase extends AbstractJUnit4SpringContextTests {
}