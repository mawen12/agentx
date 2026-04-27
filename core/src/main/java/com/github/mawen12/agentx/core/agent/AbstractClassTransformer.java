package com.github.mawen12.agentx.core.agent;

import com.github.mawen12.agentx.api.interceptor.Interceptor;
import com.github.mawen12.agentx.api.interceptor.InterceptorChain;
import com.github.mawen12.agentx.api.interceptor.InterceptorChainRouter;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.utility.JavaModule;

import java.security.ProtectionDomain;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractClassTransformer implements ClassTransformer, AgentBuilder.Transformer{

    protected abstract String getAdviceKey();

    protected abstract List<Interceptor> getInterceptors();

    @Override
    public AgentBuilder build(AgentBuilder builder) {
        InterceptorChainRouter.INSTANCE.add(this.getAdviceKey(), new InterceptorChain(this.getInterceptors()));
        return builder.type(this.getClassMatcher(), this.getClassLoaderMatcher()).transform(this);
    }

    @Override
    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, ProtectionDomain protectionDomain) {
        String adviceKey = getAdviceKey();

        List<AgentBuilder.Transformer> transformers = this.getMethodMatchers()
                .stream()
                .map(methodMatcher -> new ForAdviceTransformer(methodMatcher.getByteBuddyMatcher(), adviceKey, methodMatcher.isConstructor() ? ConstructorInlineAdvice.class  : CommonInlineAdvice.class))
                .collect(Collectors.toList());

        if (this.addDynamicField()) {
            transformers.add(new DynamicFieldTransformer());
        }

        List<Interceptor> interceptors = getInterceptors();
        for (Interceptor interceptor : interceptors) {
            interceptor.init();
        }

        for (AgentBuilder.Transformer transformer : transformers) {
            builder = transformer.transform(builder, typeDescription, classLoader, module, protectionDomain);
        }

        return builder;
    }
}
