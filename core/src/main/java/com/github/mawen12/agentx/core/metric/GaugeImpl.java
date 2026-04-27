package com.github.mawen12.agentx.core.metric;

import com.github.mawen12.agentx.api.metric.Gauge;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GaugeImpl<T> implements com.codahale.metrics.Gauge<T> {
    private Gauge<T> gauge;

    public static <T> GaugeImpl<T> build(Gauge<T> gauge)  {
        return gauge == null ? null : new GaugeImpl<>(gauge);
    }

    @Override
    public T getValue() {
        return gauge.getValue();
    }
}
