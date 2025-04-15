package com.tiemens.wordsearch.gui;

import com.tiemens.wordsearch.model.WordSearchModel;
import com.tiemens.wordsearch.modelio.WordSearchJson;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class WordSearchGuiMain {
    private static final String defaultJsonInputFilename =
            //"src/input/wordsearch-1.json";
            //"src/input/book-132.json";
	    //"src/input/book-131.json";
            "src/input/book-130.json";
    // rc/input/wordsearch-3.json";

    private TwoSidedPane twoSidedPane;

    public static void main(String[] args) {
        String usefilename = defaultJsonInputFilename;
        if ((args != null) && (args.length >= 1)) {
            if (! args[0].isEmpty()) {
                usefilename = args[0];
            }
        }
        new WordSearchGuiMain(usefilename);
    }

    public WordSearchGuiMain(String jsonInputFilename) {
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
        private WordSearchControlGui control;

        public TwoSidedPane() {

        }

        public void setJsonInput(String filename) {

            try {
                WordSearchJson wsj = new WordSearchJson();
                WordSearchModel wsm = wsj.loadFromJsonFile(filename);
                setWordSearchModel(wsm);
                this.guiContext.setFilename( filename );

                System.out.println("GRID: rows=" + wsm.getGrid().getRows());

            } catch (IOException e) {
                System.out.println("IOException reading file " + filename);
                e.printStackTrace();
            }
        }

        public void setWordSearchModel(WordSearchModel wsm) {

            control = new WordSearchControlGui();
            grid = new WordSearchGridGui();
            lookFor = new WordSearchLookForGui();

            this.guiContext = new GuiContext(wsm, grid, lookFor, control);

            grid.setGuiContext(this.guiContext);
            lookFor.setGuiContext(this.guiContext);
            control.setGuiContext(this.guiContext);

            control.createGui();
            grid.createGui();
            lookFor.createGui();
        }

        public void loadGui() {

            setLayout(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(2, 2, 2, 2);

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.NORTH;
            add(control, gbc);

            gbc.gridx = 0;
            gbc.gridy++;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.weightx = 60;
            add(wrapIt(grid), gbc);

            gbc.gridx++;
            // gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.EAST;
            gbc.weightx = 40;
            add(lookFor, gbc);

        }

        private JPanel wrapIt(JPanel wrapped) {
            JPanel parent_panel = new JPanel();
            parent_panel.setLayout(new BoxLayout(parent_panel, BoxLayout.Y_AXIS));

            wrapped.setAlignmentX(Component.CENTER_ALIGNMENT);
            parent_panel.add(wrapped);

            // parent_panel.setBorder(BorderFactory.createLineBorder(Color.RED, 5)); // Add a black line border with a thickness of 5 pixels

            return parent_panel;
        }

        private JPanel wrapIt3(JPanel child_panel) {
            JPanel parent_panel = new JPanel();
            parent_panel.setLayout(new BoxLayout(parent_panel, BoxLayout.PAGE_AXIS));
            Box horizontalBox = Box.createHorizontalBox();
            horizontalBox.add(Box.createHorizontalGlue());
            horizontalBox.add(child_panel);
            horizontalBox.add(Box.createHorizontalGlue());
            Box verticalBox = Box.createVerticalBox();
            verticalBox.add(Box.createVerticalGlue());
            verticalBox.add(horizontalBox); // one inside the other
            verticalBox.add(Box.createVerticalGlue());
            return parent_panel;
        }

        private JPanel wrapIt2(JPanel wrapped) {
            JPanel ret = new JPanel();
            ret.setLayout(new GridBagLayout());
            ret.add(wrapped, new GridBagConstraints());

            return ret;
        }
    }
}
