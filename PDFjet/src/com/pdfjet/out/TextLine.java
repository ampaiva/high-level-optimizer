package com.pdfjet.out;

/**
 *  Used to create text line objects.
 *
 *
 */
public class TextLine implements Drawable {

    public float x;

    public float y;

    public Font font;

    public Font fallbackFont;

    public String str;

    public String uri;

    public String key;

    public boolean underline = false;

    public boolean strikeout = false;

    public int degrees = 0;

    public int color = Color.black;

    public float box_x;

    public float box_y;

    public int textEffect = Effect.NORMAL;

    /**
     *  Constructor for creating text line objects.
     *
     *  @param font the font to use.
     */
    public TextLine(Font font) {
        this.font = font;
    }

    /**
     *  Constructor for creating text line objects.
     *
     *  @param font the font to use.
     *  @param text the text.
     */
    public TextLine(Font font, String text) {
        this.font = font;
        this.str = text;
    }

    /**
     *  Sets the position where this text line will be drawn on the page.
     *
     *  @param x the x coordinate of the top left corner of the text line.
     *  @param y the y coordinate of the top left corner of the text line.
     */
    public void setPosition(double x, double y) {
        this.x = (float) x;
        this.y = (float) y;
    }

    /**
     *  Sets the position where this text line will be drawn on the page.
     *
     *  @param x the x coordinate of the top left corner of the text line.
     *  @param y the y coordinate of the top left corner of the text line.
     */
    public void setPosition(float x, float y) {
        setLocation(x, y);
    }

    /**
     *  Sets the location where this text line will be drawn on the page.
     *
     *  @param x the x coordinate of the top left corner of the text line.
     *  @param y the y coordinate of the top left corner of the text line.
     */
    public void setLocation(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     *  Sets the text.
     *
     *  @param text the text.
     */
    public void setText(String text) {
        this.str = text;
    }

    /**
     *  Sets the font to use for this text line.
     *
     *  @param font the font to use.
     */
    public void setFont(Font font) {
        this.font = font;
    }

    /**
     *  Gets the font to use for this text line.
     *
     *  @return font the font to use.
     */
    public Font getFont() {
        return font;
    }

    /**
     *  Sets the fallback font.
     *
     *  @param fallbackFont the fallback font.
     */
    public void setFallbackFont(Font fallbackFont) {
        this.fallbackFont = fallbackFont;
    }

    /**
     *  Sets the color for this text line.
     *
     *  @param color the color is specified as an integer.
     */
    public void setColor(int color) {
        this.color = color;
    }

    /**
     * Sets the pen color.
     * 
     * @param color the color. See the Color class for predefined values or define your own using 0x00RRGGBB packed integers.
     */
    public void setColor(int[] color) {
        this.color = color[0] << 16 | color[1] << 8 | color[2];
    }

    /**
     *  Returns the text line color.
     *
     *  @return the text line color.
     */
    public int getColor() {
        return this.color;
    }

    /**
     *  Returns the text.
     *
     *  @return the text.
     */
    public String getText() {
        return str;
    }

    /**
     * Returns the y coordinate of the destination.
     * 
     * @return the y coordinate of the destination.
     */
    public float getDestinationY() {
        return y - font.getSize();
    }

    /**
     *  Returns the width of this TextLine.
     *
     *  @return the width.
     */
    public float getWidth() {
        if (fallbackFont == null) {
            return font.stringWidth(str);
        }
        return font.stringWidth(fallbackFont, str);
    }

    /**
     *  Returns the height of this TextLine.
     *
     *  @return the height.
     */
    public float getHeight() {
        return font.getHeight();
    }

    /**
     *  Sets the URI for the "click text line" action.
     *
     *  @param uri the URI
     */
    public void setURIAction(String uri) {
        this.uri = uri;
    }

    /**
     * Returns the action URI.
     * 
     * @return the action URI.
     */
    public String getURIAction() {
        return this.uri;
    }

    /**
     *  Sets the destination key for the action.
     *
     *  @param key the destination name.
     */
    public void setGoToAction(String key) {
        this.key = key;
    }

    /**
     * Returns the GoTo action string.
     * 
     * @return the GoTo action string.
     */
    public String getGoToAction() {
        return this.key;
    }

    /**
     *  Sets the underline variable.
     *  If the value of the underline variable is 'true' - the text is underlined.
     *
     *  @param underline the underline flag.
     */
    public void setUnderline(boolean underline) {
        this.underline = underline;
    }

    /**
     * Returns the underline flag.
     * 
     * @return the underline flag.
     */
    public boolean getUnderline() {
        return this.underline;
    }

    /**
     *  Sets the strike variable.
     *  If the value of the strike variable is 'true' - a strike line is drawn through the text.
     *
     *  @param strike the strike value.
     */
    public void setStrikeLine(boolean strike) {
        this.strikeout = strike;
    }

    /**
     *  Sets the strike variable.
     *  If the value of the strike variable is 'true' - a strike line is drawn through the text.
     *
     *  @param strike the strike value.
     */
    public void setStrikeout(boolean strike) {
        this.strikeout = strike;
    }

    /**
     * Returns the strikeout flag.
     * 
     * @return the strikeout flag.
     */
    public boolean getStrikeout() {
        return this.strikeout;
    }

    /**
     *  Sets the direction in which to draw the text.
     *
     *  @param degrees the number of degrees.
     */
    public void setTextDirection(int degrees) {
        this.degrees = degrees;
    }

    /**
     * Returns the text effect.
     * 
     * @return the text effect.
     */
    public int getTextEffect() {
        return textEffect;
    }

    /**
     * Sets the text effect.
     * 
     * @param textEffect Effect.NORMAL, Effect.SUBSCRIPT or Effect.SUPERSCRIPT.
     */
    public void setTextEffect(int textEffect) {
        this.textEffect = textEffect;
    }

    /**
     *  Places this text line in the specified box.
     *
     *  @param box the specified box.
     */
    public void placeIn(Box box) throws Exception {
        placeIn(box, 0.0f, 0.0f);
    }

    /**
     *  Places this text line in the box at the specified offset.
     *
     *  @param box the specified box.
     *  @param x_offset the x offset from the top left corner of the box.
     *  @param y_offset the y offset from the top left corner of the box.
     */
    public void placeIn(Box box, double x_offset, double y_offset) throws Exception {
        placeIn(box, (float) x_offset, (float) y_offset);
    }

    /**
     *  Places this text line in the box at the specified offset.
     *
     *  @param box the specified box.
     *  @param x_offset the x offset from the top left corner of the box.
     *  @param y_offset the y offset from the top left corner of the box.
     */
    public void placeIn(Box box, float x_offset, float y_offset) throws Exception {
        box_x = box.x + x_offset;
        box_y = box.y + y_offset;
    }

    /**
     *  Draws this text line on the specified page.
     *
     *  @param page the page to draw this text line on.
     */
    public void drawOn(Page page) throws Exception {
        drawOn(page, true);
    }

    /**
     *  Draws this text line on the specified page if the draw parameter is true.
     *
     *  @param page the page to draw this text line on.
     *  @param draw if draw is false - no action is performed.
     */
    protected void drawOn(Page page, boolean draw) throws Exception {
        if (page == null || !draw) return;
        page.setTextDirection(degrees);
        x += box_x;
        y += box_y;
        if (uri != null || key != null) {
            page.annots.add(new Annotation(uri, key, x, page.height - (y - font.ascent), x + font.stringWidth(str), page.height - (y - font.descent)));
        }
        if (str != null) {
            page.setBrushColor(color);
            if (fallbackFont == null) {
                page.drawString(font, str, x, y);
            } else {
                page.drawString(font, fallbackFont, str, x, y);
            }
        }
        if (underline) {
            page.setPenWidth(font.underlineThickness);
            page.setPenColor(color);
            float lineLength = font.stringWidth(str);
            double radians = Math.PI * degrees / 180.0;
            double x_adjust = font.underlinePosition * Math.sin(radians);
            double y_adjust = font.underlinePosition * Math.cos(radians);
            double x2 = x + lineLength * Math.cos(radians);
            double y2 = y - lineLength * Math.sin(radians);
            page.moveTo(x + x_adjust, y + y_adjust);
            page.lineTo(x2 + x_adjust, y2 + y_adjust);
            page.strokePath();
        }
        if (strikeout) {
            page.setPenWidth(font.underlineThickness);
            page.setPenColor(color);
            float lineLength = font.stringWidth(str);
            double radians = Math.PI * degrees / 180.0;
            double x_adjust = (font.body_height / 4.0) * Math.sin(radians);
            double y_adjust = (font.body_height / 4.0) * Math.cos(radians);
            double x2 = x + lineLength * Math.cos(radians);
            double y2 = y - lineLength * Math.sin(radians);
            page.moveTo(x - x_adjust, y - y_adjust);
            page.lineTo(x2 - x_adjust, y2 - y_adjust);
            page.strokePath();
        }
        page.setTextDirection(0);
    }
}
