package com.github.mawen12.agentx.core.plugins.jdbc;

import com.github.mawen12.agentx.api.interceptor.Interceptor;
import com.github.mawen12.agentx.api.plugins.Plugin;
import com.github.mawen12.agentx.api.utils.Lists;
import com.github.mawen12.agentx.api.utils.Sets;
import com.github.mawen12.agentx.core.agent.AbstractPlugin;
import com.github.mawen12.agentx.core.agent.MethodMatcherWrapper;
import com.github.mawen12.agentx.core.plugins.jdbc.interceptor.preapre.JdbcConnPrepareOrCreateStmtInterceptor;
import com.google.auto.service.AutoService;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.List;
import java.util.Set;

import static net.bytebuddy.matcher.ElementMatchers.*;

@AutoService(Plugin.class)
public class JdbcConnPlugin extends AbstractPlugin {

    @Override
    public Domain domain() {
        return Domain.JDBC;
    }

    @Override
    public Component component() {
        return Component.CONNECTION;
    }

    @Override
    protected List<Interceptor> getInterceptors() {
        return Lists.of(JdbcConnPrepareOrCreateStmtInterceptor.INSTANCE);
    }

    @Override
    public ElementMatcher.Junction<TypeDescription> getClassMatcher() {
        return hasSuperType(named("java.sql.Connection"));
    }

    @Override
    public Set<MethodMatcherWrapper> getMethodMatchers() {
        return Sets.of(
                MethodMatcherWrapper.ofMethod(
                        named("createStatement").and(isPublic())
                                .or(named("prepareCall").and(isPublic()))
                                .or(named("prepareStatement").and(isPublic()))
                )
        );
    }
}
