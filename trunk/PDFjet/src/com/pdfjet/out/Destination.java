package com.pdfjet.out;

/**
 *  Used to create PDF destination objects.
 *
 *
 */
public class Destination {

    public String name;

    public int pageObjNumber;

    public float yPosition;

    /**
     *  This class is used to create destination objects.
     *
     *  @param name the name of this destination object.
     *  @param yPosition the y coordinate of the top left corner.
     *
     */
    public Destination(String name, double yPosition) {
        this(name, (float) yPosition);
    }

    /**
     *  This class is used to create destination objects.
     *
     *  @param name the name of this destination object.
     *  @param yPosition the y coordinate of the top left corner.
     *
     */
    public Destination(String name, float yPosition) {
        this.name = name;
        this.yPosition = yPosition;
    }

    protected void setPageObjNumber(int pageObjNumber) {
        this.pageObjNumber = pageObjNumber;
    }
}
