package com.ampaiva.hlo.cm;

public class CodePosition {
    private final int position;
    private final int offset;

    public CodePosition(int position, int offset) {
        this.position = position;
        this.offset = offset;
    }

    public int getPosition() {
        return position;
    }

    public int getOffset() {
        return offset;
    };

    @Override
    public String toString() {
        return "CodePosition [position=" + position + ", offset=" + offset + "]";
    }

}
