package com.mlzj.component.monitor.server.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author yhl
 * @date 2020/4/21
 */
public class TaskPoolExecutor {

    private static final ThreadPoolExecutor threadPoolExecutor;

    static {
        threadPoolExecutor  = new ThreadPoolExecutor(32, 64, 10, TimeUnit.SECONDS, new LinkedBlockingDeque<>(), Executors.defaultThreadFactory());
    }

    private TaskPoolExecutor(){}

    public static ThreadPoolExecutor getThreadPool(){
        return threadPoolExecutor;
    }
}
