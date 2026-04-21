package com.github.mawen12.agentx.api.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ScheduleHelper {
    public static final ScheduleHelper INSTANCE = new ScheduleHelper();

    private final ThreadFactory threadFactory = new AgentThreadFactory();

    public void execute(int initDelay, int delay, Runnable command) {
        Executors.newSingleThreadScheduledExecutor(threadFactory)
                .scheduleWithFixedDelay(command, initDelay, delay, TimeUnit.SECONDS);
    }
}
