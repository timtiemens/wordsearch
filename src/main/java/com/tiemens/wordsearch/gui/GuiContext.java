package com.tiemens.wordsearch.gui;

import com.tiemens.wordsearch.Pair;
import com.tiemens.wordsearch.model.WordSearchModel;

import javax.swing.*;

public class GuiContext {
    private final WordSearchModel wsm;
    private final WordSearchGridGui gridGui;
    private final WordSearchLookForGui lookForGui;


    public GuiContext(WordSearchModel wsm,
                      WordSearchGridGui gridGui,
                      WordSearchLookForGui lookForGui) {
        this.wsm = wsm;
        this.gridGui = gridGui;
        this.lookForGui = lookForGui;
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
}
