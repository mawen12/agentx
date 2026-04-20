package com.github.mawen12.agent.api.interceptor;

import java.util.HashMap;
import java.util.Map;

public class InterceptorChainRouter {
    public static final InterceptorChainRouter INSTANCE = new InterceptorChainRouter();

    private final Map<String, InterceptorChain> byMethodName = new HashMap<>();

    public void add(String methodName, InterceptorChain chain) {
        if (byMethodName.containsKey(methodName)) {
            throw new IllegalStateException("[agent] method " + methodName + " already exists");
        }
        byMethodName.put(methodName, chain);
    }

    public InterceptorChain resolve(String methodName) {
        return byMethodName.get(methodName);
    }
}
