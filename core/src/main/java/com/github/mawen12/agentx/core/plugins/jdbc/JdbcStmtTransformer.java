package com.github.mawen12.agentx.core.plugins.jdbc;

import com.github.mawen12.agentx.api.interceptor.Interceptor;
import com.github.mawen12.agentx.api.utils.Lists;
import com.github.mawen12.agentx.api.utils.Sets;
import com.github.mawen12.agentx.core.agent.AbstractClassTransformer;
import com.github.mawen12.agentx.core.agent.ClassTransformer;
import com.github.mawen12.agentx.core.agent.MethodMatcherWrapper;
import com.github.mawen12.agentx.core.plugins.jdbc.interceptor.metric.JdbcStmtMetricInterceptor;
import com.github.mawen12.agentx.core.plugins.jdbc.interceptor.preapre.JdbcStmtPrepareInterceptor;
import com.google.auto.service.AutoService;
import net.bytebuddy.description.NamedElement;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.List;
import java.util.Set;

import static net.bytebuddy.matcher.ElementMatchers.*;
import static net.bytebuddy.matcher.ElementMatchers.isAbstract;
import static net.bytebuddy.matcher.ElementMatchers.isInterface;
import static net.bytebuddy.matcher.ElementMatchers.isOverriddenFrom;
import static net.bytebuddy.matcher.ElementMatchers.nameStartsWith;
import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.not;

@AutoService(ClassTransformer.class)
public class JdbcStmtTransformer extends AbstractClassTransformer {

    @Override
    public boolean addDynamicField() {
        return true;
    }

    @Override
    protected String getAdviceKey() {
        return "Jdbc-Statement";
    }

    @Override
    protected List<Interceptor> getInterceptors() {
        return Lists.of(JdbcStmtPrepareInterceptor.INSTANCE, JdbcStmtMetricInterceptor.INSTANCE);
    }

    @Override
    public ElementMatcher.Junction<TypeDescription> getClassMatcher() {
        return hasSuperType(named("java.sql.Statement"))
                .and(not(isAbstract()))
                .and(not(isInterface()));
    }

    @Override
    public Set<MethodMatcherWrapper> getMethodMatchers() {
        ElementMatcher.Junction<NamedElement> overrideFrom = named("java.sql.Statement").or(named("java.sql.PreparedStatement"));

        return Sets.of(
                MethodMatcherWrapper.ofMethod(
                        nameStartsWith("execute")
                                .and(isOverriddenFrom(overrideFrom))
                ),
                MethodMatcherWrapper.ofMethod(
                        named("addBatch")
                                .or(named("clearBatch")))
        );
    }
}
