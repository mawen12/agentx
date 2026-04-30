package com.github.mawen12.agentx.core.plugins.tomcat.interceptor.metric;

import com.github.mawen12.agentx.api.metric.GaugeMetricModel;
import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class TomcatGaugeMetricModel implements GaugeMetricModel {
    private final Integer currentThreadsBusy;
    private final Integer currentThreadCount;
    private final Integer maxThreads;

    @Override
    public Map<String, Object> toHashMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("current-threads-busy", currentThreadsBusy);
        map.put("current-thread-count", currentThreadCount);
        map.put("max-threads", maxThreads);
        return map;
    }
}
