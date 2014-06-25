package com.pdfjet.out;

/**
 * Interface that is required for components that can be drawn on a PDF page as part of Optional Content Group. 
 *
 * @author Mark Paxton
 */
public interface Drawable {

    /**
     * Draw the component implementing this interface on the PDF page.
     *
     * @param page the page to draw on.
     * @throws Exception
     */
    public void drawOn(Page page) throws Exception;
}
