package com.ampaiva.hlo.cm;

import japa.parser.ParseException;

import java.util.List;

public interface IConcernMetric {

    public abstract void parse(String source) throws ParseException;

    public abstract int getMetric();

    public abstract List<ConcernMetricNode> getNodes();

}