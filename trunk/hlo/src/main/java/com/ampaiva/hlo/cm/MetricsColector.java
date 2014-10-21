package com.ampaiva.hlo.cm;

import japa.parser.ParseException;

import java.util.List;

import com.ampaiva.hlo.util.Helper;

public class MetricsColector {
    private final List<String> sources;

    public MetricsColector(final List<String> sources) {
        this.sources = sources;

    }

    public ConcernMetricsTable getMetrics() throws ParseException {
        ConcernMetricsTable concernMetricsTable = new ConcernMetricsTable();
        for (String source : sources) {
            LOCC locc = new LOCC(Helper.convertString2InputStream(source));
            concernMetricsTable.getHash().put(locc.getKey(), locc);
        }
        return concernMetricsTable;

    }

}
