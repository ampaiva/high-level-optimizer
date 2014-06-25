package com.pdfjet.out;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Container for drawable objects that can be drawn on a page as part of Optional Content Group. 
 * Please see the PDF specification and Example_30 for more details.
 *
 * @author Mark Paxton
 */
public class OptionalContentGroup {

    public String name;

    public int ocgNumber;

    public int objNumber;

    public boolean visible;

    public boolean printable;

    public boolean exportable;

    public List<Drawable> components;

    public OptionalContentGroup(String name) {
        this.name = name;
        this.components = new ArrayList<Drawable>();
    }

    public void add(Drawable d) {
        components.add(d);
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setPrintable(boolean printable) {
        this.printable = printable;
    }

    public void setExportable(boolean exportable) {
        this.exportable = exportable;
    }

    public void drawOn(Page p) throws Exception {
        if (!components.isEmpty()) {
            p.pdf.groups.add(this);
            ocgNumber = p.pdf.groups.size();
            p.pdf.newobj();
            p.pdf.append("<<\n");
            p.pdf.append("/Type /OCG\n");
            p.pdf.append("/Name (" + name + ")\n");
            p.pdf.append(">>\n");
            p.pdf.endobj();
            objNumber = p.pdf.objNumber;
            p.append("/OC /OC");
            p.append(ocgNumber);
            p.append(" BDC\n");
            for (Drawable component : components) {
                component.drawOn(p);
            }
            p.append("\nEMC\n");
        }
    }
}
