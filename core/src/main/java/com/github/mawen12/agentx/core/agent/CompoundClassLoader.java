package com.github.mawen12.agentx.core.agent;

import com.github.mawen12.agentx.api.Agent;
import com.github.mawen12.agentx.api.logging.Logger;

public class CompoundClassLoader {
    private static final Logger LOGGER = Agent.getLogger(CompoundClassLoader.class);

    public static ClassLoader compound(ClassLoader agent, ClassLoader other) {
        if (other == null) {
            return agent;
        }

        try {
            agent.getClass().getDeclaredMethod("add", ClassLoader.class).invoke(agent, other);
        } catch (Exception e) {
            LOGGER.warn("this may be bug if it was running in production", e);
        }
        return agent;
    }
}
