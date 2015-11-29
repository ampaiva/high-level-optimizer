package com.ampaiva.hlo.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import com.github.javaparser.ParseException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.junit.Test;

public class ProjectVisitorTest {

    @Test
    public void testGetCUS() throws ParseException, FileNotFoundException, IOException {
        ProjectVisitor projectVisitor = new ProjectVisitor("src/test/resources",
                new String[] { "com/ampaiva/in/util" }, ".*", true);
        Map<String, String> cus = projectVisitor.getCUS();
        assertNotNull(cus);
        assertEquals(4, cus.size());
        assertNotNull(cus.get("com.ampaiva.in.util.Snippet"));
    }

    @Test
    public void testGetCUSOnlySnippet() throws ParseException, FileNotFoundException, IOException {
        ProjectVisitor projectVisitor = new ProjectVisitor("src/test/resources",
                new String[] { "com/ampaiva/in/util" }, "Snippet", false);
        Map<String, String> cus = projectVisitor.getCUS();
        assertNotNull(cus);
        assertEquals(1, cus.size());
        assertNotNull(cus.get("com.ampaiva.in.util.Snippet"));
    }

    @Test
    public void testGetCUSOnlySnippetNoPackage() throws ParseException, FileNotFoundException, IOException {
        ProjectVisitor projectVisitor = new ProjectVisitor("src/test/resources", new String[] { "" }, "Snippet", false);
        Map<String, String> cus = projectVisitor.getCUS();
        assertNotNull(cus);
        assertEquals(1, cus.size());
        assertNotNull(cus.get("Snippet"));
    }
}
