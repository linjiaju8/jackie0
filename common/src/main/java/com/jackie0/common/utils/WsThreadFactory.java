package com.jackie0.common.utils;

import java.util.concurrent.ThreadFactory;

/**
 * WEB自助线程工厂
 *
 * @author linjj
 * @date 2016/04/12
 * </p>
 */
public class WsThreadFactory implements ThreadFactory {
    private int counter;

    public String getPrefix() {
        return prefix;
    }

    public int getCounter() {
        return counter;
    }

    private String prefix;

    public WsThreadFactory(String prefix) {
        this.prefix = prefix;
        this.counter = 0;
    }

    @Override
    public Thread newThread(Runnable r) {
        ++counter;
        return new Thread(r, prefix + "-" + counter);
    }
}
