package com.ampaiva.hlo.cm;

import java.util.HashMap;

public class ConcernMetricsTable {
    private final HashMap<String, ConcernMetric> hash = new HashMap<String, ConcernMetric>();

    public HashMap<String, ConcernMetric> getHash() {
        return hash;
    }

    @Override
    public String toString() {
        return "ConcernMetricsTable [hash=" + hash + "]";
    }
}
