package personal.experiments.loadfactor;

import java.util.*;

/**
 * Created by Ken on 8/27/17.
 */
public class HashTable {

    class Node{
        int value;
        Node next;

        public Node(int value){
            this.value=value;
        }
    }

    Map<Long, Node> keys=new HashMap<Long, Node>();
    int bucketSize=0;
    int currentSize=0;
    Hash hash;

    public HashTable(int size){
        this.bucketSize=size;
        hash=new SimpleHash(size);
    }

    public void put(int num){
        long key=hash.hash(num+"");
        if(keys.containsKey(key)){
            Node n=keys.get(key);
            while(n.next!=null){
                if(n.value==num) return;
                n=n.next;
            }
            n.next=new Node(num);
        }else{
            keys.put(key, new Node(num));
        }
        currentSize+=1;
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
     * Return the cache misses times when finding num. If num is not in the table, return -1;
     * @param num
     * @return
     */
    public int missingTimes(int num){
        long key=hash.hash(num+"");
        int times=0;
        if(!keys.containsKey(key)) return -1;
        else{
            Node n=keys.get(key);
            while(n!=null){
                if(n.value==num) return times;
                else n=n.next;
                times+=1;
            }
            return -1;
        }
    }

    public int size(){
        return this.currentSize;
    }

    public int keyNumber(){
        return this.keys.size();
    }


}
