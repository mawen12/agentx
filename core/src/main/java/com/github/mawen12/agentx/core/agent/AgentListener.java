package com.github.mawen12.agentx.core.agent;

import com.github.mawen12.agentx.api.Agent;
import com.github.mawen12.agentx.api.logging.Logger;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.utility.JavaModule;

public enum AgentListener implements AgentBuilder.Listener {
    INSTANCE;

    private static final Logger LOGGER = Agent.getLogger(AgentListener.class);

    @Override
    public void onDiscovery(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {

    }

    @Override
    public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded, DynamicType dynamicType) {
        LOGGER.debug("onTransformation: {} loaded: {} from classLoader {}", typeDescription, loaded, classLoader);
    }

    @Override
    public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded) {

    }

    @Override
    public void onError(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded, Throwable throwable) {
        LOGGER.warn("transform ends exceptionally, which is sometimes normal and sometimes there is an error: {} error: {} loaded: {} form classLoader {}",
                typeName, throwable, loaded, classLoader);
    }

    @Override
    public void onComplete(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {

    }
}
