package com.mlzj.component.mq.server.utils;

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
        threadPoolExecutor  = new ThreadPoolExecutor(128, 256, 10, TimeUnit.SECONDS, new LinkedBlockingDeque<>(), Executors.defaultThreadFactory());
    }

    private TaskPoolExecutor(){}

    public static ThreadPoolExecutor gerThreadPool(){
        return threadPoolExecutor;
    }
}
