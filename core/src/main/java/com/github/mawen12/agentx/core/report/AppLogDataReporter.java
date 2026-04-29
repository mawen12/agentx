package com.github.mawen12.agentx.core.report;

import com.github.mawen12.agentx.api.log.AppLogData;
import com.github.mawen12.agentx.api.report.AppLogDataReportable;
import com.github.mawen12.agentx.api.report.sender.Sender;

public class AppLogDataReporter implements AppLogDataReportable {
    private Sender sender;

    @Override
    public void report(AppLogData log) {
        sender.send();
    }

    @Override
    public void setSender(Sender sender) {

    }
}
