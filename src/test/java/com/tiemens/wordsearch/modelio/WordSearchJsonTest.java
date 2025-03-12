package com.tiemens.wordsearch.modelio;

import com.tiemens.wordsearch.model.WordSearchModel;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WordSearchJsonTest {

    @Test
    public void testBookJson() throws IOException {
        String filename = "src/input/book-132.json";

        WordSearchJson wsj = new WordSearchJson();
        WordSearchModel wsm = wsj.loadFromJsonFile(filename);

        System.out.println("------------ start");
        wsm.toDebugString(System.out);
        System.out.println("------------ end");

    }

    @Test
    public void testJsonLoad() throws IOException {
        String filename = "src/input/wordsearch-1.json";

        WordSearchJson wsj = new WordSearchJson();
        WordSearchModel wsm = wsj.loadFromJsonFile(filename);

        //System.out.println("Starts with=" + startsWith + " ret=" + ret);

        // can't do this 'arbitrarily':
        assertEquals("mismatch rows", wsm.getRows(), 14);
        assertEquals("mismatch cols", wsm.getCols(), 11);

        System.out.println("------------ start");
        wsm.toDebugString(System.out);
        System.out.println("------------ end");


        subtest(wsm, 4, 4, WordSearchModel.Direction.R, "PLACING");
        subtest(wsm, 7, 9, WordSearchModel.Direction.UP, "VALNTOMB");
        subtest(wsm, 0, 0, WordSearchModel.Direction.DR, "GNIRPSISTNO");
        subtest(wsm, 10, 10, WordSearchModel.Direction.UL, new StringBuilder("GNIRPSISTNO").reverse().toString());

        subtestintermediate(wsm, 4, 4,
                List.of("PDRBC", "PGTLS", "PLACING", "PSISTNO",
                        "PGFARNBBDL", "PYRCT", "PSIMD", "PRING"));
        subtestintermediate(wsm, 0, 3,
                List.of("O", "O", "OCSEESBI", "OBORCDAR",
                        "OUDRSYITSIBEIP", "OINU", "OPSG", "O"));

        // System.out.println("WSM=" + wsm.toString());
    }

    private void subtest(WordSearchModel wsm, int row, int col, WordSearchModel.Direction direction, String expecting) {
        WordSearchModel.RowCol coords = new WordSearchModel.RowCol(row, col);
        String actual = wsm.computeStringForRowColAndDirection(coords, direction);

        //System.out.println("  getat coords=" + coords + " actual=" + actual);
        assertEquals("subtest", actual, expecting);
    }

    private void subtestintermediate(WordSearchModel wsm, int row, int col,  List<String> expected) {
        WordSearchModel.RowCol rowCol = new WordSearchModel.RowCol(row, col);
        WordSearchModel.MatchToLookFor ret = new WordSearchModel.MatchToLookFor();
        WordSearchModel.MatchToLookFor actual = wsm.computeStringsForRowColAllDirections(rowCol, ret);

        for (WordSearchModel.Direction direction : WordSearchModel.Direction.values()) {
            String actualValue = actual.getForRowColAndDirection(rowCol, direction);
            assertTrue(expected.contains(actualValue));
            System.out.println("  found dir=" + direction.toString() + " value=" + actualValue);
        }

    }
}
