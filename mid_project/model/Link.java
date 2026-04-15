package mid_project.model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Link {
    private static final Color LINK_COLOR = new Color(32, 32, 32);

    private final BasicObject source;
    private final PortLocation sourcePort;
    private final BasicObject target;
    private final PortLocation targetPort;
    private final LinkType type;

    public Link(BasicObject source, PortLocation sourcePort, BasicObject target, PortLocation targetPort, LinkType type) {
        this.source = source;
        this.sourcePort = sourcePort;
        this.target = target;
        this.targetPort = targetPort;
        this.type = type;
    }

    public void draw(Graphics2D graphics) {
        Point start = source.getPortPoint(sourcePort);
        Point end = target.getPortPoint(targetPort);

        if (start.distance(end) < 1.0) {
            return;
        }

        graphics.setColor(LINK_COLOR);
        graphics.setStroke(new BasicStroke(2f));

        switch (type) {
            case ASSOCIATION -> drawAssociation(graphics, start, end);
            case GENERALIZATION -> drawGeneralization(graphics, start, end);
            case COMPOSITION -> drawComposition(graphics, start, end);
        }
    }

    private void drawAssociation(Graphics2D graphics, Point start, Point end) {
        graphics.draw(new Line2D.Double(start, end));

        Point2D.Double base = pointBackFromEnd(start, end, 14);
        double[] unit = unitVector(start, end);
        double perpX = -unit[1] * 6;
        double perpY = unit[0] * 6;

        graphics.draw(new Line2D.Double(end.x, end.y, base.x + perpX, base.y + perpY));
        graphics.draw(new Line2D.Double(end.x, end.y, base.x - perpX, base.y - perpY));
    }

    private void drawGeneralization(Graphics2D graphics, Point start, Point end) {
        Point2D.Double base = pointBackFromEnd(start, end, 18);
        double[] unit = unitVector(start, end);
        double perpX = -unit[1] * 8;
        double perpY = unit[0] * 8;

        graphics.draw(new Line2D.Double(start.x, start.y, base.x, base.y));

        Polygon triangle = new Polygon();
        triangle.addPoint(end.x, end.y);
        triangle.addPoint((int) Math.round(base.x + perpX), (int) Math.round(base.y + perpY));
        triangle.addPoint((int) Math.round(base.x - perpX), (int) Math.round(base.y - perpY));

        Color originalColor = graphics.getColor();
        graphics.setColor(Color.WHITE);
        graphics.fillPolygon(triangle);
        graphics.setColor(originalColor);
        graphics.drawPolygon(triangle);
    }

    private void drawComposition(Graphics2D graphics, Point start, Point end) {
        Point2D.Double mid = pointBackFromEnd(start, end, 10);
        Point2D.Double back = pointBackFromEnd(start, end, 20);
        double[] unit = unitVector(start, end);
        double perpX = -unit[1] * 7;
        double perpY = unit[0] * 7;

        graphics.draw(new Line2D.Double(start.x, start.y, back.x, back.y));

        Polygon diamond = new Polygon();
        diamond.addPoint(end.x, end.y);
        diamond.addPoint((int) Math.round(mid.x + perpX), (int) Math.round(mid.y + perpY));
        diamond.addPoint((int) Math.round(back.x), (int) Math.round(back.y));
        diamond.addPoint((int) Math.round(mid.x - perpX), (int) Math.round(mid.y - perpY));

        graphics.fillPolygon(diamond);
        graphics.drawPolygon(diamond);
    }

    private Point2D.Double pointBackFromEnd(Point start, Point end, double distance) {
        double[] unit = unitVector(start, end);
        return new Point2D.Double(end.x - unit[0] * distance, end.y - unit[1] * distance);
    }

    private double[] unitVector(Point start, Point end) {
        double dx = end.x - start.x;
        double dy = end.y - start.y;
        double length = Math.max(1d, Math.hypot(dx, dy));
        return new double[] {dx / length, dy / length};
    }
}
