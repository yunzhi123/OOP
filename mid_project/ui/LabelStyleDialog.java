package mid_project.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public final class LabelStyleDialog {
    private LabelStyleDialog() {
    }

    public static LabelStyleResult showDialog(Window owner, String initialLabel, Color initialColor) {
        JDialog dialog = new JDialog(owner, "Customize Label Style", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setLayout(new BorderLayout(12, 12));

        JTextField labelField = new JTextField(initialLabel == null ? "" : initialLabel, 18);
        Color defaultColor = initialColor == null ? Color.BLACK : initialColor;
        Color[] selectedColor = {defaultColor};

        JPanel swatch = new JPanel();
        swatch.setPreferredSize(new Dimension(36, 24));
        swatch.setBackground(defaultColor);

        JButton chooseColorButton = new JButton("Choose...");
        chooseColorButton.addActionListener(event -> {
            Color chosen = JColorChooser.showDialog(dialog, "Label Color", selectedColor[0]);
            if (chosen != null) {
                selectedColor[0] = chosen;
                swatch.setBackground(chosen);
            }
        });

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(6, 6, 6, 6);
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.HORIZONTAL;

        constraints.gridx = 0;
        constraints.gridy = 0;
        formPanel.add(new JLabel("Label Name"), constraints);

        constraints.gridx = 1;
        constraints.weightx = 1;
        formPanel.add(labelField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 0;
        formPanel.add(new JLabel("Label Color"), constraints);

        JPanel colorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        colorPanel.add(swatch);
        colorPanel.add(chooseColorButton);

        constraints.gridx = 1;
        constraints.fill = GridBagConstraints.NONE;
        formPanel.add(colorPanel, constraints);

        LabelStyleResult[] result = new LabelStyleResult[1];
        JButton okButton = new JButton("OK");
        okButton.addActionListener(event -> {
            result[0] = new LabelStyleResult(labelField.getText().trim(), selectedColor[0]);
            dialog.dispose();
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(event -> dialog.dispose());

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.add(okButton);
        actionPanel.add(cancelButton);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(actionPanel, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setLocationRelativeTo((Component) owner);
        dialog.setResizable(false);
        dialog.setVisible(true);
        return result[0];
    }

    public record LabelStyleResult(String labelName, Color labelColor) {
    }
}
