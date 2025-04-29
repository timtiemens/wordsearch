package com.tiemens.wordsearch.model;

import com.tiemens.wordsearch.RowCol;

import java.util.*;

public class Answers {
    //  map one "ANSWER" to AnswerWord-record:
    private final Map<String, AnswerWordRecord> mapWord2AnswerWord = new HashMap<>();
    private final WordSearchModel wordSearchModel;

    // redundant, duplicate data:
    private List<String> answerWordString;


    private Answers(WordSearchModel wsm) {
        this.wordSearchModel = wsm;
    }

    public static Answers createEmpty() {
        return new Answers(null);
    }

    public static Answers createFull(WordSearchModel wsm) {
        Answers ret;

        if (wsm.getAnswers() == null) {
            ret = new Answers(wsm);
            // createFull(wsm);
        } else {
            ret = wsm.getAnswers();
        }

        LookForWords lookForWords = wsm.getLookForWords();
        for (String word : lookForWords.getLookFor()) {
            ret.mapWord2AnswerWord.put(word, ret.fillIn1word(wsm, word));
        }
        return ret;
    }

    private AnswerWordRecord fillIn1word(WordSearchModel wsm, String word) {
        RowColDirectionRecord rowColRecord = wsm.getMatchToLookFor().getRowColDirectionRecord(word);

        if (mapWord2AnswerWord.containsKey(word) || (rowColRecord == null)) {
            // throw new IllegalArgumentException("rowcol=" + rowColRecord);
            return null;
        } else {
            RowCol firstRowCol = rowColRecord.rowCol();
            WordSearchModel.Direction direction = rowColRecord.direction();
            List<RowCol> listRowCol = iterateAnswer(wsm, word, firstRowCol, direction);

            AnswerWordRecord record = new AnswerWordRecord(word, firstRowCol, direction, listRowCol);

            System.out.println("ANSWER (" + word + ") ret=" + record);
            return record;

        }
    }

    // starting at firstRowCol, going in direction, return List<RowCol>
    private List<RowCol> iterateAnswer(WordSearchModel wsm, String word, RowCol firstRowCol, WordSearchModel.Direction direction) {
        List<RowCol> ret = new ArrayList<>();

        RowCol current = null;
        for (int i = 0, n = word.length(); i < n; i++) {
            final RowCol rowCol;
            if (current == null) {
                current = firstRowCol;
            } else {
                current = currentGetNext(current, direction);
            }
            ret.add(current);
        }
        return ret;
    }

    private RowCol currentGetNext(RowCol current, WordSearchModel.Direction direction) {
        return current.computeDirectionRaw(direction);
    }

    private static void fill(WordSearchModel wsm, String word) {
        /*
        RowColDirectionRecord record = wsm.getMatchToLookFor().getRowColDirectionRecord(word);

        if (record != null) {
            Map<RowColDirectionRecord, List<String>> currentMapDCR2ListString = record.;
            currentMapDCR2ListString.putIfAbsent(word, new List<>());

            final RowCol maxRowCol = wsm.getMaxRowCol();

            RowCol rowCol = record.rowCol();
            final WordSearchModel.Direction direction = record.direction();
            System.out.println("ANSWER rowCol=" + rowCol + " direction=" + direction);
            JTextField jtf = mapRowCol2JTextField.get(rowCol);


            // 2nd-Nth characters:
            for (int i = 1, n = word.length(); i < n; i++) {
                rowCol = rowCol.computeDirection(direction, maxRowCol);
                jtf = mapRowCol2JTextField.get(rowCol);
                jtf.setBackground(getSelectedColor());
            }
        }

         */
    }


    //private Answers(Map<String, List<String>> map2listrowcol) {
//        this.map2listrowcol = map2listrowcol;
  //  }

    public static record AnswerWordRecord(String word, RowCol firstRowCol, WordSearchModel.Direction direction, List<RowCol> rowColList) {
        public String getStringForRowColList() {
            StringBuilder sb = new StringBuilder();
            String sep = "";
            for (RowCol rowcol : rowColList) {
                sb.append(sep);
                sep = ",";
                sb.append("r" + rowcol.getRow() + "c" + rowcol.getCol());
            }
            return sb.toString();
        }
    }

    private String testfreemarker(String word) {
        return "" + getAnswerWordRecordForWord(word).direction();
    }
    public AnswerWordRecord getAnswerWordRecordForWord(String word) {
        return mapWord2AnswerWord.get(word);
    }

    // all
    private void computeDependents() {
        computeAnswerWordString();
    }

    // 1
    private void computeAnswerWordString() {
        List<String> ret = new ArrayList<>(mapWord2AnswerWord.keySet());
        Collections.sort(ret);

        answerWordString = ret;
    }

    public List<String> getAnswerWordString() {
        if (answerWordString == null) {
            computeDependents();
        }
        return answerWordString;
    }


}
