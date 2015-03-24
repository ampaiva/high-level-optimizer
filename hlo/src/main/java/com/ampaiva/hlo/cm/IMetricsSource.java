package com.ampaiva.hlo.cm;

import java.util.List;

public interface IMetricsSource {

    public abstract List<IConcernMetric> getConcernMetrics();

}