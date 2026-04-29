package com.github.mawen12.agentx.core.report.sender;

import com.github.mawen12.agentx.api.report.sender.Call;
import com.github.mawen12.agentx.api.report.sender.HttpConfig;
import com.github.mawen12.agentx.api.report.sender.Sender;

public abstract class AbstractHttpSender implements Sender {
    public static final String SENDER_NAME = "http";

    protected HttpConfig config;

    public AbstractHttpSender(HttpConfig config) {
        this.config = config;
        initClient();
    }

    @Override
    public String name() {
        return SENDER_NAME;
    }

    @Override
    public boolean isAvailable() {
        return config.isEnabled();
    }

    @Override
    public Call<Void> send() {
        if (!config.isEnabled()) {
            return Call.NOOP_VOID;
        }

        return doSend();
    }

    abstract void initClient();

    abstract Call<Void> doSend();
}
