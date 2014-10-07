package com.ampaiva.hlo.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;

import java.util.Map;

import org.junit.Test;

public class ProjectVisitorTest {

    @Test
    public void testGetCUS() throws ParseException {
        ProjectVisitor projectVisitor = new ProjectVisitor("src/test/resources", new String[] { "com/ampaiva/in/util" });
        Map<String, CompilationUnit> cus = projectVisitor.getCUS();
        assertNotNull(cus);
        assertEquals(4, cus.size());
        assertNotNull(cus.get("com.ampaiva.in.util.Snippet"));
    }
}
