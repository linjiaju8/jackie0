package com.jackie0.common.utils;

import com.google.code.kaptcha.Constants;
import com.jackie0.common.exception.BusinessException;
import com.jackie0.common.vo.ResultVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.hibernate.validator.HibernateValidatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 验证工具类
 *
 * @author linjj
 * @date 2015/03/02
 * </p>
 * Copyright © 2014	OP-MOBILE
 */
public class ValidatorUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidatorUtils.class);

    /**
     * 获取验证对象
     *
     * @param failFast failFast模式下，当第一个验证失败即终止验证返回结果
     * @return 验证对象
     */
    public static Validator getHibernateValidator(boolean failFast) {
        HibernateValidatorConfiguration configuration = Validation.byProvider(HibernateValidator.class).configure();
        ValidatorFactory factory = configuration.buildValidatorFactory();
        return factory.unwrap(HibernateValidatorFactory.class)
                .usingContext()
                .failFast(failFast)
                .getValidator();
    }

    /**
     * 使用HibernateValidator验证实体类数据
     * 如果验证失败该方法直接抛出{@link BusinessException}
     *
     * @param dataT 要验证的数据
     * @param <T>   要验证数据的泛型类型
     */
    public static <T> void validate(T dataT) throws BusinessException {
        Validator validator = ValidatorUtils.getHibernateValidator(true);
        Set<ConstraintViolation<T>> violations = validator.validate(dataT);
        if (CollectionUtils.isNotEmpty(violations)) {
            ConstraintViolation<T> constraintViolation = violations.iterator().next();
            // hibernate.validator无法自动获取国际化资源中定义的错误信息，因为国际化资源配置统一由平台配置，而平台并未把hibernate.validator的异常消息资源文件加入springmvc托管，还得手动通过I18nUtils获取
            throw new BusinessException("11", I18nUtils.getMessage(constraintViolation.getMessageTemplate().replaceAll("\\{", "").replaceAll("}", "")));
        }
    }

    /**
     * 使用HibernateValidator验证实体类数据
     * 改方法返回整个验证结果，可以根据参数failFast配置是否第一个验证失败即终止验证返回结果
     *
     * @param dataT    要验证的数据
     * @param failFast failFast模式下，当第一个验证失败即终止验证返回结果
     * @param <T>      要验证数据的泛型类型
     * @return 验证结果
     */
    public static <T> Set<ConstraintViolation<T>> validate(T dataT, boolean failFast) {
        Validator validator = ValidatorUtils.getHibernateValidator(failFast);
        return validator.validate(dataT);
    }

    /**
     * 使用HibernateValidator验证实体类数据
     * 改方法返回整个验证结果，可以根据参数failFast配置是否第一个验证失败即终止验证返回结果
     *
     * @param dataT    要验证的数据
     * @param failFast failFast模式下，当第一个验证失败即终止验证返回结果
     * @param <T>      要验证数据的泛型类型
     * @return 验证结果
     */
    public static <T> ResultVO validateData(T dataT, boolean failFast) {
        ResultVO validResult = new ResultVO();
        // 通过org.hibernate.validator验证实体参考对应实体注解验证规则
        Set<ConstraintViolation<T>> constraintViolations = ValidatorUtils.validate(dataT, failFast);
        if (CollectionUtils.isNotEmpty(constraintViolations)) {
            ConstraintViolation<T> dataConstraintViolation = constraintViolations.iterator().next();
            // hibernate.validator无法自动获取国际化资源中定义的错误信息，因为国际化资源配置统一由平台配置，而平台并未把hibernate.validator的异常消息资源文件加入springmvc托管，还得手动通过I18nUtils获取
            validResult.setErrorCode(ResultVO.FAIL);
            validResult.setErrorMsg(I18nUtils.getMessage(dataConstraintViolation.getMessageTemplate().replaceAll("\\{", "").replaceAll("}", "")));
            validResult.setResult(dataConstraintViolation.getPropertyPath());
            return validResult;
        } else {
            validResult.setErrorCode(ResultVO.SUCCESS);
            validResult.setErrorMsg(I18nUtils.getMessage("ws.validate.success"));
        }
        return validResult;
    }

    /**
     * 验证验证码
     *
     * @param httpServletRequest {@link HttpServletRequest}
     * @param kaptchaReceived    接收的验证码
     * @return true 验证通过，false 验证不通过
     */
    public static boolean validateCaptcha(HttpServletRequest httpServletRequest, String kaptchaReceived) {
        LOGGER.info("客户端接收的验证码--->{}", kaptchaReceived);
        String kaptchaExpected = (String) httpServletRequest.getSession()
                .getAttribute(Constants.KAPTCHA_SESSION_KEY);
        LOGGER.info("服务端保存的验证码--->{}", kaptchaExpected);
        return kaptchaReceived != null && kaptchaReceived.equalsIgnoreCase(kaptchaExpected);
    }

    /**
     * 密码验证
     *
     * @param password 要验证的密码
     * @return 密码强度，至少要满足强度2
     */
    public static int validatePassword(String password) {
        if (StringUtils.isBlank(password) || password.length() < 6) {
            return 0;
        }
        int ls = 0;
        Pattern lowerCasePattern = Pattern.compile("[a-z]+");
        Pattern upperCasePattern = Pattern.compile("[A-Z]+");
        Pattern numberCasePattern = Pattern.compile("[0-9]+");
        Pattern otherCasePattern = Pattern.compile("[^a-zA-Z0-9]+");
        Matcher passwordMatcher = lowerCasePattern.matcher(password);
        if (passwordMatcher.find()) {
            ls++;
        }
        passwordMatcher = upperCasePattern.matcher(password);
        if (passwordMatcher.find()) {
            ls++;
        }
        passwordMatcher = numberCasePattern.matcher(password);
        if (passwordMatcher.find()) {
            ls++;
        }
        passwordMatcher = otherCasePattern.matcher(password);
        if (passwordMatcher.find()) {
            ls++;
        }
        return ls;
    }
}
