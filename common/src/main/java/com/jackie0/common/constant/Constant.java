package com.jackie0.common.constant;


import java.sql.Timestamp;
import java.util.GregorianCalendar;

/**
 * 自定义常量类
 * ClassName:Constant <br/>
 * Date:     2015年07月29日 20:25 <br/>
 *
 * @author linjiaju
 * @see
 * @since JDK 1.8
 */
public class Constant {
    /**
     * 项目默认编码
     */
    public static final String DEF_ENC = "UTF-8";

    /**
     * http前缀
     */
    public static final String HTTP_PREFIX = "http://";

    /**
     * https前缀
     */
    public static final String HTTPS_PREFIX = "https://";

    /**
     * 系统数据库归档时间字段默认归档时间
     */
    public static final Timestamp DEF_ARCHIVE_BASE_DATE = new Timestamp(new GregorianCalendar(2099, 1, 1).getTime().getTime());

    /**
     * web自助密码验证的最低安全级别
     * 0：无要求
     * 1：至少8位字母、数字或字符
     * 2：至少8位小写字母、大写字母、数字或字符任意两个的组合
     * 3：以此类推任意3个组合
     * 4：以此类推任意4个组合
     */
    public static final int PASSWORD_MINIMUM_SECURITY_LEVEL = 2;

    /**
     * 一次in条件元素的最高值，如果高于此值，则做分组多次查询
     */
    public static final int MAX_IN_PARAM_COUNT = 500;
}
