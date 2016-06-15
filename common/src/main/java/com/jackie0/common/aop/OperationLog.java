package com.jackie0.common.aop;


import com.jackie0.common.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 记录操作日志
 * ClassName:OperationLog <br/>
 * Date:     2015年08月14日 9:39 <br/>
 *
 * @author linjiaju
 * @see
 * @since JDK 1.8
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface OperationLog {
    /**
     * 该条日志要跳转的页面地址，如果为空则取首页地址
     * url生成规则，如果以http://开头，则返回url，否则会加上contextPath
     *
     * @return URL地址，需要完整的地址如:http://localhost:8080/
     */
    String url() default "";

    /**
     * 操作类型名称，参考枚举{@link OperationType#getValue()}
     *
     * @return 操作类型值
     */
    OperationType operationType();

    /**
     * 操作类型名称
     *
     * @return 操作类型值
     */
    String operationName() default "";

    /**
     * 异常时是否抛出异常终止程序，默认不抛异常，主要是看是不是把记录日志操作成功或失败放在事务中
     *
     * @return false 不抛异常，不影响业务方法的执行，true 记录日志操作异常时抛出异常
     */
    boolean abend() default false;

    /**
     * 旧业务数据数据在拦截方法中的下标，从0开始，-2无就数据
     * 如果是方法返回值用-1
     * 旧业务数据数据：查询条件/删除前/修改前/登录的数据
     *
     * @return 参数下标
     */
    int oldDataIndex() default 0;

    /**
     * @return 跳转历史日志操作页面时给页面传的旧业务数据的参数名称，默认是旧业务数据类名称的首字母小写
     */
    String oldDataParamName() default "";

    /**
     * 新业务数据数据在拦截方法中的下标，从0开始，-2无就数据
     * 如果是方法返回值用-1
     * 新业务数据数据：查询结果/新增后/修改后的数据
     *
     * @return 参数下标
     */
    int newDataIndex() default -1;

    /**
     * @return 跳转历史日志操作页面时给页面传的新业务数据的参数名称，默认是新业务数据类名称的首字母小写
     */
    String newDataParamName() default "";

    /**
     * 日志描述
     * 支持通配符{n.searchParam}，n>=0时表示取第N个参数的field属性的值,n=-1表示取返回值的field属性的值
     * 暂不支持多层嵌套
     *
     * @return 日志描述
     */
    String description() default "";
}
