package com.github.mawen12.agentx.core.plugins.tomcat;

import com.github.mawen12.agentx.api.interceptor.Interceptor;
import com.github.mawen12.agentx.core.agent.AbstractClassTransformer;
import com.github.mawen12.agentx.core.agent.ClassTransformer;
import com.github.mawen12.agentx.core.agent.MethodMatcherWrapper;
import com.github.mawen12.agentx.core.plugins.tomcat.interceptor.prepare.EndpointPrepareInterceptor;
import com.google.auto.service.AutoService;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.takesNoArguments;

@AutoService(ClassTransformer.class)
public class TomcatEndpointTransformer extends AbstractClassTransformer {

    @Override
    protected String getAdviceKey() {
        return TomcatEndpointTransformer.class.getName();
    }

    @Override
    protected List<Interceptor> getInterceptors() {
        return Collections.singletonList(EndpointPrepareInterceptor.INSTANCE);
    }

    @Override
    public ElementMatcher.Junction<TypeDescription> getClassMatcher() {
        return named("org.apache.tomcat.util.net.AbstractEndpoint");
    }

    @Override
    public Set<MethodMatcherWrapper> getMethodMatchers() {
        return Collections.singleton(
                MethodMatcherWrapper.ofMethod(
                        named("start").and(takesNoArguments())
                )
        );
    }
}
