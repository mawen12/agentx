package com.github.mawen12.agentx.api.log;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppLogData {
    private final String logger;
    private final String threadName;
    private final String level;
    private final long epochMillis;
    private final String message;
    private final Throwable throwable;
    private final String agentSource;
}
