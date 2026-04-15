package mid_project.model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompositeObject extends AbstractDiagramObject {
    private static final Color SELECT_COLOR = new Color(39, 112, 189);
    private final List<DiagramObject> children;

    public CompositeObject(List<DiagramObject> children) {
        this.children = new ArrayList<>(children);
    }

    @Override
    public void draw(Graphics2D graphics) {
        for (int index = children.size() - 1; index >= 0; index--) {
            children.get(index).draw(graphics);
        }
    }

    @Override
    public void drawSelection(Graphics2D graphics) {
        Rectangle bounds = getBounds();
        graphics.setColor(SELECT_COLOR);
        graphics.setStroke(new BasicStroke(
                2f,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER,
                10f,
                new float[] {8f, 6f},
                0f
        ));
        graphics.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    @Override
    public boolean contains(Point point) {
        return getBounds().contains(point);
    }

    @Override
    public Rectangle getBounds() {
        if (children.isEmpty()) {
            return new Rectangle();
        }

        Rectangle union = new Rectangle(children.get(0).getBounds());
        for (int index = 1; index < children.size(); index++) {
            union = union.union(children.get(index).getBounds());
        }
        return union;
    }

    @Override
    public void moveBy(int deltaX, int deltaY) {
        for (DiagramObject child : children) {
            child.moveBy(deltaX, deltaY);
        }
    }

    @Override
    public boolean isComposite() {
        return true;
    }

    public List<DiagramObject> getChildren() {
        return Collections.unmodifiableList(children);
    }
}
