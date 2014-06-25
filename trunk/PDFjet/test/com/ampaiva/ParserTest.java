package com.ampaiva;

import static org.junit.Assert.assertNotNull;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParserClass() throws ParseException, IOException {
        File fileIn = Helper.createFile("test-classes", "com.ampaiva", "PointTestClass");

        CompilationUnit cu = Parser.parserClass(fileIn);
        assertNotNull(cu);
        System.out.println(cu);
        Parser.changeToPublic(cu);
        Parser.inlineGetSet(cu);
        System.out.println(cu);
        Helper.writeFile(Helper.createFile("test-classes", "com.ampaiva.out", "PointTestClass"), cu.toString());
    }
}
