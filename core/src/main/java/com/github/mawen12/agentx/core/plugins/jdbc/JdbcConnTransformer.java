package com.github.mawen12.agentx.core.plugins.jdbc;

import com.github.mawen12.agentx.api.interceptor.Interceptor;
import com.github.mawen12.agentx.api.utils.Lists;
import com.github.mawen12.agentx.api.utils.Sets;
import com.github.mawen12.agentx.core.agent.AbstractClassTransformer;
import com.github.mawen12.agentx.core.agent.ClassTransformer;
import com.github.mawen12.agentx.core.agent.MethodMatcherWrapper;
import com.github.mawen12.agentx.core.plugins.jdbc.interceptor.preapre.JdbcConnPrepareOrCreateStmtInterceptor;
import com.google.auto.service.AutoService;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.List;
import java.util.Set;

import static net.bytebuddy.matcher.ElementMatchers.*;

@AutoService(ClassTransformer.class)
public class JdbcConnTransformer extends AbstractClassTransformer {

    @Override
    protected String getAdviceKey() {
        return JdbcConnTransformer.class.getName();
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
