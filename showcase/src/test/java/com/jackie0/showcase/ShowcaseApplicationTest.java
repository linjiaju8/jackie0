package com.jackie0.showcase;

import com.jackie0.common.constant.CommonExceptionCodeConstant;
import com.jackie0.common.utils.I18nUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

/**
 * showcase工程配置及应用相关测试
 *
 * @author jackie0
 * @since Java8
 * date 2016-06-26 16:03
 */
public class ShowcaseApplicationTest extends BaseSprintTestCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShowcaseApplicationTest.class);

    @Test
    public void testI18n() {
        String i18nMsg = I18nUtils.getMessage(CommonExceptionCodeConstant.TEST_ERROR, "参数1", "参数2");
        Assert.assertNotNull(i18nMsg);
        LOGGER.info("国际化测试成功，获取common包的默认国际化信息：{}", i18nMsg);
        i18nMsg = I18nUtils.getMessage(CommonExceptionCodeConstant.TEST_ERROR, Locale.US, "参数1", "参数2");
        Assert.assertNotNull(i18nMsg);
        LOGGER.info("国际化测试成功，获取common包的en_US国际化信息：{}", i18nMsg);
        Locale hkLocale = new Locale("zh", "HK");
        i18nMsg = I18nUtils.getMessage(CommonExceptionCodeConstant.TEST_ERROR, hkLocale, "参数1", "参数2");
        Assert.assertNotNull(i18nMsg);
        LOGGER.info("国际化测试成功，获取common包的zh_HK国际化信息：{}", i18nMsg);

        i18nMsg = I18nUtils.getMessage("jackie0.showcase.showcaseUser.userName.NotBlank");
        Assert.assertNotNull(i18nMsg);
        LOGGER.info("国际化测试成功，获取showcase包的默认国际化信息：{}", i18nMsg);

        i18nMsg = I18nUtils.getMessage("jackie0.showcase.showcaseUser.userName.NotBlank", Locale.US);
        Assert.assertNotNull(i18nMsg);
        LOGGER.info("国际化测试成功，获取showcase包的en_US国际化信息：{}", i18nMsg);

        i18nMsg = I18nUtils.getMessage("jackie0.showcase.showcaseUser.userName.NotBlank", hkLocale);
        Assert.assertNotNull(i18nMsg);
        LOGGER.info("国际化测试成功，获取showcase包的zh_HK国际化信息：{}", i18nMsg);
    }
}
