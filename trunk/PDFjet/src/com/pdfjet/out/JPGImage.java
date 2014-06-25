package com.pdfjet.out;

import java.io.*;

/**
 * Used to embed JPG images in the PDF document.
 *
 */
public class JPGImage {

    public static final char M_SOF0 = (char) 0x00C0;

    public static final char M_SOF1 = (char) 0x00C1;

    public static final char M_SOF2 = (char) 0x00C2;

    public static final char M_SOF3 = (char) 0x00C3;

    public static final char M_SOF5 = (char) 0x00C5;

    public static final char M_SOF6 = (char) 0x00C6;

    public static final char M_SOF7 = (char) 0x00C7;

    public static final char M_SOF9 = (char) 0x00C9;

    public static final char M_SOF10 = (char) 0x00CA;

    public static final char M_SOF11 = (char) 0x00CB;

    public static final char M_SOF13 = (char) 0x00CD;

    public static final char M_SOF14 = (char) 0x00CE;

    public static final char M_SOF15 = (char) 0x00CF;

    public int width;

    public int height;

    public long size;

    public int colorComponents;

    public byte[] data;

    public InputStream stream;

    public JPGImage(JPGImage jpg, InputStream stream) throws Exception {
        if (jpg == null) {
            readJPGImage(stream);
            stream.close();
        } else {
            this.width = jpg.width;
            this.height = jpg.height;
            this.size = jpg.size;
            this.colorComponents = jpg.colorComponents;
            this.stream = stream;
        }
    }

    public JPGImage(InputStream inputStream) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[2048];
        int count;
        while ((count = inputStream.read(buf, 0, buf.length)) != -1) {
            baos.write(buf, 0, count);
        }
        inputStream.close();
        data = baos.toByteArray();
        readJPGImage(new ByteArrayInputStream(data));
    }

    protected InputStream getInputStream() {
        return this.stream;
    }

    protected int getWidth() {
        return this.width;
    }

    protected int getHeight() {
        return this.height;
    }

    protected long getFileSize() {
        return this.size;
    }

    protected int getColorComponents() {
        return this.colorComponents;
    }

    protected byte[] getData() {
        return this.data;
    }

    private void readJPGImage(InputStream is) throws Exception {
        char ch1 = (char) is.read();
        char ch2 = (char) is.read();
        size += 2;
        if (ch1 == 0x00FF && ch2 == 0x00D8) {
            boolean foundSOFn = false;
            while (true) {
                char ch = nextMarker(is);
                switch(ch) {
                    case M_SOF0:
                    case M_SOF1:
                    case M_SOF2:
                    case M_SOF3:
                    case M_SOF5:
                    case M_SOF6:
                    case M_SOF7:
                    case M_SOF9:
                    case M_SOF10:
                    case M_SOF11:
                    case M_SOF13:
                    case M_SOF14:
                    case M_SOF15:
                        is.read();
                        is.read();
                        is.read();
                        size += 3;
                        height = readTwoBytes(is);
                        width = readTwoBytes(is);
                        colorComponents = is.read();
                        size++;
                        foundSOFn = true;
                        break;
                    default:
                        skipVariable(is);
                        break;
                }
                if (foundSOFn) {
                    while (is.read() != -1) {
                        size++;
                    }
                    break;
                }
            }
        } else {
            throw new Exception();
        }
    }

    private int readTwoBytes(InputStream is) throws Exception {
        int value = is.read();
        value <<= 8;
        value |= is.read();
        size += 2;
        return value;
    }

    private char nextMarker(InputStream is) throws Exception {
        int discarded_bytes = 0;
        char ch = ' ';
        ch = (char) is.read();
        size++;
        while (ch != 0x00FF) {
            discarded_bytes++;
            ch = (char) is.read();
            size++;
        }
        do {
            ch = (char) is.read();
            size++;
        } while (ch == 0x00FF);
        if (discarded_bytes != 0) {
            throw new Exception();
        }
        return ch;
    }

    private void skipVariable(InputStream is) throws Exception {
        int length = readTwoBytes(is);
        if (length < 2) {
            throw new Exception();
        }
        length -= 2;
        while (length > 0) {
            is.read();
            size++;
            length--;
        }
    }
}
