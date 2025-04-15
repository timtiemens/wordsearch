package com.tiemens.wordsearch.model;


import com.tiemens.wordsearch.Pair;
import com.tiemens.wordsearch.RowCol;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tiemens.wordsearch.model.WordSearchModel.Direction;

// multi-step intermediate -
//  a) Just store the 8 strings from compute
//  b) later, allow for paring-down the lists, in 'answerMap'
public class MatchToLookFor {
    // rowcol -> in this direction -> String n-letters value
    private final Map<RowCol, Map<Direction, String>> map = new HashMap<>();

    // break the above "String" into a List<String> by length
    private Map<RowCol, Map<Direction, List<String>>> answerMap;
    // reverse the direction: word/string -> RowColDirection
    private Map<String, Pair<RowCol, Direction>> answerMapReverse;

    public void addRowColDirectionValue(RowCol rowCol,
                                        Direction direction,
                                        String maxPossibleLetters) {
        if (maxPossibleLetters != null) {
            if (!map.containsKey(rowCol)) {
                map.put(rowCol, new HashMap<>());
            }
            map.get(rowCol).put(direction, maxPossibleLetters);

            if (maxPossibleLetters.trim().isEmpty()) {
                throw new IllegalArgumentException("Must be exactly 1+ letters: " + maxPossibleLetters);
            }
        }


        System.out.println("Store rowCol=" + rowCol + " direction=" + direction.toString() + " value=" + maxPossibleLetters + " map.size=" + map.size());
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

            // safety check:
            for (Direction direction : subRet.keySet()) {
                if (subRet.get(direction).size() > 1) {
                    throw new IllegalArgumentException("For direction " + direction +
                                                       " we have this many solutions=" + subRet.get(direction).size());
                }
            }
        }
        this.answerMap = ret;
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

    public RowColDirectionRecord getRowColDirectionRecord(String word) {
        // TODO:
        Pair<RowCol, Direction> pair = getPairForWord(word);
        return new RowColDirectionRecord(pair.getKey(), pair.getValue());
    }


    private static int fulldebug = 0;
    
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

}

