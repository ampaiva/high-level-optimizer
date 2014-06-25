package com.pdfjet.out;

import java.io.*;
import java.util.zip.*;

class Decompressor extends Inflater {

    public ByteArrayOutputStream bos = null;

    public Decompressor(byte[] data) throws Exception {
        super.setInput(data);
        bos = new ByteArrayOutputStream(data.length);
        byte[] buf = new byte[2048];
        while (!super.finished()) {
            int count = super.inflate(buf);
            bos.write(buf, 0, count);
        }
    }

    public byte[] getDecompressedData() {
        return bos.toByteArray();
    }
}
