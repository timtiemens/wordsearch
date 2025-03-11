package com.tiemens.wordsearch.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class LookForWordsTest {

    @Test
    public void testIterator() {
        List<String> input = List.of( "ASSAY CEREBRUM CODE CREDO DING EMS ENNUI FLAT IMBUING ION",
                "LIMB MISPLACING PURGATIVE SAT SCRAM SPRING TRAGICS VIA RUMBLE ADS",
                "ATLAS CLASSIFYING COPS CUMIN DITTO EMU FIERIEST IMAM INBRED",
                "LATTICED LODE OVAL RITE SCIATICA SMOG TOMB UPLIFTING VIDEODISC ADO BEVY");

        List<String> expected = List.of("CLASSIFYING",
                "CEREBRUM", "CREDO", "CUMIN", "CODE", "COPS");

        LookForWords lfw = new LookForWords( input );

        String startsWith = "C";
        List<String> ret = lfw.wordsThatStartWith(startsWith);
        System.out.println("Starts with=" + startsWith + " ret=" + ret);
        Assert.assertEquals("mismatch", ret, expected);

        startsWith = "G";
        ret = lfw.wordsThatStartWith(startsWith);
        System.out.println("Starts with=" + startsWith + " ret=" + ret);
        Assert.assertEquals("mismatch2", ret, new ArrayList<>());
    }
}
