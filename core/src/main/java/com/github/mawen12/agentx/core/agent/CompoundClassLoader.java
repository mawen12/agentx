package com.github.mawen12.agentx.core.agent;

public class CompoundClassLoader {

    public static ClassLoader compound(ClassLoader agentxClassLoader, ClassLoader other) {
        if (other == null) {
            return agentxClassLoader;
        }

        try {
            agentxClassLoader.getClass().getDeclaredMethod("add", ClassLoader.class).invoke(agentxClassLoader, other);
        } catch (Exception e) {
            System.err.println("this may be bug if it was running in production " + e.getMessage());
        }
        return agentxClassLoader;
    }
}
