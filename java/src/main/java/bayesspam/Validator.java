package bayesspam;

import java.util.Map;

/**
 * Created by Ken on 11/18/17.
 */
public class Validator {
    Map<String, Double> spammap;
    Map<String, Double> hammap;


    public Validator(Map<String, Double> spammap, Map<String, Double> hammap){
        this.spammap = spammap;
        this.hammap = hammap;
    }

    public boolean validate(SMSWords sms){
        double probabilitySpam = this.getProbability(true, sms);
        double probabilityham = this.getProbability(false, sms);
        return probabilitySpam<probabilityham;
    }

    private double getProbability(boolean spam, SMSWords sms){
        double parameter1 = 1;
        double parameter2 = 1;
        Map<String, Double> map = spam?this.spammap:this.hammap;
        for(String word:sms.getWords()){
            if(map.containsKey(word)){
                parameter1*=map.get(word);
                parameter2*=(1-map.get(word));
            }
        }
        return parameter1 / (parameter1+parameter2);
    }
}
