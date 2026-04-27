package com.github.mawen12.agentx.core.agent;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MethodMatcherWrapper {
    private boolean isConstructor;

    private ElementMatcher.Junction<MethodDescription> byteBuddyMatcher;

    public static MethodMatcherWrapper ofConstructor(ElementMatcher.Junction<MethodDescription> byteBuddyMatcher) {
        return new MethodMatcherWrapper(true, byteBuddyMatcher);
    }

    public static MethodMatcherWrapper ofMethod(ElementMatcher.Junction<MethodDescription> byteBuddyMatcher) {
        return new MethodMatcherWrapper(false, byteBuddyMatcher);
    }
}
