package com.github.mawen12.agentx.api.report;

import com.github.mawen12.agentx.api.report.sender.Sender;

public interface Reportable<S> {

    void report(S content);

    void setSender(Sender sender);
}
