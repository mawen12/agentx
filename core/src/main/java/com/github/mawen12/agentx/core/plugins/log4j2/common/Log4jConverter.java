package com.github.mawen12.agentx.core.plugins.log4j2.common;

import com.github.mawen12.agentx.api.interceptor.MethodInfo;
import com.github.mawen12.agentx.api.log.AppLogData;
import com.github.mawen12.agentx.api.log.LogConverter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.Message;

public class Log4jConverter implements LogConverter {

    public AppLogData convert(MethodInfo methodInfo) {
        Object[] args = methodInfo.getArgs();
        if (args == null) {
            return null;
        }

        AppLogData.AppLogDataBuilder builder = AppLogData.builder();
        Logger logger = (Logger) methodInfo.getInvoker();
        if (logger.getName() == null || logger.getName().isEmpty()) {
            builder.logger("ROOT");
        } else {
            builder.logger(logger.getName());
        }

        Level level = (Level) methodInfo.getArgs()[0];
        builder.level(level.name());

        builder.threadName(Thread.currentThread().getName());
        builder.epochMillis(System.currentTimeMillis());

        Message message = (Message) methodInfo.getArgs()[4];
        builder.message(message.getFormattedMessage());

        Throwable throwable = (Throwable) methodInfo.getArgs()[5];
        if (throwable != null) {
            builder.throwable(throwable);
        }


        return builder.build();
    }
}
