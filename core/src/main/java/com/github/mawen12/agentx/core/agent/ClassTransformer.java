package com.github.mawen12.agentx.core.agent;

import com.github.mawen12.agentx.api.config.ConfigAware;
import com.github.mawen12.agentx.api.plugins.Plugin;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.util.Set;

public interface ClassTransformer extends Plugin, ConfigAware, AgentBuilder.Transformer {
    default ElementMatcher<ClassLoader> getClassLoaderMatcher() {
        return ElementMatchers.any();
    }

    ElementMatcher.Junction<TypeDescription> getClassMatcher();

    Set<MethodMatcherWrapper> getMethodMatchers();

    AgentBuilder build(AgentBuilder builder);

    default boolean addDynamicField() {
        return false;
    }

}
