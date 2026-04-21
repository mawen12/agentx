package com.github.mawen12.agentx.api.field;

import com.github.mawen12.agentx.api.Agent;
import com.github.mawen12.agentx.api.logging.Logger;

public class DynamicFieldAccessorHelper {
    private static final Logger LOGGER = Agent.getLogger(DynamicFieldAccessor.class);

    public static <T> T getDynamicFieldValue(Object target) {
        if (target instanceof DynamicFieldAccessor) {
            return (T) ((DynamicFieldAccessor) target).getAgent$$Field$$Data();
        }

        LOGGER.warn("{} must implements DynamicFieldAccessor for getValue", target.getClass().getName());
        return null;
    }

    public static void setDynamicFieldValue(Object target, Object value) {
        if (target instanceof DynamicFieldAccessor) {
            ((DynamicFieldAccessor) target).setAgent$$Field$$Data(value);
            return;
        }

        LOGGER.warn("{} must implements DynamicFieldAccessor for setValue", target.getClass().getName());
    }
}
