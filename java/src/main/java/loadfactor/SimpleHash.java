package loadfactor;

/**
 * Created by Ken on 8/27/17.
 */
public class SimpleHash implements Hash {

    int size;

    public SimpleHash(int size){
        this.size=size;
    }

    public long hash(String s){
        long hash=2166136261L;
        long prime=16777619L;
        for(int i=0; i<s.length();i++){
            char c=s.charAt(i);
            hash^=c;
            hash= (hash * prime) % (((long)1)<<32);
        }
        return hash%size;
    }

}
