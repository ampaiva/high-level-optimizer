package com.ampaiva.hlo.util;

import japa.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class SourceColector {
    private final List<String> sources = new ArrayList<String>();

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
            sources.add(entry.getValue());
        }
        return this;
    }

    public SourceColector addInputStream(InputStream stringBufferInputStream) throws ParseException, IOException {
        sources.add(Helper.convertInputStream2String(stringBufferInputStream));
        return this;
    }

    public List<String> getSources() {
        return sources;
    }
}
