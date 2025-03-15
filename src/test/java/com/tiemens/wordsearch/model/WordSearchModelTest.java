package com.tiemens.wordsearch.model;

import com.tiemens.wordsearch.RowCol;
import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;

public class WordSearchModelTest {

    private static final int rows = 5;
    private static final int cols = 5;

    private WordSearchModel create() {
        List<String> lookFor = List.of("ABC", "DEF");
        List<String> rowStrings = List.of("A B C D E", "F G H I J", "K L M N O", "P Q R S T", "U V W X Y");

        WordSearchModel wsm = WordSearchModel.create(lookFor, rowStrings);

        return wsm;
    }

    @Test
    public void testIterator() {

        WordSearchModel wsm = create();

        Iterator<RowCol> iterator = wsm.getGrid().iterator();

        int count = 0;
        while (iterator.hasNext()) {
            RowCol rowCol = iterator.next();
            System.out.println(" next=" + rowCol);
            count++;
        }
        Assert.assertEquals("mismatch", rows * cols, count);

    }
}
