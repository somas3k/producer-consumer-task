package Task1;

public class ConsumerThread extends Thread {

    private ProductionMonitor monitor;
    private int conditionToTake;

    public ConsumerThread(ProductionMonitor monitor, int conditionToTake) {
        this.monitor = monitor;
        this.conditionToTake = conditionToTake;
    }

    @Override
    public void run() {
        while(true){
            try {
                Product product = monitor.takeAProduct(conditionToTake);
                System.out.println("Consumer " + Thread.currentThread().getId() + " took a ready product");

            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }

    }
}
