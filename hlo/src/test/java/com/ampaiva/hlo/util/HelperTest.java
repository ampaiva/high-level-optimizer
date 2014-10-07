package com.ampaiva.hlo.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class HelperTest {

    @Test
    public void testGetFilesFolderDoesNotExist() {
        try {
            Helper.getFilesRecursevely("invalid/folder/location");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException ex) {
            //OK
        }
    }

    @Test
    public void testGetFiles() {
        assertEquals(4, Helper.getFilesRecursevely("src/test/resources/com/ampaiva/in/util").size());
    }

}
