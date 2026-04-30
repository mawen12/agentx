package com.github.mawen12.agentx.api.spi;

import com.github.mawen12.agentx.api.config.ConfigAware;

public interface BeanProvider extends ConfigAware {

    void afterPropertiesSet();

    State onState();

    enum State {
        Start, AgentReady, DruidReady, SpringBootReady;
    }

    interface Listener {
        void onState();
    }
}
