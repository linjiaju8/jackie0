package com.jackie0.common.service;

import com.jackie0.common.BaseSprintTestCase;
import com.jackie0.common.entity.BaseOperationLog;
import com.jackie0.common.entity.OperationLog;
import com.jackie0.common.enumeration.OperationType;
import com.jackie0.common.mongo.entity.MongoOperationLog;
import com.jackie0.common.utils.DataUtils;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * common工程配置及应用相关测试
 *
 * @author jackie0
 * @since Java8
 * date 2016-06-15 16:29
 */
public class OperationLogServiceTest extends BaseSprintTestCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(OperationLogServiceTest.class);

    @Autowired
    private OperationLogService operationLogService;

    @Test
    public void testCreateOperationLog() {
        BaseOperationLog baseOperationLogCreated = operationLogService.createOperationLog(null);
        Assert.assertTrue(baseOperationLogCreated == null);

        BaseOperationLog baseOperationLog = new OperationLog();
        ((OperationLog) baseOperationLog).setDescription("单元测试数据");
        ((OperationLog) baseOperationLog).setOperationName("登陆操作");
        ((OperationLog) baseOperationLog).setOperationType(OperationType.LOGIN.getValue());
        ((OperationLog) baseOperationLog).setOperationTime(new Timestamp(System.currentTimeMillis()));
        DataUtils.setBaseEntityField(((OperationLog) baseOperationLog), OperationType.CREATE);
        baseOperationLogCreated = operationLogService.createOperationLog(baseOperationLog);
        Assert.assertTrue(baseOperationLogCreated instanceof OperationLog);

        baseOperationLog = new MongoOperationLog();
        ((MongoOperationLog) baseOperationLog).setDescription("单元测试数据");
        ((MongoOperationLog) baseOperationLog).setOperationName("登陆操作");
        ((MongoOperationLog) baseOperationLog).setOperationType(OperationType.LOGIN.getValue());
        ((MongoOperationLog) baseOperationLog).setOperationTime(new Timestamp(System.currentTimeMillis()));
        baseOperationLogCreated = operationLogService.createOperationLog(baseOperationLog);
        Assert.assertTrue(baseOperationLogCreated instanceof MongoOperationLog);
    }

    @Test
    public void testFindOne() {

    }

    @Test
    public void testFindByPage() {

    }
}
