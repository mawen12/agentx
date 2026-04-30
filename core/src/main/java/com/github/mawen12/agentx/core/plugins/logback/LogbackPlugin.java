package com.github.mawen12.agentx.core.plugins.logback;

import com.github.mawen12.agentx.api.interceptor.Interceptor;
import com.github.mawen12.agentx.api.plugins.Plugin;
import com.github.mawen12.agentx.core.agent.AbstractPlugin;
import com.github.mawen12.agentx.core.agent.MethodMatcherWrapper;
import com.github.mawen12.agentx.core.plugins.logback.interceptor.log.LogbackLogInterceptor;
import com.google.auto.service.AutoService;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static net.bytebuddy.matcher.ElementMatchers.*;

@AutoService(Plugin.class)
public class LogbackPlugin extends AbstractPlugin {

    @Override
    public Domain domain() {
        return Domain.LOGBACK;
    }

    @Override
    public Component component() {
        return Component.LOG;
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
