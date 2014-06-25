package com.pdfjet.out;

/**
 *  Creates a CheckBox, which can be set checked or unchecked.
 *  Default is checked, with a blue check mark.
 *  Default box is black, default font size is 14.0f.
 */
public class CheckBox {

    public boolean boxChecked = true;

    public float x;

    public float y;

    public float w = 12.0f;

    public float h = 12.0f;

    public int checkColor = Color.blue;

    public int boxColor = Color.black;

    public float penWidth = 0.3f;

    public float checkWidth = 3.0f;

    public int mark = 1;

    public Font font = null;

    public String text = null;

    public String uri = null;

    /**
     *  Creates a CheckBox with blue check mark.
     *
     */
    public CheckBox() {
    }

    public CheckBox(Font font, String text) {
        this.font = font;
        this.text = text;
        this.boxChecked = false;
    }

    /**
     *  Creates a CheckBox.
     *
     *  @param boxChecked boolean - true or false. Default is true. 
     *  @param checkColor the color of the check mark. Default is blue.
     */
    public CheckBox(boolean boxChecked, int checkColor) {
        this.boxChecked = boxChecked;
        this.checkColor = checkColor;
    }

    /**
     *  Creates a CheckBox.
     *
     *  @param boxChecked boolean - If true box is checked. If false no check mark.
     *  Use default green check mark. 
     */
    public CheckBox(boolean boxChecked) {
        this.boxChecked = boxChecked;
    }

    /**
     *  Sets the color of the check mark.
     *
     *  @param checkColor the check mark color specified as an 0xRRGGBB integer.
     */
    public void setCheckColor(int checkColor) {
        this.checkColor = checkColor;
    }

    /**
     *  Sets the color of the check box.
     *
     *  @param boxColor the check box color specified as an 0xRRGGBB integer.
     */
    public void setBoxColor(int boxColor) {
        this.boxColor = boxColor;
    }

    /**
     *  Set the x,y position on the Page.
     *
     *  @param x the x coordinate on the Page.
     *  @param y the y coordinate on the Page.
     */
    public void setPosition(double x, double y) {
        setPosition((float) x, (float) y);
    }

    /**
     *  Set the x,y position on the Page.
     *
     *  @param x the x coordinate on the Page.
     *  @param y the y coordinate on the Page.
     */
    public void setPosition(float x, float y) {
        setLocation(x, y);
    }

    /**
     *  Set the x,y location on the Page.
     *
     *  @param x the x coordinate on the Page.
     *  @param y the y coordinate on the Page.
     */
    public void setLocation(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     *  Set the size of the CheckBox.
     *
     *  @param size the size of the CheckBox.
     */
    public void setSize(double size) {
        setSize((float) size);
    }

    /**
     *  Set the size of the CheckBox.
     *
     *  @param size the size of the CheckBox.
     */
    public void setSize(float size) {
        this.h = size;
        this.w = size;
        this.checkWidth = size / 4.0f;
        this.penWidth = size / 40.0f;
    }

    /**
     *  Sets the type of check mark.
     *
     *  @param mark the type of check mark.
     *  1 = check (the default)
     *  2 = X
     *  
     */
    public void setMarkType(int mark) {
        if (mark > 0 && mark < 3) {
            this.mark = mark;
        }
    }

    /**
     *  Gets the height of the CheckBox.
     *
     */
    public float getHeight() {
        return this.h;
    }

    /**
     *  Gets the width of the CheckBox.
     *
     */
    public float getWidth() {
        return this.w;
    }

    /**
     *  Get the x coordinate of the upper left corner.
     *
     */
    public float getX() {
        return this.x;
    }

    /**
     *  Get the y coordinate of the upper left corner.
     *
     */
    public float getY() {
        return this.y;
    }

    public void setChecked(boolean boxChecked) {
        this.boxChecked = boxChecked;
    }

    /**
     *  Sets the URI for the "click text line" action.
     *
     *  @param uri the URI.
     */
    public void setURIAction(String uri) {
        this.uri = uri;
    }

    /**
     *  Draws this CheckBox on the specified Page.
     *
     *  @param page the Page where the CheckBox is to be drawn.
     */
    public void drawOn(Page page) throws Exception {
        page.setPenWidth(penWidth);
        page.moveTo(x, y);
        page.lineTo(x + w, y);
        page.lineTo(x + w, y + h);
        page.lineTo(x, y + h);
        page.closePath();
        page.setPenColor(boxColor);
        page.strokePath();
        if (this.boxChecked) {
            page.setPenWidth(checkWidth);
            if (mark == 1) {
                page.moveTo(x + checkWidth / 2, y + h / 2);
                page.lineTo(x + w / 3, (y + h) - checkWidth / 2);
                page.lineTo(x + w - checkWidth / 2, y + checkWidth / 2);
            } else {
                page.moveTo(x + checkWidth / 2, y + checkWidth / 2);
                page.lineTo((x + w) - checkWidth / 2, (y + h) - checkWidth / 2);
                page.moveTo((x + w) - checkWidth / 2, y + checkWidth / 2);
                page.lineTo(x + checkWidth / 2, (y + h) - checkWidth / 2);
            }
            page.setPenColor(checkColor);
            page.setLineCapStyle(Cap.ROUND);
            page.strokePath();
        }
        if (font != null && text != null) {
            float x_text = x + 5f * w / 4f;
            float y_text = (page.height - y) - 7f * h / 8f;
            page.drawString(font, text, x_text, y + h);
            if (uri != null) {
                page.annots.add(new Annotation(uri, text, x_text, y_text + font.descent, x_text + font.stringWidth(text), y_text + font.ascent));
            }
        }
        page.setPenWidth(0f);
        page.setPenColor(Color.black);
    }
}
