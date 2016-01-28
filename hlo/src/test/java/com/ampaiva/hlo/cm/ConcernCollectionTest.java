package com.ampaiva.hlo.cm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ampaiva.hlo.util.SourceHandler;
import com.github.javaparser.ParseException;

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

        List<SourceHandler> sources = concernCollection.getMethodSources();
        assertNotNull(sources);
        assertEquals(methodNames.size(), sources.size());
        assertEquals("public SimpleClass(){\n    }", sources.get(1).getSnippet());
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

        List<SourceHandler> sources = concernCollection.getMethodSources();
        assertNotNull(sources);
        assertEquals(methodNames.size(), sources.size());
        assertEquals("public SimpleClass(){\n       System.out.println();\n    }", sources.get(1).getSnippet());
    }

    @Test
    public void testGetMetricInSimpleClassWithOneCallCastExpr() throws ParseException {
        StringBuilder sb = new StringBuilder();
        sb.append("public class SimpleClass {\n");
        sb.append("    public int getInt(){\n");
        sb.append("      return((Number) o[0]).intValue();\n");
        sb.append("    }\n");
        sb.append("}");
        IMethodCalls concernCollection = getCCBySource(sb.toString());
        List<List<String>> sequences = concernCollection.getSequences();
        assertNotNull(sequences);
        assertEquals(2, sequences.size());
        List<String> sequence1 = sequences.get(1);
        assertEquals(1, sequence1.size());
        assertEquals("Number.intValue", sequence1.get(0));

        List<String> methodNames = concernCollection.getMethodNames();
        assertNotNull(methodNames);
        assertEquals(2, methodNames.size());
        assertEquals("SimpleClass.", methodNames.get(0));
        assertEquals("SimpleClass.getInt", methodNames.get(1));

        List<SourceHandler> sources = concernCollection.getMethodSources();
        assertNotNull(sources);
        assertEquals(methodNames.size(), sources.size());
        assertEquals("public int getInt(){\n      return((Number) o[0]).intValue();\n    }",
                sources.get(1).getSnippet());
    }

    @Test
    public void testGetMetricInSimpleClassWithObjectCreationExpr() throws ParseException {
        StringBuilder sb = new StringBuilder();
        sb.append("import com.floreantpos.model.dao.RefundTransactionDAO;");
        sb.append("public class SimpleClass {\n");
        sb.append("    public void create(){\n");
        sb.append("      new RefundTransactionDAO().getTotalRefundForTerminal(terminal);\n");
        sb.append("    }\n");
        sb.append("}");
        IMethodCalls concernCollection = getCCBySource(sb.toString());
        List<String> methodNames = concernCollection.getMethodNames();
        assertNotNull(methodNames);
        assertEquals(2, methodNames.size());
        assertEquals("SimpleClass.", methodNames.get(0));
        assertEquals("SimpleClass.create", methodNames.get(1));

        List<List<String>> sequences = concernCollection.getSequences();
        assertNotNull(sequences);
        assertEquals(2, sequences.size());
        List<String> sequence1 = sequences.get(1);
        assertEquals(2, sequence1.size());
        assertEquals("com.floreantpos.model.dao.RefundTransactionDAO.RefundTransactionDAO", sequence1.get(0));
        assertEquals("com.floreantpos.model.dao.RefundTransactionDAO.getTotalRefundForTerminal", sequence1.get(1));
    }

    @Test
    public void testGetMetricInSimpleClassWithEnclosedExpr() throws ParseException {
        StringBuilder sb = new StringBuilder();
        sb.append("import org.w3c.dom.Element;");
        sb.append("public class SimpleClass {\n");

        sb.append("    public Element create(){\n");
        sb.append("      Element element;\n");
        sb.append("      (element = ((Element) node)).getTagName();\n");
        sb.append("    }\n");
        sb.append("}");
        IMethodCalls concernCollection = getCCBySource(sb.toString());
        List<String> methodNames = concernCollection.getMethodNames();
        assertNotNull(methodNames);
        assertEquals(2, methodNames.size());
        assertEquals("SimpleClass.", methodNames.get(0));
        assertEquals("SimpleClass.create", methodNames.get(1));

        List<List<String>> methods = concernCollection.getSequences();
        assertNotNull(methods);
        assertEquals(2, methods.size());
        List<String> sequences = methods.get(1);
        assertEquals(1, sequences.size());
        assertEquals("org.w3c.dom.Element.getTagName", sequences.get(0));
    }

    @Test
    public void testGetMetricInSimpleClassWithTryStmt() throws ParseException {
        StringBuilder sb = new StringBuilder();
        sb.append("public class SimpleClass {\n");
        sb.append("    public void methodWithTry(){\n");
        sb.append("      try(Resource r = m0();) {\n");
        sb.append("         m1();\n");
        sb.append("      }\n");
        sb.append("      catch(Exception ex){\n");
        sb.append("         m2();\n");
        sb.append("      }\n");
        sb.append("      finally{\n");
        sb.append("         m3();\n");
        sb.append("      }\n");
        sb.append("    }\n");
        sb.append("}");
        IMethodCalls concernCollection = getCCBySource(sb.toString());
        List<String> methodNames = concernCollection.getMethodNames();
        assertNotNull(methodNames);
        assertEquals(2, methodNames.size());
        assertEquals("SimpleClass.", methodNames.get(0));
        assertEquals("SimpleClass.methodWithTry", methodNames.get(1));

        List<List<String>> methods = concernCollection.getSequences();
        assertNotNull(methods);
        assertEquals(2, methods.size());
        List<String> sequences = methods.get(1);
        assertEquals(4, sequences.size());
        assertEquals("SimpleClass.m0", sequences.get(0));
        assertEquals("SimpleClass.m1", sequences.get(1));
        assertEquals("SimpleClass.m2", sequences.get(2));
        assertEquals("SimpleClass.m3", sequences.get(3));
    }

    @Test
    public void testGetMetricInSimpleDoStmt() throws ParseException {
        StringBuilder sb = new StringBuilder();
        sb.append("public class SimpleClass {\n");
        sb.append("    public void methodWithDo(){\n");
        sb.append("      do{\n");
        sb.append("         m0();\n");
        sb.append("      } while(m1());\n");
        sb.append("    }\n");
        sb.append("}");
        IMethodCalls concernCollection = getCCBySource(sb.toString());
        List<String> methodNames = concernCollection.getMethodNames();
        assertNotNull(methodNames);
        assertEquals(2, methodNames.size());
        assertEquals("SimpleClass.", methodNames.get(0));
        assertEquals("SimpleClass.methodWithDo", methodNames.get(1));

        List<List<String>> methods = concernCollection.getSequences();
        assertNotNull(methods);
        assertEquals(2, methods.size());
        List<String> sequences = methods.get(1);
        assertEquals(2, sequences.size());
        assertEquals("SimpleClass.m0", sequences.get(0));
        assertEquals("SimpleClass.m1", sequences.get(1));
    }

    @Test
    public void testGetMetricInParametersInsidecall() throws ParseException {
        StringBuilder sb = new StringBuilder();
        sb.append("import java.sql.ResultSet;\n");
        sb.append("import com.foo.Project;\n");
        sb.append("public class SimpleClass {\n");
        sb.append("    public void methodWithParametersInsidecall(){\n");
        sb.append("      ResultSet rs = null;\n");
        sb.append("      new Project(rs.getInt(1), rs.getString(1));\n");
        sb.append("    }\n");
        sb.append("}");
        IMethodCalls concernCollection = getCCBySource(sb.toString());
        List<String> methodNames = concernCollection.getMethodNames();
        assertNotNull(methodNames);
        assertEquals(2, methodNames.size());
        assertEquals("SimpleClass.", methodNames.get(0));
        assertEquals("SimpleClass.methodWithParametersInsidecall", methodNames.get(1));

        List<List<String>> methods = concernCollection.getSequences();
        assertNotNull(methods);
        assertEquals(2, methods.size());
        List<String> sequences = methods.get(1);
        assertEquals(3, sequences.size());
        assertEquals("java.sql.ResultSet.getInt", sequences.get(0));
        assertEquals("java.sql.ResultSet.getString", sequences.get(1));
        assertEquals("com.foo.Project.Project", sequences.get(2));
    }

    @Test
    public void testGetMetricInParametersInsideMethodcall() throws ParseException {
        StringBuilder sb = new StringBuilder();
        sb.append("import java.sql.ResultSet;\n");
        sb.append("import java.util.List;\n");
        sb.append("import com.foo.Project;\n");
        sb.append("public class SimpleClass {\n");
        sb.append("    public void methodWithParametersInsidecall(List<Project> list){\n");
        sb.append("      ResultSet rs = null;\n");
        sb.append("      list.add(new Project(rs.getInt(1), rs.getString(1)));\n");
        sb.append("    }\n");
        sb.append("}");
        IMethodCalls concernCollection = getCCBySource(sb.toString());
        List<String> methodNames = concernCollection.getMethodNames();
        assertNotNull(methodNames);
        assertEquals(2, methodNames.size());
        assertEquals("SimpleClass.", methodNames.get(0));
        assertEquals("SimpleClass.methodWithParametersInsidecall", methodNames.get(1));

        List<List<String>> methods = concernCollection.getSequences();
        assertNotNull(methods);
        assertEquals(2, methods.size());
        List<String> sequences = methods.get(1);
        assertEquals(4, sequences.size());
        assertEquals("java.sql.ResultSet.getInt", sequences.get(0));
        assertEquals("java.sql.ResultSet.getString", sequences.get(1));
        assertEquals("com.foo.Project.Project", sequences.get(2));
        assertEquals("java.util.List.add", sequences.get(3));
    }

    @Test
    public void testGetMetricInStringValueOf() throws ParseException {
        StringBuilder sb = new StringBuilder();
        sb.append("public class SimpleClass {\n");
        sb.append("    public String method(){\n");
        sb.append("      return String.valueOf(1);\n");
        sb.append("    }\n");
        sb.append("}");
        IMethodCalls concernCollection = getCCBySource(sb.toString());
        List<String> methodNames = concernCollection.getMethodNames();
        assertNotNull(methodNames);
        assertEquals(2, methodNames.size());
        assertEquals("SimpleClass.", methodNames.get(0));
        assertEquals("SimpleClass.method", methodNames.get(1));

        List<List<String>> methods = concernCollection.getSequences();
        assertNotNull(methods);
        assertEquals(2, methods.size());
        List<String> sequences = methods.get(1);
        assertEquals(1, sequences.size());
        assertEquals("String.valueOf", sequences.get(0));
    }

    @Test
    public void testGetMetricInStringOperator() throws ParseException {
        StringBuilder sb = new StringBuilder();
        sb.append("import javax.servlet.http.HttpServletRequest;\n");
        sb.append("public class SimpleClass {\n");
        sb.append("    public String method(HttpServletRequest request){\n");
        sb.append("      String descricaoQueixa = request.getParameter(\"descricaoQueixa\");\n");
        sb.append("    }\n");
        sb.append("}");
        IMethodCalls concernCollection = getCCBySource(sb.toString());
        List<String> methodNames = concernCollection.getMethodNames();
        assertNotNull(methodNames);
        assertEquals(2, methodNames.size());
        assertEquals("SimpleClass.", methodNames.get(0));
        assertEquals("SimpleClass.method", methodNames.get(1));

        List<List<String>> methods = concernCollection.getSequences();
        assertNotNull(methods);
        assertEquals(2, methods.size());
        List<String> sequences = methods.get(1);
        assertEquals(1, sequences.size());
        assertEquals("javax.servlet.http.HttpServletRequest.getParameter", sequences.get(0));
    }

    @Test
    public void testGetMetricInSimpleClassWithStaticImportsOneCall() throws ParseException {
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

        List<SourceHandler> sources = concernCollection.getMethodSources();
        assertNotNull(sources);
        assertEquals(methodNames.size(), sources.size());
        assertEquals("public SimpleClass(){\n       foo();\n       out.println();\n    }", sources.get(1).getSnippet());
        assertEquals("void foo() {\n    }", sources.get(2).getSnippet());
    }

    @Test
    public void testGetMetricInSimpleClassWithImportsOneCall() throws ParseException {
        StringBuilder sb = new StringBuilder();
        sb.append("package com.ampaiva.test;");
        sb.append("import javax.persistence.Persistence;");
        sb.append("public class SimpleClass {\n");
        sb.append("    public SimpleClass(){\n");
        sb.append("       foo();\n");
        sb.append("       Persistence.createEntityManagerFactory(persistenceUnitName);\n");
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
        assertEquals("javax.persistence.Persistence.createEntityManagerFactory", sequence1.get(1));

        List<String> sequence2 = sequences.get(2);
        assertEquals(0, sequence2.size());

        List<String> methodNames = concernCollection.getMethodNames();
        assertNotNull(methodNames);
        assertEquals(sequences.size(), methodNames.size());
        assertEquals("com.ampaiva.test.SimpleClass.", methodNames.get(0));
        assertEquals("com.ampaiva.test.SimpleClass.SimpleClass", methodNames.get(1));
        assertEquals("com.ampaiva.test.SimpleClass.foo", methodNames.get(2));

        List<SourceHandler> sources = concernCollection.getMethodSources();
        assertNotNull(sources);
        assertEquals(methodNames.size(), sources.size());
        assertEquals(
                "public SimpleClass(){\n       foo();\n       Persistence.createEntityManagerFactory(persistenceUnitName);\n    }",
                sources.get(1).getSnippet());
        assertEquals("void foo() {\n    }", sources.get(2).getSnippet());
    }

    @Test
    public void testGetMetricInSimpleClassWithImportsOneCallClassVariable() throws ParseException {
        StringBuilder sb = new StringBuilder();
        sb.append("package com.ampaiva.test;");
        sb.append("import javax.persistence.EntityManagerFactory;");
        sb.append("public class SimpleClass {\n");
        sb.append("    private EntityManagerFactory emFactory;\n");
        sb.append("    public SimpleClass(){\n");
        sb.append("       emFactory.createEntityManager();\n");
        sb.append("    }\n");
        sb.append("}");
        IMethodCalls concernCollection = getCCBySource(sb.toString());
        List<List<String>> sequences = concernCollection.getSequences();
        assertNotNull(sequences);
        assertEquals(2, sequences.size());
        List<String> sequence1 = sequences.get(1);
        assertEquals(1, sequence1.size());
        assertEquals("javax.persistence.EntityManagerFactory.createEntityManager", sequence1.get(0));

        List<String> methodNames = concernCollection.getMethodNames();
        assertNotNull(methodNames);
        assertEquals(sequences.size(), methodNames.size());
        assertEquals("com.ampaiva.test.SimpleClass.", methodNames.get(0));
        assertEquals("com.ampaiva.test.SimpleClass.SimpleClass", methodNames.get(1));

        List<SourceHandler> sources = concernCollection.getMethodSources();
        assertNotNull(sources);
        assertEquals(methodNames.size(), sources.size());
        assertEquals("public SimpleClass(){\n       emFactory.createEntityManager();\n    }",
                sources.get(1).getSnippet());
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
        List<SourceHandler> methodSources = concernCollection.getMethodSources();
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
    public void testGetMetricInSimpleClassWithFor() throws ParseException {
        StringBuilder sb = new StringBuilder();
        sb.append("package com.ampaiva.test;\n");
        sb.append("import java.util.Iterator;");
        sb.append("import java.util.Collection;");
        sb.append("import com.sun.j2ee.blueprints.lineitem.ejb.LineItemLocal;");
        sb.append("public class SimpleClass {\n");
        sb.append("    public SimpleClass(){\n");
        sb.append("       Collection lineItems = getLineItems();");
        sb.append("       for (Iterator iterator = lineItems.iterator(); iterator.hasNext();) {\n");
        sb.append("          LineItemLocal lineItem = (LineItemLocal) iterator.next();\n");
        sb.append("       }\n");
        sb.append("    }\n");
        sb.append("}");
        IMethodCalls concernCollection = getCCBySource(sb.toString());
        List<List<String>> sequences = concernCollection.getSequences();
        assertNotNull(sequences);
        assertEquals(2, sequences.size());
        List<String> sequence1 = sequences.get(1);
        assertEquals(4, sequence1.size());
        assertEquals("com.ampaiva.test.SimpleClass.getLineItems", sequence1.get(0));
        assertEquals("java.util.Collection.iterator", sequence1.get(1));
        assertEquals("java.util.Iterator.hasNext", sequence1.get(2));
        assertEquals("java.util.Iterator.next", sequence1.get(3));
    }

    @Test
    public void testGetMetricInSimpleClassWithForEach() throws ParseException {
        StringBuilder sb = new StringBuilder();
        sb.append("package com.ampaiva.test;\n");
        sb.append("import java.util.Iterator;");
        sb.append("import java.util.Collection;");
        sb.append("import com.sun.j2ee.blueprints.lineitem.ejb.LineItemLocal;");
        sb.append("public class SimpleClass {\n");
        sb.append("    public SimpleClass(){\n");
        sb.append("       Collection lineItems = getLineItems();");
        sb.append("       for (Iterator iterator : lineItems.iterator()) {\n");
        sb.append("          LineItemLocal lineItem = (LineItemLocal) iterator.next();\n");
        sb.append("       }\n");
        sb.append("    }\n");
        sb.append("}");
        IMethodCalls concernCollection = getCCBySource(sb.toString());
        List<List<String>> sequences = concernCollection.getSequences();
        assertNotNull(sequences);
        assertEquals(2, sequences.size());
        List<String> sequence1 = sequences.get(1);
        assertEquals(3, sequence1.size());
        assertEquals("com.ampaiva.test.SimpleClass.getLineItems", sequence1.get(0));
        assertEquals("java.util.Collection.iterator", sequence1.get(1));
        assertEquals("java.util.Iterator.next", sequence1.get(2));
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
        List<String> methodNames = concernCollection.getMethodNames();
        assertNotNull(methodNames);
        assertEquals("com.ampaiva.test.SimpleClass.", methodNames.get(0));

        List<List<String>> sequences = concernCollection.getSequences();
        assertNotNull(sequences);
        assertEquals(sequences.size(), methodNames.size());
        assertEquals(1, sequences.size());
        List<String> sequence0 = sequences.get(0);
        assertEquals(1, sequence0.size());
        assertEquals("com.ampaiva.other.FooClass.foo", sequence0.get(0));

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

    @Test
    public void testSimpleReturn() throws ParseException {
        StringBuilder sb = new StringBuilder();
        sb.append("public class SimpleClass {\n");
        sb.append("    public boolean foo(){\n");
        sb.append("      return Constants.isPersistent();\n");
        sb.append("    }\n");
        sb.append("}");
        ConcernCollection concernCollection = getCCBySource(sb.toString());
        List<List<String>> sequences = concernCollection.getSequences();
        assertNotNull(sequences);
        assertEquals(2, sequences.size());
        List<String> sequence1 = sequences.get(1);
        assertEquals(1, sequence1.size());
        assertEquals("Constants.isPersistent", sequence1.get(0));
    }

    @Test
    public void testIf() throws ParseException {
        StringBuilder sb = new StringBuilder();
        sb.append("public class SimpleClass {\n");
        sb.append("    public void foo(){\n");
        sb.append("      if(Constants.isPersistent())\n");
        sb.append("         b();\n");
        sb.append("    }\n");
        sb.append("}");
        ConcernCollection concernCollection = getCCBySource(sb.toString());
        List<List<String>> sequences = concernCollection.getSequences();
        assertNotNull(sequences);
        assertEquals(2, sequences.size());
        List<String> sequence1 = sequences.get(1);
        assertEquals(2, sequence1.size());
        assertEquals("Constants.isPersistent", sequence1.get(0));
        assertEquals("SimpleClass.b", sequence1.get(1));
    }
}
