package com.github.mawen12.agentx.core.plugins.jdbc;

import com.github.mawen12.agentx.api.interceptor.Interceptor;
import com.github.mawen12.agentx.api.plugins.Plugin;
import com.github.mawen12.agentx.api.utils.Lists;
import com.github.mawen12.agentx.api.utils.Sets;
import com.github.mawen12.agentx.core.agent.AbstractPlugin;
import com.github.mawen12.agentx.core.agent.MethodMatcherWrapper;
import com.github.mawen12.agentx.core.plugins.jdbc.interceptor.metric.JdbcDataSourceMetricInterceptor;
import com.google.auto.service.AutoService;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.List;
import java.util.Set;

import static net.bytebuddy.matcher.ElementMatchers.*;

@AutoService(Plugin.class)
public class JdbcDataSourcePlugin extends AbstractPlugin {

    @Override
    public Domain domain() {
        return Domain.JDBC;
    }

    @Override
    public Component component() {
        return Component.DATASOURCE;
    }

    @Override
    protected List<Interceptor> getInterceptors() {
        return Lists.of(JdbcDataSourceMetricInterceptor.INSTANCE);
    }

    @Override
    public ElementMatcher.Junction<TypeDescription> getClassMatcher() {
        return hasSuperType(named("javax.sql.DataSource"));
    }

    @Override
    public Set<MethodMatcherWrapper> getMethodMatchers() {
        return Sets.of(
                MethodMatcherWrapper.ofMethod(
                        named("getConnection")
                                .and(isPublic())
                                .and(returns(hasSuperType(named("java.sql.Connection"))))
                                .and(takesArguments(0))),
                MethodMatcherWrapper.ofMethod(
                        named("getConnection")
                                .and(isPublic())
                                .and(returns(hasSuperType(named("java.sql.Connection"))))
                                .and(takesArgument(0, named("java.lang.String")))
                                .and(takesArgument(1, named("java.lang.String"))))
        );
    }
}
