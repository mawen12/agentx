package com.github.mawen12.agentx.core.agent;

import com.github.mawen12.agentx.api.config.Config;
import com.github.mawen12.agentx.api.interceptor.Interceptor;
import com.github.mawen12.agentx.api.interceptor.InterceptorChain;
import com.github.mawen12.agentx.api.interceptor.InterceptorChainRouter;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.utility.JavaModule;

import java.security.ProtectionDomain;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class AbstractPlugin implements ClassTransformer {

    protected abstract List<Interceptor> getInterceptors();

    private InterceptorChain chain;

    private Config config;

    @Override
    public boolean isEnabled() {
        String upperKey = String.join(",", "plugin", domain().name(), component().name(), "enabled");
        Boolean enabled = config.getBoolean(upperKey.toLowerCase());
        return Objects.equals(enabled, true);
    }

    @Override
    public void setConfig(Config config) {
        this.config = config;
    }

    @Override
    public AgentBuilder build(AgentBuilder builder) {
        return builder.type(this.getClassMatcher(), this.getClassLoaderMatcher()).transform(this);
    }

    @Override
    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, ProtectionDomain protectionDomain) {
        String adviceKey = getAdviceKey();
        if (chain == null) {
            List<Interceptor> decoratorList = this.getInterceptors()
                    .stream()
                    .map(delegate -> new InterceptorDecorator(delegate, config, domain().getName(), component().getName()))
                    .collect(Collectors.toList());
            chain = new InterceptorChain(decoratorList);
            InterceptorChainRouter.INSTANCE.add(adviceKey, chain);

            List<Interceptor> interceptors = getInterceptors();
            for (Interceptor interceptor : interceptors) {
                interceptor.init();
            }
        }

        List<AgentBuilder.Transformer> transformers = this.getMethodMatchers()
                .stream()
                .map(methodMatcher -> new ForAdviceTransformer(methodMatcher.getByteBuddyMatcher(), adviceKey, methodMatcher.isConstructor() ? ConstructorInlineAdvice.class : CommonInlineAdvice.class))
                .collect(Collectors.toList());

        if (this.addDynamicField()) {
            transformers.add(new DynamicFieldTransformer());
        }

        for (AgentBuilder.Transformer transformer : transformers) {
            builder = transformer.transform(builder, typeDescription, classLoader, module, protectionDomain);
        }

        return builder;
    }

    private String getAdviceKey() {
        return domain() + "." + component();
    }
}
