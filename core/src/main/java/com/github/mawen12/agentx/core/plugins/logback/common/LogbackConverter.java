package com.github.mawen12.agentx.core.plugins.logback.common;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxy;
import com.github.mawen12.agentx.api.interceptor.MethodInfo;
import com.github.mawen12.agentx.api.log.AppLogData;
import com.github.mawen12.agentx.api.log.LogConverter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum LogbackConverter implements LogConverter {
    INSTANCE;

    @Override
    public AppLogData convert(MethodInfo methodInfo) {
        Object[] args = methodInfo.getArgs();
        if (args == null) {
            return null;
        }

        AppLogData.AppLogDataBuilder builder = AppLogData.builder();
        if (!(args[0] instanceof ILoggingEvent)) {
            return null;
        }

        ILoggingEvent loggingEvent = (ILoggingEvent) args[0];
        String logger = loggingEvent.getLoggerName();
        if (logger == null || logger.isEmpty()) {
            logger = "ROOT";
        }
        builder.level(logger);

        Level level = loggingEvent.getLevel();
        builder.level(level.toString());

        builder.threadName(Thread.currentThread().getName());
        builder.epochMillis(loggingEvent.getTimeStamp());

        builder.message(loggingEvent.getFormattedMessage());

        IThrowableProxy proxy = loggingEvent.getThrowableProxy();
        Throwable throwable = null;
        if (proxy instanceof ThrowableProxy) {
            throwable = ((ThrowableProxy) proxy).getThrowable();
        }
        if (throwable != null) {
            builder.throwable(throwable);
        }

        return builder.build();
    }
}
