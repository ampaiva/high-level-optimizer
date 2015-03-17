package com.ampaiva.hlo.cm;

import java.util.HashMap;
import java.util.List;

public class ConcernMetricsTable {
    private final HashMap<String, List<ConcernMetric>> hash = new HashMap<String, List<ConcernMetric>>();

    public HashMap<String, List<ConcernMetric>> getHash() {
        return hash;
    }

    public ConcernMetric getConcernMetric(Class<?> cls) {

        for (ConcernMetric concernMetric : getHash().entrySet().iterator().next().getValue()) {
            if (concernMetric.getClass().isAssignableFrom(cls)) {
                return concernMetric;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "ConcernMetricsTable [hash=" + hash + "]";
    }
}
