package mid_project;

import javax.swing.SwingUtilities;

import mid_project.ui.WorkflowEditorFrame;

public class SwingApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            WorkflowEditorFrame frame = new WorkflowEditorFrame();
            frame.setVisible(true);
        });
    }
}