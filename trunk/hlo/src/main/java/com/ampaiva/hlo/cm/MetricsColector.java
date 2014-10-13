package com.ampaiva.hlo.cm;

import japa.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.ampaiva.hlo.util.Helper;
import com.ampaiva.hlo.util.ProjectVisitor;

public class MetricsColector {
    private final Map<String, String> cus = new HashMap<String, String>();

    public MetricsColector addFolder(String sourceFolder) throws ParseException, FileNotFoundException, IOException {
        return addFiles(sourceFolder, ".*");
    }

    public MetricsColector addFiles(String sourceFolder, String classRegEx) throws ParseException,
            FileNotFoundException, IOException {
        return addFiles(sourceFolder, classRegEx, true);
    }

    public MetricsColector addFiles(String sourceFolder, String classRegEx, boolean searchChildrenFolders)
            throws ParseException, FileNotFoundException, IOException {
        ProjectVisitor projectVisitor = new ProjectVisitor(sourceFolder, new String[] { "" }, classRegEx,
                searchChildrenFolders);
        cus.putAll(projectVisitor.getCUS());
        return this;
    }

    public ConcernMetricsTable getMetrics() throws ParseException {
        ConcernMetricsTable concernMetricsTable = new ConcernMetricsTable();
        for (Entry<String, String> entry : cus.entrySet()) {
            LOCC locc = new LOCC(Helper.convertString2InputStream(entry.getValue()));
            concernMetricsTable.getHash().put(locc.getKey(), locc);
        }
        return concernMetricsTable;

    }

    public MetricsColector addInputStream(InputStream stringBufferInputStream) throws ParseException, IOException {
        ProjectVisitor.getCU(cus, stringBufferInputStream);
        return this;
    }
}
