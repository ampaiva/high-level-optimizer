package com.ampaiva.hlo.util;

import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.TypeDeclaration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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

    public Map<String, String> getCUS() throws ParseException, FileNotFoundException, IOException {
        Map<String, String> cus = new HashMap<String, String>();
        for (String sourceSubFolder : sourceSubFolders) {
            String sourceFolder = rootFolder + File.separator + sourceSubFolder;
            List<File> files = Helper.getFilesRecursevely(sourceFolder, classRegEx, searchChildrenFolders);
            for (File file : files) {
                getCU(cus, file);
            }
        }
        return cus;
    }

    public void getCU(Map<String, String> cus, File file) throws ParseException, FileNotFoundException, IOException {
        String cu = Helper.convertInputStream2String(Helper.convertFile2InputStream(file));
        put(cus, cu);
    }

    public static void getCU(Map<String, String> cus, InputStream in) throws ParseException, IOException {
        put(cus, Helper.convertInputStream2String(in));
    }

    public static Map<String, String> getCU(InputStream file) throws ParseException, IOException {
        Map<String, String> cus = new HashMap<String, String>();
        getCU(cus, file);
        return cus;
    }

    private static void put(Map<String, String> cus, String source) throws ParseException {
        CompilationUnit cu = Helper.parserString(source);
        List<TypeDeclaration> types = cu.getTypes();
        for (TypeDeclaration typeDeclaration : types) {
            StringBuilder sb = new StringBuilder();
            if (cu.getPackage() != null) {
                sb.append(cu.getPackage().getName()).append(".");
            }
            sb.append(typeDeclaration.getName());
            cus.put(sb.toString(), source);
        }
    }
}
