package com.ampaiva.hlo;

import japa.parser.ParseException;

import java.util.Map.Entry;
import java.util.TreeSet;

import com.ampaiva.hlo.cm.ConcernMetric;
import com.ampaiva.hlo.cm.MetricsColector;

public class Main {
    public static void main(String[] args) throws ParseException {
        MetricsColector metricsColector = new MetricsColector().addFolder("../HW/src");
        TreeSet<String> set = new TreeSet<String>();
        for (Entry<String, ConcernMetric> entry : metricsColector.getMetrics().getHash().entrySet()) {
            String key = entry.getKey().split("\\.")[entry.getKey().split("\\.").length - 1];
            int value = entry.getValue().getMetric();
            set.add(key + "=" + value);
        }
        for (String string : set) {
            System.out.println(string);
        }
    }
}
