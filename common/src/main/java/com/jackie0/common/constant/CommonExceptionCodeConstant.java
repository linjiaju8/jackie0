package com.jackie0.common.constant;

/**
 * 通用异常编码常量类
 *
 * @author jackie0
 * @since Java8
 * date 2016-07-06 17:45
 */
public class CommonExceptionCodeConstant {
    /**
     * 测试异常
     */
    public static final String TEST_ERROR = "COMMON_ERROR_0";

    /**
     * 404找不到页面
     */
    public static final String ERROR_404 = "COMMON_ERROR_1";

    /**
     * 500服务器内部错误
     */
    public static final String ERROR_500 = "COMMON_ERROR_2";

    /**
     * 权限不足
     */
    public static final String INSUFFICIENT_PERMISSIONS_ERROR = "COMMON_ERROR_3";

    /**
     * 未知异常
     */
    public static final String UNKNOW_ERROR = "COMMON_ERROR_4";

    /**
     * 数据验证异常
     */
    public static final String DATA_VALIDATION_ERROR = "COMMON_ERROR_5";

    /**
     * 创建Excel工作簿异常
     */
    public static final String CREATE_EXCEL_WORKBOOK_ERROR = "COMMON_ERROR_6";

    /**
     * 读取Excel数据异常，无法创建数据对象
     */
    public static final String READ_EXCEL_DATA_ERROR = "COMMON_ERROR_7";

    /**
     * 读取Excel数据异常，无法给数据对象赋值
     */
    public static final String SET_EXCEL_DATA_ERROR = "COMMON_ERROR_8";

    /**
     * 导出数据到Excel异常，要导出的数据为空
     */
    public static final String EXPORT_EXCEL_DATA_NULL_ERROR = "COMMON_ERROR_9";

    private CommonExceptionCodeConstant() {
    }
}
