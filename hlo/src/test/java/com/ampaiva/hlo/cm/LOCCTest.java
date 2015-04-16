package com.ampaiva.hlo.cm;

import static org.junit.Assert.assertEquals;
import japa.parser.ParseException;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ampaiva.hlo.util.Helper;
import com.ampaiva.hlo.util.TestUtils;

public class LOCCTest {

    private LOCC locc;

    /**
     * Setup mocks before each test.
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        locc = new LOCC();
    }

    /**
     * Verifies all mocks after each test.
     */
    @After()
    public void tearDown() {
        locc = null;
    }

    private LOCC getLOCC(String className) throws ParseException, IOException {
        String source = getCU(className);
        locc.parse(source);
        return locc;
    }

    private ConcernMetric getLOCCBySource(String source) throws ParseException {
        locc.parse(source);
        return locc;
    }

    private String getCU(String className) throws ParseException, IOException {
        File file = TestUtils.getCU("cm", className);
        return Helper.convertFile2String(file);
    }

    @Test
    public void testGetMetricInOneLineBrace0LineAfterTry() throws ParseException {
        locc.parse("class C {{try{}catch (Exception e){}}}");
        assertEquals(1, locc.getMetric());
    }

    @Test
    public void testGetMetricInOneLineBrace0LineAfterTry1LineBeforeCatch() throws ParseException {
        final String source = "class C {{try{\n// Comment\n//\n\n}catch (Exception e){}}}";
        locc.parse(source);
        assertEquals(2, locc.getMetric());
        ConcernMetricNodes nodes = locc.getNodes();
        assertEquals(2, nodes.size());
        ConcernMetricNode actual = nodes.get(0);
        assertEquals(11, actual.getOffset());
        assertEquals(4, actual.getLength());
    }

    @Test
    public void testGetMetricImportWithBlankLines() throws ParseException {
        final String source = "package com.ampaiva.in;\n\n\n\n\n\npublic class AddressRepositoryRDB2 {\npublic void insert(Address end) throws ObjectAlreadyInsertedException {}}\n";
        locc.parse(source);
        assertEquals(1, locc.getMetric());
        ConcernMetricNodes nodes = locc.getNodes();
        assertEquals(1, nodes.size());
    }

    @Test
    public void testGetMetricInOneLineBrace1LineAfterTry1LineBeforeCatch() throws ParseException {
        StringBuilder sb = new StringBuilder();
        sb.append("class C {");
        sb.append("{try{\n");
        sb.append("\n");
        sb.append("}catch (Exception e){}}");
        sb.append("}");
        locc.parse(sb.toString());

        assertEquals(2, locc.getMetric());
    }

    @Test
    public void testGetMetricInSimpleClass() throws ParseException {
        StringBuilder sb = new StringBuilder();
        sb.append("public class SimpleClass {\n");
        sb.append("    public void doNothing() throws RuntimeException {\n");
        sb.append("        throw new RuntimeException(\"1232\");\n");
        sb.append("    }\n");
        sb.append("}");
        locc.parse(sb.toString());
        assertEquals(2, locc.getMetric());
        ConcernMetricNode concernMetricNode = locc.getNodes().get(0);
        assertEquals(2, concernMetricNode.getBeginLine());
        assertEquals(2, concernMetricNode.getEndLine());
        assertEquals(51, concernMetricNode.getEndColumn());
        assertEquals(27 + 1 + 28, concernMetricNode.getOffset());
        assertEquals(23, concernMetricNode.getLength());
    }

    @Test
    public void testGetMetricInOneLineBrace1LineAfterTry1Stmt1LineBeforeCatch() throws ParseException {
        StringBuilder sb = new StringBuilder();
        sb.append("class C {");
        sb.append("{try{\n");
        sb.append("x();\n");
        sb.append("}catch (Exception e){}}");
        sb.append("}");
        locc.parse(sb.toString());
        assertEquals(2, locc.getMetric());
    }

    @Test
    public void testGetMetricInOneLineBrace1LineAfterTryComments1Stmt1LineBeforeCatch() throws ParseException {
        StringBuilder sb = new StringBuilder();
        sb.append("class C {");
        sb.append("{try{\n");
        sb.append("// Comment;\n");
        sb.append("// Comment;\n");
        sb.append("// Comment;\n");
        sb.append("// Comment;\n");
        sb.append("x();\n");
        sb.append("}catch (Exception e){}}");
        sb.append("}");
        locc.parse(sb.toString());
        assertEquals(2, locc.getMetric());
    }

    @Test
    public void testGetMetricInOneLineBrace1LineAfterTry2Stmts1LineBeforeCatch() throws ParseException {
        StringBuilder sb = new StringBuilder();
        sb.append("class C {");
        sb.append("{try{\n");
        sb.append("x();\n");
        sb.append("x();}catch (Exception e){}}");
        sb.append("}");
        locc.parse(sb.toString());
        assertEquals(2, locc.getMetric());
    }

    @Test
    public void testGetMetricInOneLineBrace1LineAfterTry2Stmts2LinesBeforeCatch() throws ParseException {
        StringBuilder sb = new StringBuilder();
        sb.append("class C {");
        sb.append("{try{\n");
        sb.append("x();\n");
        sb.append("x();\n");
        sb.append("}catch (Exception e){}}");
        sb.append("}");
        locc.parse(sb.toString());
        assertEquals(2, locc.getMetric());
    }

    @Test
    public void testGetMetricTryInsideTry() throws ParseException {
        StringBuilder sb = new StringBuilder();
        sb.append("class C {");
        sb.append("{try{\n");
        sb.append("x();\n");
        sb.append("{try{\n");
        sb.append("x();\n");
        sb.append("x();\n");
        sb.append("}catch (Exception e){}}");
        sb.append("x();\n");
        sb.append("}catch (Exception e){}}");
        sb.append("}");
        locc.parse(sb.toString());
        assertEquals(4, locc.getMetric());
    }

    @Test
    public void testGetMetricIn2LinesBrace1LineAfterTry() throws ParseException {
        String source = "class C {{try\n{}catch (Exception e){}}}";
        locc.parse(source);
        assertEquals(2, locc.getMetric());
    }

    @Test
    public void testGetMetricIn3LinesBrace2LinesAfterTry() throws ParseException {
        String source = "class C {{try\n\n{}catch (Exception e){}}}";
        locc.parse(source);
        assertEquals(3, locc.getMetric());
    }

    @Test
    public void testGetMetricIn4LinesBrace3LinesAfterTry() throws ParseException {
        String source = "class C {{try\n\n\n{}catch (Exception e){}}}";
        locc.parse(source);
        assertEquals(4, locc.getMetric());
    }

    @Test
    public void testGetMetricIn2LinesBrace0LineAfterTry() throws ParseException {
        String source = "class C {{try{\nx();}catch (Exception e){}}}";
        locc.parse(source);
        assertEquals(2, locc.getMetric());
    }

    @Test
    public void testGetMetricnThrowsExceptionin1Line() throws ParseException {
        assertEquals(1, getLOCCBySource("class C {void x(){throw new RuntimeException();}}").getMetric());
    }

    @Test
    public void testGetMetricnThrowsExceptionin1LineInsideIf() throws ParseException {
        assertEquals(1, getLOCCBySource("class C {void x(){if(true)throw new RuntimeException();}}").getMetric());
    }

    @Test
    public void testGetMetricnThrowsExceptionin1LineVariableDeclaration() throws ParseException {
        assertEquals(0, getLOCCBySource("class C {void x(){int i = 1;}}").getMetric());
    }

    @Test
    public void testGetMetricnThrowsExceptionin1LineAssign() throws ParseException {
        assertEquals(0, getLOCCBySource("class C {void x(){out = response.getWriter();}}").getMetric());
    }

    @Test
    public void testGetMetricnThrowsExceptionin1LineForStmt() throws ParseException {
        assertEquals(0, getLOCCBySource("class C {void x(){for(int i=0; i<3; i++){x();continue;}}}").getMetric());
    }

    @Test
    public void testGetMetricnThrowsExceptionin1LineWhileStmt() throws ParseException {
        assertEquals(0, getLOCCBySource("class C {void x(){while(true){x();break;}}}").getMetric());
    }

    @Test
    public void testGetMetricnThrowsExceptionin1LineDoStmt() throws ParseException {
        assertEquals(0, getLOCCBySource("class C {void x(){do{}while(true);}}").getMetric());
    }

    @Test
    public void testGetMetricnThrowsExceptionin1LineReturnStmt() throws ParseException {
        assertEquals(0, getLOCCBySource("class C {void x(){return;}}").getMetric());
    }

    @Test
    public void testGetMetricnThrowsExceptionin1LineSynchronizedStmt() throws ParseException {
        assertEquals(0, getLOCCBySource("class C {void x(){synchronized (this){}}}").getMetric());
    }

    @Test
    public void testGetMetricSwitchStmt() throws ParseException {
        assertEquals(0, getLOCCBySource("class C {void x(){switch(z){case 1: break; default:break;}}}").getMetric());
    }

    @Test
    public void testGetMetricConstructorDeclaration() throws ParseException {
        assertEquals(1, getLOCCBySource("class C {C(F f) throws Exception{super(f);}}").getMetric());
    }

    @Test
    public void testGetMetricnThrowsExceptionin1LineReturnStmtBoolean() throws ParseException {
        assertEquals(0, getLOCCBySource("class C {boolean x(){return true;}}").getMetric());
    }

    @Test
    public void testGetMetricnThrowsExceptionin1LineArrayCreationExpr() throws ParseException {
        assertEquals(0, getLOCCBySource("class C {C[] x(){return new C[1];}}").getMetric());
    }

    @Test
    public void testGetMetricEnclosedExpr() throws ParseException {
        assertEquals(0, getLOCCBySource("class C {boolean x(){return (z == 0) ? true : false;}}").getMetric());
    }

    @Test
    public void testGetMetricnThrowsExceptionin1LineObjectCreationExpr() throws ParseException {
        assertEquals(0, getLOCCBySource("class C {C x(){return new C();}}").getMetric());
    }

    @Test
    public void testGetMetricnThrowsExceptionin1LineArrayAccessExpr() throws ParseException {
        assertEquals(0, getLOCCBySource("class C {void x(){C[] z = new C[1];z[0]=null;}}").getMetric());
    }

    @Test
    public void testGetMetricnThrowsExceptionin1LineCastExpr() throws ParseException {
        assertEquals(0, getLOCCBySource("class C {void x(){int i = (int)2.0;}}").getMetric());
    }

    @Test
    public void testGetMetricnThrowsExceptionin1LineThisExpr() throws ParseException {
        assertEquals(0, getLOCCBySource("class C {int z = 0;C x(){return this;}int y(){return this.z;}}").getMetric());
    }

    @Test
    public void testGetMetricnThrowsExceptionin2Line2() throws ParseException {
        assertEquals(2, getLOCCBySource("class C {void x(){throw new RuntimeException(\"\"\n+\"\");}}").getMetric());
    }

    @Test
    public void testGetMetricMethodSignatureThrowsExceptionin1Line() throws ParseException {
        assertEquals(1,
                getLOCCBySource("class C {void throwSomething() throws TestException, AnotherException{doNothing();}}")
                        .getMetric());
    }

    @Test
    public void testGetMetricMethodSignatureThrowsExceptionin2Lines() throws ParseException {
        assertEquals(
                2,
                getLOCCBySource("class C {void throwSomething() throws TestException,\nAnotherException{doNothing();}}")
                        .getMetric());
    }

    @Test
    public void testGetMetricinWithVariousEH() throws ParseException, IOException {
        assertEquals(8, getLOCC("MethodWithVariousEH").getMetric());
    }

    @Test
    public void testGetMetricMethodWithEHCatchAfterBrace() throws ParseException, IOException {
        assertEquals(8, getLOCC("MethodWithEHCatchAfterBrace").getMetric());
    }

    @Test
    public void testGetMetricMethodWithEHInsideTry() throws ParseException, IOException {
        assertEquals(12, getLOCC("MethodWithEHInsideTry").getMetric());
    }

    @Test
    public void testGetMetricinMethodThrowsRuntimeException() throws ParseException, IOException {
        assertEquals(1, getLOCC("MethodThrowsRuntimeException").getMetric());
    }
}
