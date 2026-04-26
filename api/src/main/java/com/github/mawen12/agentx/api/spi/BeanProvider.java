package com.github.mawen12.agentx.api.spi;

public interface BeanProvider {

    void afterPropertiesSet();

    State onState();

    enum State {
        Start, AgentReady, DruidReady, SpringBootReady;
    }

    interface Listener {
        void onState();
    }
}
