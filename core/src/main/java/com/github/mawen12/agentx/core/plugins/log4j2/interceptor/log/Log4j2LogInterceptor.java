package com.github.mawen12.agentx.core.plugins.log4j2.interceptor.log;

import com.github.mawen12.agentx.api.Agent;
import com.github.mawen12.agentx.api.context.Context;
import com.github.mawen12.agentx.api.interceptor.MethodInfo;
import com.github.mawen12.agentx.api.interceptor.NonReentrantInterceptor;
import com.github.mawen12.agentx.api.log.AppLogData;
import com.github.mawen12.agentx.api.log.LogConverter;
import com.github.mawen12.agentx.api.logging.Logger;
import com.github.mawen12.agentx.api.utils.AgentHelperClassLoader;
import com.google.common.collect.MapMaker;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentMap;

public class Log4j2LogInterceptor implements NonReentrantInterceptor {
    public static Log4j2LogInterceptor INSTANCE = new Log4j2LogInterceptor();

    private static final Logger LOGGER = Agent.getLogger(Log4j2LogInterceptor.class);

    private final ConcurrentMap<ClassLoader, LogConverter> converters = new MapMaker().weakKeys().makeMap();

    @Override
    public void init() {
        AgentHelperClassLoader.registerUrl(this.getClass());
    }

    @Override
    public void doBefore(MethodInfo methodInfo, Context ctx) {
        ClassLoader appClassLoader = methodInfo.getInvoker().getClass().getClassLoader();
        LogConverter log4jConverter = converters.get(appClassLoader);
        if (log4jConverter == null) {
            try {
                ClassLoader help = AgentHelperClassLoader.getClassLoader(appClassLoader, Agent.getAgentClassLoader());
                Class<?> clazz = help.loadClass("com.github.mawen12.agentx.core.plugins.log4j2.common.Log4jConverter");
//                log4jConverter = (Log4jConverter) clazz.getConstructor().newInstance();
                // 使用接口而非实现类转换，避免出现 java.lang.ClassCastException: com.github.mawen12.agentx.core.plugins.log4j2.common.Log4jConverter cannot be cast to com.github.mawen12.agentx.core.plugins.log4j2.common.Log4jConverter
                log4jConverter = (LogConverter) clazz.getConstructor().newInstance();
                converters.putIfAbsent(appClassLoader, log4jConverter);
            } catch (ClassNotFoundException | InvocationTargetException | InstantiationException |
                     IllegalAccessException | NoSuchMethodException e) {
                LOGGER.warn("load class com.github.mawen12.agentx.core.plugins.log4j2.common.LogConvert failed", e);
                return;
            }
        }

        AppLogData data = log4jConverter.convert(methodInfo);
        if (data != null) {
            LOGGER.info("app-log: Logger: {}, Thread: {}, Level: {}, Message: {}",
                    data.getLogger(), data.getThreadName(), data.getLevel(),
                    data.getMessage(), data.getThrowable());
        }
    }

    @Override
    public void doAfter(MethodInfo methodInfo, Context ctx) {
        // NOP
    }

    @Override
    public Signal signal() {
        return Signal.LOGGING;
    }
}
