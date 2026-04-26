package com.github.mawen12.agentx.core.metric;

import com.github.mawen12.agentx.api.metric.GaugeMetricModel;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ErrorPercentModelGauge implements GaugeMetricModel {
    private final BigDecimal m1ErrorPercent;
    private final BigDecimal m5ErrorPercent;
    private final BigDecimal m15ErrorPercent;

    public ErrorPercentModelGauge(BigDecimal m1ErrorPercent, BigDecimal m5ErrorPercent, BigDecimal m15ErrorPercent) {
        this.m1ErrorPercent = m1ErrorPercent;
        this.m5ErrorPercent = m5ErrorPercent;
        this.m15ErrorPercent = m15ErrorPercent;
    }

    @Override
    public Map<String, Object> toHashMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("m1errpct", m1ErrorPercent);
        map.put("m5errpct", m5ErrorPercent);
        map.put("m15errpct", m15ErrorPercent);
        return map;
    }
}
