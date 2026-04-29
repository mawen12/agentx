package com.github.mawen12.agentx.core.agent;

import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import static net.bytebuddy.matcher.ElementMatchers.*;

public class AgentIgnore {

    public static ElementMatcher<TypeDescription> ignored() {
        return isSynthetic()
                .or(nameStartsWith("java.").and(not(named("java.util.concurrent.ThreadPoolExecutor"))))
                .or(nameStartsWith("sun."))
                .or(nameStartsWith("javax."))
                // 确保能够增强 log4j2
//                .or(nameStartsWith("org.apache.logging"))
                // 忽略自身的包，即可解决出现 com.github.mawen12.agentx.api.metrics.Metric 找不到的问题
                .or(nameStartsWith("com.github.mawen12.agentx."));
    }
}
