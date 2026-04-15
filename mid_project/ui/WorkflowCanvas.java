package mid_project.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import mid_project.model.BasicObject;
import mid_project.model.CanvasModel;
import mid_project.model.EditorTool;
import mid_project.model.Link;
import mid_project.model.LinkType;
import mid_project.model.PortHit;
import mid_project.model.ShapeType;
import mid_project.state.CanvasState;
import mid_project.state.CreateLinkState;
import mid_project.state.CreateShapeState;
import mid_project.state.SelectState;

public class WorkflowCanvas extends JPanel {
    public static final int PORT_TOLERANCE = 12;
    private static final Color GRID_COLOR = new Color(238, 240, 244);

    private final CanvasModel model = new CanvasModel();
    private CanvasState currentState;
    private EditorTool persistentTool = EditorTool.SELECT;
    private EditorTool activeTool = EditorTool.SELECT;
    private Consumer<EditorTool> toolChangedListener = ignored -> {
    };

    public WorkflowCanvas() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(1600, 900));
        currentState = new SelectState(this);

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                currentState.mousePressed(event);
            }

            @Override
            public void mouseReleased(MouseEvent event) {
                currentState.mouseReleased(event);
            }

            @Override
            public void mouseDragged(MouseEvent event) {
                currentState.mouseDragged(event);
            }

            @Override
            public void mouseMoved(MouseEvent event) {
                currentState.mouseMoved(event);
            }
        };

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }

    public CanvasModel getModel() {
        return model;
    }

    public void setToolChangedListener(Consumer<EditorTool> toolChangedListener) {
        this.toolChangedListener = toolChangedListener == null ? ignored -> { } : toolChangedListener;
        this.toolChangedListener.accept(activeTool);
    }

    public void activatePersistentTool(EditorTool tool) {
        persistentTool = switch (tool) {
            case ASSOCIATION, GENERALIZATION, COMPOSITION, SELECT -> tool;
            default -> EditorTool.SELECT;
        };
        currentState = buildState(persistentTool);
        activeTool = persistentTool;
        model.setHoverObject(null);
        toolChangedListener.accept(activeTool);
        repaint();
    }

    public void activateTemporaryCreateTool(EditorTool tool) {
        ShapeType shapeType = tool == EditorTool.OVAL ? ShapeType.OVAL : ShapeType.RECT;
        currentState = new CreateShapeState(this, shapeType);
        activeTool = tool;
        model.setHoverObject(null);
        toolChangedListener.accept(activeTool);
        repaint();
    }

    public void restorePersistentTool() {
        currentState = buildState(persistentTool);
        activeTool = persistentTool;
        toolChangedListener.accept(activeTool);
        repaint();
    }

    public void addCreatedObject(BasicObject object) {
        model.addObject(object);
        repaint();
    }

    public void addCreatedLink(LinkType linkType, PortHit source, PortHit target) {
        model.addLink(new Link(source.owner(), source.location(), target.owner(), target.location(), linkType));
        repaint();
    }

    public void groupSelected() {
        if (model.groupSelection()) {
            repaint();
        }
    }

    public void ungroupSelected() {
        if (model.ungroupSelection()) {
            repaint();
        }
    }

    public void customizeLabel() {
        BasicObject selected = model.getSingleSelectedBasicObject();
        if (selected == null) {
            return;
        }

        Window owner = SwingUtilities.getWindowAncestor(this);
        LabelStyleDialog.LabelStyleResult result = LabelStyleDialog.showDialog(owner, selected.getLabel(), selected.getLabelColor());
        if (result == null) {
            return;
        }

        selected.setLabel(result.labelName());
        selected.setLabelColor(result.labelColor());
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics.create();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        drawGrid(graphics2D);
        model.draw(graphics2D);
        currentState.drawOverlay(graphics2D);
        graphics2D.dispose();
    }

    private CanvasState buildState(EditorTool tool) {
        return switch (tool) {
            case ASSOCIATION -> new CreateLinkState(this, LinkType.ASSOCIATION);
            case GENERALIZATION -> new CreateLinkState(this, LinkType.GENERALIZATION);
            case COMPOSITION -> new CreateLinkState(this, LinkType.COMPOSITION);
            default -> new SelectState(this);
        };
    }

    private void drawGrid(Graphics2D graphics) {
        graphics.setColor(GRID_COLOR);
        graphics.setStroke(new BasicStroke(1f));

        int gridSize = 24;
        for (int x = 0; x < getWidth(); x += gridSize) {
            graphics.drawLine(x, 0, x, getHeight());
        }
        for (int y = 0; y < getHeight(); y += gridSize) {
            graphics.drawLine(0, y, getWidth(), y);
        }
    }
}
