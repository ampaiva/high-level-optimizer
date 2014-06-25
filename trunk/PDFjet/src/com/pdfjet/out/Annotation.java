package com.pdfjet.out;

/**
 *  Used to create PDF annotation objects.
 *
 *
 */
public class Annotation {

    public String uri;

    public String key;

    public float x1;

    public float y1;

    public float x2;

    public float y2;

    /**
     *  This class is used to create annotation objects.
     *
     *  @param uri the URI string.
     *  @param key the destination name.
     *  @param x1 the x coordinate of the top left corner.
     *  @param y1 the y coordinate of the top left corner.
     *  @param x2 the x coordinate of the bottom right corner.
     *  @param y2 the y coordinate of the bottom right corner.
     *
     */
    public Annotation(String uri, String key, float x1, float y1, float x2, float y2) {
        this.uri = uri;
        this.key = key;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }
}
