package com.tiemens.wordsearch.gui;

import javax.swing.*;
import java.awt.*;

public class WordSearchControlGui extends JPanel {
    private GuiContext guiContext;
    // in dialog: private JFileChooser jfileChooser;
    private JLabel fileNameLabel;
    private JTextField fileNameTextField;
    private JCheckBox leaveHighlights;


    public void setGuiContext(GuiContext guiContext) {
        this.guiContext = guiContext;
    }

    public void createGui() {
        // jfileChooser = new JFileChooser();
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout());
        fileNameLabel = new JLabel("Filename:");
        fileNameTextField = new JTextField();
        fileNameTextField.setEditable(false);
        // TODO: add callback to bring up jFileChooser dialog
        leaveHighlights = new JCheckBox("Leave Highlights", false); // guiContext.getLeaveHighlights());
        jPanel.add(fileNameLabel);
        jPanel.add(fileNameTextField);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(jPanel);
        add(leaveHighlights);
    }

    public boolean getLeaveHighlights() {
        return leaveHighlights.isSelected();
    }

    public void setLeaveHighlights(boolean newValue) {
        leaveHighlights.setSelected(newValue);
    }

    public void setFilename(String filename) {
        fileNameTextField.setText(filename);
    }
}
