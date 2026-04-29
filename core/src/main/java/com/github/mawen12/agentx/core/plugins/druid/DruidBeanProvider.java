package com.github.mawen12.agentx.core.plugins.druid;

import com.github.mawen12.agentx.api.spi.BeanProvider;
import com.github.mawen12.agentx.core.plugins.druid.metric.DruidDataSourceMetric;

//@AutoService(BeanProvider.class)
public class DruidBeanProvider implements BeanProvider {

    @Override
    public void afterPropertiesSet() {
        DruidDataSourceMetric.buildAndRun();
    }

    @Override
    public State onState() {
        return State.SpringBootReady;
    }
}
