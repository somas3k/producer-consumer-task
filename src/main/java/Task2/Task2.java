package Task2;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Task2 {
    public void runTask2(IBuffer buffer,int M, int producersCount, int consumersCount, int loops){

        buffer.setM(M);
        ExecutorService executorService = Executors.newFixedThreadPool(producersCount+consumersCount);
        ArrayList<Producer> producers = new ArrayList<>();
        ArrayList<Consumer> consumers = new ArrayList<>();
        ArrayList<Future> futures = new ArrayList<>();

        for(int i = 0 ; i < producersCount; i++){
            Producer producer = new Producer(buffer, M);
            producers.add(producer);
            Future f = executorService.submit(producer);

        }
        for(int i = 0 ; i < consumersCount; i++){
            Consumer consumer =new Consumer(buffer, M, loops);
            consumers.add(consumer);
            Future f = executorService.submit(consumer);
            futures.add(f);
        }


        for(Future f :futures){
            try {
                f.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executorService.shutdownNow();

        HashMap<Integer, Double> producersAvgTimes = new HashMap<>();
        HashMap<Integer, Double> consumersAvgTimes = new HashMap<>();

        for(int i = 1; i <= M; i++){
            int j = 0;
            double sum = 0;
            for(Producer p : producers){
                if(p.getAverageTimes().containsKey(i)){
                    j++;
                    sum+=p.getAverageTimes().get(i);
                }
                if(j!=0){
                    producersAvgTimes.put(i, sum/j);
                }
            }
            j = 0;
            sum = 0;
            for(Consumer c : consumers){
                if(c.getAverageTimes().containsKey(i)){
                    j++;
                    sum+=c.getAverageTimes().get(i);
                }
                if(j!=0){
                    consumersAvgTimes.put(i, sum/j);
                }
            }
        }

        XYSeries seriesProducers = new XYSeries("Producers");
        XYSeries seriesConsumers = new XYSeries("Consumers");
        XYSeriesCollection dataset = new XYSeriesCollection();

        for(Integer i : producersAvgTimes.keySet()){
            seriesProducers.add(i, producersAvgTimes.get(i));
        }
        dataset.addSeries(seriesProducers);
        for(Integer i : consumersAvgTimes.keySet()){
            seriesConsumers.add(i, consumersAvgTimes.get(i));
        }

        dataset.addSeries(seriesConsumers);

        JFreeChart chart = ChartFactory.createXYLineChart("TW", "M", "Time", dataset, PlotOrientation.VERTICAL, true, true, false);
        ChartPanel panel = new ChartPanel(chart);
        ApplicationFrame frame = new ApplicationFrame("TW");
        frame.setContentPane(panel);
        frame.pack();
        RefineryUtilities.centerFrameOnScreen(frame);
        frame.setVisible(true);
    }

}
