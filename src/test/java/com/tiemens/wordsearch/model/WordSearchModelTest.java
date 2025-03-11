package com.tiemens.wordsearch.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;

public class WordSearchModelTest {

    private static final int rows = 5;
    private static final int cols = 5;

    private WordSearchModel create() {
        List<String> search = List.of("ABC", "DEF");
        List<String> rowStrings = List.of();

        WordSearchModel wsm = WordSearchModel.create(search, rowStrings);

        return wsm;
    }

    @Test
    public void testIterator() {

        WordSearchModel wsm = create();

        Iterator<WordSearchModel.RowCol> iterator = wsm.getGrid().iterator();

        int count = 0;
        while (iterator.hasNext()) {
            WordSearchModel.RowCol coords = iterator.next();
            System.out.println(" next=" + coords);
            count++;
        }
        Assert.assertEquals("mismatch", rows * cols, count);

    }
}
