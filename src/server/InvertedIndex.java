package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class InvertedIndex {

    List<String> stopWords = Arrays.asList("a", "able", "about",
            "across", "after", "all", "almost", "also", "am", "among", "an",
            "and", "any", "are", "as", "at", "be", "because", "been", "but",
            "by", "can", "cannot", "could", "dear", "did", "do", "does",
            "either", "else", "ever", "every", "for", "from", "get", "got",
            "had", "has", "have", "he", "her", "hers", "him", "his", "how",
            "however", "i", "if", "in", "into", "is", "it", "its", "just",
            "least", "let", "like", "likely", "may", "me", "might", "most",
            "must", "my", "neither", "no", "nor", "not", "of", "off", "often",
            "on", "only", "or", "other", "our", "own", "rather", "said", "say",
            "says", "she", "should", "since", "so", "some", "than", "that",
            "the", "their", "them", "then", "there", "these", "they", "this",
            "tis", "to", "too", "twas", "us", "wants", "was", "we", "were",
            "what", "when", "where", "which", "while", "who", "whom", "why",
            "will", "with", "would", "yet", "you", "your");

    ConcurrentHashMap<String, List<Tuple>> index = new ConcurrentHashMap<String, List<Tuple>>();
    List<String> files = new ArrayList<String>();


    public void indexFile(File file) throws IOException {
        String filePath = file.getPath();
        int fileNo = files.indexOf(file.getPath());
        if (fileNo == -1) {
            files.add(file.getPath());
            fileNo = files.size() - 1;
        }
        int pos = 0;
        BufferedReader reader = new BufferedReader(new FileReader(file));
        for (String line = reader.readLine(); line != null; line = reader
                .readLine()) {
            for (String _word : line.split("\\W+")) {
                String word = _word.toLowerCase();
                pos++;
                if (stopWords.contains(word))
                    continue;
                List<Tuple> idx = index.get(word);
                if (idx == null) {
                    idx = new LinkedList<Tuple>();
                    index.put(word, idx);
                }
                idx.add(new Tuple(filePath, fileNo, pos));
            }
        }
        System.out.println("indexed " + file.getPath() + " " + pos + " words");
    }

    public String search(List<String> words) {
        String result = "";
        for (String _word : words) {
            Set<String> answer = new HashSet<String>();
            String word = _word.toLowerCase();
            List<Tuple> idx = index.get(word);
            if (idx != null) {
                for (Tuple t : idx) {
                    answer.add(files.get(t.fileNo));
                }
            }
            result += word;
            for (String f : answer) {
                result += (" " + f);
            }
        }
        return result;
    }

    static ArrayList<String> pathList = new ArrayList<String>();

    public static List<String> getFileNamesList(ArrayList<String> pathList){
        List<String> results = new ArrayList<String>();

        for(String p: pathList){
            File[] files = new File(p).listFiles();

            for(File f: files) {
                if (f.isFile()){
                    results.add(p + f.getName());
                }
            }
        }

        return results;
    }

}
