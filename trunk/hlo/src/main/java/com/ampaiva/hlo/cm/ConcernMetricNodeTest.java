package com.ampaiva.hlo.cm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class ConcernMetricNodeTest {

    @Test
    public void testGetPositionInvalidSource() {
        String source = "xxx";
        try {
            ConcernMetricNode concernMetricNode = new ConcernMetricNode(2, 3, 4, 5);
            concernMetricNode.getCodePosition(source);
            fail("IllegalArgumentException should be thrown since souce '" + source
                    + "' does not contain ConcernMetricNode");
        } catch (IllegalArgumentException ex) {
            // Passed
        }
    }

    @Test
    public void testGetPositionandOffsetL1C1() {
        //                ....
        String source = "1";
        ConcernMetricNode concernMetricNode = new ConcernMetricNode(1, 1, 1, 2);
        assertEquals(1, concernMetricNode.getCodePosition(source).getPosition());
        assertEquals(1, concernMetricNode.getCodePosition(source).getOffset());
    }

    @Test
    public void testGetPositionandOffsetL1C2() {
        //                ....
        String source = "123456789";
        ConcernMetricNode concernMetricNode = new ConcernMetricNode(1, 2, 1, 5);
        assertEquals(2, concernMetricNode.getCodePosition(source).getPosition());
        assertEquals(3, concernMetricNode.getCodePosition(source).getOffset());
    }

    @Test
    public void testGetPositionandOffsetL2C2() {
        String source = "1234567890\n1234567890";
        ConcernMetricNode concernMetricNode = new ConcernMetricNode(2, 2, 2, 5);
        assertEquals(10 + 2, concernMetricNode.getCodePosition(source).getPosition());
        assertEquals(3, concernMetricNode.getCodePosition(source).getOffset());
    }

    @Test
    public void testGetPositionandOffsetL2C2_L3C3() {
        String source = "1234567890\n1234567890\n1234567890";
        ConcernMetricNode concernMetricNode = new ConcernMetricNode(2, 2, 3, 5);
        assertEquals(10 + 2, concernMetricNode.getCodePosition(source).getPosition());
        assertEquals(10 + 3, concernMetricNode.getCodePosition(source).getOffset());
    }

}
