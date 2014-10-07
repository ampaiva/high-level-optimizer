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
        assertEquals(1, getLOCCBySource("class C {{try{\nx();}catch (Exception e){}}}").getMetric());
    }

    @Test
    public void testGetMetricnOneLine() throws ParseException {
        assertEquals(1, getLOCC("MethodSignatureThrowsExceptioninOneLine").getMetric());
    }

    @Test
    public void testGetMetricinTwoLines() throws ParseException {
        assertEquals(2, getLOCC("MethodSignatureThrowsExceptioninTwoLines").getMetric());
    }

    @Test
    public void testGetMetricinWithVariousEH() throws ParseException {
        assertEquals(7, getLOCC("MethodWithVariousEH").getMetric());
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
