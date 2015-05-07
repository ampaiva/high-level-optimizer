package com.ampaiva.hlo.util.view;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ampaiva.hlo.util.view.IProgressReport.Phase;

public class ProgressReportTest {

    @Test
    public void testOnChangedHigherLevel() {
        ProgressReport progressReport = new ProgressReport(1) {
            @Override
            public void print(String text) {
                org.junit.Assert.fail("Cannot print level higher than 1");
            };
        };
        progressReport.onChanged(Phase.FINISHED, null, 1, 1, 2);
    }

    @Test
    public void testOnChangedOK() {
        final boolean printed[] = new boolean[1];
        ProgressReport progressReport = new ProgressReport(1) {
            @Override
            public void print(String text) {
                printed[0] = true;
            };
        };
        progressReport.onChanged(Phase.FINISHED, null, 1, 1, 1);
        assertTrue(printed[0]);
    }

    @Test
    public void testOnChangedUnlimited() {
        final boolean printed[] = new boolean[1];
        ProgressReport progressReport = new ProgressReport() {
            @Override
            public void print(String text) {
                assertTrue(text.endsWith("1/1"));
                printed[0] = true;
            };
        };
        progressReport.onChanged(Phase.BEGIN_ITEM, null, 0, 1, 100);
        assertTrue(printed[0]);
    }

    @Test
    public void testOnChangedFinishedUnlimited() {
        final boolean printed[] = new boolean[1];
        ProgressReport progressReport = new ProgressReport() {
            @Override
            public void print(String text) {
                assertFalse(text.endsWith("1/1"));
                printed[0] = true;
            };
        };
        progressReport.onChanged(Phase.FINISHED, null, 1, 1, 100);
        assertTrue(printed[0]);
    }

    @Test
    public void testOnChangedUnlimitedWithInfo() {
        final boolean printed[] = new boolean[1];
        ProgressReport progressReport = new ProgressReport() {
            @Override
            public void print(String text) {
                assertTrue(text.startsWith(" "));
                assertTrue(text.endsWith("Hello"));
                printed[0] = true;
            };
        };
        progressReport.onChanged(Phase.FINISHED, null, 1, 1, 100, "Hello");
        assertTrue(printed[0]);
    }

}
