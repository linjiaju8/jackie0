package com.jackie0.common.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jackie0.common.constant.Constant;
import com.jackie0.common.mongo.entity.MongoOperationLog;
import com.jackie0.common.service.OperationLogService;
import com.jackie0.common.utils.UrlUtils;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    private Logger LOGGER = LoggerFactory.getLogger(OperationLogAspect.class);

    @Autowired
    private OperationLogService operationLogService;

    /**
     * 定义有{@link OperationLog}注解的方法的切入点，该方法只是一个标识
     *
     * @param operationLog {@link OperationLog} 注解
     */
    @Pointcut("within(com.jackie0.*) && @annotation(operationLog)")
    public void operationLogMeasuringPointcut(OperationLog operationLog) {
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
                String oldDataParamName = operationLog.oldDataParamName();
                oldDataParamName = getParamName(oldDataClassPath, oldDataParamName);
                operationLogEntity.setOldDataParamName(oldDataParamName);
            }
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
                String newDataParamName = operationLog.newDataParamName();
                newDataParamName = getParamName(newDataClassPath, newDataParamName);
                operationLogEntity.setNewDataParamName(newDataParamName);
            }
        } catch (RuntimeException e) {
            if (operationLog.abend()) {
                throw e;
            } else {
                LOGGER.error("记录客户登录日志异常：{}但是参数operationLog.abend()为true，该异常将被捕获！", e.getMessage());
            }
        }
        try {
            HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String url = StringUtils.isBlank(operationLog.url()) ? UrlUtils.getCurUrl(httpServletRequest) : operationLog.url(); // TODO 如果没有url应该是首页地址，首页地址需要读取系统参数的，区分子系统
            url = url.startsWith(Constant.HTTP_PREFIX) || url.startsWith(Constant.HTTPS_PREFIX) ? url : (UrlUtils.getCurUrl(httpServletRequest) + url);
            operationLogEntity.setUrl(url);
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

    private String getParamName(String dataClassPath, String dataParamName) {
        if (StringUtils.isBlank(dataClassPath)) {
            String[] oldDataClassNames = dataClassPath.split("\\.");
            String oldDataClassName = ArrayUtils.isNotEmpty(oldDataClassNames) ? oldDataClassNames[oldDataClassNames.length - 1] : "";
            String firstString = oldDataClassName.substring(0, 1);
            dataParamName = oldDataClassName.replaceFirst(firstString, firstString.toLowerCase());
        }
        return dataParamName;
    }

    /**
     * 生成日志描述
     *
     * @param description 原始描述
     * @param parames     AOP拦截方法参数
     * @param retVal      AOP拦截方法执行返回值
     * @return 日志描述
     */
    private String buildDescription(String description, Object[] parames, Object retVal) {
        String newDescription = "";
        if (StringUtils.isNotBlank(description)) {
            newDescription = description;
            String[] descriptionLeftArray = description.split("\\{");
            List<String> regexs = new ArrayList<>();
            // 查找通配符中{参数下标.属性}字符串
            if (ArrayUtils.isNotEmpty(descriptionLeftArray)) {
                for (String descriptionLeft : descriptionLeftArray) {
                    if (descriptionLeft.contains("}")) {
                        regexs.add(descriptionLeft.split("}")[0]);
                    }
                }
            }

            // 替换通配符的值
            for (String regex : regexs) {
                String[] paramIndexAndObj = regex.split("\\.");
                if (ArrayUtils.isNotEmpty(paramIndexAndObj) && paramIndexAndObj.length == 2) {
                    if (NumberUtils.isNumber(paramIndexAndObj[0])) {
                        Object paramData = null;
                        if (NumberUtils.toInt(paramIndexAndObj[0]) >= 0) {
                            paramData = parames[NumberUtils.toInt(paramIndexAndObj[0])];
                        } else if (NumberUtils.toInt(paramIndexAndObj[0]) == -1) {
                            paramData = retVal;
                        }
                        if (paramData != null) {
                            Object paramVal;
                            if (paramData instanceof Map) {
                                paramVal = ((Map) paramData).get(paramIndexAndObj[1]);
                            } else {
                                Field field = ReflectionUtils.findField(paramData.getClass(), paramIndexAndObj[1]);
                                paramVal = ReflectionUtils.getField(field, paramData);
                            }
                            newDescription = newDescription.replace("{" + regex + "}", Objects.toString(paramVal, ""));
                        }
                    }
                }
            }
        }
        return newDescription;
    }
}
