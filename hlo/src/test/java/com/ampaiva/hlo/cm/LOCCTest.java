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
