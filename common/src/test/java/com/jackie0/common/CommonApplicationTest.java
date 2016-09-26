package com.jackie0.common;

import com.jackie0.common.constant.CommonExceptionCodeConstant;
import com.jackie0.common.utils.I18nUtils;
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

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * common工程配置及应用相关测试
 *
 * @author jackie0
 * @since Java8
 * date 2016-06-15 16:29
 */
public class CommonApplicationTest extends BaseSprintTestCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonApplicationTest.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MongoClient mongoClient;

    @Test
    public void testLog() {
        LOGGER.warn("测试warn级别日志打印！");
        LOGGER.trace("测试trace级别日志打印！");
        LOGGER.debug("测试debug级别日志打印！");
        LOGGER.info("测试info级别日志打印！");
        LOGGER.error("测试error级别日志打印！");
    }

    @Test
    public void testRedis() {
        redisTemplate.execute((RedisCallback) connection -> {
            connection.set("junitTestName".getBytes(), "junitTestName".getBytes());
            LOGGER.info("redis写数据测试，key：{}，value：{}", "junitTestName".getBytes(), "junitTestName".getBytes());
            return "junitTestName".getBytes();
        });

        redisTemplate.execute((RedisCallback) connection -> {
            byte[] bytes = connection.get("junitTestName".getBytes());
            LOGGER.info("redis读数据测试，读取数据：{}", new String(bytes));
            return bytes;
        });

        redisTemplate.execute((RedisCallback) connection -> {
            LOGGER.info("redis删数据测试，删之前数据value：{}", new String(connection.get("junitTestName".getBytes())));
            LOGGER.info("redis删数据测试，删数据key：{}", "junitTestName".getBytes());
            connection.del("junitTestName".getBytes());
            LOGGER.info("redis删数据测试，删之后数据value：{}", connection.get("junitTestName".getBytes()));
            return "junitTestName".getBytes();
        });
    }

    @Test
    public void testDataSource() {
        List<Map<String, Object>> resultList = jdbcTemplate.queryForList("select 1 from dual");
        LOGGER.info("数据源配置成功，执行测试SQL结果：{}", resultList.toString());
    }

    @Test
    public void testMongo() {
        MongoClientOptions mongoClientOptions = mongoClient.getMongoClientOptions();
        LOGGER.info("mongoDB配置成功，获取配置参数：{}", mongoClientOptions.toString());
    }

    @Test
    public void testI18n() {
        String i18nMsg = I18nUtils.getMessage(CommonExceptionCodeConstant.ERROR_TEST, "参数1", "参数2");
        Assert.assertNotNull(i18nMsg);
        LOGGER.info("国际化测试成功，获取的默认国际化信息：{}", i18nMsg);
        i18nMsg = I18nUtils.getMessage(CommonExceptionCodeConstant.ERROR_TEST, Locale.US, "参数1", "参数2");
        Assert.assertNotNull(i18nMsg);
        LOGGER.info("国际化测试成功，获取的en_US国际化信息：{}", i18nMsg);
        Locale hkLocale = new Locale("zh", "HK");
        i18nMsg = I18nUtils.getMessage(CommonExceptionCodeConstant.ERROR_TEST, hkLocale, "参数1", "参数2");
        Assert.assertNotNull(i18nMsg);
        LOGGER.info("国际化测试成功，获取的zh_HK国际化信息：{}", i18nMsg);
    }
}
