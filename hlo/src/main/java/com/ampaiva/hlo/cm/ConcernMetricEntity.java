package com.ampaiva.hlo.cm;

public class ConcernMetricEntity {
    private final String metric;
    private final int value;

    public ConcernMetricEntity(String metric, int value) {
        this.metric = metric;
        this.value = value;
    }

    public String getMetric() {
        return metric;
    }

    @Override
    public String toString() {
        return "ConcernMetricEntity [metric=" + metric + ", value=" + value + "]";
    }

    public int getValue() {
        return value;
    }
}
