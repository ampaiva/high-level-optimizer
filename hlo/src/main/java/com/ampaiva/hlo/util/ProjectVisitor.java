package com.ampaiva.hlo.util;

import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.TypeDeclaration;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectVisitor {
    private final String rootFolder;
    private final String[] sourceSubFolders;
    private final String classRegEx;
    private final boolean searchChildrenFolders;

    public ProjectVisitor(String rootFolder, String[] sourceSubFolders, String classRegEx, boolean searchChildrenFolders) {
        this.rootFolder = rootFolder;
        this.sourceSubFolders = sourceSubFolders;
        this.classRegEx = classRegEx;
        this.searchChildrenFolders = searchChildrenFolders;
    }

    public Map<String, CompilationUnit> getCUS() throws ParseException {
        Map<String, CompilationUnit> cus = new HashMap<String, CompilationUnit>();
        for (String sourceSubFolder : sourceSubFolders) {
            String sourceFolder = rootFolder + File.separator + sourceSubFolder;
            List<File> files = Helper.getFilesRecursevely(sourceFolder, classRegEx, searchChildrenFolders);
            for (File file : files) {
                getCU(cus, file);
            }
        }
        return cus;
    }

    public void getCU(Map<String, CompilationUnit> cus, File file) throws ParseException {
        CompilationUnit cu = Helper.parserClass(file);
        put(cus, cu);
    }

    public static void getCU(Map<String, CompilationUnit> cus, InputStream file) throws ParseException {
        CompilationUnit cu = Helper.parserClass(file);
        put(cus, cu);
    }

    public static Map<String, CompilationUnit> getCU( InputStream file) throws ParseException {
        Map<String, CompilationUnit> cus = new HashMap<String, CompilationUnit>();
        getCU(cus, file);
       return cus;
    }

    private static void put(Map<String, CompilationUnit> cus, CompilationUnit cu) {
        List<TypeDeclaration> types = cu.getTypes();
        for (TypeDeclaration typeDeclaration : types) {
            cus.put(cu.getPackage().getName() + "." + typeDeclaration.getName(), cu);
        }
    }
}
