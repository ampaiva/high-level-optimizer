package com.ampaiva.hlo.cm;

import japa.parser.ParseException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class MetricsColector {
    private final IMetricsSource metricsSource;
    private final ICodeSource codeSource;

    public MetricsColector(IMetricsSource metricsSource, ICodeSource codeSource) {
        this.metricsSource = metricsSource;
        this.codeSource = codeSource;

    }

    public ConcernMetricsTable getMetrics() throws IOException {
        ConcernMetricsTable concernMetricsTable = new ConcernMetricsTable();
        Map<String, String> codeMap = codeSource.getCodeSource();
        for (Entry<String, String> entry : codeMap.entrySet()) {
            String key = entry.getKey();
            String source = entry.getValue();
            try {
                List<IConcernMetric> concernMetrics = metricsSource.getConcernMetrics();
                for (IConcernMetric concernMetric : concernMetrics) {
                    concernMetric.parse(source);
                }
                concernMetricsTable.getHash().put(key, concernMetrics);
            } catch (ParseException e) {
                throw new IOException("Error parsing " + key + ": " + e.toString());
            }
        }

        return concernMetricsTable;
    }
}
