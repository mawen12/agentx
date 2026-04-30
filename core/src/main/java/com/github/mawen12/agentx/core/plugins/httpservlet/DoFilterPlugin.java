package com.github.mawen12.agentx.core.plugins.httpservlet;

import com.github.mawen12.agentx.api.interceptor.Interceptor;
import com.github.mawen12.agentx.api.plugins.Plugin;
import com.github.mawen12.agentx.api.utils.Lists;
import com.github.mawen12.agentx.api.utils.Sets;
import com.github.mawen12.agentx.core.agent.AbstractPlugin;
import com.github.mawen12.agentx.core.agent.MethodMatcherWrapper;
import com.github.mawen12.agentx.core.plugins.httpservlet.interceptor.metric.DoFilterMetricInterceptor;
import com.google.auto.service.AutoService;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.List;
import java.util.Set;

import static com.github.mawen12.agentx.core.agent.MethodMatcherWrapper.ofMethod;
import static net.bytebuddy.matcher.ElementMatchers.*;

@AutoService(Plugin.class)
public class DoFilterPlugin extends AbstractPlugin {

    @Override
    public Domain domain() {
        return Domain.HTTP_SERVLET;
    }

    @Override
    public Component component() {
        return Component.SERVLET;
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
    public Set<MethodMatcherWrapper> getMethodMatchers() {
        return Sets.of(
                ofMethod(
                        named("doFilter").and(takesArgument(0, named("javax.servlet.ServletRequest")))
                                .and(takesArgument(1, named("javax.servlet.ServletResponse")))),
                ofMethod(
                        named("service").and(takesArgument(0, named("javax.servlet.ServletRequest")))
                                .and(takesArgument(1, named("javax.servlet.ServletResponse")))
                                .and(isDefaultMethod()))
        );
    }
}
