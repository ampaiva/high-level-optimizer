package com.ampaiva.hlo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.ampaiva.hlo.util.Helper;
import com.ampaiva.hlo.util.Helper2;

public class ParserTest {

    private static final String SRC_TEST_RESOURCES = "src/test/resources";

    @Test
    public void testParserClass() throws ParseException, IOException {
        String className = "PointTestClass";
        CompilationUnit cu = getCU(className);
        System.out.println(cu);
        Parser.changeToPublic(cu);
        Parser.inlineGetSet(cu);
        System.out.println(cu);
        saveCU(className, cu);
        testCU(className);
    }

    @Test
    public void testChangeToPublic() throws ParseException, IOException {
        String className = "SimpleVarTestClass";
        CompilationUnit cu = getCU(className);
        Parser.changeToPublic(cu);
        saveCU(className, cu);
        testCU(className);
    }

    private void testCU(String className) throws IOException {
        String expected = Helper.readClass(SRC_TEST_RESOURCES, "com.ampaiva.expected." + className);
        String out = Helper.readClass(SRC_TEST_RESOURCES, "com.ampaiva.out." + className);
        assertEquals(expected, out);
    }

    private void saveCU(String className, CompilationUnit cu) throws IOException {
        Helper2.writeFile(Helper.createFile(SRC_TEST_RESOURCES, "com.ampaiva.out", className), cu.toString());
    }

    private CompilationUnit getCU(String className) throws ParseException {
        File fileIn = Helper.createFile(SRC_TEST_RESOURCES, "com.ampaiva.in", className);

        CompilationUnit cu = Parser.parserClass(fileIn);
        assertNotNull(cu);
        return cu;
    }

    @Test
    public void testChangeToFinal() throws ParseException, IOException {
        String[] classNames = { "DuckTestClass", "WoodDuckTestClass" };
        List<CompilationUnit> cus = new ArrayList<CompilationUnit>(2);
        for (String className : classNames) {
            cus.add(getCU(className));
        }

        Parser.changeToFinal(cus);
        for (CompilationUnit cu : cus) {
            String className = cu.getTypes().get(0).getName();
            saveCU(className, cu);
            testCU(className);
        }
    }
}
