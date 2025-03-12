package com.tiemens.wordsearch.gui;

import com.tiemens.wordsearch.Pair;
import com.tiemens.wordsearch.model.WordSearchModel;

import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class WordSearchGridGui extends JPanel {
    private GuiContext guiContext;
    //private WordSearchModel wsm;

    private Map<WordSearchModel.RowCol, JTextField> mapRowCol2JTextField = new HashMap<>();
    private Color theOriginalBackground = null;

    public WordSearchGridGui() {

    }

    public void setGuiContext(GuiContext guiContext) {
        this.guiContext = guiContext;
    }

    public void createGui() {
        WordSearchModel.WordSearchGrid grid = guiContext.getWsm().getGrid();

        setLayout(new GridLayout(grid.getRows(), grid.getCols()));

        for (int row = 0; row < grid.getRows(); row++) {
            for (int col = 0; col< grid.getCols(); col++) {
                JTextField cell = createJTextField();
                this.add(cell);

                WordSearchModel.RowCol rowCol = new WordSearchModel.RowCol(row, col);
                mapRowCol2JTextField.put(rowCol, cell);

                if (theOriginalBackground == null) {
                    setTheOriginalBackground( cell.getBackground() );
                }
            }
        }

        updateGui();
    }

    public void updateGui() {
        for (WordSearchModel.RowCol rowCol : mapRowCol2JTextField.keySet()) {
            JTextField jtf = mapRowCol2JTextField.get(rowCol);
            String value = guiContext.getWsm().getGrid().getAt(rowCol);
            jtf.setText(value);

            System.out.println("GUI Grid.rowCol " + rowCol + " set to " + value);
        }
    }

    private JTextField createJTextField() {
        JTextField ret = new JTextField();
        ret.setEditable(false);

        return ret;
    }

    private void setTheOriginalBackground(Color color) {
        theOriginalBackground = color;
    }
    private Color getTheOriginalBackground() {
        return theOriginalBackground;
    }
    private static final Color SELECTED_COLOR = new  Color(100, 100, 240);
    private Color getSelectedColor() {
        return SELECTED_COLOR;
    }

    public void unselectAll() {
        for (JTextField jtf : mapRowCol2JTextField.values()) {
            jtf.setBackground( getTheOriginalBackground() );
        }
    }
    public void selectWord(String word) {
        unselectAll();

        Pair<WordSearchModel.RowCol, WordSearchModel.Direction> pair = guiContext.getWsm().getMatchToLookFor().getPairForWord(word);

        if (pair != null) {
            final WordSearchModel.RowCol maxRowCol = guiContext.getWsm().getMaxRowCol();

            WordSearchModel.RowCol rowCol = pair.getKey();
            final WordSearchModel.Direction direction = pair.getValue();
            System.out.println("GRIDGUID rowCol=" + rowCol + " direction=" + direction);
            JTextField jtf = mapRowCol2JTextField.get(rowCol);

            // 1st character:
            Color first = getSelectedColor();
            //  change the color a little:
            first = new Color(first.getRed() - 75, first.getGreen() - 75, 255);
            jtf.setBackground(first);
            for (int i = 1, n = word.length(); i < n; i++) {
                rowCol = rowCol.computeDirection(direction, maxRowCol);
                // 2-N character:
                jtf = mapRowCol2JTextField.get(rowCol);
                jtf.setBackground(getSelectedColor());
            }

            repaint();
        }
    }
}
