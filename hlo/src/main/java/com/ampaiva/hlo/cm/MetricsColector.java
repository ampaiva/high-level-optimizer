package com.ampaiva.hlo.cm;

import japa.parser.ParseException;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import com.ampaiva.hlo.util.Helper;

public class MetricsColector {
    private final Map<String, String> sources;

    public MetricsColector(final Map<String, String> sources) {
        this.sources = sources;

    }

    public ConcernMetricsTable getMetrics() throws ParseException {
        ConcernMetricsTable concernMetricsTable = new ConcernMetricsTable();
        for (Entry<String, String> source : sources.entrySet()) {
            LOCC locc = new LOCC(source.getKey(), Helper.convertString2InputStream(source.getValue()));
            ConcernCollection concernCollection = new ConcernCollection(source.getKey(),
                    Helper.convertString2InputStream(source.getValue()));
            concernMetricsTable.getHash().put(locc.getKey(),
                    Arrays.asList(new ConcernMetric[] { locc, concernCollection }));
        }
        return concernMetricsTable;

    }

}
