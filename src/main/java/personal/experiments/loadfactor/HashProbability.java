package personal.experiments.loadfactor;

import java.util.*;

/**
 * Created by Ken on 8/27/17.
 */
public class HashProbability {

    class Node{
        int value;
        Node next;

        public Node(int value){
            this.value=value;
        }
    }

    Map<Long, Node> keys=new HashMap<Long, Node>();
    int size=0;
    Hash hash;

    public HashProbability(int size){
        this.size=size;
        hash=new SimpleHash(size);
    }

    public void put(int num){
        long key=hash.hash(num+"");
        if(keys.containsKey(key)){
            Node n=keys.get(key);
            while(n.next!=null){
                n=n.next;
            }
            n.next=new Node(num);
        }else{
            keys.put(key, new Node(num));
        }
    }

    public boolean exists(int num){
        long key=hash.hash(num+"");
        if(!keys.containsKey(key)) return false;
        else{
            Node n=keys.get(key);
            while(n!=null){
                if(n.value==num) return true;
                else n=n.next;
            }
            return false;
        }
    }

    /**
     * Insert number 1,2...,maxNum into hashtable, return the number of conflict.
     * @param maxNum
     * @return
     */
    public int runTest(int maxNum){
        Set<Integer> set=new HashSet<Integer>();
        Random random=new Random();
        for(int i=1;i<=maxNum;i++) {
            int num=random.nextInt();
            while(set.contains(num)) num=random.nextInt();
            set.add(num);
            this.put(num);
        }
        return maxNum-keys.size();
    }


    public static void main(String[] args){
        int buckets=800;
        //System.out.println("[bucket]"+buckets+",[maxNum]:"+maxNum+",[conflict]"+conflict+
        //        ",[conflict ratio]"+(double)conflict/(double)maxNum);
        List<Double[]> data=new ArrayList<Double[]>();
        for(double frac=0;frac<=1;frac+=0.05){
            HashProbability hp=new HashProbability(buckets);
            int conflict=hp.runTest((int)(buckets*frac));
            Double[] d={frac, (double)conflict/(buckets*frac)};
            //Double[] d={frac, (double)conflict};
            data.add(d);
        }
        LineChart chart=new LineChart("Hashing conflict", "Hashing conflict", buckets);
        chart.setConflicts(data);
        //chart.setConflictRatio(number, ratio);
        chart.build();
    }
}
