package mid_project.model;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public interface DiagramObject {
    void draw(Graphics2D graphics);

    void drawSelection(Graphics2D graphics);

    boolean contains(Point point);

    Rectangle getBounds();

    void moveBy(int deltaX, int deltaY);

    int getDepth();
 
    void setDepth(int depth);

    boolean isComposite();

    default boolean isFullyInside(Rectangle area) {
        return area.contains(getBounds());
    }

    default PortHit hitTestPort(Point point, int tolerance) {
        return null;
    }
}
