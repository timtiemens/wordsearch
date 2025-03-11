package com.tiemens.wordsearch.modelio;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.tiemens.wordsearch.model.WordSearchModel;

//@JsonIgnoreProperties(ignoreUnknown = true)
public class WordSearchJson {

    private String name;
    private String notes;
    private List<String> rows;
    private List<String> search;


    //@JsonIgnore
    //private WordSearchModel.WordSearchGrid wsg = null;

    @JsonIgnore
    private static final boolean debug = true;

    public WordSearchJson() {

    }

    public WordSearchModel loadFromJsonFile(String filename) throws IOException {
        WordSearchModel ret = null;

        ret = WordSearchJson.fromFile(filename).asWordSearchModel();

        return ret;
    }

    public static WordSearchJson fromFile(String filename) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = new FileInputStream(filename);

        if (debug) {
            InputStream is2 = new FileInputStream(filename);
            Scanner s = new Scanner(is2).useDelimiter("\\A");
            String result = s.hasNext() ? s.next() : "";
            System.out.println("InputStream=" + result);
        }

        WordSearchJson ret = mapper.readValue(is, WordSearchJson.class);
        if (debug) {
            System.out.println("name=" + ret.name);
            System.out.println("Number rows=" + ret.rows.size());
            System.out.println("Number search=" + ret.search.size());
        }

        return ret;
    }

    public WordSearchModel asWordSearchModel() {
        WordSearchModel ret = null;

        List<String> rowsAsInput = getRows();
        System.out.println("RowsAsIn.size=" + rowsAsInput.size());
        ret = WordSearchModel.create(this.search, this.rows);

        //try {
        //} catch (IOException e) {
        //}

        return ret;
    }


    public String getName() {
        return name;
    }

    public String getNotes() {
        return notes;
    }

    public List<String> getRows() {
        return rows;
    }

    public List<String> getSearch() {
        return search;
    }
}

