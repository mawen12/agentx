package com.github.mawen12.agentx.core.plugins.springboot;

import com.github.mawen12.agentx.api.interceptor.Interceptor;
import com.github.mawen12.agentx.api.plugins.Plugin;
import com.github.mawen12.agentx.api.utils.Lists;
import com.github.mawen12.agentx.api.utils.Sets;
import com.github.mawen12.agentx.core.agent.AbstractPlugin;
import com.github.mawen12.agentx.core.agent.MethodMatcherWrapper;
import com.github.mawen12.agentx.core.plugins.springboot.interceptor.SpringBootReadyPrepareInterceptor;
import com.google.auto.service.AutoService;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.List;
import java.util.Set;

import static net.bytebuddy.matcher.ElementMatchers.*;

@AutoService(Plugin.class)
public class SpringBootReadyEventPlugin extends AbstractPlugin {

    @Override
    public Domain domain() {
        return Domain.SPRING_BOOT;
    }

    @Override
    public Component component() {
        return Component.READ_EVENT;
    }

    @Override
    protected List<Interceptor> getInterceptors() {
        return Lists.of(SpringBootReadyPrepareInterceptor.INSTANCE);
    }

    @Override
    public ElementMatcher.Junction<TypeDescription> getClassMatcher() {
        return hasSuperType(named("org.springframework.context.ApplicationEventPublisher"));
    }

    @Override
    public Set<MethodMatcherWrapper> getMethodMatchers() {
        return Sets.of(
                MethodMatcherWrapper.ofMethod(
                        named("publishEvent").and(takesArguments(1))
                )
        );
    }
}
