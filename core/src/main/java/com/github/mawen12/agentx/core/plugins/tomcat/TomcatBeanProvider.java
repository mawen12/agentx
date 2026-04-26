package com.github.mawen12.agentx.core.plugins.tomcat;

import com.github.mawen12.agentx.api.spi.BeanProvider;
import com.github.mawen12.agentx.core.plugins.tomcat.metric.TomcatMetric;
import com.google.auto.service.AutoService;

@AutoService(BeanProvider.class)
public class TomcatBeanProvider implements BeanProvider {

    @Override
    public void afterPropertiesSet() {
        TomcatMetric.buildAndRun();
    }

    @Override
    public State onState() {
        return State.SpringBootReady;
    }
}
