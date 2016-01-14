package com.ampaiva.hlo.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SourceHandlerTest {

    @Test
    public void testSourceHandler() {
        String source = "1\r\n2\n3\r4\r\n\n6\n";
        SourceHandler sourceHandler = new SourceHandler(source, 1, 1, 1, 2);
        String[] lines = sourceHandler.getLines();
        assertEquals(6, lines.length);
    }

    @Test
    public void testSourceHandler2() {
        String source = "1\r\n2\r\n3\r4\r\n\n6\n";
        SourceHandler sourceHandler = new SourceHandler(source, 1, 1, 1, 2);
        String[] lines = sourceHandler.getLines();
        assertEquals(6, lines.length);
    }

    @Test
    public void testGetPositionandOffsetL2C2() {
        String source = "1234567890\n1234567890";
        SourceHandler sourceHandler = new SourceHandler(source, 2, 2, 2, 5);
        assertEquals(10 + 1 + 2, sourceHandler.getOffset());
        assertEquals(4, sourceHandler.getLength());
        assertEquals("2345", sourceHandler.getSnippet());
    }

}
