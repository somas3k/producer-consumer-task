package Task2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Producer extends Thread {
    private IBuffer buffer;
    private int M;
    private Map<Integer, ArrayList<Long>> times = new ConcurrentHashMap<>();


    public Producer(IBuffer buffer, int m) {
        this.buffer = buffer;
        M = m;
    }

    @Override
    public void run(){
        int j = 0;
        while(true){
            int count = (int)(Math.random()*M+1);
            int[] elements = new int[count];
            for(int i = 0; i < count; ++i){
                elements[i]=(int)(Math.random() * 10) + 1;
            }
            long start = System.nanoTime();
            buffer.put(elements);
            long length = System.nanoTime()-start;

            if(!times.containsKey(count)){
                ArrayList<Long> time = new ArrayList<>();
                time.add(length);
                times.put(count, time);
            }
            else{
                ArrayList<Long> time = times.get(count);
                time.add(length);
            }
            j++;
        }
    }

    public Map<Integer, Double> getAverageTimes(){
        Set<Integer> keys = times.keySet();
        HashMap<Integer, Double> averageTimes = new HashMap<>();
        //System.out.println(keys);
        for(Integer i : keys){
            long sum = 0;
            for(Long time : times.get(i)){
                sum += time;
            }
            averageTimes.put(i, sum/(double)(times.get(i).size()));
        }
        return averageTimes;

    }
}
