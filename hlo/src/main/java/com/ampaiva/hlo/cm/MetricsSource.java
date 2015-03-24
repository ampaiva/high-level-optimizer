package com.ampaiva.hlo.cm;

import java.util.Arrays;
import java.util.List;

public class MetricsSource implements IMetricsSource {
    public List<IConcernMetric> getConcernMetrics() {
        LOCC locc = new LOCC();
        ConcernCollection concernCollection = new ConcernCollection();
        return Arrays.asList(new IConcernMetric[] { locc, concernCollection });
    }
}
