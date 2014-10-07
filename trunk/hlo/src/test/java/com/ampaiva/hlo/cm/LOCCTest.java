package com.ampaiva.hlo.cm;

import static org.junit.Assert.assertEquals;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;

import org.junit.Test;

import com.ampaiva.hlo.util.TestUtils;

public class LOCCTest {

    private LOCC getLOCC(String className) throws ParseException {
        return new LOCC(getCU(className));
    }

    private LOCC getLOCCBySource(String source) throws ParseException {
        return new LOCC(getCUBySource(source));
    }

    private CompilationUnit getCU(String className) throws ParseException {
        CompilationUnit cu = TestUtils.getCU("cm", className);
        return cu;
    }

    private CompilationUnit getCUBySource(String source) throws ParseException {
        CompilationUnit cu = TestUtils.getCUBySource(source);
        return cu;
    }

    @Test
    public void testGetMetricInOneLineBrace0LineAfterTry() throws ParseException {
        assertEquals(1, getLOCCBySource("class C {{try{}catch (Exception e){}}}").getMetric());
    }

    @Test
    public void testGetMetricInOneLineBrace0LineAfterTry1LineBeforeCatch() throws ParseException {
        assertEquals(2, getLOCCBySource("class C {{try{\n}catch (Exception e){}}}").getMetric());
    }

    @Test
    public void testGetMetricInOneLineBrace1LineAfterTry1LineBeforeCatch() throws ParseException {
        StringBuilder sb = new StringBuilder();
        sb.append("class C {");
        sb.append("{try{\n");
        sb.append("\n");
        sb.append("}catch (Exception e){}}");
        sb.append("}");
        assertEquals(2, getLOCCBySource(sb.toString()).getMetric());
    }

    @Test
    public void testGetMetricInOneLineBrace1LineAfterTry1Stmt1LineBeforeCatch() throws ParseException {
        StringBuilder sb = new StringBuilder();
        sb.append("class C {");
        sb.append("{try{\n");
        sb.append("x();\n");
        sb.append("}catch (Exception e){}}");
        sb.append("}");
        assertEquals(2, getLOCCBySource(sb.toString()).getMetric());
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
        assertEquals(2, getLOCCBySource(sb.toString()).getMetric());
    }

    @Test
    public void testGetMetricInOneLineBrace1LineAfterTry2Stmts1LineBeforeCatch() throws ParseException {
        StringBuilder sb = new StringBuilder();
        sb.append("class C {");
        sb.append("{try{\n");
        sb.append("x();\n");
        sb.append("x();}catch (Exception e){}}");
        sb.append("}");
        assertEquals(2, getLOCCBySource(sb.toString()).getMetric());
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
        assertEquals(2, getLOCCBySource(sb.toString()).getMetric());
    }

    @Test
    public void testGetMetricIn2LinesBrace1LineAfterTry() throws ParseException {
        assertEquals(2, getLOCCBySource("class C {{try\n{}catch (Exception e){}}}").getMetric());
    }

    @Test
    public void testGetMetricIn3LinesBrace2LinesAfterTry() throws ParseException {
        assertEquals(3, getLOCCBySource("class C {{try\n\n{}catch (Exception e){}}}").getMetric());
    }

    @Test
    public void testGetMetricIn4LinesBrace3LinesAfterTry() throws ParseException {
        assertEquals(4, getLOCCBySource("class C {{try\n\n\n{}catch (Exception e){}}}").getMetric());
    }

    @Test
    public void testGetMetricIn2LinesBrace0LineAfterTry() throws ParseException {
        assertEquals(2, getLOCCBySource("class C {{try{\nx();}catch (Exception e){}}}").getMetric());
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
    public void testGetMetricinWithVariousEH() throws ParseException {
        assertEquals(8, getLOCC("MethodWithVariousEH").getMetric());
    }

    @Test
    public void testGetMetricMethodWithEHCatchAfterBrace() throws ParseException {
        assertEquals(8, getLOCC("MethodWithEHCatchAfterBrace").getMetric());
    }

    @Test
    public void testGetMetricMethodWithEHInsideTry() throws ParseException {
        assertEquals(12, getLOCC("MethodWithEHInsideTry").getMetric());
    }

    @Test
    public void testGetMetricinMethodThrowsRuntimeException() throws ParseException {
        assertEquals(1, getLOCC("MethodThrowsRuntimeException").getMetric());
    }
}
