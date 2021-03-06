/**
 *  Cell.java
 *
Copyright (c) 2014, Innovatics Inc.
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice,
      this list of conditions and the following disclaimer.

 * Redistributions in binary form must reproduce the above copyright notice,
      this list of conditions and the following disclaimer in the documentation
      and / or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.pdfjet.opt;

/**
 * Used to create table cell objects. See the Table class for more
 * information.
 * 
 */
public class Cell {

    protected Font font;
    protected String text;
    protected Image image;
    protected Point point;
    protected CompositeTextLine compositeTextLine;

    protected float width = 70f;
    protected float top_padding = 2f;
    protected float bottom_padding = 2f;
    protected float left_padding = 2f;
    protected float right_padding = 2f;
    protected float lineWidth = 0.2f;

    private int background = -1;
    private int pen = Color.black;
    private int brush = Color.black;

    // Cell properties
    // Colspan:
    // bits 0 to 15
    // Border:
    // bit 16 - top
    // bit 17 - bottom
    // bit 18 - left
    // bit 19 - right
    // Text Alignment:
    // bit 20
    // bit 21
    // Text Decoration:
    // bit 22 - underline
    // bit 23 - strikeout
    // Future use:
    // bits 24 to 31
    private int properties = 0x000F0001;
    private String uri;

    /**
     * Creates a cell object and sets the font.
     * 
     * @param font the font.
     */
    public Cell(Font font) {
        this.font = font;
    }

    /**
     * Creates a cell object and sets the font and the cell text.
     * 
     * @param font the font.
     * @param text the text.
     */
    public Cell(Font font, String text) {
        this.font = font;
        this.text = text;
    }

    /**
     * Sets the font for this cell.
     * 
     * @param font the font.
     */
    public void setFont(Font font) {
        this.font = font;
    }

    /**
     * Returns the font used by this cell.
     * 
     * @return the font.
     */
    public Font getFont() {
        return this.font;
    }

    /**
     * Sets the cell text.
     * 
     * @param text the cell text.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Returns the cell text.
     * 
     * @return the cell text.
     */
    public String getText() {
        return this.text;
    }

    /**
     * Sets the image inside this cell.
     * 
     * @param image the image.
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * Returns the cell image.
     * 
     * @return the image.
     */
    public Image getImage() {
        return this.image;
    }

    /**
     * Sets the point inside this cell. See the Point class and
     * Example_09 for more information.
     * 
     * @param point the point.
     */
    public void setPoint(Point point) {
        this.point = point;
    }

    /**
     * Returns the cell point.
     * 
     * @return the point.
     */
    public Point getPoint() {
        return this.point;
    }

    /**
     * Sets the composite text object.
     * 
     * @param compositeTextLine the composite text object.
     */
    public void setCompositeTextLine(CompositeTextLine compositeTextLine) {
        this.compositeTextLine = compositeTextLine;
    }

    /**
     * Returns the composite text object.
     * 
     * @return the composite text object.
     */
    public CompositeTextLine getCompositeTextLine() {
        return this.compositeTextLine;
    }

    /**
     * Sets the width of this cell.
     * 
     * @param width the specified width.
     */
    public void setWidth(float width) {
        this.width = width;
    }

    /**
     * Returns the cell width.
     * 
     * @return the cell width.
     */
    public float getWidth() {
        return this.width;
    }

    /**
     * Sets the top padding of this cell.
     * 
     * @param padding the top padding.
     */
    public void setTopPadding(float padding) {
        this.top_padding = padding;
    }

    /**
     * Sets the bottom padding of this cell.
     * 
     * @param padding the bottom padding.
     */
    public void setBottomPadding(float padding) {
        this.bottom_padding = padding;
    }

    /**
     * Sets the left padding of this cell.
     * 
     * @param padding the left padding.
     */
    public void setLeftPadding(float padding) {
        this.left_padding = padding;
    }

    /**
     * Sets the right padding of this cell.
     * 
     * @param padding the right padding.
     */
    public void setRightPadding(float padding) {
        this.right_padding = padding;
    }

    /**
     * Returns the cell height.
     * 
     * @return the cell height.
     */
    public float getHeight() {
        if (image != null) {
            return image.getHeight() + top_padding + bottom_padding;
        }
        return font.body_height + top_padding + bottom_padding;
    }

    /**
     * Sets the border line width.
     * 
     * @param lineWidth the border line width.
     */
    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
    }

    /**
     * Returns the border line width.
     * 
     * @return the border line width.
     */
    public float getLineWidth() {
        return this.lineWidth;
    }

    /**
     * Sets the background to the specified color.
     * 
     * @param color the color specified as 0xRRGGBB integer.
     */
    public void setBgColor(int color) {
        this.background = color;
    }

    /**
     * Returns the background color of this cell.
     * 
     */
    public int getBgColor() {
        return this.background;
    }

    /**
     * Sets the pen color.
     * 
     * @param color the color specified as 0xRRGGBB integer.
     */
    public void setPenColor(int color) {
        this.pen = color;
    }

    /**
     * Returns the pen color.
     * 
     */
    public int getPenColor() {
        return pen;
    }

    /**
     * Sets the brush color.
     * 
     * @param color the color specified as 0xRRGGBB integer.
     */
    public void setBrushColor(int color) {
        this.brush = color;
    }

    /**
     * Returns the brush color.
     * 
     * @return the brush color.
     */
    public int getBrushColor() {
        return brush;
    }

    /**
     * Sets the pen and brush colors to the specified color.
     * 
     * @param color the color specified as 0xRRGGBB integer.
     */
    public void setFgColor(int color) {
        this.pen = color;
        this.brush = color;
    }

    protected void setProperties(int properties) {
        this.properties = properties;
    }

    protected int getProperties() {
        return this.properties;
    }

    /**
     * Sets the column span private variable.
     * 
     * @param colspan the specified column span value.
     */
    public void setColSpan(int colspan) {
        this.properties &= 0x00FF0000;
        this.properties |= (colspan & 0x0000FFFF);
    }

    /**
     * Returns the column span private variable value.
     * 
     * @return the column span value.
     */
    public int getColSpan() {
        return (this.properties & 0x0000FFFF);
    }

    /**
     * Sets the cell border object.
     * 
     * @param border the border object.
     */
    public void setBorder(int border, boolean visible) {
        if (visible) {
            this.properties |= border;
        } else {
            this.properties &= (~border & 0x00FFFFFF);
        }
    }

    /**
     * Returns the cell border object.
     * 
     * @return the cell border object.
     */
    public boolean getBorder(int border) {
        return (this.properties & border) != 0;
    }

    /**
     * Sets all border object parameters to false. This cell will have
     * no borders when drawn on the page.
     */
    public void setNoBorders() {
        this.properties &= 0x00F0FFFF;
    }

    /**
     * Sets the cell text alignment.
     * 
     * @param alignment the alignment code. Supported values:
     *            Align.LEFT, Align.RIGHT and Align.CENTER.
     */
    public void setTextAlignment(int alignment) {
        this.properties &= 0x00CFFFFF;
        this.properties |= (alignment & 0x00300000);
    }

    /**
     * Returns the text alignment.
     * 
     */
    public int getTextAlignment() {
        return (this.properties & 0x00300000);
    }

    /**
     * Sets the underline text parameter. If the value of the
     * underline variable is 'true' - the text is underlined.
     * 
     * @param underline the underline text parameter.
     */
    public void setUnderline(boolean underline) {
        if (underline) {
            this.properties |= 0x00400000;
        } else {
            this.properties &= 0x00BFFFFF;
        }
    }

    /**
     * Returns the underline text parameter.
     * 
     * @return the underline text parameter.
     */
    public boolean getUnderline() {
        return (properties & 0x00400000) != 0;
    }

    /**
     * Sets the strikeout text parameter.
     * 
     * @param strikeout the strikeout text parameter.
     */
    public void setStrikeout(boolean strikeout) {
        if (strikeout) {
            this.properties |= 0x00800000;
        } else {
            this.properties &= 0x007FFFFF;
        }
    }

    /**
     * Returns the strikeout text parameter.
     * 
     * @return the strikeout text parameter.
     */
    public boolean getStrikeout() {
        return (properties & 0x00800000) != 0;
    }

    public void setURIAction(String uri) {
        this.uri = uri;
    }

    /**
     * Draws the point, text and borders of this cell.
     * 
     */
    protected void paint(Page page, float x, float y, float w, float h) throws Exception {
        if (background != -1) {
            drawBackground(page, x, y, w, h);
        }
        if (image != null) {
            image.setLocation(x + left_padding, y + top_padding);
            image.drawOn(page);
        }
        drawBorders(page, x, y, w, h);
        if (text != null) {
            drawText(page, x, y, w);
        }
        if (point != null) {
            if (point.align == Align.LEFT) {
                point.x = x + (2 * point.r);
            } else if (point.align == Align.RIGHT) {
                point.x = (x + w) - (this.right_padding / 2);
            }
            point.y = y + (h / 2);
            page.setBrushColor(point.color);
            if (point.uri != null) {
                page.annots.add(new Annotation(point.uri, null, point.x - point.r, page.height - (point.y - point.r),
                        point.x + point.r, page.height - (point.y + point.r)));
            }
            page.drawPoint(point);
        }
    }

    private void drawBackground(Page page, float x, float y, float cell_w, float cell_h) throws Exception {
        page.setBrushColor(background);
        page.fillRect(x, y, cell_w, cell_h);
    }

    private void drawBorders(Page page, float x, float y, float cell_w, float cell_h) throws Exception {

        page.setPenColor(pen);
        page.setPenWidth(lineWidth);

        if (getBorder(Border.TOP) && getBorder(Border.BOTTOM) && getBorder(Border.LEFT) && getBorder(Border.RIGHT)) {
            page.drawRect(x, y, cell_w, cell_h);
        } else {
            if (getBorder(Border.TOP)) {
                page.moveTo(x, y);
                page.lineTo(x + cell_w, y);
                page.strokePath();
            }
            if (getBorder(Border.BOTTOM)) {
                page.moveTo(x, y + cell_h);
                page.lineTo(x + cell_w, y + cell_h);
                page.strokePath();
            }
            if (getBorder(Border.LEFT)) {
                page.moveTo(x, y);
                page.lineTo(x, y + cell_h);
                page.strokePath();
            }
            if (getBorder(Border.RIGHT)) {
                page.moveTo(x + cell_w, y);
                page.lineTo(x + cell_w, y + cell_h);
                page.strokePath();
            }
        }

    }

    private void drawText(Page page, float x, float y, float cell_w) throws Exception {

        float x_text;
        float y_text = y + font.ascent + this.top_padding;

        page.setPenColor(pen);
        page.setBrushColor(brush);

        if (getTextAlignment() == Align.RIGHT) {
            if (compositeTextLine == null) {
                x_text = (x + cell_w) - (font.stringWidth(text) + this.right_padding);
                page.drawString(font, text, x_text, y_text);
                if (getUnderline()) {
                    underlineText(page, font, text, x_text, y_text);
                }
                if (getStrikeout()) {
                    strikeoutText(page, font, text, x_text, y_text);
                }
            } else {
                x_text = (x + cell_w) - (compositeTextLine.getWidth() + this.right_padding);
                compositeTextLine.setPosition(x_text, y_text);
                compositeTextLine.drawOn(page);
            }
        } else if (getTextAlignment() == Align.CENTER) {
            if (compositeTextLine == null) {
                x_text = x + this.left_padding
                        + (((cell_w - (left_padding + right_padding)) - font.stringWidth(text)) / 2);
                page.drawString(font, text, x_text, y_text);
                if (getUnderline()) {
                    underlineText(page, font, text, x_text, y_text);
                }
                if (getStrikeout()) {
                    strikeoutText(page, font, text, x_text, y_text);
                }
            } else {
                x_text = x + this.left_padding
                        + (((cell_w - (left_padding + right_padding)) - compositeTextLine.getWidth()) / 2);
                compositeTextLine.setPosition(x_text, y_text);
                compositeTextLine.drawOn(page);
            }
        } else if (getTextAlignment() == Align.LEFT) {
            x_text = x + this.left_padding;
            if (compositeTextLine == null) {
                page.drawString(font, text, x_text, y_text);
                if (getUnderline()) {
                    underlineText(page, font, text, x_text, y_text);
                }
                if (getStrikeout()) {
                    strikeoutText(page, font, text, x_text, y_text);
                }
            } else {
                compositeTextLine.setPosition(x_text, y_text);
                compositeTextLine.drawOn(page);
            }
        } else {
            throw new Exception("Invalid Text Alignment!");
        }

        if (uri != null) {
            float w = (compositeTextLine != null) ? compositeTextLine.getWidth() : font.stringWidth(text);
            // Please note: The font descent is a negative number.
            page.annots.add(new Annotation(uri, null, x_text, (page.height - y_text) + font.descent, x_text + w,
                    (page.height - y_text) + font.ascent));
        }
    }

    private void underlineText(Page page, Font font, String text, float x, float y) throws Exception {
        float descent = font.getDescent();
        page.setPenWidth(font.underlineThickness);
        page.moveTo(x, y + descent);
        page.lineTo(x + font.stringWidth(text), y + descent);
        page.strokePath();
    }

    private void strikeoutText(Page page, Font font, String text, float x, float y) throws Exception {
        page.setPenWidth(font.underlineThickness);
        page.moveTo(x, y - (font.getAscent() / 3f));
        page.lineTo(x + font.stringWidth(text), y - (font.getAscent() / 3f));
        page.strokePath();
    }

    protected int getNumVerCells() {
        int n = 1;
        String[] tokens = getText().split("\\s+");
        if (tokens.length == 1) {
            return n;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            if (font.stringWidth(sb.toString() + " " + token) > (getWidth() - (this.left_padding + this.right_padding))) {
                sb = new StringBuilder(token);
                n++;
            } else {
                if (i > 0) {
                    sb.append(" ");
                }
                sb.append(token);
            }
        }
        return n;
    }

} // End of Cell.java
