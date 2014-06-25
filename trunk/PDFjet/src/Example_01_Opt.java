import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.pdfjet.opt.Box;
import com.pdfjet.opt.Color;
import com.pdfjet.opt.Letter;
import com.pdfjet.opt.Line;
import com.pdfjet.opt.PDF;
import com.pdfjet.opt.Page;
import com.pdfjet.opt.Point;

/**
 * Example_01.java We will draw the American flag using Box, Line and
 * Point objects.
 */
public class Example_01_Opt {

    int run_inline_02(int pages) throws Exception, FileNotFoundException {
        PDF pdf = new PDF(new BufferedOutputStream(new FileOutputStream("Example_01.pdf")));

        for (int i = 0; i < pages; i++) {
            Page page = new Page(pdf, Letter.PORTRAIT);

            Box flag = new Box();
            flag.setLocation(100f, 100f);
            flag.setSize(190f, 100f);
            flag.setColor(Color.white);
            flag.drawOn(page);

            float sw = 7.69f; // stripe width
            //Line stripe = new Line(0.0f, sw / 2, 190.0f, sw / 2);
            Line stripe = new Line();
            stripe.x1 = 0.0f;
            stripe.y1 = sw / 2;
            stripe.x2 = 190.0f;
            stripe.y2 = sw / 2;

            //stripe.setWidth(sw);
            stripe.width = sw;
            //stripe.setColor(Color.oldgloryred);
            stripe.color = Color.oldgloryred;
            for (int row = 0; row < 7; row++) {
                //stripe.placeIn(flag, 0.0f, row * 2 * sw);
                stripe.box_x = flag.x + 0.0f;
                stripe.box_y = flag.y + (row * 2 * sw);
                stripe.drawOn(page);
            }

            Box union = new Box();
            union.setSize(76.0f, 53.85f);
            union.setColor(Color.oldgloryblue);
            union.setFillShape(true);
            union.placeIn(flag, 0f, 0f);
            union.drawOn(page);

            float h_si = 12.6f; // horizontal star interval
            float v_si = 10.8f; // vertical star interval
            Point star = new Point(h_si / 2, v_si / 2);
            star.shape = Point.STAR;
            star.r = 3.0f;
            star.color = Color.white;
            star.fillShape = true;

            for (int row = 0; row < 6; row++) {
                for (int col = 0; col < 5; col++) {
                    //star.placeIn(union, row * h_si, col * v_si);
                    star.box_x = union.x + (row * h_si);
                    star.box_y = union.y + (col * v_si);
                    star.drawOn(page);
                }
            }
            star.x = h_si;
            star.y = v_si;
            for (int row = 0; row < 5; row++) {
                for (int col = 0; col < 4; col++) {
                    //star.placeIn(union, row * h_si, col * v_si);
                    star.box_x = union.x + (row * h_si);
                    star.box_y = union.y + (col * v_si);
                    star.drawOn(page);
                }
            }
        }

        pdf.close();
        return 0;
    }
}
