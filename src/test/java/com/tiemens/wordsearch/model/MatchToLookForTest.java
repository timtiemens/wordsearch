package com.tiemens.wordsearch.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MatchToLookForTest {

    @Test
    public void testFindMatches() {
        List<String> input = List.of( "ASSAY CEREBRUM CODE CREDO DING EMS ENNUI FLAT IMBUING ION",
                "LIMB MISPLACING PURGATIVE SAT SCRAM SPRING TRAGICS VIA RUMBLE ADS",
                "ATLAS CLASSIFYING COPS CUMIN DITTO EMU FIERIEST IMAM INBRED",
                "LATTICED LODE OVAL RITE SCIATICA SMOG TOMB UPLIFTING VIDEODISC ADO BEVY");

        List<String> expectedStartsWith = List.of("CLASSIFYING",
                "CEREBRUM", "CREDO", "CUMIN", "CODE", "COPS");

        List<String> expected = List.of("CEREBRUM");

        LookForWords lfw = new LookForWords( input );


        String maxValue = "CEREBRUMBLE";
        String startsWith = maxValue.substring(0, 1); // C
        List<String> startsWithList = lfw.wordsThatStartWith(startsWith);
        List<String> startsWithCandidates = WordSearchModel.MatchToLookFor.computeCandidates(maxValue,
                startsWithList);
        assertEquals( expected, startsWithCandidates );

        expected = List.of("CREDO");
        maxValue = "CREDOECG";
        startsWithCandidates = WordSearchModel.MatchToLookFor.computeCandidates(maxValue,
                startsWithList);
        assertEquals( expected, startsWithCandidates );
    }
}
