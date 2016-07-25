package com.jackie0.common.service;

import com.jackie0.common.BaseSprintTestCase;
import com.jackie0.common.dao.SystemParameterDao;
import com.jackie0.common.entity.SystemParameter;
import com.jackie0.common.enumeration.OperationType;
import com.jackie0.common.utils.DataUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

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
        systemParameter.setParameterKey("testSystemParameter");
        systemParameter.setParameterValue("test");
        systemParameter.setDescription("单元测试");
        SystemParameter savedSystemParameter = systemParameterService.createSystemParameter(systemParameter);
        Assert.assertTrue(savedSystemParameter != null && StringUtils.isNotBlank(savedSystemParameter.getSystemParameterId()));
    }

    @Test
    public void testUpdateSystemParameter() {
        SystemParameter systemParameter = new SystemParameter();
        systemParameter.setParameterKey("testSystemParameter");
        systemParameter.setParameterValue("test");
        systemParameter.setDescription("单元测试");
        SystemParameter createResult = systemParameterService.createSystemParameter(systemParameter);
        createResult.setParameterValue("test2");
        SystemParameter updateResult = systemParameterService.updateSystemParameter(createResult);
        Assert.assertTrue(updateResult != null && systemParameter.getParameterValue().equals(updateResult.getParameterValue()));
    }

    @Test
    public void testDeleteSystemParameterById() {
        SystemParameter systemParameter = new SystemParameter();
        systemParameter.setParameterKey("testSystemParameter");
        systemParameter.setParameterValue("test");
        systemParameter.setDescription("单元测试");
        SystemParameter createResult = systemParameterService.createSystemParameter(systemParameter);
        SystemParameter deleteResult = systemParameterService.deleteSystemParameterById(createResult.getSystemParameterId());
        Assert.assertTrue(createResult.getSystemParameterId().equals(deleteResult.getSystemParameterId()));
    }

    @Test
    public void testFindSystemParameterByPage() {
        List<SystemParameter> systemParameters = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            SystemParameter systemParameter = new SystemParameter();
            systemParameter.setParameterKey("testSystemParameter" + (i + 1));
            systemParameter.setParameterValue("test" + (i + 1));
            systemParameter.setDescription("单元测试");
            DataUtils.setBaseEntityField(systemParameter, OperationType.CREATE);
            systemParameters.add(systemParameter);
        }
        systemParameterDao.save(systemParameters);
        Page<SystemParameter> systemParameterPage = systemParameterService.findSystemParameterByPage(new SystemParameter());
        Assert.assertTrue(systemParameterPage.getTotalElements() == 25);
    }

    @Test
    public void testFindSystemParameterByKey() {
        SystemParameter systemParameter = new SystemParameter();
        systemParameter.setParameterKey("testSystemParameter");
        systemParameter.setParameterValue("test");
        systemParameter.setDescription("单元测试");
        systemParameterService.createSystemParameter(systemParameter);
        SystemParameter systemParameterTest = systemParameterService.findSystemParameterByKey(systemParameter.getParameterKey());
        Assert.assertTrue(systemParameter.getParameterValue().equals(systemParameterTest.getParameterValue()));
    }

    @Test
    public void testFindSystemParameterValueByKey() {
        SystemParameter systemParameter = new SystemParameter();
        systemParameter.setParameterKey("testSystemParameter");
        systemParameter.setParameterValue("test");
        systemParameter.setDescription("单元测试");
        systemParameterService.createSystemParameter(systemParameter);
        String systemParameterValue = systemParameterService.findSystemParameterValueByKey(systemParameter.getParameterKey());
        Assert.assertTrue(systemParameter.getParameterValue().equals(systemParameterValue));
    }

    @After
    public void clear() {
        List<SystemParameter> systemParameters = systemParameterDao.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.like(root.get("description").as(String.class), "单元测试%"));
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        });
        if (CollectionUtils.isNotEmpty(systemParameters)) {
            systemParameterDao.delete(systemParameters);
        }
        LOGGER.debug("SystemParameterServiceTest清理单元测试数据成功！{}", ArrayUtils.toString(systemParameters));
    }
}
