package com.github.mawen12.agentx.core.report.sender;

import com.github.mawen12.agentx.api.utils.AgentThreadFactory;

public class HttpThreadFactory extends AgentThreadFactory {

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, "AgentHttpSender-" + counter.incrementAndGet());
    }
}
