package com.tiemens.wordsearch.springboot;

import org.springframework.stereotype.Service;

import com.tiemens.wordsearch.model.WordSearchModel;
import com.tiemens.wordsearch.modelio.WordSearchJson;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class SpringService {

    public List<String> getFilenamesFromDirectory(String dirName) {
        File directory = new File("src/input");

        FilenameFilter jsonFileFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".json");
            }
        };

        List<String> thelist =  getFilenames(directory, jsonFileFilter);
        Collections.sort(thelist);

        return thelist;
    }

    private List<String> getFilenames(File directory, FilenameFilter fileFilter) {
        File[] theFiles = directory.listFiles(fileFilter);
        List<String> theList = new ArrayList<>();

        for (File file : theFiles) {
            theList.add( file.getName() );
        }

        return theList;
    }


    public WordSearchModel getFromFile(String shortName) {
        String dirName = "src/input";
        String filename = dirName + "/" + shortName;
        try {
            return WordSearchJson.fromFile(filename).asWordSearchModel();
        } catch (IOException e) {
            System.out.println("Threw IO exception on " + filename + " : " + e);
            return null;
        }
    }
}
