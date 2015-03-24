package com.ampaiva.hlo.cm;

import java.util.List;

public interface IConcernMetric {

    public abstract void parse(String source);

    public abstract int getMetric();

    public abstract List<ConcernMetricNode> getNodes();

}