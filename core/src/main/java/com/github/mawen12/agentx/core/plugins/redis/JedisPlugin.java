package com.github.mawen12.agentx.core.plugins.redis;

import com.github.mawen12.agentx.api.interceptor.Interceptor;
import com.github.mawen12.agentx.api.plugins.Plugin;
import com.github.mawen12.agentx.core.agent.AbstractPlugin;
import com.github.mawen12.agentx.core.agent.MethodMatcherWrapper;
import com.github.mawen12.agentx.core.plugins.redis.interceptor.metric.JedisMetricInterceptor;
import com.google.auto.service.AutoService;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static net.bytebuddy.matcher.ElementMatchers.*;
import static net.bytebuddy.matcher.ElementMatchers.isInterface;
import static net.bytebuddy.matcher.ElementMatchers.isOverriddenFrom;

@AutoService(Plugin.class)
public class JedisPlugin extends AbstractPlugin {

    @Override
    protected List<Interceptor> getInterceptors() {
        return Collections.singletonList(JedisMetricInterceptor.INSTANCE);
    }

    @Override
    public ElementMatcher.Junction<TypeDescription> getClassMatcher() {
        return hasSuperClass(named("redis.clients.jedis.BinaryJedis"));
    }

    @Override
    public Set<MethodMatcherWrapper> getMethodMatchers() {
        ElementMatcher.Junction<TypeDescription> overrideFrom =
                named("redis.clients.jedis.commands.JedisCommands").and(isInterface())
                        .or(named("redis.clients.jedis.JedisCommands").and(isInterface()))
                        .or(named("redis.clients.jedis.commands.AdvancedJedisCommands").and(isInterface()))
                        .or(named("redis.clients.jedis.commands.BasicCommands").and(isInterface()))
                        .or(named("redis.clients.jedis.commands.ClusterCommands").and(isInterface()))
                        .or(named("redis.clients.jedis.commands.ModuleCommands").and(isInterface()))
                        .or(named("redis.clients.jedis.commands.MultiKeyCommands").and(isInterface()))
                        .or(named("redis.clients.jedis.commands.ScriptingCommands").and(isInterface()))
                        .or(named("redis.clients.jedis.commands.SentinelCommands").and(isInterface()))
                        .or(named("redis.clients.jedis.commands.BinaryJedisCommands").and(isInterface()))
                        .or(named("redis.clients.jedis.commands.MultiKeyBinaryCommands").and(isInterface()))
                        .or(named("redis.clients.jedis.commands.AdvancedBinaryJedisCommands").and(isInterface()))
                        .or(named("redis.clients.jedis.commands.BinaryScriptingCommands").and(isInterface()));

        return Collections.singleton(
                MethodMatcherWrapper.ofMethod(isOverriddenFrom(overrideFrom))
        );
    }

    @Override
    public Domain domain() {
        return Domain.JEDIS;
    }

    @Override
    public Component component() {
        return Component.CACHE;
    }
}
