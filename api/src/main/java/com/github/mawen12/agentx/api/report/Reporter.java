package com.github.mawen12.agentx.api.report;

import com.github.mawen12.agentx.api.config.Config;
import com.github.mawen12.agentx.api.log.AppLogData;

public interface Reporter {
    Reporter NOOP = NoOp.INSTANCE;

    void init(Config config);

    void report(AppLogData data);

    enum NoOp implements Reporter {
        INSTANCE;

        @Override
        public void init(Config config) {

        }

        @Override
        public void report(AppLogData data) {

        }
    }
}
