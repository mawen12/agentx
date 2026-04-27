package com.github.mawen12.agentx.core.plugins.httpservlet;

import com.github.mawen12.agentx.api.interceptor.Interceptor;
import com.github.mawen12.agentx.api.utils.Lists;
import com.github.mawen12.agentx.api.utils.Sets;
import com.github.mawen12.agentx.core.agent.AbstractClassTransformer;
import com.github.mawen12.agentx.core.agent.ClassTransformer;
import com.github.mawen12.agentx.core.plugins.httpservlet.interceptor.metric.DoFilterMetricInterceptor;
import com.google.auto.service.AutoService;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.List;
import java.util.Set;

import static net.bytebuddy.matcher.ElementMatchers.*;

@AutoService(ClassTransformer.class)
public class DoFilterTransformer extends AbstractClassTransformer {

    @Override
    protected String getAdviceKey() {
        return DoFilterTransformer.class.getName();
    }

    @Override
    protected List<Interceptor> getInterceptors() {
        return Lists.of(DoFilterMetricInterceptor.INSTANCE);
    }

    @Override
    public ElementMatcher.Junction<TypeDescription> getClassMatcher() {
        return hasSuperType(named("javax.servlet.Filter"))
                .or(hasSuperClass(named("javax.servlet.http.HttpServlet")));
    }

    @Override
    public Set<ElementMatcher.Junction<MethodDescription>> getMethodMatchers() {
        return Sets.of(
                named("doFilter").and(takesArgument(0, named("javax.servlet.ServletRequest")))
                        .and(takesArgument(1, named("javax.servlet.ServletResponse"))),
                named("service").and(takesArgument(0, named("javax.servlet.ServletRequest")))
                        .and(takesArgument(1, named("javax.servlet.ServletResponse")))
                        .and(isDefaultMethod())
        );
    }
}
