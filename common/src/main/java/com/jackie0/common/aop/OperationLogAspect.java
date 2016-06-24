package com.jackie0.common.aop;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 记录客户操作日志切面
 * ClassName:CustLogAspect <br/>
 * Date:     2015年08月14日 9:39 <br/>
 *
 * @author linjiaju
 * @see
 * @since JDK 1.7
 */
@Aspect
@Component
public class OperationLogAspect {
    Logger LOGGER = LoggerFactory.getLogger(OperationLogAspect.class);

    @Resource
    private CustLogService custLogService;

    /**
     * 定义有{@link CustLog}注解的方法的切入点，该方法只是一个标识
     *
     * @param custLog {@link CustLog} 注解
     */
    @Pointcut("within(com.kingmed..*) && @annotation(custLog)")
    public void custLogMeasuringPointcut(CustLog custLog) {
    }

    /**
     * 在方法正确返回值之后被执行
     *
     * @param jp      包含了切入点信息的对象
     * @param custLog {@link CustLog} 注解
     */
    @AfterReturning(value = "custLogMeasuringPointcut(custLog)", argNames = "jp,custLog,retVal", returning = "retVal")
    public void logCustLog(JoinPoint jp, CustLog custLog, Object retVal) {
        Object[] parames = jp.getArgs();//获取目标方法体参数
        String className = jp.getTarget().getClass().toString();//获取目标类名
        String signature = jp.getSignature().toString();//获取目标方法签名
        com.kingmed.ws.custservice.entity.CustLog custLogEntity = new com.kingmed.ws.custservice.entity.CustLog();
        custLogEntity.setOperationTime(new Timestamp(System.currentTimeMillis()));
        custLogEntity.setOperationUser(Permission.getCurrentUserId("未知用户"));
        custLogEntity.setOperationType(custLog.operationType().getValue());
        custLogEntity.setOperationName(custLog.operationName());
        custLogEntity.setDescription(buildDescription(custLog.description(), parames, retVal));

        try {
            Object oldData = null;
            if (custLog.oldDataIndex() >= 0) {
                oldData = parames[custLog.oldDataIndex()];
            } else if (custLog.oldDataIndex() == -1) {
                oldData = retVal;
            }
            if (oldData != null) {
                custLogEntity.setOldJsonData(JsonUtil.javaObjToJson(oldData));
                String oldDataClassPath = oldData.getClass().getName();
                custLogEntity.setOldDataClass(oldDataClassPath);
                String oldDataParamName = custLog.oldDataParamName();
                if (StringUtils.isBlank(oldDataParamName)) {
                    String[] oldDataClassNames = oldDataClassPath.split("\\.");
                    String oldDataClassName = ArrayUtils.isNotEmpty(oldDataClassNames) ? oldDataClassNames[oldDataClassNames.length - 1] : "";
                    String firstString = oldDataClassName.substring(0, 1);
                    oldDataParamName = oldDataClassName.replaceFirst(firstString, firstString.toLowerCase());
                }
                custLogEntity.setOldDataParamName(oldDataParamName);
            }
            Object newData = null;
            if (custLog.newDataIndex() >= 0) {
                newData = parames[custLog.newDataIndex()];
            } else if (custLog.newDataIndex() == -1) {
                newData = retVal;
            }
            if (newData != null) {
                custLogEntity.setNewJsonData(JsonUtil.javaObjToJson(newData));
                String newDataClassPath = newData.getClass().getName();
                custLogEntity.setNewDataClass(newDataClassPath);
                String newDataParamName = custLog.newDataParamName();
                if (StringUtils.isBlank(newDataParamName)) {
                    String[] newDataClassNames = newDataClassPath.split("\\.");
                    String newDataClassName = ArrayUtils.isNotEmpty(newDataClassNames) ? newDataClassNames[newDataClassNames.length - 1] : "";
                    String firstString = newDataClassName.substring(0, 1);
                    newDataParamName = newDataClassName.replaceFirst(firstString, firstString.toLowerCase());
                }
                custLogEntity.setNewDataParamName(newDataParamName);
            }
        } catch (Exception e) {
            if (custLog.abend()) {
                throw e;
            } else {
                LOGGER.error("记录客户登录日志异常：{}", e.getMessage());
            }
        }
        try {
            String url = StringUtils.isBlank(custLog.url()) ? Constant.getProperties("indexUrl") : custLog.url();
            HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            url = url.startsWith(Constant.HTTP_PREFIX) || url.startsWith(Constant.HTTPS_PREFIX) ? url : (UrlUtils.getCurUrl(httpServletRequest) + url);
            custLogEntity.setUrl(url);
            custLogService.createCustLog(custLogEntity);
            LOGGER.debug("类：{}中的方法：{}配置客户日志注解：{}，执行记录日志操作成功！", className, signature, CustLog.class.getName());
        } catch (Exception e) {
            if (custLog.abend()) {
                throw e;
            } else {
                LOGGER.error("记录客户登录日志异常：{}", e.getMessage());
            }
        }
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
                            Object paramVal = ReflectUtil.getFieldValue(paramData, paramIndexAndObj[1]);
                            newDescription = newDescription.replace("{" + regex + "}", Objects.toString(paramVal, ""));
                        }
                    }
                }
            }
        }
        return newDescription;
    }
}
