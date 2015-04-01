package com.ampaiva.hlo.util;

/*
 * Copyright (C) 2008 J�lio Vilmar Gesser.
 * 
 * This file is part of Java 1.5 parser and Abstract Syntax Tree.
 *
 * Java 1.5 parser and Abstract Syntax Tree is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Java 1.5 parser and Abstract Syntax Tree is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Java 1.5 parser and Abstract Syntax Tree.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 30/06/2008
 */

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Julio Vilmar Gesser
 */
public final class Helper {

    private Helper() {
        // hide the constructor
    }

    private static File getFile(String sourceFolder, String clazz) {
        String folder = getFolderByPackage(sourceFolder, clazz);

        return new File(folder + ".java");
    }

    public static CompilationUnit parserClass(File fileIn) throws ParseException {
        try {
            return JavaParser.parse(fileIn);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static CompilationUnit parserClass(InputStream inputStream) throws ParseException {
        return JavaParser.parse(inputStream);
    }

    public static CompilationUnit parserClass(String sourceFolder, String clazz) throws ParseException {
        return parserClass(getFile(sourceFolder, clazz));
    }

    public static InputStream convertFile2InputStream(File file) throws FileNotFoundException {
        return new FileInputStream(file);
    }

    public static String convertFile2String(File file) throws ParseException, IOException {
        return convertInputStream2String(convertFile2InputStream(file));
    }

    public static InputStream convertString2InputStream(String source) throws ParseException {
        return new ByteArrayInputStream(source.getBytes());
    }

    public static String convertInputStream2String(InputStream in) throws IOException {
        StringBuilder out = new StringBuilder();
        byte[] buffer = new byte[4096];
        int readed = 0;

        while ((readed = in.read(buffer)) > 0) {
            out.append(new String(buffer, 0, readed));
        }
        return out.toString();
    }

    public static CompilationUnit parserString(String source) throws ParseException {
        return JavaParser.parse(new ByteArrayInputStream(source.getBytes()));
    }

    public static String readClass(String sourceFolder, String clazz) throws IOException {
        return readFile(getFile(sourceFolder, clazz));
    }

    public static File[] getFiles(String sourceFolder, String packageName) {
        String folder = getFolderByPackage(sourceFolder, packageName);
        return new File(folder).listFiles();
    }

    public static List<File> getFilesRecursevely(String sourceFolder) {
        return getFilesRecursevely(sourceFolder, ".*");
    }

    public static List<File> getFilesRecursevely(final String sourceFolder, final String classRegEx) {
        return getFilesRecursevely(sourceFolder, classRegEx, true);
    }

    public static List<File> getFilesRecursevely(String sourceFolder, final String classRegEx,
            boolean searchChildrenFolders) {
        List<File> files = new ArrayList<File>();
        File folder = new File(sourceFolder);
        if (!folder.exists()) {
            throw new IllegalArgumentException("Folder " + sourceFolder + " does not exist");
        }
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException(sourceFolder + " is not a folder");
        }
        File[] filesAndFolders = new File(sourceFolder).listFiles(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                if (!name.toLowerCase().endsWith(".java")) {
                    return true;
                }
                return name.substring(0, name.length() - 5).matches(classRegEx);
            }
        });
        for (File file : filesAndFolders) {
            if (file.isDirectory()) {
                if (searchChildrenFolders) {
                    files.addAll(getFilesRecursevely(file.getAbsolutePath()));
                }
                continue;
            }
            files.add(file);
        }
        return files;
    }

    public static File createFile(String sourceFolder, String packageName, String clazz) {
        String folder = getFolderByPackage(sourceFolder, packageName);
        new File(folder).mkdirs();
        return new File(folder, clazz + ".java");
    }

    private static String getFolderByPackage(String sourceFolder, String packageName) {
        String folder = "." + File.separator + sourceFolder;
        String[] dirs = packageName.split("\\.");
        for (String dir : dirs) {
            folder = folder + File.separator + dir;
        }
        return folder;
    }

    public static String readFile(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        try {
            StringBuilder ret = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                ret.append(line);
                ret.append("\n");
            }
            return ret.toString();
        } finally {
            reader.close();
        }
    }

}
