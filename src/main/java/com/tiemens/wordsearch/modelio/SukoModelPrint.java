package com.tiemens.wordsearch.modelio;

import java.util.ArrayList;
import java.util.List;


public class SukoModelPrint {

    /*
    public static void justprintSolution(SukoModel sukoModel) {
        List<String> lines = justGetLines(sukoModel);
        for (String s : lines) {
            System.out.println(s);
        }
    }

    public static List<String> justGetLines(SukoModel sukoModel) {
        List<String> ret = new ArrayList<>();
        List<Integer> cellValues = sukoModel.getCellValues();

        for (int i = 0, n = 3; i < n; i++) {
            String line = justPrintLine(cellValues, i);
            ret.add(line);
        }
        return ret;
    }

    private static String justPrintLine(List<Integer> cellValues, int row) {
        StringBuilder sb = new StringBuilder();
        String sep = "";

        for (int c = 0, n = 3; c < n; c++) {
            sb.append(sep);
            sep = "  ";
            sb.append(cellValues.get(row * 3 + c));
        }
        return sb.toString();
    }

     */
}
