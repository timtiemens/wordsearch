package com.tiemens.wordsearch.model;

import java.util.*;

public class LookForWords {

    private final List<String> lookFor;
    private final Map<String, List<String>> mapFirstChartoWord;

    private static final List<String> EMPTY_LIST = Collections.unmodifiableList(new ArrayList<String>());
    /**
     *
     * @param in list of "Big String" i.e. separated by spaced
     */
    public LookForWords(List<String> in) {
        this.lookFor = splitSpaces(in);
        this.mapFirstChartoWord = calculateFirstLetterMap();
    }

    public List<String> getLookFor() {
        return lookFor;
    }

    public List<String> wordsThatStartWith(final String firstLetter) {
        return mapFirstChartoWord.getOrDefault(firstLetter, EMPTY_LIST);
    }

    private List<String> splitSpaces(List<String> in) {
        List<String> ret = new ArrayList<>();
        for (String bigString : in) {
            String[] split = bigString.split("\\s+");
            for (String s : split) {
                if (ret.contains(s)) {
                    throw new IllegalArgumentException("List already contains '" + s + "'");
                }
                ret.add(s);
            }
        }
        return ret;
    }

    private Map<String, List<String>> calculateFirstLetterMap() {
        // List<String> is ordered longest to shortest word...
        Map<String, List<String>> ret = new HashMap<>();

        for (String word : lookFor) {
            final String firstLetter = word.substring(0, 1);
            if (! ret.containsKey(firstLetter)) {
                ret.put(firstLetter, new ArrayList<>());
            }
            ret.get(firstLetter).add(word);
        }
        Comparator<String> comp = Comparator
                .comparingInt(String::length)
                .reversed()
                .thenComparing(String::compareTo);
        for (String key : ret.keySet()) {
            ret.get(key).sort(comp);
        }

        return ret;
    }


}
