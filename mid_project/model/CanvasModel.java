package mid_project.model;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class CanvasModel {
    private final List<DiagramObject> rootObjects = new ArrayList<>();
    private final List<Link> links = new ArrayList<>();
    private final LinkedHashSet<DiagramObject> selection = new LinkedHashSet<>();
    private DiagramObject hoverObject;

    public void draw(Graphics2D graphics) {
        for (Link link : links) {
            link.draw(graphics);
        }

        for (int index = rootObjects.size() - 1; index >= 0; index--) {
            rootObjects.get(index).draw(graphics);
        }

        if (!selection.isEmpty()) {
            for (DiagramObject selected : getSelectedObjectsInDisplayOrder()) {
                selected.drawSelection(graphics);
            }
        } else if (hoverObject != null) {
            hoverObject.drawSelection(graphics);
        }
    }

    public void addObject(DiagramObject object) {
        rootObjects.add(0, object);
        normalizeDepths();
        selectSingle(object);
    }

    public void addLink(Link link) {
        links.add(link);
    }

    public DiagramObject findTopLevelObject(Point point) {
        for (DiagramObject object : rootObjects) {
            if (object.contains(point)) {
                return object;
            }
        }
        return null;
    }

    public PortHit findTopLevelPort(Point point, int tolerance) {
        for (DiagramObject object : rootObjects) {
            PortHit hit = object.hitTestPort(point, tolerance);
            if (hit != null) {
                return hit;
            }

            if (object.contains(point)) {
                return null;
            }
        }
        return null;
    }

    public List<DiagramObject> findObjectsInside(Rectangle area) {
        List<DiagramObject> contained = new ArrayList<>();
        for (DiagramObject object : rootObjects) {
            if (object.isFullyInside(area)) {
                contained.add(object);
            }
        }
        return contained;
    }

    public void selectSingle(DiagramObject object) {
        selection.clear();
        if (object != null) {
            selection.add(object);
        }
        hoverObject = null;
    }

    public void selectObjects(Collection<DiagramObject> objects) {
        selection.clear();
        selection.addAll(objects);
        hoverObject = null;
    }

    public void clearSelection() {
        selection.clear();
        hoverObject = null;
    }

    public Set<DiagramObject> getSelection() {
        return Set.copyOf(selection);
    }

    public DiagramObject getSingleSelectedObject() {
        return selection.size() == 1 ? selection.iterator().next() : null;
    }

    public BasicObject getSingleSelectedBasicObject() {
        DiagramObject object = getSingleSelectedObject();
        return object instanceof BasicObject basicObject ? basicObject : null;
    }

    public CompositeObject getSingleSelectedComposite() {
        DiagramObject object = getSingleSelectedObject();
        return object instanceof CompositeObject compositeObject ? compositeObject : null;
    }

    public void setHoverObject(DiagramObject hoverObject) {
        this.hoverObject = hoverObject;
    }

    public void bringToFront(DiagramObject object) {
        if (object == null || !rootObjects.remove(object)) {
            return;
        }

        rootObjects.add(0, object);
        normalizeDepths();
    }

    public void bringToFront(Collection<DiagramObject> objects) {
        List<DiagramObject> ordered = new ArrayList<>();
        for (DiagramObject object : rootObjects) {
            if (objects.contains(object)) {
                ordered.add(object);
            }
        }

        if (ordered.isEmpty()) {
            return;
        }

        rootObjects.removeAll(ordered);
        for (int index = ordered.size() - 1; index >= 0; index--) {
            rootObjects.add(0, ordered.get(index));
        }
        normalizeDepths();
    }

    public List<DiagramObject> getSelectedObjectsInDisplayOrder() {
        List<DiagramObject> ordered = new ArrayList<>();
        for (DiagramObject object : rootObjects) {
            if (selection.contains(object)) {
                ordered.add(object);
            }
        }
        return ordered;
    }

    public void moveObjects(Collection<DiagramObject> objects, int deltaX, int deltaY) {
        for (DiagramObject object : objects) {
            object.moveBy(deltaX, deltaY);
        }
    }

    public boolean canGroupSelection() {
        return selection.size() >= 2;
    }

    public boolean groupSelection() {
        List<DiagramObject> orderedSelection = getSelectedObjectsInDisplayOrder();
        if (orderedSelection.size() < 2) {
            return false;
        }

        int insertIndex = rootObjects.indexOf(orderedSelection.get(0));
        rootObjects.removeAll(orderedSelection);

        CompositeObject composite = new CompositeObject(orderedSelection);
        rootObjects.add(insertIndex, composite);
        normalizeDepths();
        selectSingle(composite);
        return true;
    }

    public boolean canUngroupSelection() {
        return selection.size() == 1 && selection.iterator().next() instanceof CompositeObject;
    }

    public boolean ungroupSelection() {
        if (!canUngroupSelection()) {
            return false;
        }

        CompositeObject composite = (CompositeObject) selection.iterator().next();
        int insertIndex = rootObjects.indexOf(composite);
        List<DiagramObject> children = new ArrayList<>(composite.getChildren());

        rootObjects.remove(composite);
        for (int index = children.size() - 1; index >= 0; index--) {
            rootObjects.add(insertIndex, children.get(index));
        }

        normalizeDepths();
        selectObjects(children);
        return true;
    }

    private void normalizeDepths() {
        for (int index = 0; index < rootObjects.size(); index++) {
            rootObjects.get(index).setDepth(index);
        }
    }
}
