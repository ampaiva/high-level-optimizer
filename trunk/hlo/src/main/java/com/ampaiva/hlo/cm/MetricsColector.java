package com.ampaiva.hlo.cm;

import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;

import java.util.Map;
import java.util.Map.Entry;

import com.ampaiva.hlo.util.ProjectVisitor;

public class MetricsColector {

    private final ProjectVisitor projectVisitor;

    public MetricsColector(String sourceFolder) {
        projectVisitor = new ProjectVisitor(sourceFolder, new String[] { "" });
    }

    public ConcernMetricsTable getMetrics() throws ParseException {
        Map<String, CompilationUnit> cus = projectVisitor.getCUS();
        ConcernMetricsTable concernMetricsTable = new ConcernMetricsTable();
        for (Entry<String, CompilationUnit> entry : cus.entrySet()) {
            LOCC locc = new LOCC(entry.getValue());
            concernMetricsTable.getHash().put(entry.getKey(), new ConcernMetricEntity("LOCC", locc.getMetric()));
        }
        return concernMetricsTable;

    }
}
