package com.ampaiva.hlo.cm;

import java.util.HashMap;
import java.util.List;

public class ConcernMetricsTable {
    private final HashMap<String, List<IConcernMetric>> hash = new HashMap<String, List<IConcernMetric>>();

    public HashMap<String, List<IConcernMetric>> getHash() {
        return hash;
    }

    public IConcernMetric getConcernMetric(Class<?> cls) {

        for (IConcernMetric concernMetric : getHash().entrySet().iterator().next().getValue()) {
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
