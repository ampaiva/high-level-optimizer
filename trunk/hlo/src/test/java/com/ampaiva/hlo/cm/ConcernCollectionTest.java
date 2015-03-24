package com.ampaiva.hlo.cm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import japa.parser.ParseException;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ConcernCollectionTest {

    private ConcernCollection loccBySource;

    /**
     * Setup mocks before each test.
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        loccBySource = new ConcernCollection();
    }

    /**
     * Verifies all mocks after each test.
     */
    @After()
    public void tearDown() {
        loccBySource = null;
    }

    private ConcernCollection getLOCCBySource(String source) throws ParseException {
        loccBySource.parse(source);
        return loccBySource;
    }

    @Test
    public void testGetMetricInSimpleClass() throws ParseException {
        StringBuilder sb = new StringBuilder();
        sb.append("public class SimpleClass {\n");
        sb.append("    public SimpleClass(){\n");
        sb.append("    }\n");
        sb.append("}");
        ConcernCollection concernCollection = getLOCCBySource(sb.toString());
        ConcernMetricNodes nodes = concernCollection.getNodes();
        assertEquals(0, nodes.size());
        List<List<String>> sequences = concernCollection.getSequences();
        assertNotNull(sequences);
        assertEquals(1, sequences.size());
        List<String> sequence0 = sequences.get(0);
        assertEquals(0, sequence0.size());

        List<String> methodNames = concernCollection.getMethodNames();
        assertNotNull(methodNames);
        assertEquals(sequences.size(), methodNames.size());
        assertEquals("SimpleClass.SimpleClass", methodNames.get(0));
    }

    @Test
    public void testGetMetricInSimpleClassWithOneCall() throws ParseException {
        StringBuilder sb = new StringBuilder();
        sb.append("public class SimpleClass {\n");
        sb.append("    public SimpleClass(){\n");
        sb.append("       System.out.println();\n");
        sb.append("    }\n");
        sb.append("}");
        IMethodCalls concernCollection = getLOCCBySource(sb.toString());
        List<List<String>> sequences = concernCollection.getSequences();
        assertNotNull(sequences);
        assertEquals(1, sequences.size());
        List<String> sequence0 = sequences.get(0);
        assertEquals(1, sequence0.size());
        assertEquals("System.out.println", sequence0.get(0));

        List<String> methodNames = concernCollection.getMethodNames();
        assertNotNull(methodNames);
        assertEquals(sequences.size(), methodNames.size());
        assertEquals("SimpleClass.SimpleClass", methodNames.get(0));
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
        IMethodCalls concernCollection = getLOCCBySource(sb.toString());
        List<List<String>> sequences = concernCollection.getSequences();
        assertNotNull(sequences);
        assertEquals(2, sequences.size());
        List<String> sequence0 = sequences.get(0);
        assertEquals(2, sequence0.size());
        assertEquals("com.ampaiva.test.SimpleClass.foo", sequence0.get(0));
        assertEquals("java.lang.System.out.println", sequence0.get(1));

        List<String> sequence1 = sequences.get(1);
        assertEquals(0, sequence1.size());

        List<String> methodNames = concernCollection.getMethodNames();
        assertNotNull(methodNames);
        assertEquals(sequences.size(), methodNames.size());
        assertEquals("com.ampaiva.test.SimpleClass.SimpleClass", methodNames.get(0));
        assertEquals("com.ampaiva.test.SimpleClass.foo", methodNames.get(1));
    }

    @Test
    public void testGetMetricInSimpleClassWithVariableDeclarationWithImport() throws ParseException {
        StringBuilder sb = new StringBuilder();
        sb.append("package com.ampaiva.test;");
        sb.append("import com.ampaiva.other.FooClass;");
        sb.append("public class SimpleClass {\n");
        sb.append("    public SimpleClass(){\n");
        sb.append("       FooClass variable = new FooClass();\n");
        sb.append("       variable.foo();\n");
        sb.append("    }\n");
        sb.append("void foo() {\n");
        sb.append("    }\n");
        sb.append("}");
        IMethodCalls concernCollection = getLOCCBySource(sb.toString());
        List<List<String>> sequences = concernCollection.getSequences();
        assertNotNull(sequences);
        assertEquals(2, sequences.size());
        List<String> sequence0 = sequences.get(0);
        assertEquals(2, sequence0.size());
        assertEquals("com.ampaiva.other.FooClass.FooClass", sequence0.get(0));
        assertEquals("com.ampaiva.other.FooClass.foo", sequence0.get(1));

        List<String> sequence1 = sequences.get(1);
        assertEquals(0, sequence1.size());

        List<String> methodNames = concernCollection.getMethodNames();
        assertNotNull(methodNames);
        assertEquals(sequences.size(), methodNames.size());
        assertEquals("com.ampaiva.test.SimpleClass.SimpleClass", methodNames.get(0));
        assertEquals("com.ampaiva.test.SimpleClass.foo", methodNames.get(1));
    }

    @Test
    public void testGetMetricInSimpleClassWithVariableDeclarationWithTwoImport() throws ParseException {
        StringBuilder sb = new StringBuilder();
        sb.append("package com.ampaiva.test;");
        sb.append("import com.ampaiva.other.FooClass1;");
        sb.append("import com.ampaiva.other.FooClass2;");
        sb.append("public class SimpleClass {\n");
        sb.append("    public SimpleClass(){\n");
        sb.append("       FooClass1 variable = new FooClass1();\n");
        sb.append("       variable.foo();\n");
        sb.append("    }\n");
        sb.append("void foo() {\n");
        sb.append("       FooClass2 variable = new FooClass2();\n");
        sb.append("       variable.foo();\n");
        sb.append("    }\n");
        sb.append("}");
        IMethodCalls concernCollection = getLOCCBySource(sb.toString());
        List<List<String>> sequences = concernCollection.getSequences();
        assertNotNull(sequences);
        assertEquals(2, sequences.size());
        List<String> sequence0 = sequences.get(0);
        assertEquals(2, sequence0.size());
        assertEquals("com.ampaiva.other.FooClass1.FooClass1", sequence0.get(0));
        assertEquals("com.ampaiva.other.FooClass1.foo", sequence0.get(1));
        List<String> sequence1 = sequences.get(1);
        assertEquals(2, sequence1.size());
        assertEquals("com.ampaiva.other.FooClass2.FooClass2", sequence1.get(0));
        assertEquals("com.ampaiva.other.FooClass2.foo", sequence1.get(1));

        List<String> methodNames = concernCollection.getMethodNames();
        assertNotNull(methodNames);
        assertEquals(sequences.size(), methodNames.size());
        assertEquals("com.ampaiva.test.SimpleClass.SimpleClass", methodNames.get(0));
        assertEquals("com.ampaiva.test.SimpleClass.foo", methodNames.get(1));
    }

    @Test
    public void testGetMetricInSimpleClassWithVariableDeclarationWithoutImport() throws ParseException {
        StringBuilder sb = new StringBuilder();
        sb.append("package com.ampaiva.test;");
        sb.append("public class SimpleClass {\n");
        sb.append("    public SimpleClass(){\n");
        sb.append("       com.ampaiva.other.FooClass variable = new com.ampaiva.other.FooClass();\n");
        sb.append("       variable.foo();\n");
        sb.append("    }\n");
        sb.append("}");
        IMethodCalls concernCollection = getLOCCBySource(sb.toString());
        List<List<String>> sequences = concernCollection.getSequences();
        assertNotNull(sequences);
        assertEquals(1, sequences.size());
        List<String> sequence0 = sequences.get(0);
        assertEquals(2, sequence0.size());
        assertEquals("com.ampaiva.other.FooClass.FooClass", sequence0.get(0));
        assertEquals("com.ampaiva.other.FooClass.foo", sequence0.get(1));
    }

    @Test
    public void testgetSequencesObjCreationInitClass() throws ParseException {
        StringBuilder sb = new StringBuilder();
        sb.append("package com.ampaiva.test;");
        sb.append("public class SimpleClass {\n");
        sb.append("       com.ampaiva.other.FooClass variable = new com.ampaiva.other.FooClass();\n");
        sb.append("}");
        ConcernCollection concernCollection = getLOCCBySource(sb.toString());
        List<List<String>> sequences = concernCollection.getSequences();
        assertNotNull(sequences);
        assertEquals(1, sequences.size());
        List<String> sequence0 = sequences.get(0);
        assertEquals(1, sequence0.size());
        assertEquals("com.ampaiva.other.FooClass.FooClass", sequence0.get(0));

        List<String> methodNames = concernCollection.getMethodNames();
        assertNotNull(methodNames);
        assertEquals(sequences.size(), methodNames.size());
        assertEquals("com.ampaiva.test.SimpleClass.", methodNames.get(0));
    }

    @Test
    public void testgetSequencesMethodCallInitClass() throws ParseException {
        StringBuilder sb = new StringBuilder();
        sb.append("package com.ampaiva.test;");
        sb.append("public class SimpleClass {\n");
        sb.append("      private String[] fooStr = {com.ampaiva.other.FooClass.foo()};\n");
        sb.append("}");
        ConcernCollection concernCollection = getLOCCBySource(sb.toString());
        List<List<String>> sequences = concernCollection.getSequences();
        assertNotNull(sequences);
        assertEquals(1, sequences.size());
        List<String> sequence0 = sequences.get(0);
        assertEquals(1, sequence0.size());
        assertEquals("com.ampaiva.other.FooClass.foo", sequence0.get(0));

        List<String> methodNames = concernCollection.getMethodNames();
        assertNotNull(methodNames);
        assertEquals(sequences.size(), methodNames.size());
        assertEquals("com.ampaiva.test.SimpleClass.", methodNames.get(0));
    }
}
