package com.github.mawen12.agentx.api.logging;

public interface LoggerFactory {
    LoggerFactory NOOP = NoOp.INSTANCE;

    Logger getLogger(String name);

    default Logger getLogger(Class<?> clazz) {
        return getLogger(clazz.getCanonicalName());
    }

    enum NoOp implements LoggerFactory {
        INSTANCE;

        @Override
        public Logger getLogger(String name) {
            return Logger.NOOP_SUPPLIER.apply(name);
        }
    }
}
