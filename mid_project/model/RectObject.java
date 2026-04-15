package mid_project.model;

import java.awt.Rectangle;
import java.awt.Shape;
import java.util.List;

public class RectObject extends BasicObject {
    private static final List<PortLocation> PORTS = List.of(
            PortLocation.NORTH_WEST,
            PortLocation.NORTH,
            PortLocation.NORTH_EAST,
            PortLocation.EAST,
            PortLocation.SOUTH_EAST,
            PortLocation.SOUTH,
            PortLocation.SOUTH_WEST,
            PortLocation.WEST
    );

    public RectObject(Rectangle bounds) {
        super(bounds);
    }

    @Override
    protected Shape createShape(Rectangle bounds) {
        return new Rectangle(bounds);
    }

    @Override
    protected List<PortLocation> supportedPorts() {
        return PORTS;
    }
}
