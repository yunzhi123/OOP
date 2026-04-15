package mid_project.state;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import mid_project.model.BasicObject;
import mid_project.model.OvalObject;
import mid_project.model.RectObject;
import mid_project.model.ShapeType;
import mid_project.ui.WorkflowCanvas;

public class CreateShapeState implements CanvasState {
    private static final Color PREVIEW_COLOR = new Color(39, 112, 189);

    private final WorkflowCanvas canvas;
    private final ShapeType shapeType;
    private Point startPoint;
    private Point currentPoint;

    public CreateShapeState(WorkflowCanvas canvas, ShapeType shapeType) {
        this.canvas = canvas;
        this.shapeType = shapeType;
    }

    @Override
    public void mousePressed(MouseEvent event) {
        startPoint = event.getPoint();
        currentPoint = event.getPoint();
        canvas.getModel().setHoverObject(null);
        canvas.repaint();
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        if (startPoint == null) {
            return;
        }
        currentPoint = event.getPoint();
        canvas.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        if (startPoint == null) {
            return;
        }

        currentPoint = event.getPoint();
        Rectangle bounds = buildBounds(startPoint, currentPoint);
        BasicObject object = shapeType == ShapeType.RECT ? new RectObject(bounds) : new OvalObject(bounds);
        canvas.addCreatedObject(object);

        startPoint = null;
        currentPoint = null;
        canvas.restorePersistentTool();
    }

    @Override
    public void drawOverlay(Graphics2D graphics) {
        if (startPoint == null || currentPoint == null) {
            return;
        }

        Rectangle bounds = buildBounds(startPoint, currentPoint);
        graphics.setColor(PREVIEW_COLOR);
        graphics.setStroke(new BasicStroke(
                1.5f,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER,
                10f,
                new float[] {7f, 4f},
                0f
        ));

        if (shapeType == ShapeType.RECT) {
            graphics.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
        } else {
            graphics.drawOval(bounds.x, bounds.y, bounds.width, bounds.height);
        }
    }

    private Rectangle buildBounds(Point start, Point end) {
        int endX = end.x;
        int endY = end.y;

        if (Math.abs(endX - start.x) < BasicObject.MIN_SIZE) {
            endX = start.x + (endX >= start.x ? BasicObject.MIN_SIZE : -BasicObject.MIN_SIZE);
        }
        if (Math.abs(endY - start.y) < BasicObject.MIN_SIZE) {
            endY = start.y + (endY >= start.y ? BasicObject.MIN_SIZE : -BasicObject.MIN_SIZE);
        }

        return new Rectangle(
                Math.min(start.x, endX),
                Math.min(start.y, endY),
                Math.abs(endX - start.x),
                Math.abs(endY - start.y)
        );
    }
}
