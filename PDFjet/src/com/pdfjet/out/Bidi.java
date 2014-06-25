package com.pdfjet.out;

/**
 *  Provides simple BIDI processing for Hebrew.
 *  Please see Example_27.
 *  Note that the base level must be right to left.
 *  This means you can only use Hebrew text with a few Latin words and numbers embedded in it.
 *  Not the other way around.
 */
public class Bidi {

    /**
     *  Reorders the string so that Hebrew text flows from right to left while numbers and Latin text flow from left to right.
     *
     *  @param str the input string.
     *  @return the processed string.
     */
    public static String reorderVisually(String str) {
        StringBuilder sb1 = new StringBuilder(str).reverse();
        StringBuilder sb2 = new StringBuilder();
        StringBuilder sb3 = new StringBuilder();
        for (int i = 0; i < sb1.length(); i++) {
            int ch = sb1.charAt(i);
            if (ch >= 0x591 && ch <= 0x05F4) {
                if (sb2.length() != 0) {
                    sb3.append(sb2.reverse());
                    sb2 = new StringBuilder();
                }
                sb3.append((char) ch);
            } else {
                sb2.append((char) ch);
            }
        }
        sb3.append(sb2.reverse());
        return sb3.toString();
    }
}
