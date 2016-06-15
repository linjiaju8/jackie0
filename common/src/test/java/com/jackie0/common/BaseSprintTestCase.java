package com.jackie0.common;

import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 配置测试类 只需继承该类即可测试 方法加上 @Test
 *
 * @author jackie0
 * @since Jdk 1.8
 * date 2016-06-15 16:17
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CommonApplication.class)
@ActiveProfiles("test")
public class BaseSprintTestCase extends AbstractJUnit4SpringContextTests {
}