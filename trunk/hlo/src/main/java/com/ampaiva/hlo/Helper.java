package com.ampaiva.hlo;
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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringBufferInputStream;

/**
 * @author Julio Vilmar Gesser
 */
final class Helper {

    private Helper() {
        // hide the constructor
    }

    private static File getFile(String sourceFolder, String clazz) {
        String folder = getFolderByPackage(sourceFolder, clazz);

        return new File(folder + ".java");
    }

    public static CompilationUnit parserClass(String sourceFolder, String clazz) throws ParseException {
        try {
            return JavaParser.parse(getFile(sourceFolder, clazz));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static CompilationUnit parserString(String source) throws ParseException {
        return JavaParser.parse(new StringBufferInputStream(source));
    }

    public static String readClass(String sourceFolder, String clazz) throws IOException {
        return readFile(getFile(sourceFolder, clazz));
    }

    public static File[] getFiles(String sourceFolder, String packageName) {
        String folder = getFolderByPackage(sourceFolder, packageName);
        return new File(folder).listFiles();
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

    public static void writeFile(File file, String contents) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
        try {
            writer.write(contents);
        } finally {
            writer.close();
        }
    }

}
