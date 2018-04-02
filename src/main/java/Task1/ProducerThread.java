package Task1;

import java.util.Random;

public class ProducerThread extends Thread {

    private ProductionMonitor monitor;
    private Random random;


    public ProducerThread(ProductionMonitor monitor) {
        this.monitor = monitor;
        random = new Random();
    }

    @Override
    public void run() {
        while(true){
            try {
                int taskNumber = monitor.takeATask(new Product(0));
                System.out.println("Producer " + Thread.currentThread().getId() + " gave a new task " + taskNumber);
                Thread.sleep(Math.abs(random.nextInt() % 1000 ));
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }

    }
}
