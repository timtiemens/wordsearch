package com.tiemens.wordsearch.gui;

import com.tiemens.wordsearch.model.LookForWords;
import com.tiemens.wordsearch.model.WordSearchModel;

import javax.swing.*;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordSearchLookForGui extends JPanel {
    private GuiContext guiContext;
    //private WordSearchModel wsm;

    private List<String> theList;
    private Map<WordSearchModel.RowCol, JTextField> mapRowCol2LookFor = new HashMap<>();

    public WordSearchLookForGui() {
    }

    public void setGuiContext(GuiContext gc) {
        this.guiContext = gc;
    }

    public void createGui() {
        LookForWords lookFor = guiContext.getWsm().getLookForWords();

        this.theList = lookFor.getLookFor();

        setLayout(new GridLayout(theList.size(), 1));

        for (int row = 0, n = theList.size(); row < n; row++) {
            JTextField cell = createJTextField();
            this.add(cell);

            WordSearchModel.RowCol rowCol = new WordSearchModel.RowCol(row, 0);
            mapRowCol2LookFor.put(rowCol, cell);
        }

        updateGui();
    }

    public void updateGui() {
        for (WordSearchModel.RowCol rowCol : mapRowCol2LookFor.keySet()) {
            JTextField jtf = mapRowCol2LookFor.get(rowCol);
            String value = theList.get(rowCol.getRow());
            jtf.setText(value);

            System.out.println("GUI LookFor.rowCol " + rowCol + " set to " + value);
        }
    }

    private JTextField createJTextField() {
        JTextField ret = new JTextField();
        ret.setEditable(false);

        //ret.addActionListener(myActionListener);
        ret.addFocusListener(myFocusListener);

        return ret;
    }

    public MyFocusListener myFocusListener = new MyFocusListener();
    public class MyFocusListener implements FocusListener {
        @Override
        public void focusLost(final FocusEvent pE) {
        }

        @Override
        public void focusGained(final FocusEvent pE) {
            JTextField textField = (JTextField) pE.getSource();
            // textField.selectAll();
            String word = textField.getText();
            guiContext.setHighlightedWord(word, textField);
        }
    }

    public static MyActionListener myActionListener = new MyActionListener();
    public static class MyActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JTextField textField = (JTextField) e.getSource();
            System.out.println("@@ callback, selectedAll()");
            textField.selectAll();

            //Highlighter highlighter = textField.getHighlighter();
        }
    }
}
