package com.jackie0.common;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * common工程配置及应用相关测试
 *
 * @author jackie0
 * @since Java8
 * date 2016-06-15 16:29
 */
public class CommonApplicationTest extends BaseSprintTestCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonApplicationTest.class);

    @Test
    public void testLog() {
        LOGGER.warn("测试warn级别日志打印！");
        LOGGER.trace("测试trace级别日志打印！");
        LOGGER.debug("测试debug级别日志打印！");
        LOGGER.info("测试info级别日志打印！");
        LOGGER.error("测试error级别日志打印！");
    }
}
