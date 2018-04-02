package Task1;

public class Task1 {
    public static void run(){
        int N = 100;
        int M = 5;
        ProductionMonitor monitor  = new ProductionMonitor(N);
        ProducerThread producer = new ProducerThread(monitor);
        producer.start();

        for(int i = 1; i <=M; i++){
            ProcessingThread processingThread = new ProcessingThread(monitor, i);
            processingThread.start();
        }

        ConsumerThread consumer = new ConsumerThread(monitor, M);
        consumer.start();
    }
}
