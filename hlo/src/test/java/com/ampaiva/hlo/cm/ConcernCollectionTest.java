package com.ampaiva.hlo.cm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import com.github.javaparser.ParseException;

import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ConcernCollectionTest {

    private ConcernCollection concernCollection;

    /**
     * Setup mocks before each test.
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        concernCollection = new ConcernCollection();
    }

    /**
     * Verifies all mocks after each test.
     */
    @After()
    public void tearDown() {
        concernCollection = null;
    }

    private ConcernCollection getCCBySource(String source) throws ParseException {
        concernCollection.parse(source);
        return concernCollection;
    }

    @Test
    public void testGetMetricInSimpleClass() throws ParseException {
        StringBuilder sb = new StringBuilder();
        sb.append("public class SimpleClass {\n");
        sb.append("    public SimpleClass(){\n");
        sb.append("    }\n");
        sb.append("}");
        ConcernCollection concernCollection = getCCBySource(sb.toString());
        ConcernMetricNodes nodes = concernCollection.getNodes();
        assertEquals(0, nodes.size());
        List<List<String>> sequences = concernCollection.getSequences();
        assertNotNull(sequences);
        assertEquals(2, sequences.size());
        List<String> sequence1 = sequences.get(1);
        assertEquals(0, sequence1.size());

        List<String> methodNames = concernCollection.getMethodNames();
        assertNotNull(methodNames);
        assertEquals(2, methodNames.size());
        assertEquals("SimpleClass.", methodNames.get(0));
        assertEquals("SimpleClass.SimpleClass", methodNames.get(1));

        List<String> sources = concernCollection.getMethodSources();
        assertNotNull(sources);
        assertEquals(methodNames.size(), sources.size());
        assertEquals("public  SimpleClass() {\r\n}", sources.get(1));
    }

    @Test
    public void testGetMetricInSimpleClassWithOneCall() throws ParseException {
        StringBuilder sb = new StringBuilder();
        sb.append("public class SimpleClass {\n");
        sb.append("    public SimpleClass(){\n");
        sb.append("       System.out.println();\n");
        sb.append("    }\n");
        sb.append("}");
        IMethodCalls concernCollection = getCCBySource(sb.toString());
        List<List<String>> sequences = concernCollection.getSequences();
        assertNotNull(sequences);
        assertEquals(2, sequences.size());
        List<String> sequence1 = sequences.get(1);
        assertEquals(1, sequence1.size());

        List<String> methodNames = concernCollection.getMethodNames();
        assertNotNull(methodNames);
        assertEquals(2, methodNames.size());
        assertEquals("SimpleClass.", methodNames.get(0));
        assertEquals("SimpleClass.SimpleClass", methodNames.get(1));

        List<String> sources = concernCollection.getMethodSources();
        assertNotNull(sources);
        assertEquals(methodNames.size(), sources.size());
        assertEquals("public  SimpleClass() {\r\n    System.out.println();\r\n}", sources.get(1));
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
        IMethodCalls concernCollection = getCCBySource(sb.toString());
        List<List<String>> sequences = concernCollection.getSequences();
        assertNotNull(sequences);
        assertEquals(3, sequences.size());
        List<String> sequence1 = sequences.get(1);
        assertEquals(2, sequence1.size());
        assertEquals("com.ampaiva.test.SimpleClass.foo", sequence1.get(0));
        assertEquals("java.lang.System.out.println", sequence1.get(1));

        List<String> sequence2 = sequences.get(2);
        assertEquals(0, sequence2.size());

        List<String> methodNames = concernCollection.getMethodNames();
        assertNotNull(methodNames);
        assertEquals(sequences.size(), methodNames.size());
        assertEquals("com.ampaiva.test.SimpleClass.", methodNames.get(0));
        assertEquals("com.ampaiva.test.SimpleClass.SimpleClass", methodNames.get(1));
        assertEquals("com.ampaiva.test.SimpleClass.foo", methodNames.get(2));

        List<String> sources = concernCollection.getMethodSources();
        assertNotNull(sources);
        assertEquals(methodNames.size(), sources.size());
        assertEquals("public  SimpleClass() {\r\n    foo();\r\n    out.println();\r\n}", sources.get(1));
        assertEquals("void foo() {\r\n}", sources.get(2));
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
        sb.append("    void foo() {\n");
        sb.append("    }\n");
        sb.append("}");
        IMethodCalls concernCollection = getCCBySource(sb.toString());
        List<List<String>> sequences = concernCollection.getSequences();
        assertNotNull(sequences);
        assertEquals(3, sequences.size());

        List<String> sequence0 = sequences.get(0);
        assertEquals(0, sequence0.size());

        List<String> sequence1 = sequences.get(1);
        assertEquals(2, sequence1.size());
        assertEquals("com.ampaiva.other.FooClass.FooClass", sequence1.get(0));
        assertEquals("com.ampaiva.other.FooClass.foo", sequence1.get(1));

        List<String> sequence2 = sequences.get(2);
        assertEquals(0, sequence2.size());

        List<String> methodNames = concernCollection.getMethodNames();
        List<String> methodSources = concernCollection.getMethodSources();
        assertNotNull(methodNames);
        assertNotNull(methodSources);
        assertEquals(methodSources.size(), methodNames.size());
        assertEquals("com.ampaiva.test.SimpleClass.", methodNames.get(0));
        assertEquals("com.ampaiva.test.SimpleClass.SimpleClass", methodNames.get(1));
        assertEquals("com.ampaiva.test.SimpleClass.foo", methodNames.get(2));
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
        IMethodCalls concernCollection = getCCBySource(sb.toString());
        List<List<String>> sequences = concernCollection.getSequences();
        assertNotNull(sequences);
        assertEquals(3, sequences.size());
        List<String> sequence1 = sequences.get(1);
        assertEquals(2, sequence1.size());
        assertEquals("com.ampaiva.other.FooClass1.FooClass1", sequence1.get(0));
        assertEquals("com.ampaiva.other.FooClass1.foo", sequence1.get(1));
        List<String> sequence2 = sequences.get(2);
        assertEquals(2, sequence2.size());
        assertEquals("com.ampaiva.other.FooClass2.FooClass2", sequence2.get(0));
        assertEquals("com.ampaiva.other.FooClass2.foo", sequence2.get(1));

        List<String> methodNames = concernCollection.getMethodNames();
        assertNotNull(methodNames);
        assertEquals(sequences.size(), methodNames.size());
        assertEquals("com.ampaiva.test.SimpleClass.", methodNames.get(0));
        assertEquals("com.ampaiva.test.SimpleClass.SimpleClass", methodNames.get(1));
        assertEquals("com.ampaiva.test.SimpleClass.foo", methodNames.get(2));
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
        IMethodCalls concernCollection = getCCBySource(sb.toString());
        List<List<String>> sequences = concernCollection.getSequences();
        assertNotNull(sequences);
        assertEquals(2, sequences.size());
        List<String> sequence1 = sequences.get(1);
        assertEquals(2, sequence1.size());
        assertEquals("com.ampaiva.other.FooClass.FooClass", sequence1.get(0));
        assertEquals("com.ampaiva.other.FooClass.foo", sequence1.get(1));
    }

    @Test
    public void testgetSequencesObjCreationInitClass() throws ParseException {
        StringBuilder sb = new StringBuilder();
        sb.append("package com.ampaiva.test;");
        sb.append("public class SimpleClass {\n");
        sb.append("       com.ampaiva.other.FooClass variable = new com.ampaiva.other.FooClass();\n");
        sb.append("}");
        ConcernCollection concernCollection = getCCBySource(sb.toString());
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
        ConcernCollection concernCollection = getCCBySource(sb.toString());
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

    @Test
    public void testCatchVariable() throws ParseException {
        StringBuilder sb = new StringBuilder();
        sb.append("package com.ampaiva.test;");
        sb.append("public class SimpleClass {\n");
        sb.append("    public SimpleClass(){\n");
        sb.append("      try{\n");
        sb.append("         foo();\n");
        sb.append("      }\n");
        sb.append("      catch(ServiceLocatorException ne){\n");
        sb.append("         ne.getMessage();\n");
        sb.append("      }\n");
        sb.append("    }\n");
        sb.append("}");
        ConcernCollection concernCollection = getCCBySource(sb.toString());
        List<List<String>> sequences = concernCollection.getSequences();
        assertNotNull(sequences);
        assertEquals(2, sequences.size());
        List<String> sequence0 = sequences.get(0);
        assertEquals(0, sequence0.size());
        List<String> sequence1 = sequences.get(1);
        assertEquals(2, sequence1.size());
        assertEquals("com.ampaiva.test.SimpleClass.foo", sequence1.get(0));
        assertEquals("com.ampaiva.test.ServiceLocatorException.getMessage", sequence1.get(1));
    }

    @Test
    public void testMethodParameter() throws ParseException {
        StringBuilder sb = new StringBuilder();
        sb.append("package com.ampaiva.test;");
        sb.append("import com.ampaiva.other.FooClass1;");
        sb.append("public class SimpleClass {\n");
        sb.append("    public void foo(FooClass1 foo1, FooClass2 foo2){\n");
        sb.append("      foo1.foo();");
        sb.append("      foo2.foo();");
        sb.append("    }\n");
        sb.append("}");
        ConcernCollection concernCollection = getCCBySource(sb.toString());
        List<List<String>> sequences = concernCollection.getSequences();
        assertNotNull(sequences);
        assertEquals(2, sequences.size());
        List<String> sequence0 = sequences.get(1);
        assertEquals(2, sequence0.size());
        assertEquals("com.ampaiva.other.FooClass1.foo", sequence0.get(0));
        assertEquals("com.ampaiva.test.FooClass2.foo", sequence0.get(1));
    }

    @Test
    public void testMethodSources() throws ParseException, IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("public class SimpleClass {\n");
        sb.append("    public static Integer x = new Integer(1);");
        sb.append("    public void foo(){\n");
        sb.append("    }\n");
        sb.append("    public static Integer y = new Integer(1);");
        sb.append("}");
        ConcernCollection concernCollection = getCCBySource(sb.toString());
        assertEquals(2, concernCollection.getMethodNames().size());
        assertEquals(concernCollection.getMethodNames().size(), concernCollection.getMethodSources().size());
    }

    @Test
    public void testNestedCalls() throws ParseException {
        StringBuilder sb = new StringBuilder();
        sb.append("package com.ampaiva.test;");
        sb.append("import java.util.List;");
        sb.append("import com.ampaiva.test.model.Clone;");
        sb.append("public class SimpleClass {\n");
        sb.append("    public boolean foo(List<Clone> list){\n");
        sb.append("      boolean result = list.get(10).isAvailable();\n");
        sb.append("      return result;\n");
        sb.append("    }\n");
        sb.append("}");
        ConcernCollection concernCollection = getCCBySource(sb.toString());
        List<List<String>> sequences = concernCollection.getSequences();
        assertNotNull(sequences);
        assertEquals(2, sequences.size());
        List<String> sequence0 = sequences.get(1);
        assertEquals(2, sequence0.size());
        assertEquals("java.util.List.get", sequence0.get(0));
        assertEquals("java.util.List.get.isAvailable", sequence0.get(1));
    }

}
