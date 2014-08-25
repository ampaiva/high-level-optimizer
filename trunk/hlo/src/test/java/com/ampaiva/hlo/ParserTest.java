package com.ampaiva.hlo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class ParserTest {

    private static final String SRC_TEST_RESOURCES = "src/test/resources";

    @Test
    public void testParserClass() throws ParseException, IOException {
        String className = "PointTestClass";
        File fileIn = Helper.createFile(SRC_TEST_RESOURCES, "com.ampaiva.in", className);

        CompilationUnit cu = Parser.parserClass(fileIn);
        assertNotNull(cu);
        System.out.println(cu);
        Parser.changeToPublic(cu);
        Parser.inlineGetSet(cu);
        System.out.println(cu);
        Helper.writeFile(Helper.createFile(SRC_TEST_RESOURCES, "com.ampaiva.out", className), cu.toString());
        String expected = Helper.readClass(SRC_TEST_RESOURCES, "com.ampaiva.expected." + className);
        String out = Helper.readClass(SRC_TEST_RESOURCES, "com.ampaiva.out." + className);
        assertEquals(expected, out);
    }
}
