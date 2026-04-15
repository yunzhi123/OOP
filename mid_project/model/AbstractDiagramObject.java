package mid_project.model;

public abstract class AbstractDiagramObject implements DiagramObject {
    private int depth;

    @Override
    public int getDepth() {
        return depth;
    }

    @Override
    public void setDepth(int depth) {
        this.depth = Math.max(0, Math.min(99, depth));
    }

    @Override
    public boolean isComposite() {
        return false;
    }
}
