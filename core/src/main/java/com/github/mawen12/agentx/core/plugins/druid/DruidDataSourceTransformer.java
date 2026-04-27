package com.github.mawen12.agentx.core.plugins.druid;

import com.github.mawen12.agentx.api.interceptor.Interceptor;
import com.github.mawen12.agentx.core.agent.AbstractClassTransformer;
import com.github.mawen12.agentx.core.agent.ClassTransformer;
import com.github.mawen12.agentx.core.agent.MethodMatcherWrapper;
import com.github.mawen12.agentx.core.plugins.druid.metric.DruidDataSourceInterceptor;
import com.google.auto.service.AutoService;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static net.bytebuddy.matcher.ElementMatchers.*;

@AutoService(ClassTransformer.class)
public class DruidDataSourceTransformer extends AbstractClassTransformer {

    @Override
    protected String getAdviceKey() {
        return DruidDataSourceTransformer.class.getName();
    }

    @Override
    protected List<Interceptor> getInterceptors() {
        return Collections.singletonList(DruidDataSourceInterceptor.INSTANCE);
    }

    @Override
    public ElementMatcher.Junction<TypeDescription> getClassMatcher() {
        return named("com.alibaba.druid.pool.DruidDataSource");
    }

    @Override
    public Set<MethodMatcherWrapper> getMethodMatchers() {
        return Collections.singleton(MethodMatcherWrapper.ofConstructor(
                isConstructor().and(takesArguments(1))
        ));
    }
}
