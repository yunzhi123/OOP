package mid_project.state;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import mid_project.model.LinkType;
import mid_project.model.PortHit;
import mid_project.ui.WorkflowCanvas;

public class CreateLinkState implements CanvasState {
    private static final Color PREVIEW_COLOR = new Color(39, 112, 189);

    private final WorkflowCanvas canvas;
    private final LinkType linkType;
    private PortHit sourcePort;
    private Point currentPoint;

    public CreateLinkState(WorkflowCanvas canvas, LinkType linkType) {
        this.canvas = canvas;
        this.linkType = linkType;
    }

    @Override
    public void mousePressed(MouseEvent event) {
        sourcePort = canvas.getModel().findTopLevelPort(event.getPoint(), WorkflowCanvas.PORT_TOLERANCE);
        currentPoint = event.getPoint();
        canvas.repaint();
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        if (sourcePort == null) {
            return;
        }
        currentPoint = event.getPoint();
        canvas.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        if (sourcePort == null) {
            return;
        }

        PortHit targetPort = canvas.getModel().findTopLevelPort(event.getPoint(), WorkflowCanvas.PORT_TOLERANCE);
        if (targetPort != null && targetPort.owner() != sourcePort.owner()) {
            canvas.addCreatedLink(linkType, sourcePort, targetPort);
        }

        sourcePort = null;
        currentPoint = null;
        canvas.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent event) {
        canvas.getModel().setHoverObject(canvas.getModel().findTopLevelObject(event.getPoint()));
        canvas.repaint();
    }

    @Override
    public void drawOverlay(Graphics2D graphics) {
        if (sourcePort == null || currentPoint == null) {
            return;
        }

        Point start = sourcePort.owner().getPortPoint(sourcePort.location());
        Point end = currentPoint;

        graphics.setColor(PREVIEW_COLOR);
        graphics.setStroke(new BasicStroke(1.8f));

        switch (linkType) {
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
