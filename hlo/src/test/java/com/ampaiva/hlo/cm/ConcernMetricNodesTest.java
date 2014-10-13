package com.ampaiva.hlo.cm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ConcernMetricNodesTest {
    String source = "1234567890\n1234567890\n1234567890\n1234567890\n1234567890\n1234567890\n1234567890\n1234567890";

    @Test
    public void testcountLinesEmpty() {
        ConcernMetricNodes concernMetricNodes = new ConcernMetricNodes();
        assertEquals(0, concernMetricNodes.countLines());
    }

    @Test
    public void testcountLinesSingle() {
        ConcernMetricNodes concernMetricNodes = new ConcernMetricNodes();
        assertEquals(1, concernMetricNodes.add(source, 1, 1, 1, 1).countLines());
    }

    @Test
    public void testcountLinesSameLine() {
        ConcernMetricNodes concernMetricNodes = new ConcernMetricNodes();
        assertEquals(1, concernMetricNodes.add(source, 1, 1, 1, 1).add(source, 1, 1, 1, 1).countLines());
    }

    @Test
    public void testcountLinesNextLine() {
        ConcernMetricNodes concernMetricNodes = new ConcernMetricNodes();
        assertEquals(2, concernMetricNodes.add(source, 1, 1, 1, 1).add(source, 2, 1, 2, 1).countLines());
    }

    @Test
    public void testcountLinesIntersection() {
        ConcernMetricNodes concernMetricNodes = new ConcernMetricNodes();
        assertEquals(4, concernMetricNodes.add(source, 1, 1, 4, 1).add(source, 2, 1, 3, 1).countLines());
    }

    @Test
    public void testcountLinesPartialIntersection() {
        ConcernMetricNodes concernMetricNodes = new ConcernMetricNodes();
        assertEquals(6, concernMetricNodes.add(source, 1, 1, 4, 1).add(source, 2, 1, 6, 1).countLines());
    }

    @Test
    public void testcountLinesSorted() {
        ConcernMetricNodes concernMetricNodes = new ConcernMetricNodes();
        concernMetricNodes.add(source, 2, 1, 2, 1).add(source, 1, 1, 1, 1).add(source, 3, 1, 3, 1)
                .add(source, 3, 1, 3, 1).add(source, 6, 1, 8, 1).add(source, 6, 1, 7, 1).add(source, 5, 1, 5, 1)
                .add(source, 4, 1, 4, 1);
        int currentBeginLine = 0, currentEndLine = 0;
        for (ConcernMetricNode concernMetricNode : concernMetricNodes) {
            assertTrue(concernMetricNode.toString(), concernMetricNode.getBeginLine() >= currentBeginLine);
            assertTrue(concernMetricNode.toString(), concernMetricNode.getEndLine() >= currentEndLine);
            currentBeginLine = concernMetricNode.getBeginLine();
            currentEndLine = concernMetricNode.getEndLine();
        }
    }

}
