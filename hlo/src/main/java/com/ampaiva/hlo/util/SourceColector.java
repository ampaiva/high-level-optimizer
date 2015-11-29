package com.ampaiva.hlo.util;

import com.github.javaparser.ParseException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class SourceColector {
    private final Map<String, String> sources = new HashMap<String, String>();

    public SourceColector addFolder(String sourceFolder) throws ParseException, FileNotFoundException, IOException {
        return addFiles(sourceFolder, ".*");
    }

    public SourceColector addFiles(String sourceFolder, String classRegEx) throws ParseException,
            FileNotFoundException, IOException {
        return addFiles(sourceFolder, classRegEx, true);
    }

    public SourceColector addFiles(String sourceFolder, String classRegEx, boolean searchChildrenFolders)
            throws ParseException, FileNotFoundException, IOException {
        ProjectVisitor projectVisitor = new ProjectVisitor(sourceFolder, new String[] { "" }, classRegEx,
                searchChildrenFolders);
        for (Entry<String, String> entry : projectVisitor.getCUS().entrySet()) {
            sources.put(entry.getKey(), entry.getValue());
        }
        return this;
    }

    public SourceColector addInputStream(String key, InputStream stringBufferInputStream) throws ParseException,
            IOException {
        sources.put(key, Helper.convertInputStream2String(stringBufferInputStream));
        return this;
    }

    public Map<String, String> getSources() {
        return sources;
    }
}
