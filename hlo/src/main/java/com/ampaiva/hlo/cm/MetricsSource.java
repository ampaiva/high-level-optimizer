package com.ampaiva.hlo.cm;

import java.util.Arrays;
import java.util.List;

public class MetricsSource implements IMetricsSource {
    @Override
    public List<IConcernMetric> getConcernMetrics() {
        ConcernCollection concernCollection = new ConcernCollection();
        return Arrays.asList(new IConcernMetric[] { concernCollection });
    }
}
