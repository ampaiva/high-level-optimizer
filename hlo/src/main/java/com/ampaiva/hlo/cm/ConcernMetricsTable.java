package com.ampaiva.hlo.cm;

import java.util.HashMap;

public class ConcernMetricsTable {
    private final HashMap<String, ConcernMetricEntity> hash = new HashMap<String, ConcernMetricEntity>();

    public HashMap<String, ConcernMetricEntity> getHash() {
        return hash;
    }

}
