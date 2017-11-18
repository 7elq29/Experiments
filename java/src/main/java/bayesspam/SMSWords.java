package bayesspam;

import java.util.List;
import java.util.Set;

/**
 * Created by Ken on 11/18/17.
 */
public class SMSWords {
    private boolean isSpam;
    private Set<String> words;

    public SMSWords(boolean isSpam, Set<String> words){
        this.isSpam = isSpam;
        this.words = words;
    }

    public boolean isSpam(){
        return isSpam;
    }

    public Set<String> getWords(){
        return this.words;
    }
}
