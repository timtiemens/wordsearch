package com.tiemens.wordsearch.gui;

import com.tiemens.wordsearch.RowCol;
import com.tiemens.wordsearch.model.RowColDirectionRecord;
import com.tiemens.wordsearch.model.WordSearchModel;

import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Font;

import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;

public class WordSearchGridGui extends JPanel {
    private GuiContext guiContext;
    //private WordSearchModel wsm;

    private Map<RowCol, JTextField> mapRowCol2JTextField = new HashMap<>();
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

                RowCol rowCol = new RowCol(row, col);
                mapRowCol2JTextField.put(rowCol, cell);

                if (theOriginalBackground == null) {
                    setTheOriginalBackground( cell.getBackground() );
                }
            }
        }

        updateGui();
    }

    public void updateGui() {
        for (RowCol rowCol : mapRowCol2JTextField.keySet()) {
            JTextField jtf = mapRowCol2JTextField.get(rowCol);
            String value = guiContext.getWsm().getGrid().getAt(rowCol);
            jtf.setText(value);

            System.out.println("GUI Grid.rowCol " + rowCol + " set to " + value);
        }
    }

    private Font slightlyBigger = null;
    private boolean debug = true;
    private JTextField createJTextField() {
        JTextField ret = new JTextField();
        ret.setEditable(false);
        // todo: font size?
        if (debug) {
            System.out.println("font=" + ret.getFont());
            debug = false;
        }
        if (slightlyBigger == null) {
            int newsize;
            newsize = ret.getFont().getSize();
            System.out.println("*** Original Size = " + newsize);
            newsize = newsize + 11;
            slightlyBigger = new Font("Serif", Font.PLAIN, newsize);
                    // Font.font("Serif", Font.BOLD, 12);
        }
        ret.setFont(slightlyBigger);
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
        if (! guiContext.getLeaveHighlights()) {
            for (JTextField jtf : mapRowCol2JTextField.values()) {
                jtf.setBackground(getTheOriginalBackground());
            }
        }
    }
    public void selectWord(String word) {
        unselectAll();

        // TODO: refactor here
        RowColDirectionRecord pair = guiContext.getWsm().getMatchToLookFor().getRowColDirectionRecord(word);

        if (pair != null) {
            final RowCol maxRowCol = guiContext.getWsm().getMaxRowCol();

            RowCol rowCol = pair.rowCol();
            final WordSearchModel.Direction direction = pair.direction();
            System.out.println("GRIDGUID rowCol=" + rowCol + " direction=" + direction);
            JTextField jtf = mapRowCol2JTextField.get(rowCol);

            // 1st character:
            Color first = getSelectedColor();
            //  change the color a little:
            first = new Color(first.getRed() - 75, first.getGreen() - 75, 255);
            jtf.setBackground(first);

            // 2nd-Nth characters:
            for (int i = 1, n = word.length(); i < n; i++) {
                rowCol = rowCol.computeDirection(direction, maxRowCol);
                jtf = mapRowCol2JTextField.get(rowCol);
                jtf.setBackground(getSelectedColor());
            }

            repaint();
        }
    }
}
