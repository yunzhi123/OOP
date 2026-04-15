package mid_project.state;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.List;

import mid_project.model.BasicObject;
import mid_project.model.DiagramObject;
import mid_project.model.PortHit;
import mid_project.model.PortLocation;
import mid_project.ui.WorkflowCanvas;

public class SelectState implements CanvasState {
    private static final Color MARQUEE_FILL = new Color(39, 112, 189, 48);
    private static final Color MARQUEE_BORDER = new Color(39, 112, 189);

    private final WorkflowCanvas canvas;
    private Point pressPoint;
    private Point lastDragPoint;
    private Rectangle marquee;
    private List<DiagramObject> movingObjects = List.of();
    private ResizeSession resizeSession;

    public SelectState(WorkflowCanvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void mousePressed(MouseEvent event) {
        pressPoint = event.getPoint();
        lastDragPoint = pressPoint;
        marquee = null;
        movingObjects = List.of();
        resizeSession = null;

        PortHit portHit = canvas.getModel().findTopLevelPort(pressPoint, WorkflowCanvas.PORT_TOLERANCE);
        if (portHit != null) {
            canvas.getModel().selectSingle(portHit.owner());
            canvas.getModel().bringToFront(portHit.owner());
            resizeSession = new ResizeSession(portHit.owner(), portHit.location(), portHit.owner().getBounds());
            canvas.repaint();
            return;
        }

        DiagramObject hitObject = canvas.getModel().findTopLevelObject(pressPoint);
        if (hitObject != null) {
            if (canvas.getModel().getSelection().contains(hitObject)) {
                canvas.getModel().bringToFront(canvas.getModel().getSelectedObjectsInDisplayOrder());
                movingObjects = canvas.getModel().getSelectedObjectsInDisplayOrder();
            } else {
                canvas.getModel().selectSingle(hitObject);
                canvas.getModel().bringToFront(hitObject);
                movingObjects = canvas.getModel().getSelectedObjectsInDisplayOrder();
            }
            canvas.repaint();
            return;
        }

        marquee = new Rectangle(pressPoint);
        canvas.repaint();
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        if (resizeSession != null) {
            resizeSession.object().resizeFrom(resizeSession.handle(), resizeSession.originalBounds(), event.getPoint());
            canvas.repaint();
            return;
        }

        if (!movingObjects.isEmpty()) {
            int deltaX = event.getX() - lastDragPoint.x;
            int deltaY = event.getY() - lastDragPoint.y;
            if (deltaX != 0 || deltaY != 0) {
                canvas.getModel().moveObjects(movingObjects, deltaX, deltaY);
            }
            lastDragPoint = event.getPoint();
            canvas.repaint();
            return;
        }

        if (marquee != null) {
            marquee = normalizeRectangle(pressPoint, event.getPoint());
            canvas.repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        if (marquee != null) {
            Rectangle selectionArea = normalizeRectangle(pressPoint, event.getPoint());
            if (selectionArea.width > 3 || selectionArea.height > 3) {
                List<DiagramObject> selectedObjects = canvas.getModel().findObjectsInside(selectionArea);
                if (selectedObjects.isEmpty()) {
                    canvas.getModel().clearSelection();
                } else {
                    canvas.getModel().selectObjects(selectedObjects);
                }
            }
        }

        marquee = null;
        movingObjects = List.of();
        resizeSession = null;
        canvas.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent event) {
        canvas.getModel().setHoverObject(canvas.getModel().findTopLevelObject(event.getPoint()));
        canvas.repaint();
    }

    @Override
    public void drawOverlay(Graphics2D graphics) {
        if (marquee == null) {
            return;
        }

        graphics.setColor(MARQUEE_FILL);
        graphics.fillRect(marquee.x, marquee.y, marquee.width, marquee.height);
        graphics.setColor(MARQUEE_BORDER);
        graphics.setStroke(new BasicStroke(
                1.5f,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER,
                10f,
                new float[] {6f, 4f},
                0f
        ));
        graphics.drawRect(marquee.x, marquee.y, marquee.width, marquee.height);
    }

    private Rectangle normalizeRectangle(Point start, Point end) {
        return new Rectangle(
                Math.min(start.x, end.x),
                Math.min(start.y, end.y),
                Math.abs(end.x - start.x),
                Math.abs(end.y - start.y)
        );
    }

    private record ResizeSession(BasicObject object, PortLocation handle, Rectangle originalBounds) {
    }
}
