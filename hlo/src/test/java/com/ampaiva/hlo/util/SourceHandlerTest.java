package com.ampaiva.hlo.util;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

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
        assertEquals(10 + 1 + 1, sourceHandler.getOffset());
        assertEquals(4, sourceHandler.getLength());
        assertEquals("2345", sourceHandler.getSnippet());
    }

    @Test
    public void testGetPositionandOffsetL2C2r() throws IOException {
        String source = Helper.convertFile2String(new File(
                "/temp/extracted/02-ecommerce/ecommerce/src/src/main/java/com/salesmanager/core/utils/ajax/AjaxPageableResponse.java"));
        SourceHandler sourceHandler1 = new SourceHandler(source, 1, 0, 1, 0);
        assertEquals(0, sourceHandler1.getOffset());
        assertEquals(42, sourceHandler1.getLength());
        String snippet1 = sourceHandler1.getSnippet();
        assertEquals("package", snippet1.substring(0, 7));
        assertEquals("ajax;\n", snippet1.substring(snippet1.length() - 6));

        SourceHandler sourceHandler2 = new SourceHandler(source, 47, 0, 64, 0);
        assertEquals(965, sourceHandler2.getOffset());
        assertEquals(548, sourceHandler2.getLength());
        String snippet2 = sourceHandler2.getSnippet();
        assertEquals("\t", snippet2.substring(0, 1));
        assertEquals(");\n", snippet2.substring(snippet2.length() - 3));
    }

}
