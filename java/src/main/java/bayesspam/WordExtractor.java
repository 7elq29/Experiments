package bayesspam;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Ken on 11/18/17.
 */
public class WordExtractor {

    private static Set<String> stopWords = new HashSet<>();
    private List<String> lines;
    private int index;

    public WordExtractor(String stopwordPath, String path) throws IOException {
        if(stopWords.size()==0){
            this.readStopwords(stopwordPath);
        }
        this.lines = Files.lines(Paths.get(path)).collect(Collectors.toList());
        this.index = 0;
    }

    public int size(){
        return this.lines.size();
    }

    public boolean hasNext(){
        return index < lines.size();
    }

    public SMSWords next(){
        List<String> words = this.extractLine(lines.get(index++));
        boolean isSpam = words.get(0).equals("spam");
        words.remove(0);
        return new SMSWords(isSpam, new HashSet<>(words));
    }

    private List<String> extractLine(String line){
        SMSReader reader = new SMSReader(line);
        List<String> words = new ArrayList<>();
        while(reader.hasNext()){
            String word = reader.next();
            if(!stopWords.contains(word)) words.add(word);
        }
        return words;
    }

    private void readStopwords(String path) throws IOException {
        Stream<String> lines = Files.lines(Paths.get(path));
        lines.forEach(line->{
            WordExtractor.stopWords.add(line);
        });
    }
}
