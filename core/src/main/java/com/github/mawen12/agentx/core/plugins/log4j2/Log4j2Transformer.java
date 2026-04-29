package com.github.mawen12.agentx.core.plugins.log4j2;

import com.github.mawen12.agentx.api.Agent;
import com.github.mawen12.agentx.api.interceptor.Interceptor;
import com.github.mawen12.agentx.api.utils.Sets;
import com.github.mawen12.agentx.core.agent.AbstractClassTransformer;
import com.github.mawen12.agentx.core.agent.ClassTransformer;
import com.github.mawen12.agentx.core.agent.MethodMatcherWrapper;
import com.github.mawen12.agentx.core.plugins.log4j2.interceptor.log.Log4j2LogInterceptor;
import com.google.auto.service.AutoService;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static net.bytebuddy.matcher.ElementMatchers.*;

@AutoService(ClassTransformer.class)
public class Log4j2Transformer extends AbstractClassTransformer {

    @Override
    protected String getAdviceKey() {
        return Log4j2Transformer.class.getName();
    }

    @Override
    protected List<Interceptor> getInterceptors() {
        return Collections.singletonList(Log4j2LogInterceptor.INSTANCE);
    }

    @Override
    public ElementMatcher<ClassLoader> getClassLoaderMatcher() {
        return not(is(Agent.getAgentClassLoader()));
    }

    @Override
    public ElementMatcher.Junction<TypeDescription> getClassMatcher() {
        return hasSuperClass(named("org.apache.logging.log4j.spi.AbstractLogger"));
    }

    @Override
    public Set<MethodMatcherWrapper> getMethodMatchers() {
        return Sets.of(
                MethodMatcherWrapper.ofMethod(
                        named("log")
                                .and(isProtected())
                                .and(takesArguments(6))
//                                .and(takesArgument(0, named("org.apache.logging.log4j.Level")))
//                                .and(takesArgument(1, named("org.apache.logging.log4j.Marker")))
//                                .and(takesArgument(2, named("java.lang.String")))
//                                .and(takesArgument(3, named("java.lang.StackTraceElement")))
//                                .and(takesArgument(4, named("org.apache.logging.log4j.message.Message")))
//                                .and(takesArgument(5, named("java.lang.Throwable")))
                )
        );
    }
}
