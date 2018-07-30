package com.jackie0.common.ctrl;

import com.jackie0.common.utils.CustomTimestampEditor;
import com.jackie0.common.utils.I18nUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ControllerAdvice
 * ClassName:ControllerAdvice
 * Date:     2015年08月06日 16:26
 *
 * @author jackie0
 * @see
 * @since JDK 1.8
 */
@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {
    /**
     * 修改了SpringMVC默认的List映射只支持256条的限制
     * 参数绑定器初始化，定义自己的编辑器，更好的支持数据类型转换
     *
     * @param binder 数据绑定器
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setAutoGrowCollectionLimit(5000); // SpringMVC默认数组类型参数映射只支持256条，不能满足系统需求

        SimpleDateFormat dateFormat = new SimpleDateFormat(I18nUtils.getMessage("jackie0.common.date.format"));
        dateFormat.setLenient(false);

        SimpleDateFormat datetimeFormat = new SimpleDateFormat(I18nUtils.getMessage("jackie0.common.timestamp.format"));
        datetimeFormat.setLenient(false);

        //自动转换日期类型的字段格式
        binder.registerCustomEditor(Date.class, new CustomTimestampEditor(new DateFormat[]{datetimeFormat, dateFormat}, true));
    }
}
