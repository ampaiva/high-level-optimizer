package com.pdfjet.out;

import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

class BMPImage {

    public int w = 0;

    public int h = 0;

    public byte[] image;

    public byte[] deflated;

    public int bpp;

    public byte palette[][];

    public boolean r5g6b5;

    public static final int m10000000 = 0x80;

    public static final int m01000000 = 0x40;

    public static final int m00100000 = 0x20;

    public static final int m00010000 = 0x10;

    public static final int m00001000 = 0x08;

    public static final int m00000100 = 0x04;

    public static final int m00000010 = 0x02;

    public static final int m00000001 = 0x01;

    public static final int m11110000 = 0xF0;

    public static final int m00001111 = 0x0F;

    public BMPImage(java.io.InputStream is) throws Exception {
        palette = null;
        byte bm[] = getBytes(is, 2);
        if ((bm[0] == 'B' && bm[1] == 'M') || (bm[0] == 'B' && bm[1] == 'A') || (bm[0] == 'C' && bm[1] == 'I') || (bm[0] == 'C' && bm[1] == 'P') || (bm[0] == 'I' && bm[1] == 'C') || (bm[0] == 'P' && bm[1] == 'T')) {
            skipNBytes(is, 8);
            int offset = readSignedInt(is);
            int sizeOfHeader = readSignedInt(is);
            w = readSignedInt(is);
            h = readSignedInt(is);
            skipNBytes(is, 2);
            bpp = read2BytesLE(is);
            int compression = readSignedInt(is);
            if (bpp > 8) {
                r5g6b5 = (compression == 3);
                skipNBytes(is, 20);
                if (offset > 54) {
                    skipNBytes(is, offset - 54);
                }
            } else {
                skipNBytes(is, 12);
                int numpalcol = readSignedInt(is);
                if (numpalcol == 0) {
                    numpalcol = (int) Math.pow(2, bpp);
                }
                skipNBytes(is, 4);
                parsePalette(is, numpalcol);
            }
            parseData(is);
        } else {
            throw new Exception("BMP data could not be parsed!");
        }
    }

    private void parseData(java.io.InputStream is) throws Exception {
        image = new byte[w * h * 3];
        int rowsize = 4 * (int) Math.ceil(bpp * w / 32.0);
        byte row[];
        int index;
        try {
            for (int i = 0; i < h; i++) {
                row = getBytes(is, rowsize);
                switch(bpp) {
                    case 1:
                        row = bit1to8(row, w);
                        break;
                    case 4:
                        row = bit4to8(row, w);
                        break;
                    case 8:
                        break;
                    case 16:
                        if (r5g6b5) row = bit16to24(row, w); else row = bit16to24b(row, w);
                        break;
                    case 24:
                        break;
                    case 32:
                        row = bit32to24(row, w);
                        break;
                    default:
                        throw new Exception("Can only parse 1 bit, 4bit, 8bit, 16bit, 24bit and 32bit images");
                }
                index = w * (h - i - 1) * 3;
                if (palette != null) {
                    for (int j = 0; j < w; j++) {
                        image[index++] = palette[(row[j] < 0) ? row[j] + 256 : row[j]][2];
                        image[index++] = palette[(row[j] < 0) ? row[j] + 256 : row[j]][1];
                        image[index++] = palette[(row[j] < 0) ? row[j] + 256 : row[j]][0];
                    }
                } else {
                    for (int j = 0; j < w * 3; j += 3) {
                        image[index++] = row[j + 2];
                        image[index++] = row[j + 1];
                        image[index++] = row[j];
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException aiobe) {
            throw new Exception("BMP parse error: imagedata not correct");
        }
        ByteArrayOutputStream data2 = new ByteArrayOutputStream(32768);
        DeflaterOutputStream dos = new DeflaterOutputStream(data2, new Deflater());
        dos.write(image, 0, image.length);
        dos.finish();
        deflated = data2.toByteArray();
    }

    private static byte[] bit16to24(byte[] row, int width) {
        byte[] ret = new byte[width * 3];
        int j = 0;
        for (int i = 0; i < width * 2; i += 2) {
            ret[j++] = (byte) ((row[i] & 0x1F) << 3);
            ret[j++] = (byte) (((row[i + 1] & 0x07) << 5) + ((row[i] & 0xE0) >> 3));
            ret[j++] = (byte) ((row[i + 1] & 0xF8));
        }
        return ret;
    }

    private static byte[] bit16to24b(byte[] row, int width) {
        byte[] ret = new byte[width * 3];
        int j = 0;
        for (int i = 0; i < width * 2; i += 2) {
            ret[j++] = (byte) ((row[i] & 0x1F) << 3);
            ret[j++] = (byte) (((row[i + 1] & 0x03) << 6) + ((row[i] & 0xE0) >> 2));
            ret[j++] = (byte) ((row[i + 1] & 0x7C) << 1);
        }
        return ret;
    }

    private static byte[] bit32to24(byte[] row, int width) {
        byte[] ret = new byte[width * 3];
        int j = 0;
        for (int i = 0; i < width * 4; i += 4) {
            ret[j++] = row[i + 1];
            ret[j++] = row[i + 2];
            ret[j++] = row[i + 3];
        }
        return ret;
    }

    private static byte[] bit4to8(byte[] row, int width) {
        byte[] ret = new byte[width];
        for (int i = 0; i < width; i++) {
            if (i % 2 == 0) {
                ret[i] = (byte) ((row[i / 2] & m11110000) >> 4);
            } else {
                ret[i] = (byte) ((row[i / 2] & m00001111));
            }
        }
        return ret;
    }

    private static byte[] bit1to8(byte[] row, int width) {
        byte[] ret = new byte[width];
        for (int i = 0; i < width; i++) {
            switch(i % 8) {
                case 0:
                    ret[i] = (byte) ((row[i / 8] & m10000000) >> 7);
                    break;
                case 1:
                    ret[i] = (byte) ((row[i / 8] & m01000000) >> 6);
                    break;
                case 2:
                    ret[i] = (byte) ((row[i / 8] & m00100000) >> 5);
                    break;
                case 3:
                    ret[i] = (byte) ((row[i / 8] & m00010000) >> 4);
                    break;
                case 4:
                    ret[i] = (byte) ((row[i / 8] & m00001000) >> 3);
                    break;
                case 5:
                    ret[i] = (byte) ((row[i / 8] & m00000100) >> 2);
                    break;
                case 6:
                    ret[i] = (byte) ((row[i / 8] & m00000010) >> 1);
                    break;
                case 7:
                    ret[i] = (byte) ((row[i / 8] & m00000001));
                    break;
            }
        }
        return ret;
    }

    private void parsePalette(java.io.InputStream is, int size) throws Exception {
        palette = new byte[size][];
        for (int i = 0; i < size; i++) {
            palette[i] = getBytes(is, 4);
        }
    }

    private void skipNBytes(java.io.InputStream inputStream, int n) {
        try {
            getBytes(inputStream, n);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] getBytes(java.io.InputStream inputStream, int length) throws Exception {
        byte[] buf = new byte[length];
        inputStream.read(buf, 0, buf.length);
        return buf;
    }

    private int read2BytesLE(java.io.InputStream inputStream) throws Exception {
        byte[] buf = getBytes(inputStream, 2);
        int val = 0;
        val |= buf[1] & 0xff;
        val <<= 8;
        val |= buf[0] & 0xff;
        return val;
    }

    private int readSignedInt(java.io.InputStream inputStream) throws Exception {
        byte[] buf = getBytes(inputStream, 4);
        long val = 0L;
        val |= buf[3] & 0xff;
        val <<= 8;
        val |= buf[2] & 0xff;
        val <<= 8;
        val |= buf[1] & 0xff;
        val <<= 8;
        val |= buf[0] & 0xff;
        return (int) val;
    }

    public int getWidth() {
        return this.w;
    }

    public int getHeight() {
        return this.h;
    }

    public byte[] getData() {
        return this.deflated;
    }
}
