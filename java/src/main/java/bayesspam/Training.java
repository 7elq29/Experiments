package bayesspam;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Ken on 11/18/17.
 */
public class Training {

    Map<String, Double> spamMap = new HashMap<>();
    Map<String, Double> hamMap = new HashMap<>();

    public void train(List<SMSWords> sms){
        Map<Boolean, List<SMSWords>> map = sms.stream().collect(Collectors.groupingBy(SMSWords::isSpam));
        int countSpam = map.containsKey(true)?map.get(true).size():0;
        int countHam = map.containsKey(false)?map.get(false).size():0;
        Map<String, Integer> countWordSpam = this.countWord(map, true);
        Map<String, Integer> countWordHam = this.countWord(map, false);
        Map<String, Double> probabiitySpam = this.computeProbability(countWordSpam, countSpam);
        Map<String, Double> probabiityHam = this.computeProbability(countWordHam, countHam);

        Set<String> allWords = new HashSet<>();
        allWords.addAll(countWordHam.keySet());
        allWords.addAll(countWordSpam.keySet());
        double ps = (double)countSpam / (double)(countHam+countSpam);
        double ph = (double)countHam / (double)(countHam+countSpam);
        for(String key:allWords){
            spamMap.put(key, probabiitySpam.getOrDefault(key, 0D)*ps/
                    (probabiitySpam.getOrDefault(key, 0D)*ps
                            +probabiityHam.getOrDefault(key, 0D)*ph));
            hamMap.put(key, probabiityHam.getOrDefault(key, 0D)*ph/
                    (probabiitySpam.getOrDefault(key, 0D)*ps
                            +probabiityHam.getOrDefault(key, 0D)*ph));
        }
    }

    private Map<String, Double> computeProbability(Map<String, Integer> count, int total){
        if(total==0) throw new RuntimeException("No data of spam or ham found");
        Map<String, Double> rs = new HashMap<>();
        for(String key:count.keySet()){
            rs.put(key, (double)count.get(key)/(double)total);
        }
        return rs;
    }

    private Map<String, Integer> countWord(Map<Boolean, List<SMSWords>> map, boolean isSpam){
        return map.get(isSpam).stream().map((s)->{
            Map<String, Integer> m = new HashMap<>();
            for(String word:s.getWords()){
                m.put(word, m.getOrDefault(word,0)+1);
            }
            return m;
        }).reduce(new HashMap<String, Integer>(), (map1, map2)->{
            for(String key:map2.keySet()){
                map1.put(key, map1.getOrDefault(key,0)+map2.get(key));
            }
            return map1;
        });
    }
}
