package com.pdfjet.out;

import java.io.*;

/**
 * Used to create images from pre-processed raw image data files.
 * Please see Example_24.
 *
 * To create raw PDF image data file you can use the script convert-png-to-raw-image.sh
 * On Windows use convert-png-to-raw-image.cmd
 */
public class PDFImage {

    public InputStream stream;

    public long size;

    public int colorComponents;

    public int w;

    public int h;

    public int bitsPerComponent;

    /**
     * Used to construct images from pre-processed raw image data files.
     * 
     * @param path the path to the image file.
     * @throws Exception
     */
    public PDFImage(String path, InputStream stream, long size) throws Exception {
        String fileName = path.substring(path.lastIndexOf("/") + 1);
        String[] tokens = fileName.split("\\.");
        String[] dim = tokens[2].split("x");
        this.stream = stream;
        this.size = size;
        this.colorComponents = tokens[1].equals("rgb") ? 3 : 1;
        this.w = Integer.valueOf(dim[0]);
        this.h = Integer.valueOf(dim[1]);
        this.bitsPerComponent = Integer.valueOf(dim[2]);
    }

    protected InputStream getInputStream() {
        return stream;
    }

    protected int getWidth() {
        return this.w;
    }

    protected int getHeight() {
        return this.h;
    }

    protected long getSize() {
        return this.size;
    }

    protected int getColorComponents() {
        return this.colorComponents;
    }

    protected int getBitsPerComponent() {
        return this.bitsPerComponent;
    }
}
