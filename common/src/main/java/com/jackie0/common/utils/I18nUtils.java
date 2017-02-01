/**
 * Copyright (C),Kingmed
 *
 * @FileName: SmsUtils.java
 * @Package: com.kingmed.ws.util
 * @Description: 短信处理工具类
 * @Author linjiaju
 * @Date 2015年11月30日 16:45
 * @History: //修改记录
 * 〈author〉      〈time〉      〈version〉       〈desc〉
 * 修改人姓名            修改时间            版本号              描述
 */
package com.jackie0.common.utils;

import com.jackie0.common.constant.Constant;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * 国际化资源获取工具类
 * ClassName:I18nUtils <br/>
 * Date:     2015年08月05日 10:44 <br/>
 *
 * @author jackie0
 * @see
 * @since JDK 1.8
 */
public class I18nUtils {
    private I18nUtils() {
    }

    /**
     * 获取国际化消息
     *
     * @param key  消息的键如 hello=你好 中的hello就是key
     * @param args 参数，如：loginSuccess=用户{0}于{1}登录成功，可以传入用户名,当前系统时间进行参数绑定
     * @return 返回匹配的国际化消息
     */
    public static String getMessage(String key, Object... args) {
        return getMessage(key, null, args);
    }

    public static String getMessage(String key, Locale locle, Object... args) {
        Locale thisLocale = locle;
        if (locle == null) {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes != null) {
                // web环境中直接从request中获取
                HttpServletRequest httpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
                thisLocale = RequestContextUtils.getLocale(httpServletRequest);
            } else {
                // 非web环境中使用系统默认locale
                thisLocale = Constant.DEF_LOCALE;
            }
        }
        ApplicationContext springContext = ApplicationContextUtils.getApplicationContext();
        return springContext.getMessage(key, args, null, thisLocale);
    }
}
