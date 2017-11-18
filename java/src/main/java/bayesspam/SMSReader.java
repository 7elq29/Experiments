package bayesspam;

/**
 * Created by Ken on 11/18/17.
 */
public class SMSReader {

    private String sms;
    private int index;
    private String nextWord;

    public SMSReader(String sms){
        this.sms = sms;
        index = 0;
        nextWord = null;
    }

    public boolean hasNext(){
        StringBuffer buffer = new StringBuffer();
        while(index<sms.length()){
            char c = sms.charAt(index);
            if((c>='a' && c<='z') || (c>='A' && c<='Z')) break;
            index++;
        }
        while(index<sms.length()){
            char c = sms.charAt(index++);
            if((c>='a' && c<='z') || (c>='A' && c<='Z')) buffer.append(c);
            else break;
        }
        nextWord = buffer.length()>0?buffer.toString():null;
        return nextWord!=null;
    }

    public String next(){
        return nextWord;
    }
}
