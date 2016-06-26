package com.jackie0.common.service;

import com.jackie0.common.BaseSprintTestCase;
import com.jackie0.common.dao.OperationLogDao;
import com.jackie0.common.entity.BaseOperationLog;
import com.jackie0.common.entity.OperationLog;
import com.jackie0.common.enumeration.DeleteTag;
import com.jackie0.common.enumeration.OperationType;
import com.jackie0.common.mongo.dao.OperationLogMongoDao;
import com.jackie0.common.mongo.entity.MongoOperationLog;
import com.jackie0.common.utils.DataUtils;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.util.*;

/**
 * 操作日志service测试类
 *
 * @author jackie0
 * @since Java8
 * date 2016-06-15 16:29
 */
public class OperationLogServiceTest extends BaseSprintTestCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(OperationLogServiceTest.class);

    @Autowired
    private OperationLogService operationLogService;

    @Autowired
    private OperationLogMongoDao operationLogMongoDao;

    @Autowired
    private OperationLogDao operationLogDao;

    @Test
    public void testCreateOperationLog() {
        BaseOperationLog baseOperationLogCreated = operationLogService.createOperationLog(null);
        Assert.assertTrue(baseOperationLogCreated == null);

        BaseOperationLog baseOperationLog = new OperationLog();
        ((OperationLog) baseOperationLog).setDescription("单元测试数据");
        ((OperationLog) baseOperationLog).setOperationName("单元测试操作");
        ((OperationLog) baseOperationLog).setOperationUser("单元测试用户ID");
        ((OperationLog) baseOperationLog).setOperationType(OperationType.LOGIN.getValue());
        ((OperationLog) baseOperationLog).setOperationTime(new Timestamp(System.currentTimeMillis()));
        DataUtils.setBaseEntityField(((OperationLog) baseOperationLog), OperationType.CREATE);
        baseOperationLogCreated = operationLogService.createOperationLog(baseOperationLog);
        Assert.assertTrue(baseOperationLogCreated instanceof OperationLog);

        baseOperationLog = new MongoOperationLog();
        ((MongoOperationLog) baseOperationLog).setId(UUID.randomUUID().toString());
        ((MongoOperationLog) baseOperationLog).setDescription("单元测试数据");
        ((MongoOperationLog) baseOperationLog).setOperationName("单元测试操作");
        ((MongoOperationLog) baseOperationLog).setOperationUser("单元测试用户ID");
        ((MongoOperationLog) baseOperationLog).setOperationType(OperationType.LOGIN.getValue());
        ((MongoOperationLog) baseOperationLog).setOperationTime(new Timestamp(System.currentTimeMillis()));
        baseOperationLogCreated = operationLogService.createOperationLog(baseOperationLog);
        Assert.assertTrue(baseOperationLogCreated instanceof MongoOperationLog);
    }

    @Test
    public void testFindOne() {
        BaseOperationLog baseOperationLogCreated;

        BaseOperationLog baseOperationLog = new OperationLog();
        ((OperationLog) baseOperationLog).setDescription("单元测试数据");
        ((OperationLog) baseOperationLog).setOperationName("单元测试操作");
        ((OperationLog) baseOperationLog).setOperationUser("单元测试用户ID");
        ((OperationLog) baseOperationLog).setOperationType(OperationType.LOGIN.getValue());
        ((OperationLog) baseOperationLog).setOperationTime(new Timestamp(System.currentTimeMillis()));
        DataUtils.setBaseEntityField(((OperationLog) baseOperationLog), OperationType.CREATE);
        baseOperationLogCreated = operationLogService.createOperationLog(baseOperationLog);
        baseOperationLog = operationLogService.findOne(((OperationLog) baseOperationLogCreated).getOperationLogId(), OperationLog.class);
        Assert.assertTrue(baseOperationLog instanceof OperationLog);

        baseOperationLog = new MongoOperationLog();
        ((MongoOperationLog) baseOperationLog).setId(UUID.randomUUID().toString());
        ((MongoOperationLog) baseOperationLog).setDescription("单元测试数据");
        ((MongoOperationLog) baseOperationLog).setOperationName("单元测试操作");
        ((MongoOperationLog) baseOperationLog).setOperationUser("单元测试用户ID");
        ((MongoOperationLog) baseOperationLog).setOperationType(OperationType.LOGIN.getValue());
        ((MongoOperationLog) baseOperationLog).setOperationTime(new Timestamp(System.currentTimeMillis()));
        baseOperationLogCreated = operationLogService.createOperationLog(baseOperationLog);
        baseOperationLog = operationLogService.findOne(((MongoOperationLog) baseOperationLogCreated).getId(), MongoOperationLog.class);
        Assert.assertTrue(baseOperationLog instanceof MongoOperationLog);
    }

    @Test
    public void testFindByPage() {
        BaseOperationLog baseOperationLogCreated;

        BaseOperationLog baseOperationLog = new OperationLog();
        ((OperationLog) baseOperationLog).setDescription("单元测试数据");
        ((OperationLog) baseOperationLog).setOperationName("单元测试操作");
        ((OperationLog) baseOperationLog).setOperationUser("单元测试用户ID");
        ((OperationLog) baseOperationLog).setOperationType(OperationType.LOGIN.getValue());
        ((OperationLog) baseOperationLog).setOperationTime(new Timestamp(System.currentTimeMillis()));
        DataUtils.setBaseEntityField(((OperationLog) baseOperationLog), OperationType.CREATE);
        baseOperationLogCreated = operationLogService.createOperationLog(baseOperationLog);
        Page<? extends BaseOperationLog> operationLogPage = operationLogService.findByPage(baseOperationLogCreated);
        Assert.assertTrue(operationLogPage.getTotalElements() > 0);

        baseOperationLog = new MongoOperationLog();
        ((MongoOperationLog) baseOperationLog).setId(UUID.randomUUID().toString());
        ((MongoOperationLog) baseOperationLog).setDescription("单元测试数据");
        ((MongoOperationLog) baseOperationLog).setOperationName("单元测试操作");
        ((MongoOperationLog) baseOperationLog).setOperationUser("单元测试用户ID");
        ((MongoOperationLog) baseOperationLog).setOperationType(OperationType.LOGIN.getValue());
        ((MongoOperationLog) baseOperationLog).setOperationTime(new Timestamp(System.currentTimeMillis()));
        baseOperationLogCreated = operationLogService.createOperationLog(baseOperationLog);
        operationLogPage = operationLogService.findByPage(baseOperationLogCreated);
        Assert.assertTrue(operationLogPage.getTotalElements() > 0);
    }

    @After
    public void clear() {
        List<OperationLog> operationLogList = operationLogDao.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("operationName"), "单元测试操作"));
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        });
        operationLogDao.delete(operationLogList);
        LOGGER.debug("OperationLogServiceTest清理数据库数据成功！{}", ArrayUtils.toString(operationLogList));
        List<MongoOperationLog> mongoOperationLogList = operationLogMongoDao.findOperationLogsByOperationName("单元测试操作");
        operationLogMongoDao.delete(mongoOperationLogList);
        LOGGER.debug("OperationLogServiceTest清理mongodb数据成功！{}", ArrayUtils.toString(mongoOperationLogList));
    }
}
