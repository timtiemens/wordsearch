package com.tiemens.wordsearch.gui;

import com.tiemens.wordsearch.model.WordSearchModel;
import com.tiemens.wordsearch.modelio.WordSearchJson;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class WordSearchGuiMain {
    private static final String jsonInputFilename = "src/input/wordsearch-1.json";

    private TwoSidedPane twoSidedPane;

    public static void main(String[] args) {
        new WordSearchGuiMain();
    }

    public WordSearchGuiMain() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (false) {
                    try {
                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                             UnsupportedLookAndFeelException e) {
                        throw new RuntimeException(e);
                    }
                }

                twoSidedPane = new TwoSidedPane();
                twoSidedPane.setJsonInput(jsonInputFilename);
                twoSidedPane.loadGui();

                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.add(twoSidedPane);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    protected static class TwoSidedPane extends JPanel {
        private GuiContext guiContext;
        private WordSearchModel wsm;

        private WordSearchGridGui grid;
        private WordSearchLookForGui lookFor;

        public TwoSidedPane() {

        }

        public void setJsonInput(String filename) {

            try {
                WordSearchJson wsj = new WordSearchJson();
                WordSearchModel wsm = wsj.loadFromJsonFile(filename);
                setWordSearchModel(wsm);

                System.out.println("GRID: rows=" + wsm.getGrid().getRows());

            } catch (IOException e) {
                // nothing
            }
        }

        public void setWordSearchModel(WordSearchModel wsm) {

            grid = new WordSearchGridGui();
            lookFor = new WordSearchLookForGui();

            this.guiContext = new GuiContext(wsm, grid, lookFor);

            grid.setGuiContext(this.guiContext);
            lookFor.setGuiContext(this.guiContext);

            grid.createGui();
            lookFor.createGui();
        }

        public void loadGui() {

            setLayout(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(2, 2, 2, 2);

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.WEST;
            gbc.weightx = 70;
            add(grid, gbc);

            gbc.gridx++;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.weightx = 30;
            add(lookFor, gbc);

        }
    }
}
