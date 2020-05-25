package org.khelekore.prtree;

import java.awt.geom.Rectangle2D;

public class Rectangle2DConverter implements MBRConverter<Rectangle2D> {

    public int getDimensions () {
        return 2;
    }

    public double getMin (int axis, Rectangle2D t) {
        return axis == 0 ? t.getMinX () : t.getMinY ();
    }

    public double getMax (int axis, Rectangle2D t) {
        return axis == 0 ? t.getMaxX () : t.getMaxY ();
    }

}
