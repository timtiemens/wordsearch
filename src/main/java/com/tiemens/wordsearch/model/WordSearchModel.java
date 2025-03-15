package com.tiemens.wordsearch.model;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.tiemens.wordsearch.Pair;
import com.tiemens.wordsearch.RowCol;

import java.io.PrintStream;
import java.util.*;
import java.util.List;

public class WordSearchModel {
    private final int rows;
    private final int cols;
    private final RowCol maxRowCol;
    private final LookForWords lookForWords;
    private final WordSearchGrid grid;
    private final MatchToLookFor allMatches;

    public static WordSearchModel create(List<String> lookFor, List<String> rows) {
        List<String> canonicalRows = new ArrayList<>();
        List<String> canonicalLookFor = new ArrayList<>();
        for (String s : rows) {
            canonicalRows.add(s.replace(" ", "").toUpperCase());
        }
        for (String s : lookFor) {
            canonicalLookFor.add(s.toUpperCase());
        }
        WordSearchModel ret = new WordSearchModel(canonicalRows,
                                                  canonicalLookFor);

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

    private WordSearchModel(List<String> rowStrings, List<String> searchWords) {
        this.rows = rowStrings.size();
        this.cols = rowStrings.getFirst().length();
        this.maxRowCol = new RowCol(rows, cols);

        this.lookForWords = new LookForWords(searchWords);

        this.grid = new WordSearchGrid(rows, cols, rowStrings);

        this.allMatches = processAllMatches();
        trimDownMatches(this.allMatches, lookForWords);
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


    // multi-step intermediate -
    //  a) Just store the 8 strings from compute
    //  b) later, allow for paring-down the lists, in 'answerMap'
    public static class MatchToLookFor {
        private final Map<RowCol, Map<Direction, String>> map = new HashMap<>();

        private Map<RowCol, Map<Direction, List<String>>> answerMap;
        private Map<String, Pair<RowCol, Direction>> answerMapReverse;

        public void toDebugString(PrintStream out) {
            out.println("There are " + answerMap.size() + " row col keys and " +
                    map.size() + " regular keys");

            for (RowCol rowCol : answerMap.keySet()) {
                Map<Direction, List<String>> thisRowCol = answerMap.get(rowCol);
                for (Direction direction : Direction.values()) {
                    if (thisRowCol.containsKey(direction)) {
                        List<String> answers = thisRowCol.get(direction);
                        if (! answers.isEmpty()) {
                            out.println("" + rowCol + " dir=" + direction + " is " + answers);
                        }
                    }
                }
            }
        }

        public void addRowColDirectionValue(RowCol rowCol,
                                            Direction direction,
                                            String value) {
            if (value != null) {
                if (!map.containsKey(rowCol)) {
                    map.put(rowCol, new HashMap<>());
                }
                map.get(rowCol).put(direction, value);
            }

            System.out.println("Store rowCol=" + rowCol + " direction=" + direction.toString() + " value=" + value + " map.size=" + map.size());
        }

        public void trimDownMatches(LookForWords lookForWords) {
            Map<RowCol, Map<Direction, List<String>>> ret = new HashMap<>();

            for (RowCol rowCol : map.keySet()) {
                Map<Direction, List<String>> subRet = new HashMap<>();

                Map<Direction, String> coordMap = map.get(rowCol);

                for (Direction direction : coordMap.keySet()) {
                    final String value = coordMap.get(direction);
                    final String startsWith = value.substring(0, 1);
                    final List<String> startsWithList = lookForWords.wordsThatStartWith(startsWith);

                    if (startsWithList.isEmpty()) {
                        // nothing
                    } else {
                        List<String> candidateSolutions = computeCandidates(value, startsWithList);
                        if (candidateSolutions.size() > 1) {
                            System.out.println("** WARNING on " + value + " candidates=" + candidateSolutions);
                        }
                        subRet.put(direction, candidateSolutions);
                    }
                }
                ret.put(rowCol, subRet);
            }
            this.answerMap = ret;
        }
        public String getForRowColAndDirection(RowCol rowCol, Direction direction) {
            return map.get(rowCol).get(direction);
        }

        public Pair<RowCol, Direction> getPairForWord(String word) {
            // Map<RowCol, Map<Direction, List<String>>> answerMap;
            if (answerMapReverse == null) {
                answerMapReverse = computeAnswerMapReverse();
            }
            return answerMapReverse.get(word);
        }

        public Map<String, Pair<RowCol, Direction>> computeAnswerMapReverse() {
            Map<String, Pair<RowCol, Direction>> ret = new HashMap<>();

            for (RowCol rowCol : answerMap.keySet()) {
                for (Direction direction : answerMap.get(rowCol).keySet()) {
                    List<String> vals = answerMap.get(rowCol).get(direction);
                    if (vals.size() == 1) {
                        final String key = vals.get(0);
                        ret.put(key, new Pair<>(rowCol, direction));
                    } else {
                        if (vals.size() == 0) {
                            // ok
                            // System.out.println("  NOTE vals.size=0 for rowcol=" + rowCol);
                        } else {
                            throw new IllegalArgumentException("wrong number of answers: " + vals.size());
                        }
                    }
                }
            }

            return ret;
        }
        /**
         * Constraint: IF(S) in RETURN(S) THEN (S) in LookForStartsWith
         *             IF(S) in RETURN(S) THEN (S) in substring(longValue)
         *
         * @param longValue the "pool" to look in, starting at (0,1) and going to (0,n)
         * @param lookForStartsWith
         * @return a subset of lookForStartsWith
         */
        public static List<String> computeCandidates(String longValue, List<String> lookForStartsWith) {
            List<String> ret = new ArrayList<>();

            //if (longValue.startsWith("DING")) {
            //    System.out.println(" #$#$#  found DING");
            //}

            for (int len = longValue.length() ; len >= 2; len--) {
                 final String thisSearch = longValue.substring(0, len);
                 if  (lookForStartsWith.contains(thisSearch)) {
                     //System.out.println(" *** Found " + thisSearch);
                     ret.add(thisSearch);
                 } else {
                     if (thisSearch.equals("DING")) {
                         for (String s : lookForStartsWith) {
                             if (s.equals(thisSearch)) {
                                 System.out.println("  ### WTF thisSearch found in lookForStartsWith");
                                 ret.add(thisSearch);
                             }
                         }
                     }
                 }
            }
            System.out.println("Looking for " + longValue + " found " + ret + " in list.size=" + lookForStartsWith.size());
//            if ((fulldebug++ < 5) || (longValue.equals("DING"))) {
//                System.out.println("  lookForStartsWith=" + lookForStartsWith);
//            }
            return ret;
        }
        private static int fulldebug = 0;


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
