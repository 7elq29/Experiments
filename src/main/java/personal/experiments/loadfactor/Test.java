package personal.experiments.loadfactor;

import java.util.*;

/**
 * Created by Ken on 8/28/17.
 */
public class Test {

    HashTable hashTable;
    Set<Integer> usedData;
    Random random;
    int bucketSize;

    public Test(int size){
        this.hashTable=new HashTable(size);
        usedData=new HashSet<Integer>();
        random=new Random();
        bucketSize=size;
    }

    /**
     * Load random data to the hash table until reach load factor.
     * @param loadFactor
     */
    public void loadTillLoadFactor(double loadFactor){
        while((double)hashTable.keyNumber()/(double)bucketSize<loadFactor){
            int num=random.nextInt();
            while(usedData.contains(num)) num=random.nextInt();
            usedData.add(num);
            hashTable.put(num);
        }
        System.out.println("[Total Data]"+usedData.size()+",[Bucket used]"+hashTable.keyNumber());
    }

    public int cacheMissesPerLookup(int num){
        return hashTable.missingTimes(num);
    }

    public double averageCacheMissesPerLookup(int totalTimes){
        List<Integer> list=new ArrayList<Integer>(usedData);
        Collections.shuffle(list);
        int totalMisses=0;
        for(int i=0;i<totalTimes && i<usedData.size();i++){
            totalMisses+=cacheMissesPerLookup(list.get(i));
        }
        return (double)totalMisses/(double)totalTimes;
    }

    public static void main(String[] args){
        int buckets=1500;
        int lookupTimes=300;
        Test test=new Test(buckets);

        List<Double[]> data=new ArrayList<Double[]>();
        for(double loadFactor=0;loadFactor<1;loadFactor+=0.01){
            test.loadTillLoadFactor(loadFactor);
            data.add(new Double[]{loadFactor, test.averageCacheMissesPerLookup(lookupTimes)});
        }
        /**
         * Average cache misses per lookup
         */
        LineChart chart=new LineChart("Hashing conflict", "Hashing conflict",
                "Load Factor", "Average Cache Misses per Lookup", buckets);
        chart.setData(data, "Lookup Misses");
        chart.build();

    }

}
