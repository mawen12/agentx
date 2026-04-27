package com.github.mawen12.agentx.core.agent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.util.Set;

public interface ClassTransformer {
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
