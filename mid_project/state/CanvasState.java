package mid_project.state;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

public interface CanvasState {
    default void mousePressed(MouseEvent event) {
    }

    default void mouseDragged(MouseEvent event) {
    }

    default void mouseReleased(MouseEvent event) {
    }

    default void mouseMoved(MouseEvent event) {
    }

    default void drawOverlay(Graphics2D graphics) {
    }
}
