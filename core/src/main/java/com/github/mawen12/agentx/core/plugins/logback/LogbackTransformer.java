package com.github.mawen12.agentx.core.plugins.logback;

import com.github.mawen12.agentx.api.interceptor.Interceptor;
import com.github.mawen12.agentx.core.agent.AbstractClassTransformer;
import com.github.mawen12.agentx.core.agent.ClassTransformer;
import com.github.mawen12.agentx.core.agent.MethodMatcherWrapper;
import com.github.mawen12.agentx.core.plugins.logback.interceptor.log.LogbackLogInterceptor;
import com.google.auto.service.AutoService;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static net.bytebuddy.matcher.ElementMatchers.*;

@AutoService(ClassTransformer.class)
public class LogbackTransformer extends AbstractClassTransformer {

    @Override
    protected String getAdviceKey() {
        return LogbackTransformer.class.getName();
    }

    @Override
    protected List<Interceptor> getInterceptors() {
        return Collections.singletonList(LogbackLogInterceptor.INSTANCE);
    }

    @Override
    public ElementMatcher.Junction<TypeDescription> getClassMatcher() {
        return named("ch.qos.logback.classic.Logger");
    }

    @Override
    public Set<MethodMatcherWrapper> getMethodMatchers() {
        return Collections.singleton(
                MethodMatcherWrapper.ofMethod(
                        named("callAppenders")
                                .and(takesArguments(1))
                                .and(takesArgument(0, named("ch.qos.logback.classic.spi.ILoggingEvent")))
                )
        );
    }
}
