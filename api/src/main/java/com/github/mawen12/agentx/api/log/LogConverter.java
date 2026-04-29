package com.github.mawen12.agentx.api.log;

import com.github.mawen12.agentx.api.interceptor.MethodInfo;

public interface LogConverter {

    AppLogData convert(MethodInfo methodInfo);
}
