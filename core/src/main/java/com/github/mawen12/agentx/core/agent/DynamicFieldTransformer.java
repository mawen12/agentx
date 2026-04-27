package com.github.mawen12.agentx.core.agent;

import com.github.mawen12.agentx.api.Agent;
import com.github.mawen12.agentx.api.field.DynamicFieldAccessor;
import com.github.mawen12.agentx.api.logging.Logger;
import com.github.mawen12.agentx.core.Bootstrap;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.jar.asm.Opcodes;
import net.bytebuddy.utility.JavaModule;

import java.security.ProtectionDomain;

public class DynamicFieldTransformer implements AgentBuilder.Transformer {
    private static final Logger LOGGER = Agent.getLogger(DynamicFieldTransformer.class);

    private final AgentBuilder.Transformer.ForAdvice transformer;

    public DynamicFieldTransformer() {
        this.transformer = new AgentBuilder.Transformer.ForAdvice()
                .include(getClass().getClassLoader())
                .advice(MethodDescription::isConstructor, DynamicInstanceInitAdvice.class.getCanonicalName());
    }

    @Override
    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, ProtectionDomain protectionDomain) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(Bootstrap.LOADER);

        try {
            try {
                builder = builder.defineField(DynamicFieldAccessor.FIELD_NAME, Object.class, Opcodes.ACC_PRIVATE)
                        .implement(DynamicFieldAccessor.class)
                        .intercept(FieldAccessor.ofField(DynamicFieldAccessor.FIELD_NAME));
            } catch (Exception e) {
                LOGGER.warn("agent type {} already has field {}", typeDescription.getName(), DynamicFieldAccessor.FIELD_NAME, e);
            }
            return transformer.transform(builder, typeDescription, classLoader, module, protectionDomain);
        } finally {
            Thread.currentThread().setContextClassLoader(cl);
        }
    }
}
