package com.github.mawen12.agentx.core.plugins.jdbc;

import com.github.mawen12.agentx.api.interceptor.Interceptor;
import com.github.mawen12.agentx.api.plugins.Plugin;
import com.github.mawen12.agentx.api.utils.Lists;
import com.github.mawen12.agentx.api.utils.Sets;
import com.github.mawen12.agentx.core.agent.AbstractPlugin;
import com.github.mawen12.agentx.core.agent.MethodMatcherWrapper;
import com.github.mawen12.agentx.core.plugins.jdbc.interceptor.metric.JdbcStmtMetricInterceptor;
import com.github.mawen12.agentx.core.plugins.jdbc.interceptor.preapre.JdbcStmtPrepareInterceptor;
import com.google.auto.service.AutoService;
import net.bytebuddy.description.NamedElement;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.List;
import java.util.Set;

import static net.bytebuddy.matcher.ElementMatchers.*;

@AutoService(Plugin.class)
public class JdbcStmtPlugin extends AbstractPlugin {

    @Override
    public boolean addDynamicField() {
        return true;
    }

    @Override
    public Domain domain() {
        return Domain.JDBC;
    }

    @Override
    public Component component() {
        return Component.STATEMENT;
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
