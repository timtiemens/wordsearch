package com.tiemens.wordsearch.model;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.tiemens.wordsearch.Pair;
import com.tiemens.wordsearch.RowCol;

import java.io.PrintStream;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class WordSearchModel {
    private final int rows;
    private final int cols;
    private final RowCol maxRowCol;
    private final LookForWords lookForWords;
    private final WordSearchGrid grid;
    private final MatchToLookFor allMatches;
    private final Answers answers;

    public static WordSearchModel create(List<String> lookFor, List<String> rows) {
        List<String> canonicalRows = new ArrayList<>();
        List<String> canonicalLookFor = new ArrayList<>();
        Map<String, List<String>> canonicalAnswers = new HashMap<>();
        for (String s : rows) {
            canonicalRows.add(s.replace(" ", "").toUpperCase());
        }
        for (String s : lookFor) {
            canonicalLookFor.add(s.toUpperCase());
        }
        // TODO? canonicalAnswers
        WordSearchModel ret = new WordSearchModel(canonicalRows,
                                                  canonicalLookFor,
                                                  canonicalAnswers);

        return ret;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public RowCol getMaxRowCol() {
        return new RowCol(getRows(), getCols());
    }

    public WordSearchGrid getGrid() {
        return grid;
    }

    public LookForWords getLookForWords() {
        return lookForWords;
    }

    @Override
    public String toString() {
        return "WordSearchModel{" +
                "rows=" + rows +
                ", cols=" + cols +
                ", lookForWords=" + lookForWords +
                ", grid=" + grid +
                '}';
    }

    public MatchToLookFor computeStringsForRowColAllDirections(RowCol rowCol,
                                                               MatchToLookFor matchToLookFor) {


        for (Direction direction : Direction.values()) {
            String value = computeStringForRowColAndDirection(rowCol, direction);
            matchToLookFor.addRowColDirectionValue(rowCol, direction, value);
        }

        return matchToLookFor;
    }

    public String computeStringForRowColAndDirection(RowCol rowCol, Direction direction) {
        StringBuilder sb = new StringBuilder();
        sb.append( grid.getAt(rowCol) );
        RowCol current = rowCol;
        while ( (current = current.computeDirection(direction, maxRowCol)) != null) {
            // System.out.println("  get at rowCol=" + current);
            String add = grid.getAt(current);
            sb.append( add );
        }
        //System.out.println(" rowCol=" + rowCol + " direction=" + direction + " ret=" + sb.toString());
        return sb.toString();
    }

    private WordSearchModel(List<String> rowStrings, List<String> searchWords, Map<String,
                            List<String>> canonicalAnswers) {
        this.rows = rowStrings.size();
        this.cols = rowStrings.getFirst().length();
        this.maxRowCol = new RowCol(rows, cols);

        this.lookForWords = new LookForWords(searchWords);

        this.grid = new WordSearchGrid(rows, cols, rowStrings);

        this.allMatches = processAllMatches();
        trimDownMatches(this.allMatches, lookForWords);

        this.answers = Answers.createFull(this);
    }

    /**
     * Filter out the "impossible" (e.g. length 1)
     * filter out the "not possible" (e.g. no lookForWord starts with our starting letter)
     *
     * @param allMatches
     * @param lookForWords
     */
    private void trimDownMatches(MatchToLookFor allMatches,
                                 LookForWords lookForWords) {
        allMatches.trimDownMatches(lookForWords);
    }

    private MatchToLookFor processAllMatches() {
        MatchToLookFor ret = new MatchToLookFor();

        Iterator<RowCol> iterator = this.getGrid().iterator();
        while (iterator.hasNext()) {
            RowCol rowCol = iterator.next();
            MatchToLookFor match = this.computeStringsForRowColAllDirections(rowCol, ret);
        }

        return ret;
    }

    public void toDebugString(PrintStream out) {
        allMatches.toDebugString(out);
    }

    public MatchToLookFor getMatchToLookFor() {
        return allMatches;
    }

    //
    // Spring/Freemarker methods
    //
    public List<Integer> getRowsNumberList() {
        int n = getRows();
        return getNumbersList(n);
    }
    public List<Integer> getColsNumberList() {
        int n = getCols();
        return getNumbersList(n);
    }
    private List<Integer> getNumbersList(int n) {
        List<Integer> ret = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            ret.add( Integer.valueOf(i) );
        }
        return ret;
        /*
        return IntStream.rangeClosed(0, n)
                .boxed()
                .collect(java.util.stream.Collectors.toList());

         */
    }

    public void setAnswers(Map<String, List<String>> answers) {
        // TODO TBD to do
    }

    public Answers getAnswers() {
        return answers;
    }


    public static class RowColIterator implements Iterator<RowCol> {
        private RowCol current = null;
        private final int rows;
        private final int cols;
        private final RowCol maxCoords;

        public RowColIterator(int rows, int cols) {
            this.rows = rows;
            this.cols = cols;
            this.maxCoords = new RowCol(rows, cols);
        }

        @Override
        public boolean hasNext() {
            if (current == null) {
                return true;
            }
            if ((current.getCol() + 1) != cols) {
                return true;
            }
            if ((current.getRow() + 1)!= rows) {
                return true;
            }
            return false;
        }

        @Override
        public RowCol next() {
            if (current == null) {
                current = new RowCol(0, 0);
            } else {
                current = current.getNext(maxCoords);
            }
            return current;
        }


    }
    public static class WordSearchGrid extends com.tiemens.wordsearch.Matrix<String> {
        public WordSearchGrid(int rows, int cols, List<String> rowStrings) {
            super(rows, cols);
            processRows(rowStrings, rows, cols);
        }
        private void processRows(List<String> rowString, int maxRow, int maxCol) {
            RowCol maxCoords = new RowCol(maxRow, maxCol);
            RowCol rowCol = new RowCol(0, 0);
            for (String row : rowString) {
                for (char colChar : row.toCharArray()) {
                    String colString = "" + colChar;
                    this.setAt(rowCol, colString);
                    rowCol = rowCol.getNext(maxCoords);
                }
            }
        }
        public void setAt(RowCol rowCol, String value) {
            // System.out.println("Setting " + rowCol.toString() + " to " + value);
            this.set(rowCol.getRow(), rowCol.getCol(), value);
        }
        public String getAt(RowCol rowCol) {
            return this.get(rowCol.getRow(), rowCol.getCol());
        }
        public String getAt(int row, int col) { return this.get(row, col); }
        public Iterator<RowCol> iterator() {
            return new RowColIterator(this.getRows(), this.getCols());
        }

        @Override
        public String toString() {
            return super.toString();
        }

    }

    public static enum Direction {
        UP(-1, 0,  "UP"),
        UR(-1, 1,  "UR"),
        R (0,  1,  "R"),
        DR(1,  1,  "DR"),
        DN(1,  0,  "DN"),
        LL(1,  -1,  "LL"),
        L (0,  -1,  "L"),
        UL(-1, -1,  "UL");

        private final int deltaRow;
        private final int deltaCol;
        private final String name;
        private Direction(int deltaRow, int deltaCol, String name) {
            this.deltaRow = deltaRow;
            this.deltaCol = deltaCol;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        public int getDeltaRow() {
            return deltaRow;
        }

        public int getDeltaCol() {
            return deltaCol;
        }

        public String getName() {
            return name;
        }
    }
}
