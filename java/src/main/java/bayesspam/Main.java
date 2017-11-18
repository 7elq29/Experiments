package bayesspam;

import java.io.IOException;
import java.util.*;

/**
 * Created by Ken on 11/18/17.
 */
public class Main {

    String stopwordPath;
    String path;
    List<SMSWords> trainingset;
    List<SMSWords> testingset;
    Validator validator;

    public Main(String stopwordPath, String path){
        this.stopwordPath = stopwordPath;
        this.path = path;
        trainingset = new ArrayList<>();
        testingset = new ArrayList<>();
        validator = null;
    }

    public void build() throws IOException {
        WordExtractor extractor = new WordExtractor(stopwordPath, path);
        int index = 0, size = extractor.size();
        while(extractor.hasNext()){
            SMSWords sms = extractor.next();
            if(index++ < size/4*3) trainingset.add(sms);
            else testingset.add(sms);
        }
        Training t = new Training();
        t.train(trainingset);
        Map<String, Double> spamMap = t.spamMap;
        Map<String, Double> hamMap = t.hamMap;
        validator = new Validator(spamMap, hamMap);
    }

    public void validateAll(){
        int count = 0;
        for(SMSWords sms:testingset){
            boolean isSpam = !validate(sms);
            if(isSpam == sms.isSpam()) count+=1;
        }
        System.out.println(((double)count/(double)testingset.size())+" of the data passed");
    }

    public boolean validate(SMSWords sms){
        return validator.validate(sms);
    }


    public static void main(String[] args) throws IOException {
        String stopwordPath = "data/bayesspam/stopwords.txt";
        String path = "data/bayesspam/SMSSpamCollection";
        Main main = new Main(stopwordPath, path);
        main.build();
        main.validateAll();


    }
}
