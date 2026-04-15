package mid_project.model;

public enum EditorTool {
    SELECT("Select"),
    ASSOCIATION("Association"),
    GENERALIZATION("Generalization"),
    COMPOSITION("Composition"),
    RECT("Rect"),
    OVAL("Oval");

    private final String displayName;

    EditorTool(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }

    public boolean isTemporaryCreationTool() {
        return this == RECT || this == OVAL;
    }
}
