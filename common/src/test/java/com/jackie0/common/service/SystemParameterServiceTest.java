package com.jackie0.common.service;

import com.jackie0.common.BaseSprintTestCase;
import com.jackie0.common.dao.SystemParameterDao;
import com.jackie0.common.entity.SystemParameter;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 系统参数服务测试类
 *
 * @author jackie0
 * @since Java8
 * date 2016-07-13 15:49
 */
public class SystemParameterServiceTest extends BaseSprintTestCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(SystemParameterServiceTest.class);

    @Autowired
    private SystemParameterService systemParameterService;

    @Autowired
    private SystemParameterDao systemParameterDao;

    @Test
    public void testCreateSystemParameter() {
        SystemParameter systemParameter = new SystemParameter();
        systemParameter.setParameterKey("test");
        systemParameter.setParameterValue("test");
        systemParameter.setDescription("单元测试");
        systemParameter.setParameterValueType("");
    }
}
