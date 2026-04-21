package com.github.mawen12.agentx.api.spi;

public interface BeanProvider {

    void afterPropertiesSet();

    enum State {
        Start, AgentReady, DruidReady, SpringBootReady;
    }

    interface Listener {
        void onState();
    }
}
