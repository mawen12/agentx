package com.github.mawen12.agent.api.interceptor;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MethodInfo {
    private final Object invoker;
    private final String type;
    private final String method;
    private final Object[] args;
    private Throwable throwable;
    private Object retValue;

    private boolean changed;

    public boolean isSuccess() {
        return this.throwable == null;
    }

    public void changeArg(int index, Object arg) {
        this.args[index] = arg;
        this.changed = true;
    }
}
