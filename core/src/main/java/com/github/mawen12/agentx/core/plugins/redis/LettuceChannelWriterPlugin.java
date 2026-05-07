package com.github.mawen12.agentx.core.plugins.redis;

import com.github.mawen12.agentx.api.interceptor.Interceptor;
import com.github.mawen12.agentx.api.plugins.Plugin;
import com.github.mawen12.agentx.core.agent.AbstractPlugin;
import com.github.mawen12.agentx.core.agent.MethodMatcherWrapper;
import com.github.mawen12.agentx.core.plugins.redis.interceptor.metric.LettuceMetricInterceptor;
import com.google.auto.service.AutoService;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.github.mawen12.agentx.core.agent.MethodMatcherWrapper.ofMethod;
import static net.bytebuddy.matcher.ElementMatchers.*;

@AutoService(Plugin.class)
public class LettuceChannelWriterPlugin extends AbstractPlugin {

    @Override
    protected List<Interceptor> getInterceptors() {
        return Collections.singletonList(LettuceMetricInterceptor.INSTANCE);
    }

    @Override
    public ElementMatcher.Junction<TypeDescription> getClassMatcher() {
        return hasSuperType(named("io.lettuce.core.RedisChannelWriter"))
                .and(not(isInterface()));
    }

    @Override
    public Set<MethodMatcherWrapper> getMethodMatchers() {
        return Collections.singleton(
                ofMethod(named("write")
                        .and(takesArguments(1))
                        .and(isPublic())
                        .and(not(returns(named("void")))))
        );
    }

    @Override
    public Domain domain() {
        return Domain.LETTUCE_REDIS;
    }

    @Override
    public Component component() {
        return Component.CACHE;
    }
}
