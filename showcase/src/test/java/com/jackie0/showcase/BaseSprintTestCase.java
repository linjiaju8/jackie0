package com.jackie0.showcase;

import com.jackie0.common.CommonApplication;
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
@WebAppConfiguration
@SpringApplicationConfiguration(classes = {CommonApplication.class, ShowcaseApplication.class})
@ActiveProfiles("development")
public class BaseSprintTestCase extends AbstractJUnit4SpringContextTests {
}