package com.pdfjet.out;

class State {

    public float[] pen;

    public float[] brush;

    public float pen_width;

    public int line_cap_style;

    public int line_join_style;

    public String linePattern;

    public State(float[] pen, float[] brush, float pen_width, int line_cap_style, int line_join_style, String linePattern) {
        this.pen = new float[] { pen[0], pen[1], pen[2] };
        this.brush = new float[] { brush[0], brush[1], brush[2] };
        this.pen_width = pen_width;
        this.line_cap_style = line_cap_style;
        this.line_join_style = line_join_style;
        this.linePattern = linePattern;
    }

    public float[] getPen() {
        return pen;
    }

    public float[] getBrush() {
        return brush;
    }

    public float getPenWidth() {
        return pen_width;
    }

    public int getLineCapStyle() {
        return line_cap_style;
    }

    public int getLineJoinStyle() {
        return line_join_style;
    }

    public String getLinePattern() {
        return linePattern;
    }
}
