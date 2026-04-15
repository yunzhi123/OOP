package mid_project.model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.List;

public abstract class BasicObject extends AbstractDiagramObject {
    public static final int MIN_SIZE = 20;
    private static final int PORT_SIZE = 10;
    private static final Color FILL_COLOR = new Color(250, 250, 250);
    private static final Color BORDER_COLOR = new Color(46, 46, 46);
    private static final Color PORT_COLOR = new Color(39, 112, 189);

    protected Rectangle bounds;
    private String label = "";
    private Color labelColor = new Color(25, 25, 25);

    protected BasicObject(Rectangle bounds) {
        this.bounds = new Rectangle(bounds);
    }

    protected abstract Shape createShape(Rectangle bounds);

    protected abstract List<PortLocation> supportedPorts();

    @Override
    public void draw(Graphics2D graphics) {
        Shape shape = createShape(bounds);

        graphics.setColor(FILL_COLOR);
        graphics.fill(shape);
        graphics.setStroke(new BasicStroke(2f));
        graphics.setColor(BORDER_COLOR);
        graphics.draw(shape);

        if (!label.isBlank()) {
            FontMetrics metrics = graphics.getFontMetrics();
            int textX = bounds.x + (bounds.width - metrics.stringWidth(label)) / 2;
            int textY = bounds.y + (bounds.height + metrics.getAscent() - metrics.getDescent()) / 2;
            graphics.setColor(labelColor);
            graphics.drawString(label, textX, textY);
        }
    }

    @Override
    public void drawSelection(Graphics2D graphics) {
        graphics.setColor(PORT_COLOR);
        for (PortLocation location : supportedPorts()) {
            Point port = getPortPoint(location);
            graphics.fillRect(port.x - PORT_SIZE / 2, port.y - PORT_SIZE / 2, PORT_SIZE, PORT_SIZE);
        }
    }

    @Override
    public boolean contains(Point point) {
        return createShape(bounds).contains(point);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(bounds);
    }

    @Override
    public void moveBy(int deltaX, int deltaY) {
        bounds.translate(deltaX, deltaY);
    }

    @Override
    public PortHit hitTestPort(Point point, int tolerance) {
        for (PortLocation location : supportedPorts()) {
            Point port = getPortPoint(location);
            if (port.distance(point) <= tolerance) {
                return new PortHit(this, location);
            }
        }
        return null;
    }

    public Point getPortPoint(PortLocation location) {
        int left = bounds.x;
        int centerX = bounds.x + bounds.width / 2;
        int right = bounds.x + bounds.width;
        int top = bounds.y;
        int centerY = bounds.y + bounds.height / 2;
        int bottom = bounds.y + bounds.height;

        return switch (location) {
            case NORTH_WEST -> new Point(left, top);
            case NORTH -> new Point(centerX, top);
            case NORTH_EAST -> new Point(right, top);
            case EAST -> new Point(right, centerY);
            case SOUTH_EAST -> new Point(right, bottom);
            case SOUTH -> new Point(centerX, bottom);
            case SOUTH_WEST -> new Point(left, bottom);
            case WEST -> new Point(left, centerY);
        };
    }

    public void resizeFrom(PortLocation handle, Rectangle originalBounds, Point cursor) {
        int left = originalBounds.x;
        int right = originalBounds.x + originalBounds.width;
        int top = originalBounds.y;
        int bottom = originalBounds.y + originalBounds.height;

        Rectangle nextBounds = switch (handle) {
            case NORTH_WEST -> rectangleFromAxes(axisFromAnchor(right, cursor.x), axisFromAnchor(bottom, cursor.y));
            case NORTH -> rectangleFromAxes(new int[] {left, right}, axisFromAnchor(bottom, cursor.y));
            case NORTH_EAST -> rectangleFromAxes(axisFromAnchor(left, cursor.x), axisFromAnchor(bottom, cursor.y));
            case EAST -> rectangleFromAxes(axisFromAnchor(left, cursor.x), new int[] {top, bottom});
            case SOUTH_EAST -> rectangleFromAxes(axisFromAnchor(left, cursor.x), axisFromAnchor(top, cursor.y));
            case SOUTH -> rectangleFromAxes(new int[] {left, right}, axisFromAnchor(top, cursor.y));
            case SOUTH_WEST -> rectangleFromAxes(axisFromAnchor(right, cursor.x), axisFromAnchor(top, cursor.y));
            case WEST -> rectangleFromAxes(axisFromAnchor(right, cursor.x), new int[] {top, bottom});
        };

        bounds = nextBounds;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label == null ? "" : label;
    }

    public Color getLabelColor() {
        return labelColor;
    }

    public void setLabelColor(Color labelColor) {
        this.labelColor = labelColor == null ? this.labelColor : labelColor;
    }

    private Rectangle rectangleFromAxes(int[] xAxis, int[] yAxis) {
        return new Rectangle(xAxis[0], yAxis[0], xAxis[1] - xAxis[0], yAxis[1] - yAxis[0]);
    }

    private int[] axisFromAnchor(int anchor, int cursor) {
        if (cursor <= anchor) {
            int size = Math.max(MIN_SIZE, anchor - cursor);
            return new int[] {anchor - size, anchor};
        }

        int size = Math.max(MIN_SIZE, cursor - anchor);
        return new int[] {anchor, anchor + size};
    }
}
