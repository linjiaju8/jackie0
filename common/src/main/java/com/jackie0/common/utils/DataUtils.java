package com.jackie0.common.utils;

import com.jackie0.common.constant.Constant;
import com.jackie0.common.entity.BaseEntity;
import com.jackie0.common.enumeration.DeleteTag;
import com.jackie0.common.enumeration.OperationType;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Clob;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * 数据处理转换工具类
 * ClassName:DataUtils <br/>
 * Date:     2015年09月09日 21:31 <br/>
 *
 * @author jackie0
 * @since JDK 1.8
 */
public class DataUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataUtils.class);

    /**
     * 随机字符生成种子，去掉了容易混淆的一些字符如Oo0
     */
    private static final String RANDOM_CHAR_SEEDS = "123456789abcdefghijmnqrstuvwyABCDEFGHJLMNQRSTUVWY~!@#$%^&*()";

    /**
     * 随机数字生成种子
     */
    private static final String RANDOM_NUM_SEEDS = "0123456789";

    private DataUtils() {
    }

    /**
     * {@link Clob}值转换为String
     *
     * @param clobData 要转换的Clob值
     * @return 转换后的Clob值
     */
    public static String clob2String(Clob clobData) {
        StringBuilder stringBuilder = new StringBuilder();
        try (Reader reader = clobData.getCharacterStream()) {
            char[] buf = new char[1024];
            int len;
            while ((len = reader.read(buf)) != -1) {
                stringBuilder.append(buf, 0, len);
            }
        } catch (Exception e) {
            stringBuilder = null;
            LOGGER.info("DataUtils.clob2String(Clob clobData)异常-->{}", e);
        }
        return Objects.toString(stringBuilder, null);
    }

    /**
     * 对文件名做对应的编码处理，已兼容中文文件名
     *
     * @param fileName 要做编码处理的文件名
     * @return 编码后的文件名
     */
    public static String encodeFileName(String fileName, HttpServletRequest httpServletRequest) {
        String encodeFileName = fileName;
        String upperCaseUserAgent = httpServletRequest.getHeader("User-Agent").toUpperCase();
        if (upperCaseUserAgent.contains("MSIE") || upperCaseUserAgent.contains("LIKE GECKO")) {
            try {
                encodeFileName = URLEncoder.encode(fileName, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                LOGGER.error("DataUtils.encodeFileName(String fileName, HttpServletRequest httpServletRequest)编码文件名异常！{}", e);
            }
        } else {
            try {
                encodeFileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                LOGGER.error("DataUtils.encodeFileName(String fileName, HttpServletRequest httpServletRequest)编码文件名异常！{}", e);
            }
        }
        return encodeFileName;
    }

    /**
     * 根据操作类型自动设置{@link BaseEntity}值
     *
     * @param baseEntity    继承了{@link BaseEntity}实体对象
     * @param operationType 操作类型
     * @param <T>           实体类型
     */
    public static <T extends BaseEntity> void setBaseEntityField(T baseEntity, OperationType operationType) {
        setBaseEntityField(baseEntity, operationType, null);
    }

    /**
     * 根据操作类型自动设置{@link BaseEntity}值，可设置部分字段的默认值
     *
     * @param baseEntity        继承了{@link BaseEntity}实体对象
     * @param operationType     操作类型
     * @param defaultBaseEntity 默认值
     * @param <T>               实体类型
     */
    public static <T extends BaseEntity> void setBaseEntityField(T baseEntity, OperationType operationType, T defaultBaseEntity) {
        // TODO 操作时间及业务组织机构还未定
        String curUserId = "";
        Timestamp curTimestamp = new Timestamp(System.currentTimeMillis());
        baseEntity.setLastUpdatedBy(curUserId);
        baseEntity.setLastUpdateDate(curTimestamp);
        if (OperationType.CREATE == operationType) {
            baseEntity.setCreatedBy(curUserId);
            baseEntity.setCreationDate(curTimestamp);
            baseEntity.setArchiveBaseDate(new Timestamp(new DateTime(Constant.DEF_ARCHIVE_BASE_DATE_STR).getMillis()));
            baseEntity.setBizOrgCode("");
            baseEntity.setRecordVersion(new BigDecimal(1));
            baseEntity.setDeletedFlag(DeleteTag.IS_NOT_DELETED.getValue());
        } else if (OperationType.DELETE == operationType) {
            baseEntity.setDeletedFlag(DeleteTag.IS_DELETED.getValue());
            baseEntity.setDeletedBy(curUserId);
            baseEntity.setDeletedDate(curTimestamp);
        }
        if (defaultBaseEntity != null) {
            if (StringUtils.isNotBlank(defaultBaseEntity.getBizOrgCode())) {
                baseEntity.setBizOrgCode(defaultBaseEntity.getBizOrgCode());
            }
            if (StringUtils.isNotBlank(defaultBaseEntity.getCreatedBy())) {
                baseEntity.setCreatedBy(defaultBaseEntity.getCreatedBy());
            }
            if (StringUtils.isNotBlank(defaultBaseEntity.getLastUpdatedBy())) {
                baseEntity.setLastUpdatedBy(defaultBaseEntity.getLastUpdatedBy());
            }
            if (StringUtils.isNotBlank(defaultBaseEntity.getDeletedBy())) {
                baseEntity.setDeletedBy(defaultBaseEntity.getDeletedBy());
            }
            if (defaultBaseEntity.getArchiveBaseDate() != null) {
                baseEntity.setArchiveBaseDate(defaultBaseEntity.getArchiveBaseDate());
            }
            if (defaultBaseEntity.getRecordVersion() != null) {
                baseEntity.setRecordVersion(defaultBaseEntity.getRecordVersion());
            }
        }
    }

    /**
     * 根据种子{@link DataUtils#RANDOM_CHAR_SEEDS}获取指定位数的随机字符
     *
     * @param num 要获取的随机字符位数
     * @return 指定位数的随机字符字符串
     */
    public static String getRandomChars(int num) {
        StringBuilder randomChars = new StringBuilder();
        for (int i = 0; i < num; i++) {
            randomChars.append(RANDOM_CHAR_SEEDS.charAt(new Random().nextInt(RANDOM_CHAR_SEEDS.length())));
        }
        return StringUtils.isBlank(randomChars) ? null : randomChars.toString();
    }

    /**
     * 根据种子{@link DataUtils#RANDOM_NUM_SEEDS}获取指定位数的随机数字，并以字符串形式返回
     *
     * @param num 要获取的随机数字位数
     * @return 指定位数的随机数字字符串
     */
    public static String getRandomNumbers(int num) {
        StringBuilder randomChars = new StringBuilder();
        for (int i = 0; i < num; i++) {
            randomChars.append(RANDOM_NUM_SEEDS.charAt(new Random().nextInt(RANDOM_NUM_SEEDS.length())));
        }
        return StringUtils.isBlank(randomChars) ? null : randomChars.toString();
    }

    /**
     * 从bean类型对象中获取指定的属性值
     *
     * @param object       实体类型对象
     * @param propertyName 要获取值的属性名称
     * @param clazz        要获取值的类类型
     * @param <T>          要获取值的泛型类型
     * @return 指定的属性值
     */
    public static <T> T getField(Object object, String propertyName, Class<T> clazz) {
        Field field = ReflectionUtils.findField(object.getClass(), propertyName, clazz);
        T result = null;
        if (field != null) {
            field.setAccessible(Boolean.TRUE);
            Object resultObject = ReflectionUtils.getField(field, object);
            result = clazz.cast(resultObject);
        }
        return result;
    }

    /**
     * 从bean或Map类型对象中获取指定的属性值
     * 属性可用a.b.c形式的表达式嵌套,不支持集合类及数组
     *
     * @param object                 需要获取属性值的对象
     * @param propertyNameExpression 属性表达式
     * @param clazz                  获取的值类型
     * @param <T>                    要获取值的泛型类型
     * @return 指定的属性值
     */
    public static <T> T getFieldByExpression(Object object, String propertyNameExpression, Class<T> clazz) {
        String[] propertyNameExpressions = propertyNameExpression.split("\\.");
        Object currentObject = object;
        for (String propertyName : propertyNameExpressions) {
            if (StringUtils.isBlank(propertyName)) {
                continue;
            }
            if (object instanceof Map) {
                currentObject = ((Map) currentObject).get(propertyName);
            } else {
                currentObject = getField(currentObject, propertyName, Object.class);
            }

        }
        return clazz.cast(currentObject);
    }
}
