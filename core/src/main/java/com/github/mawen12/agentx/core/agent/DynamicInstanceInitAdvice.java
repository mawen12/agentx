package com.github.mawen12.agentx.core.agent;

import com.github.mawen12.agentx.api.field.DynamicFieldAccessor;
import net.bytebuddy.asm.Advice;

public class DynamicInstanceInitAdvice {

    @Advice.OnMethodExit
    public static void onExit(@Advice.This(optional = true) Object target) {
        if (target instanceof DynamicFieldAccessor) {
            DynamicFieldAccessor accessor = (DynamicFieldAccessor) target;
            if (accessor.getAgent$$Field$$Data() != null) {
                accessor.setAgent$$Field$$Data(null);
            }
        }
    }
}
