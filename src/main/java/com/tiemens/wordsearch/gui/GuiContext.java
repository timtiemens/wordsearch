package com.tiemens.wordsearch.gui;

import com.tiemens.wordsearch.Pair;
import com.tiemens.wordsearch.model.WordSearchModel;

import javax.swing.*;

public class GuiContext {
    private final WordSearchModel wsm;
    private final WordSearchGridGui gridGui;
    private final WordSearchLookForGui lookForGui;
    private final WordSearchControlGui control;

    // our state
    // private boolean leaveHighlights = false;  // kept in control.getLeaveHighlights

    public GuiContext(WordSearchModel wsm,
                      WordSearchGridGui gridGui,
                      WordSearchLookForGui lookForGui,
                      WordSearchControlGui control) {
        this.wsm = wsm;
        this.gridGui = gridGui;
        this.lookForGui = lookForGui;
        this.control = control;
    }

    public WordSearchModel getWsm() {
        return wsm;
    }

    public void setHighlightedWord(String word,
                                   JTextField textField) {
        System.out.println("Select word=" + word);


        textField.selectAll();

        gridGui.selectWord(word);

    }

    public boolean getLeaveHighlights() {
        return control.getLeaveHighlights();
    }

    public void setLeaveHighlights(boolean newValue) {
        control.setLeaveHighlights(newValue);
    }

    public void setFilename(String filename) {
        control.setFilename(filename);
    }
}
