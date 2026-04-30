package com.github.mawen12.agentx.core.agent;

import com.github.mawen12.agentx.api.Agent;
import com.github.mawen12.agentx.api.config.Config;
import com.github.mawen12.agentx.api.config.PluginConfig;
import com.github.mawen12.agentx.api.context.Context;
import com.github.mawen12.agentx.api.interceptor.Interceptor;
import com.github.mawen12.agentx.api.interceptor.MethodInfo;
import com.github.mawen12.agentx.api.logging.Logger;

public class InterceptorDecorator implements Interceptor {
    private static final Logger LOGGER = Agent.getLogger(InterceptorDecorator.class);

    private final Interceptor delegate;

    private final PluginConfig pluginConfig;

    public InterceptorDecorator(Interceptor delegate, Config config, String domain, String component) {
        this.delegate = delegate;
        pluginConfig = new PluginConfig(config, domain, component, delegate.signal().getName());
    }

    @Override
    public Signal signal() {
        return delegate.signal();
    }

    @Override
    public int order() {
        return delegate.order();
    }

    @Override
    public void before(MethodInfo methodInfo, Context ctx) {
        if (pluginConfig.enabled()) {
            delegate.before(methodInfo, ctx);
        } else {
            LOGGER.debug("plugin.{}.{}.{} is not enabled", pluginConfig.domain(), pluginConfig.component(), pluginConfig.signal());
        }
    }

    @Override
    public void after(MethodInfo methodInfo, Context ctx) {
        if (pluginConfig.enabled()) {
            delegate.after(methodInfo, ctx);
        } else {
            LOGGER.debug("plugin.{}.{}.{} is not enabled", pluginConfig.domain(), pluginConfig.component(), pluginConfig.signal());
        }
    }
}
