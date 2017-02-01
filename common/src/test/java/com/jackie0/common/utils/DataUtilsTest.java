package com.jackie0.common.utils;

import com.jackie0.common.BaseSprintTestCase;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据处理转换工具测试类
 * ClassName:DataUtils <br/>
 * Date:     2015年09月09日 21:31 <br/>
 *
 * @author jackie0
 * @since JDK 1.8
 */
public class DataUtilsTest extends BaseSprintTestCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataUtilsTest.class);

    @Test
    public void testGetRandomNumbers() {
        String _10SCharsString = DataUtils.getRandomChars(10);
        LOGGER.info("DataUtils.getRandomChars生成的随机字符串为：{}",_10SCharsString);
        Assert.assertTrue(_10SCharsString != null && _10SCharsString.length() == 10);
    }
}
