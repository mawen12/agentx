package com.github.mawen12.agentx.core.plugins.jvm;

import com.github.mawen12.agentx.api.spi.BeanProvider;
import com.github.mawen12.agentx.core.plugins.jvm.metric.JvmGCMetric;
import com.github.mawen12.agentx.core.plugins.jvm.metric.JvmMemoryMetric;
import com.google.auto.service.AutoService;

@AutoService(BeanProvider.class)
public class JvmBeanProvider implements BeanProvider {

    @Override
    public void afterPropertiesSet() {
        JvmGCMetric.buildAndRun();
        JvmMemoryMetric.buildAndRun();
    }

    @Override
    public State onState() {
        return State.Start;
    }
}
