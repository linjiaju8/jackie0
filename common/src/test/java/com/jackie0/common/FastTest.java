/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jackie0.common;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 快速测试类，相当于main函数
 *
 * @author jackie0
 */
public class FastTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(FastTest.class);

    @Test
    public void test() {
        LOGGER.info("test!");
    }
}
