package com.ampaiva.hlo.cm;

import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.ampaiva.hlo.util.ProjectVisitor;

public class MetricsColector {
    private final Map<String, CompilationUnit> cus = new HashMap<String, CompilationUnit>();

    public MetricsColector addFolder(String sourceFolder) throws ParseException {
        return addFiles(sourceFolder, ".*");
    }

    public MetricsColector addFiles(String sourceFolder, String classRegEx) throws ParseException {
        return addFiles(sourceFolder, classRegEx, true);
    }

    public MetricsColector addFiles(String sourceFolder, String classRegEx, boolean searchChildrenFolders)
            throws ParseException {
        ProjectVisitor projectVisitor = new ProjectVisitor(sourceFolder, new String[] { "" }, classRegEx,
                searchChildrenFolders);
        cus.putAll(projectVisitor.getCUS());
        return this;
    }

    public ConcernMetricsTable getMetrics() throws ParseException {
        ConcernMetricsTable concernMetricsTable = new ConcernMetricsTable();
        for (Entry<String, CompilationUnit> entry : cus.entrySet()) {
            LOCC locc = new LOCC(entry.getValue());
            concernMetricsTable.getHash().put(entry.getKey(), new ConcernMetricEntity("LOCC", locc.getMetric()));
        }
        return concernMetricsTable;

    }
}
