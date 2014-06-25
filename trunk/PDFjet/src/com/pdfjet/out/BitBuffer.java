package com.pdfjet.out;

/**
 * BitBuffer
 * @author Kazuhiko Arase
 */
class BitBuffer {

    public byte[] buffer;

    public int length;

    public int increments = 32;

    public BitBuffer() {
        buffer = new byte[increments];
        length = 0;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public int getLengthInBits() {
        return length;
    }

    public void put(int num, int length) {
        for (int i = 0; i < length; i++) {
            put(((num >>> (length - i - 1)) & 1) == 1);
        }
    }

    public void put(boolean bit) {
        if (length == buffer.length * 8) {
            byte[] newBuffer = new byte[buffer.length + increments];
            System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
            buffer = newBuffer;
        }
        if (bit) {
            buffer[length / 8] |= (0x80 >>> (length % 8));
        }
        length++;
    }
}
