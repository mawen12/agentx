package com.github.mawen12.agentx.core.plugins.jdbc;

import com.github.mawen12.agentx.api.interceptor.Interceptor;
import com.github.mawen12.agentx.api.utils.Lists;
import com.github.mawen12.agentx.api.utils.Sets;
import com.github.mawen12.agentx.core.agent.AbstractClassTransformer;
import com.github.mawen12.agentx.core.agent.ClassTransformer;
import com.github.mawen12.agentx.core.plugins.jdbc.interceptor.metric.JdbcDataSourceMetricInterceptor;
import com.google.auto.service.AutoService;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.List;
import java.util.Set;

import static net.bytebuddy.matcher.ElementMatchers.*;

@AutoService(ClassTransformer.class)
public class JdbcDataSourceTransformer extends AbstractClassTransformer {

    @Override
    protected String getAdviceKey() {
        return JdbcDataSourceTransformer.class.getName();
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
    public Set<ElementMatcher.Junction<MethodDescription>> getMethodMatchers() {
        return Sets.of(
                named("getConnection")
                        .and(isPublic())
                        .and(returns(hasSuperType(named("java.sql.Connection"))))
                        .and(takesArguments(0)),
                named("getConnection")
                        .and(isPublic())
                        .and(returns(hasSuperType(named("java.sql.Connection"))))
                        .and(takesArgument(0, named("java.lang.String")))
                        .and(takesArgument(1, named("java.lang.String")))
        );
    }
}
