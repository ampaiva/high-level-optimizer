package com.ampaiva.hlo;

import japa.parser.ParseException;

import java.util.Map.Entry;

import com.ampaiva.hlo.cm.ConcernMetricEntity;
import com.ampaiva.hlo.cm.MetricsColector;

public class Main {
    public static void main(String[] args) throws ParseException {
        MetricsColector metricsColector = new MetricsColector("../HW/src");
        for (Entry<String, ConcernMetricEntity> entry : metricsColector.getMetrics().getHash().entrySet()) {
            System.out.println(entry.getKey().split("\\.")[entry.getKey().split("\\.").length - 1] + "="
                    + entry.getValue().getValue());
        }
    }
}
