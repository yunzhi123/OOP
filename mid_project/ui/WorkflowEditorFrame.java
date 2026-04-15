package mid_project.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.EnumMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import mid_project.model.EditorTool;

public class WorkflowEditorFrame extends JFrame {
    private static final Color ACTIVE_BUTTON_COLOR = new Color(39, 112, 189);
    private static final Color ACTIVE_TEXT_COLOR = Color.WHITE;
    private static final Color DEFAULT_BUTTON_COLOR = UIManager.getColor("Button.background");
    private static final Color DEFAULT_TEXT_COLOR = UIManager.getColor("Button.foreground");

    private final WorkflowCanvas canvas = new WorkflowCanvas();
    private final Map<EditorTool, JButton> toolButtons = new EnumMap<>(EditorTool.class);

    public WorkflowEditorFrame() {
        super("Workflow Design Editor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setJMenuBar(buildMenuBar());
        add(buildToolBar(), BorderLayout.WEST);
        add(buildCanvasPane(), BorderLayout.CENTER);
        setMinimumSize(new Dimension(1100, 760));
        setSize(1280, 840);
        setLocationRelativeTo(null);

        canvas.setToolChangedListener(this::updateToolButtons);
    }

    private JMenuBar buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(event -> dispose());
        fileMenu.add(exitItem);

        JMenu editMenu = new JMenu("Edit");
        JMenuItem groupItem = new JMenuItem("Group");
        groupItem.addActionListener(event -> canvas.groupSelected());

        JMenuItem ungroupItem = new JMenuItem("Ungroup");
        ungroupItem.addActionListener(event -> canvas.ungroupSelected());

        JMenuItem labelItem = new JMenuItem("Label");
        labelItem.addActionListener(event -> canvas.customizeLabel());

        editMenu.add(groupItem);
        editMenu.add(ungroupItem);
        editMenu.add(labelItem);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        return menuBar;
    }

    private JToolBar buildToolBar() {
        JToolBar toolBar = new JToolBar(SwingConstants.VERTICAL);
        toolBar.setFloatable(false);
        toolBar.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        addToolButton(toolBar, EditorTool.SELECT, event -> canvas.activatePersistentTool(EditorTool.SELECT));
        addToolButton(toolBar, EditorTool.ASSOCIATION, event -> canvas.activatePersistentTool(EditorTool.ASSOCIATION));
        addToolButton(toolBar, EditorTool.GENERALIZATION, event -> canvas.activatePersistentTool(EditorTool.GENERALIZATION));
        addToolButton(toolBar, EditorTool.COMPOSITION, event -> canvas.activatePersistentTool(EditorTool.COMPOSITION));
        addToolButton(toolBar, EditorTool.RECT, event -> canvas.activateTemporaryCreateTool(EditorTool.RECT));
        addToolButton(toolBar, EditorTool.OVAL, event -> canvas.activateTemporaryCreateTool(EditorTool.OVAL));

        return toolBar;
    }

    private JScrollPane buildCanvasPane() {
        JScrollPane scrollPane = new JScrollPane(canvas);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        return scrollPane;
    }

    private void addToolButton(JToolBar toolBar, EditorTool tool, java.awt.event.ActionListener actionListener) {
        JButton button = new JButton(tool.displayName());
        button.setFocusable(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setPreferredSize(new Dimension(128, 40));
        button.addActionListener(actionListener);
        toolButtons.put(tool, button);

        toolBar.add(button);
        toolBar.add(Box.createVerticalStrut(6));
    }

    private void updateToolButtons(EditorTool activeTool) {
        for (Map.Entry<EditorTool, JButton> entry : toolButtons.entrySet()) {
            JButton button = entry.getValue();
            boolean active = entry.getKey() == activeTool;
            button.setBackground(active ? ACTIVE_BUTTON_COLOR : DEFAULT_BUTTON_COLOR);
            button.setForeground(active ? ACTIVE_TEXT_COLOR : DEFAULT_TEXT_COLOR);
        }
    }
}
