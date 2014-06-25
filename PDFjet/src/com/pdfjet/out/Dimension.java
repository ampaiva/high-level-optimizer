package com.pdfjet.out;

/**
 *  Encapsulates the width and height of a component.
 */
public class Dimension {

    public float w;

    public float h;

    /**
     *  Constructor for creating dimension objects.
     *
     *  @param width the width.
     *  @param height the height.
     */
    public Dimension(float width, float height) {
        this.w = width;
        this.h = height;
    }

    public float getWidth() {
        return w;
    }

    public float getHeight() {
        return h;
    }
}
