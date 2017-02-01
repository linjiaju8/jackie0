package com.jackie0.common.constant;


import java.util.Locale;

/**
 * 自定义常量类
 * ClassName:Constant <br/>
 * Date:     2015年07月29日 20:25 <br/>
 *
 * @author jackie0
 * @see
 * @since JDK 1.8
 */
public class Constant {

    /**
     * 项目默认编码
     */
    public static final String DEF_ENC = "UTF-8";

    /**
     * 项目默认国际化地区
     */
    public static final Locale DEF_LOCALE = Locale.SIMPLIFIED_CHINESE;

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
    public static final String DEF_ARCHIVE_BASE_DATE_STR = "2099-01-01";

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

    /**
     * 默认根节点的父节点值，因为系统的主键只约定为36位，所以该值默认也为36位，避免char做主键会补充空格的情况
     */
    public static final String DEFAULT_PARENT_CODE = "000000000000000000000000000000000000";

    /**
     * common包对应的数据库schema
     */
    public static final String COMMON_SCHEMA = "jackie0";

    /**
     * session类缓存key的前缀部分，cache的统一命名规范{@link #SESSION_CACHE_PREFIX}或{@link #DATA_CACHE_PREFIX}+业务编码+自己的缓存的业务key
     */
    public static final String SESSION_CACHE_PREFIX = "session.cache";

    /**
     * 数据类缓存key的前缀部分，cache的统一命名规范{@link #SESSION_CACHE_PREFIX}或{@link #DATA_CACHE_PREFIX}+业务编码+自己的缓存的业务key
     */
    public static final String DATA_CACHE_PREFIX = "data.cache";

    /**
     * 基础的文件存储目录
     * 如果目录是形如：user.home形式则使用{@link System#getProperty(String)}获取真实路径
     * 也可以直接设置形如：/home绝对路径的目录形式
     */
    public static final String BASE_FILE_PATH = "user.home";

    private Constant() {
        // fix Utility classes should not have public constructors
    }
}
