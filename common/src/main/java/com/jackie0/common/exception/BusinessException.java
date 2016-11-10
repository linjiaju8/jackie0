package com.jackie0.common.exception;


import com.jackie0.common.utils.I18nUtils;

/**
 * 功能： 公用业务异常类
 *
 * @author jackie0
 * @version 1.0.0
 * @date 2014/12/30 18:05
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = -2320685094638126902L;

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 错误信息
     */
    private final String errorMsg;

    /**
     * 根据errorCode构造异常信息
     *
     * @param errorCode 对应国际化资源文件中的异常编码
     * @param args      参数，如：loginSuccess=用户{0}于{1}登录成功，可以传入用户名,当前系统时间进行参数绑定
     */
    public BusinessException(String errorCode, Object... args) {
        super(I18nUtils.getMessage("exception.tipsCode", errorCode) + I18nUtils.getMessage("exception.tipsMsg") + I18nUtils.getMessage(errorCode, args));
        this.errorCode = errorCode;
        errorMsg = I18nUtils.getMessage(errorCode, args);
    }

    /**
     * 根据errorCode及异常详细构造异常信息
     *
     * @param errorCode 对应国际化资源文件中的异常编码
     * @param cause     异常详细
     * @param args      参数，如：loginSuccess=用户{0}于{1}登录成功，可以传入用户名,当前系统时间进行参数绑定
     */
    public BusinessException(String errorCode, Throwable cause, Object... args) {
        super(I18nUtils.getMessage("exception.tipsCode", errorCode) + I18nUtils.getMessage("exception.tipsMsg") + I18nUtils.getMessage(errorCode, args), cause);
        this.errorCode = errorCode;
        errorMsg = I18nUtils.getMessage(errorCode, args);
    }

    public String getErrorCode() {
        return errorCode;
    }


    public String getErrorMsg() {
        return errorMsg;
    }
}
