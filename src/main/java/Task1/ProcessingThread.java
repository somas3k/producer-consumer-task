package Task1;

import java.util.Random;

public class ProcessingThread extends Thread {
    private ProductionMonitor monitor;
    private Random random;
    private int condition;

    public ProcessingThread(ProductionMonitor monitor, int condition) {
        this.monitor = monitor;
        this.condition = condition;
        this.random = new Random();
    }

    @Override
    public void run() {
        while(true){
            try{
                int product = monitor.giveATask(condition);
                System.out.println("Thread " + Thread.currentThread().getId() + " took a task " + product);
                Thread.sleep(Math.abs(random.nextInt() % 1000 *condition/2));
                monitor.updateTask(product, condition);
                System.out.println("Thread " + Thread.currentThread().getId() + " update task " + product + " to " + condition);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
