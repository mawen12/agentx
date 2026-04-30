package com.github.mawen12.agentx.core.plugins.druid;

import com.github.mawen12.agentx.api.interceptor.Interceptor;
import com.github.mawen12.agentx.api.plugins.Plugin;
import com.github.mawen12.agentx.core.agent.AbstractPlugin;
import com.github.mawen12.agentx.core.agent.MethodMatcherWrapper;
import com.github.mawen12.agentx.core.plugins.druid.interceptor.prepare.DruidDataSourcePreapreInterceptor;
import com.google.auto.service.AutoService;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static net.bytebuddy.matcher.ElementMatchers.*;

@AutoService(Plugin.class)
public class DruidDataSourceTransformer extends AbstractPlugin {

    @Override
    public Domain domain() {
        return Domain.DRUID;
    }

    @Override
    public Component component() {
        return Component.DATASOURCE;
    }

    @Override
    protected List<Interceptor> getInterceptors() {
        return Collections.singletonList(DruidDataSourcePreapreInterceptor.INSTANCE);
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
