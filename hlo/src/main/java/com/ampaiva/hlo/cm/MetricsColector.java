package com.ampaiva.hlo.cm;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ampaiva.hlo.util.view.IProgressUpdate;
import com.ampaiva.hlo.util.view.ProgressUpdate;
import com.github.javaparser.ParseException;

public class MetricsColector {
    private final IMetricsSource metricsSource;

    public MetricsColector(IMetricsSource metricsSource) {
        this.metricsSource = metricsSource;
    }

    public ConcernMetricsTable getMetrics(Map<String, String> codeMap) throws IOException {
        ConcernMetricsTable concernMetricsTable = new ConcernMetricsTable();
        IProgressUpdate update = ProgressUpdate.start("Processing code source", codeMap.entrySet().size());
        for (Entry<String, String> entry : codeMap.entrySet()) {
            String key = entry.getKey();
            update.beginIndex(key);
            String source = entry.getValue();
            try {
                List<IConcernMetric> concernMetrics = metricsSource.getConcernMetrics();
                for (IConcernMetric concernMetric : concernMetrics) {
                    concernMetric.parse(source);
                }
                concernMetricsTable.getHash().put(key, concernMetrics);
            } catch (ParseException e) {
                System.err.println("Error parsing " + key + ": " + e.toString());
            }
        }

        return concernMetricsTable;
    }
}
