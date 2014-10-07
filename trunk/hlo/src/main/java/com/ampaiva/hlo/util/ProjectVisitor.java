package com.ampaiva.hlo.util;

import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.TypeDeclaration;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectVisitor {
    private final String rootFolder;
    private final String[] sourceSubFolders;

    public ProjectVisitor(String rootFolder, String[] sourceSubFolders) {
        this.rootFolder = rootFolder;
        this.sourceSubFolders = sourceSubFolders;
    }

    public Map<String, CompilationUnit> getCUS() throws ParseException {
        Map<String, CompilationUnit> cus = new HashMap<String, CompilationUnit>();
        for (String sourceSubFolder : sourceSubFolders) {
            String sourceFolder = rootFolder + File.separator + sourceSubFolder;
            List<File> files = Helper.getFilesRecursevely(sourceFolder);
            for (File file : files) {
                CompilationUnit cu = Helper.parserClass(file);
                List<TypeDeclaration> types = cu.getTypes();
                for (TypeDeclaration typeDeclaration : types) {
                    cus.put(cu.getPackage().getName() + "." + typeDeclaration.getName(), cu);
                }
            }
        }
        return cus;
    }

}
