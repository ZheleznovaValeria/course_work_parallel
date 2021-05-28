package server;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.io.Reader;
import java.util.concurrent.ConcurrentHashMap;


class ThreadIndex extends Thread{

    private List<String> fileNamesList = new ArrayList<String>();
    private InvertedIndex index;
    private final int startIndex;
    private final int endIndex;
    private final List<String> fileNames = new ArrayList<String>();

    public ThreadIndex(List<String> fileNamesList, InvertedIndex index, int startIndex, int endIndex) { //конструктор класу, приймає дані для обчислень
        this.fileNamesList = fileNamesList;
        this.index = index;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    public  void run(){
        for (int  i = startIndex; i <  endIndex; i++){
            try {
                File file = new File(fileNamesList.get(i));
                index.indexFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
