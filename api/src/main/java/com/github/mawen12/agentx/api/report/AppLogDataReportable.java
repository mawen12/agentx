package com.github.mawen12.agentx.api.report;

import com.github.mawen12.agentx.api.log.AppLogData;
import com.github.mawen12.agentx.api.report.sender.Sender;

public interface AppLogDataReportable extends Reportable<AppLogData> {
    AppLogDataReportable NOOP = NoOp.INSTANCE;

    enum NoOp implements AppLogDataReportable {
        INSTANCE;

        @Override
        public void report(AppLogData content) {

        }

        @Override
        public void setSender(Sender sender) {

        }


    }
}
