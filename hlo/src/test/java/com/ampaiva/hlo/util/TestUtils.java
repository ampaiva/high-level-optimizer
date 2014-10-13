package com.ampaiva.hlo.util;

import static org.junit.Assert.assertNotNull;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;

import java.io.File;
import java.io.IOException;

public class TestUtils {

    private static final String COM_AMPAIVA_IN = "com.ampaiva.in";
    private static final String SRC_TEST_RESOURCES = "src/test/resources";

    public static void saveCU(String className, CompilationUnit cu) throws IOException {
        Helper.writeFile(Helper.createFile(SRC_TEST_RESOURCES, "com.ampaiva.out", className), cu.toString());
    }

    public static File getCU(String className) throws ParseException {
        return getCU(null, className);
    }

    public static File getCU(String subPackage, String className) throws ParseException {
        String packageName = COM_AMPAIVA_IN;
        if (subPackage != null && subPackage.length() > 0) {
            packageName = packageName + "." + subPackage;
        }
        File fileIn = Helper.createFile(SRC_TEST_RESOURCES, packageName, className);

        return fileIn;
    }

    public static CompilationUnit getCUBySource(String source) throws ParseException {
        CompilationUnit cu = Helper.parserString(source);

        assertNotNull(cu);
        return cu;
    }
}
