package com.ampaiva.hlo.cm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import japa.parser.ParseException;

import java.util.List;

import org.junit.Test;

import com.ampaiva.hlo.util.Helper;

public class ConcernCollectionTest {

    private ConcernCollection getLOCCBySource(String source) throws ParseException {
        return new ConcernCollection("Test.java", Helper.convertString2InputStream(source));
    }

    @Test
    public void testGetMetricInSimpleClass() throws ParseException {
        StringBuilder sb = new StringBuilder();
        sb.append("public class SimpleClass {\n");
        sb.append("    public SimpleClass(){\n");
        sb.append("    }\n");
        sb.append("}");
        ConcernCollection concernCollection = getLOCCBySource(sb.toString());
        assertEquals("Test.java", concernCollection.getKey());
        ConcernMetricNodes nodes = concernCollection.getNodes();
        assertEquals(0, nodes.size());
        List<List<String>> sequences = concernCollection.getSequences();
        assertNotNull(sequences);
        assertEquals(1, sequences.size());
        List<String> sequence0 = sequences.get(0);
        assertEquals(0, sequence0.size());
    }

    @Test
    public void testGetMetricInSimpleClassWithOneCall() throws ParseException {
        StringBuilder sb = new StringBuilder();
        sb.append("public class SimpleClass {\n");
        sb.append("    public SimpleClass(){\n");
        sb.append("       System.out.println();\n");
        sb.append("    }\n");
        sb.append("}");
        ConcernCollection concernCollection = getLOCCBySource(sb.toString());
        List<List<String>> sequences = concernCollection.getSequences();
        assertNotNull(sequences);
        assertEquals(1, sequences.size());
        List<String> sequence0 = sequences.get(0);
        assertEquals(1, sequence0.size());
        assertEquals("System.out.println", sequence0.get(0));
    }

    @Test
    public void testGetMetricInSimpleClassWithImportsOneCall() throws ParseException {
        StringBuilder sb = new StringBuilder();
        sb.append("package com.ampaiva.test;");
        sb.append("import static java.lang.System.out;");
        sb.append("public class SimpleClass {\n");
        sb.append("    public SimpleClass(){\n");
        sb.append("       foo();\n");
        sb.append("       out.println();\n");
        sb.append("    }\n");
        sb.append("void foo() {\n");
        sb.append("    }\n");
        sb.append("}");
        ConcernCollection concernCollection = getLOCCBySource(sb.toString());
        List<List<String>> sequences = concernCollection.getSequences();
        assertNotNull(sequences);
        assertEquals(1, sequences.size());
        List<String> sequence0 = sequences.get(0);
        assertEquals(2, sequence0.size());
        assertEquals("com.ampaiva.test.SimpleClass.foo", sequence0.get(0));
        assertEquals("java.lang.System.out.println", sequence0.get(1));
    }

}
