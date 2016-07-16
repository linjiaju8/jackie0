package com.jackie0.common.service;

import com.jackie0.common.BaseSprintTestCase;
import com.jackie0.common.dao.SystemParameterDao;
import com.jackie0.common.entity.DataDict;
import com.jackie0.common.entity.SystemParameter;
import com.jackie0.common.enumeration.OperationType;
import com.jackie0.common.utils.DataUtils;
import com.jackie0.common.vo.ResultVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
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
        ResultVO createResult = systemParameterService.createSystemParameter(null);
        Assert.assertTrue(ResultVO.FAIL.equals(createResult.getErrorCode()));

        SystemParameter systemParameter = new SystemParameter();
        systemParameter.setParameterKey("test");
        createResult = systemParameterService.createSystemParameter(systemParameter);
        Assert.assertTrue(ResultVO.FAIL.equals(createResult.getErrorCode()));

        systemParameter.setParameterKey(null);
        systemParameter.setParameterValue("test");
        createResult = systemParameterService.createSystemParameter(systemParameter);
        Assert.assertTrue(ResultVO.FAIL.equals(createResult.getErrorCode()));

        systemParameter.setParameterKey("test");
        systemParameter.setDescription("单元测试");
        createResult = systemParameterService.createSystemParameter(systemParameter);
        Assert.assertTrue(ResultVO.SUCCESS.equals(createResult.getErrorCode()));
    }

    @Test
    public void testUpdateSystemParameter() {
        SystemParameter systemParameter = new SystemParameter();
        systemParameter.setParameterKey("test");
        systemParameter.setParameterValue("test");
        systemParameter.setDescription("单元测试");
        ResultVO createResult = systemParameterService.createSystemParameter(systemParameter);
        systemParameter = createResult.getResult(SystemParameter.class);
        systemParameter.setParameterValue("test2");
        ResultVO updateResult = systemParameterService.updateSystemParameter(systemParameter);
        Assert.assertTrue(ResultVO.SUCCESS.equals(updateResult.getErrorCode()) && systemParameter.getParameterValue().equals(updateResult.getResult(SystemParameter.class).getParameterValue()));
    }

    @Test
    public void testDeleteSystemParameterById() {
        SystemParameter systemParameter = new SystemParameter();
        systemParameter.setParameterKey("test");
        systemParameter.setParameterValue("test");
        systemParameter.setDescription("单元测试");
        ResultVO createResult = systemParameterService.createSystemParameter(systemParameter);
        systemParameter = createResult.getResult(SystemParameter.class);
        ResultVO deleteResult = systemParameterService.deleteSystemParameterById(systemParameter.getSystemParameterId());
        Assert.assertTrue(ResultVO.SUCCESS.equals(deleteResult.getErrorCode()));
    }

    @Test
    public void testFindSystemParameterByPage() {
        List<SystemParameter> systemParameters = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            SystemParameter systemParameter = new SystemParameter();
            systemParameter.setParameterKey("test" + (i + 1));
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
        systemParameter.setParameterKey("test");
        systemParameter.setParameterValue("test");
        systemParameter.setDescription("单元测试");
        systemParameterService.createSystemParameter(systemParameter);
        SystemParameter systemParameterTest = systemParameterService.findSystemParameterByKey(systemParameter.getParameterKey());
        Assert.assertTrue(systemParameter.getParameterValue().equals(systemParameterTest.getParameterValue()));
    }

    @Test
    public void testFindSystemParameterValueByKey() {
        SystemParameter systemParameter = new SystemParameter();
        systemParameter.setParameterKey("test");
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
