package com.lianjia.matrix.common.command.registry;

import com.lianjia.matrix.common.command.registry.entity.Project;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author 程天亮
 * @Created
 */
public class Scheduled {

    private static ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    static final int DEFAULT_EXPIRE_REGISTRY_IN_SEC = 60;

    static final int DEFAULT_CIRCLE_TIME_IN_SEC = 30;

    static void schedue(Runnable runnable) {
        scheduledExecutorService.scheduleAtFixedRate(runnable,0,DEFAULT_CIRCLE_TIME_IN_SEC, TimeUnit.SECONDS);
    }
}
