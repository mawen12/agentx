package com.github.mawen12.agentx.core.agent;

import com.github.mawen12.agentx.api.Agent;
import com.github.mawen12.agentx.api.context.Context;
import com.github.mawen12.agentx.api.interceptor.InterceptorChain;
import com.github.mawen12.agentx.api.interceptor.InterceptorChainRouter;
import com.github.mawen12.agentx.api.interceptor.MethodInfo;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bytecode.assign.Assigner;

public class ConstructorInlineAdvice {

    @Advice.OnMethodExit
    public static void onExit(@AdviceKey String key,
                              @Advice.This(optional = true) Object invoker,
                              @Advice.Origin("#t") String type,
                              @Advice.Origin("#m") String method,
                              @Advice.AllArguments(readOnly = false, typing = Assigner.Typing.DYNAMIC) Object[] args
    ) {
        Context context = Agent.getContext();
        if (context.isNoop()) {
            return;
        }

        MethodInfo methodInfo = new MethodInfo(invoker, type, method, args);

        InterceptorChain chain = InterceptorChainRouter.INSTANCE.resolve(key);
        if (chain != null) {
            chain.after(methodInfo, context);
        }
    }
}
