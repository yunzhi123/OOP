package mid_project.model;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.List;

public class OvalObject extends BasicObject {
    private static final List<PortLocation> PORTS = List.of(
            PortLocation.NORTH,
            PortLocation.EAST,
            PortLocation.SOUTH,
            PortLocation.WEST
    );

    public OvalObject(Rectangle bounds) {
        super(bounds);
    }

    @Override
    protected Shape createShape(Rectangle bounds) {
        return new Ellipse2D.Double(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    @Override
    protected List<PortLocation> supportedPorts() {
        return PORTS;
    }
}
