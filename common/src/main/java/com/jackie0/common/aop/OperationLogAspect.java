package com.jackie0.common.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jackie0.common.mongo.entity.MongoOperationLog;
import com.jackie0.common.service.OperationLogService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 记录客户操作日志切面
 * ClassName:CustLogAspect <br/>
 * Date:     2015年08月14日 9:39 <br/>
 *
 * @author jackie0
 * @see
 * @since JDK 1.8
 */
@Aspect
@Component
public class OperationLogAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(OperationLogAspect.class);

    @Autowired
    private OperationLogService operationLogService;

    /**
     * 定义有{@link OperationLog}注解的方法的切入点，该方法只是一个标识
     *
     * @param operationLog {@link OperationLog} 注解
     */
    @Pointcut("within(com.jackie0.*) && @annotation(operationLog)")
    public void operationLogMeasuringPointcut(OperationLog operationLog) {
        // 无业务逻辑，该方法起标记作用
    }

    /**
     * 在方法正确返回值之后被执行
     *
     * @param jp           包含了切入点信息的对象
     * @param operationLog {@link OperationLog} 注解
     */
    @AfterReturning(value = "operationLogMeasuringPointcut(operationLog)", argNames = "jp,operationLog,retVal", returning = "retVal")
    public void logCustLog(JoinPoint jp, OperationLog operationLog, Object retVal) throws JsonProcessingException {
        Object[] parames = jp.getArgs();//获取目标方法体参数
        String className = jp.getTarget().getClass().toString();//获取目标类名
        String signature = jp.getSignature().toString();//获取目标方法签名
        MongoOperationLog operationLogEntity = new MongoOperationLog();
        operationLogEntity.setOperationTime(new Timestamp(System.currentTimeMillis()));
        operationLogEntity.setOperationUser("未知用户"); // TODO 权限模块未完成,此处要改成当前操作人
        operationLogEntity.setOperationType(operationLog.operationType().getValue());
        operationLogEntity.setOperationName(operationLog.operationName());
        operationLogEntity.setDescription(buildDescription(operationLog.description(), parames, retVal));

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            setOldDataParam(operationLog, retVal, parames, operationLogEntity, objectMapper);
            setNewDataParam(operationLog, retVal, parames, operationLogEntity, objectMapper);
        } catch (RuntimeException e) {
            if (operationLog.abend()) {
                throw e;
            } else {
                LOGGER.error("记录客户登录日志异常：{}但是参数operationLog.abend()为true，该异常将被捕获！", e.getMessage());
            }
        }
        try {
            operationLogEntity.setUrl(operationLog.url());
            operationLogService.createOperationLog(operationLogEntity);
            LOGGER.debug("类：{}中的方法：{}配置客户日志注解：{}，执行记录日志操作成功！", className, signature, OperationLog.class.getName());
        } catch (Exception e) {
            if (operationLog.abend()) {
                throw e;
            } else {
                LOGGER.error("记录客户登录日志异常：{}但是参数operationLog.abend()为true，该异常将被捕获！", e.getMessage());
            }
        }
    }

    private void setNewDataParam(OperationLog operationLog, Object retVal, Object[] parames, MongoOperationLog operationLogEntity, ObjectMapper objectMapper) throws JsonProcessingException {
        Object newData = null;
        if (operationLog.newDataIndex() >= 0) {
            newData = parames[operationLog.newDataIndex()];
        } else if (operationLog.newDataIndex() == -1) {
            newData = retVal;
        }
        if (newData != null) {
            operationLogEntity.setNewJsonData(objectMapper.writeValueAsString(newData));
            String newDataClassPath = newData.getClass().getName();
            operationLogEntity.setNewDataClass(newDataClassPath);
            String newDataParamName = getParamName(newDataClassPath, operationLog.newDataParamName());
            operationLogEntity.setNewDataParamName(newDataParamName);
        }
    }

    private void setOldDataParam(OperationLog operationLog, Object retVal, Object[] parames, MongoOperationLog operationLogEntity, ObjectMapper objectMapper) throws JsonProcessingException {
        Object oldData = null;
        if (operationLog.oldDataIndex() >= 0) {
            oldData = parames[operationLog.oldDataIndex()];
        } else if (operationLog.oldDataIndex() == -1) {
            oldData = retVal;
        }
        if (oldData != null) {
            operationLogEntity.setOldJsonData(objectMapper.writeValueAsString(oldData));
            String oldDataClassPath = oldData.getClass().getName();
            operationLogEntity.setOldDataClass(oldDataClassPath);
            String oldDataParamName = getParamName(oldDataClassPath, operationLog.oldDataParamName());
            operationLogEntity.setOldDataParamName(oldDataParamName);
        }
    }

    private String getParamName(String dataClassPath, String dataParamName) {
        String newDataParamName = dataParamName;
        if (StringUtils.isBlank(dataClassPath)) {
            String[] oldDataClassNames = dataClassPath.split("\\.");
            String oldDataClassName = ArrayUtils.isNotEmpty(oldDataClassNames) ? oldDataClassNames[oldDataClassNames.length - 1] : "";
            String firstString = oldDataClassName.substring(0, 1);
            newDataParamName = oldDataClassName.replaceFirst(firstString, firstString.toLowerCase());
        }
        return newDataParamName;
    }

    /**
     * 生成日志描述
     *
     * @param description 原始描述
     * @param parameters  AOP拦截方法参数
     * @param retVal      AOP拦截方法执行返回值
     * @return 日志描述
     */
    private static String buildDescription(String description, Object[] parameters, Object retVal) {
        String newDescription = description;
        Pattern pattern = Pattern.compile("(\\{(\\d|-\\d)\\.(\\w+)})"); // 匹配形如{0.name}的通配符
        boolean isMatched = true;
        while (isMatched) {
            Matcher matcher = pattern.matcher(newDescription);
            isMatched = matcher.find();
            if (isMatched) {
                // 正则表达式："(\\{(\\d|-\\d)\\.(\\w+)})"，把形如{0.name}的匹配字符串分为了三组：group(1):{0.name}、group(2):0、group(3):name
                String parameterIndexStr = matcher.group(2);
                String parameterName = matcher.group(3);
                String parameterValue = getDescriptionParameterValue(parameters, retVal, parameterIndexStr, parameterName);
                newDescription = matcher.replaceFirst(parameterValue);
            }
        }
        return newDescription;
    }

    private static String getDescriptionParameterValue(Object[] parameters, Object retVal, String parameterIndexStr, String parameterName) {
        Object parameterObject;
        String parameterValue;
        if ("-1".equals(parameterIndexStr)) {
            parameterObject = retVal;
        } else {
            parameterObject = parameters[NumberUtils.toInt(parameterIndexStr)];
        }
        if (parameterObject instanceof Map) {
            parameterValue = Objects.toString(((Map) parameterObject).get(parameterName), "");
        } else {
            Field field = ReflectionUtils.findField(parameterObject.getClass(), parameterName);
            field.setAccessible(true);
            parameterValue = Objects.toString(ReflectionUtils.getField(field, parameterObject), "");
        }
        return parameterValue;
    }
}
