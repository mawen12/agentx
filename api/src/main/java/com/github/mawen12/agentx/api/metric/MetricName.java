package com.github.mawen12.agentx.api.metric;

import lombok.Getter;

import java.util.Set;

@Getter
public class MetricName {
    private final Metric.Type metricType;
    private final Metric.SubType metricSubType;
    private final String key;
    private final Set<Metric.FieldWrapper> fields;

    private final String name;

    public MetricName(Metric.Type metricType, Metric.SubType metricSubType, String key, Set<Metric.FieldWrapper> fields) {
        this.metricType = metricType;
        this.metricSubType = metricSubType;
        this.key = key;
        this.fields = fields;

        this.name = toName(metricSubType, key, metricType);
    }

    public static MetricName metricNameFor(String name) {
        return new MetricName(
                Metric.Type.values()[Integer.parseInt(name.substring(2, 3))],
                Metric.SubType.value(name.substring(0, 2)),
                name.substring(3),
                null
        );
    }

    public static String toName(Metric.SubType metricSubType, String key, Metric.Type metricType) {
        return metricSubType.getCode() + metricType.ordinal() + key;
    }
}
