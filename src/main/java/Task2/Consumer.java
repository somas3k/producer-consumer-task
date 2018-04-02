package Task2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Consumer extends Thread {
    private IBuffer buffer;
    private int M;
    private HashMap<Integer, ArrayList<Long>> times = new HashMap<>();
    private int loops;

    public Consumer(IBuffer buffer, int M, int loops) {
        this.buffer = buffer;
        this.M = M;
        this.loops = loops;
    }

    @Override
    public void run() {
        int i = 0;
        while(i < loops){
            int count = (int)(Math.random()*M+1);
            long start = System.nanoTime();
            buffer.get(count);
            long length = System.nanoTime() - start;


            if(!times.containsKey(count)){
                ArrayList<Long> time = new ArrayList<>();
                time.add(length);
                times.put(count, time);
            }
            else{
                times.get(count).add(length);
            }
            i++;
        }
        //System.out.println("End " + Thread.currentThread().getId());
    }

    public Map<Integer, Double> getAverageTimes(){
        Set<Integer> keys = times.keySet();
        HashMap<Integer, Double> averageTimes = new HashMap<>();
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
