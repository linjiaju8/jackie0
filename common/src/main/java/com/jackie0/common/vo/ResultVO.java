package com.jackie0.common.vo;

import java.io.Serializable;

/**
 * 结果VO，需要返回成功\失败操作结果时统一使用该VO
 *
 * @author jackie0
 * @since Java8
 * date 2016-06-15 16:17
 */
public class ResultVO<T extends Serializable> implements Serializable {
    private static final long serialVersionUID = 602682550615581963L;

    public static final String SUCCESS = "success";

    public static final String FAIL = "fail";

    /**
     * 错误编码
     */
    private String errorCode;

    /**
     * 错误描述
     */
    private String errorMsg;

    /**
     * 返回结果
     */
    private T result;


    public ResultVO() {
        // 无参构造参数
    }

    /**
     * 构造函数
     *
     * @param errorCode 初始化结果编码
     * @param errorMsg  初始化结果信息
     */

    public ResultVO(String errorCode, String errorMsg) {
        super();
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    /**
     * 构造函数
     *
     * @param errorCode 初始化结果编码
     * @param errorMsg  初始化结果信息
     * @param result    初始化结果对象
     */

    public ResultVO(String errorCode, String errorMsg, T result) {
        super();
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.result = result;
    }

    /**
     * 返回 result 的值
     *
     * @return result
     */
    public T getResult() {
        return result;
    }

    /**
     * 设置 result 的值
     *
     * @param result 结果对象
     */
    public void setResult(T result) {
        this.result = result;
    }

    /**
     * 返回 errorCode 的值
     *
     * @return errorCode
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * 设置 errorCode 的值
     *
     * @param errorCode 结果编码
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * 返回 errorMsg 的值
     *
     * @return errorMsg
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * 设置 errorMsg 的值
     *
     * @param errorMsg 　结果信息
     */
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString() {
        return "ResultVO{" +
                "errorCode='" + errorCode + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                ", result=" + result +
                '}';
    }
}
