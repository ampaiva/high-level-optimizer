package com.pdfjet.out;

class Salsa20 {

    public String id = null;

    public Salsa20() {
        int[] a_in = new int[16];
        StringBuilder buf = new StringBuilder(Long.toHexString(System.currentTimeMillis()));
        int len = 128 - buf.length();
        for (int i = 0; i < len; i++) {
            buf.append('0');
        }
        for (int i = 0; i < 128; i += 8) {
            a_in[i / 8] = (int) Long.parseLong(buf.substring(i, i + 8), 16);
        }
        id = bin2hex(salsa20_word_specification(a_in));
    }

    private int R(int a, int b) {
        return (a << b) | (a >>> (32 - b));
    }

    private int[] salsa20_word_specification(int[] a_in) {
        int[] a_out = new int[16];
        int[] x = new int[16];
        for (int i = 0; i < 16; ++i) {
            x[i] = a_in[i];
        }
        for (int i = 20; i > 0; i -= 2) {
            x[4] ^= R(x[0] + x[12], 7);
            x[8] ^= R(x[4] + x[0], 9);
            x[12] ^= R(x[8] + x[4], 13);
            x[0] ^= R(x[12] + x[8], 18);
            x[9] ^= R(x[5] + x[1], 7);
            x[13] ^= R(x[9] + x[5], 9);
            x[1] ^= R(x[13] + x[9], 13);
            x[5] ^= R(x[1] + x[13], 18);
            x[14] ^= R(x[10] + x[6], 7);
            x[2] ^= R(x[14] + x[10], 9);
            x[6] ^= R(x[2] + x[14], 13);
            x[10] ^= R(x[6] + x[2], 18);
            x[3] ^= R(x[15] + x[11], 7);
            x[7] ^= R(x[3] + x[15], 9);
            x[11] ^= R(x[7] + x[3], 13);
            x[15] ^= R(x[11] + x[7], 18);
            x[1] ^= R(x[0] + x[3], 7);
            x[2] ^= R(x[1] + x[0], 9);
            x[3] ^= R(x[2] + x[1], 13);
            x[0] ^= R(x[3] + x[2], 18);
            x[6] ^= R(x[5] + x[4], 7);
            x[7] ^= R(x[6] + x[5], 9);
            x[4] ^= R(x[7] + x[6], 13);
            x[5] ^= R(x[4] + x[7], 18);
            x[11] ^= R(x[10] + x[9], 7);
            x[8] ^= R(x[11] + x[10], 9);
            x[9] ^= R(x[8] + x[11], 13);
            x[10] ^= R(x[9] + x[8], 18);
            x[12] ^= R(x[15] + x[14], 7);
            x[13] ^= R(x[12] + x[15], 9);
            x[14] ^= R(x[13] + x[12], 13);
            x[15] ^= R(x[14] + x[13], 18);
        }
        for (int i = 0; i < 16; ++i) {
            a_out[i] = x[i] + a_in[i];
        }
        return a_out;
    }

    private String bin2hex(int[] binarray) {
        String table = "0123456789abcdef";
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < binarray.length; i++) {
            int a = binarray[i];
            buf.append(table.charAt(a >> 28 & 0x0000000f));
            buf.append(table.charAt(a >> 24 & 0x0000000f));
            buf.append(table.charAt(a >> 20 & 0x0000000f));
            buf.append(table.charAt(a >> 16 & 0x0000000f));
            buf.append(table.charAt(a >> 12 & 0x0000000f));
            buf.append(table.charAt(a >> 8 & 0x0000000f));
            buf.append(table.charAt(a >> 4 & 0x0000000f));
            buf.append(table.charAt(a & 0x0000000f));
        }
        return buf.substring(0, 32);
    }

    public String getID() {
        return id;
    }

    public static void main(String[] args) {
        new Salsa20();
    }
}
