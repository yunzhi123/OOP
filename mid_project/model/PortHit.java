package mid_project.model;

public class PortHit {
    private final BasicObject owner;
    private final PortLocation location;

    public PortHit(BasicObject owner, PortLocation location) {
        this.owner = owner;
        this.location = location;
    }

    public BasicObject owner() {
        return owner;
    }

    public PortLocation location() {
        return location;
    }
}
