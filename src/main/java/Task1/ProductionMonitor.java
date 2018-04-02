package Task1;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProductionMonitor {
    private Lock lock = new ReentrantLock();
    private Condition notFull = lock.newCondition();
    private Condition notEmpty = lock.newCondition();
    private Condition readyToProcess = lock.newCondition();
    private Product[] products;
    private int length;
    private int insertPointer;
    private int beginPointer;
    private int count;


    public ProductionMonitor(int N) {
        this.length = N;
        this.products = new Product[N];
        this.insertPointer = 0;
        this.count = 0;
        this.beginPointer = 0;

    }

    public int takeATask(Product product) throws InterruptedException {
        if (product.getCondition() != 0) return -1;
        lock.lock();
        try {
            while (count == length) {
                notFull.await();
            }
            int newTask = insertPointer;
            products[insertPointer] = product;
            insertPointer++;
            count++;
            if (insertPointer == length) insertPointer = 0;

            return newTask;
        }
        finally {
            notEmpty.signalAll();
            readyToProcess.signalAll();
            lock.unlock();
        }
    }

    public int giveATask(int condition) throws InterruptedException{

        lock.lock();
        try {
            //if(condition == 0 || condition == length - 1) return -1;
            while(count == 0){
                notEmpty.await();
            }
            int i = beginPointer;
            while (i != insertPointer && products[i].getCondition() >= condition) {
                i++;
                if (i == length) i = 0;
            }
            //if(products[i]==null) System.out.println("mam nulla");
            try {
//                while (count == 0) {
//                    notEmpty.await();
//                }
                while( count == 0 || products[i] == null || products[i].getCondition() != condition - 1 ){
                    readyToProcess.await();
                   // System.out.println("x");
                }
            }
            catch (NullPointerException e){
                System.out.println(i+" "+ count + " " + Thread.currentThread().getId());
            }
            return i;

        }
        finally {
            lock.unlock();
        }
    }

    public void updateTask(int i, int newCondition){
        lock.lock();
        products[i].setCondition(newCondition);
        readyToProcess.signalAll();
        lock.unlock();
    }

    public Product takeAProduct(int conditionToTake) throws InterruptedException{
        lock.lock();
        try {
            while(count == 0) {
                notEmpty.await();
            }
            while (products[beginPointer].getCondition() != conditionToTake) {
                readyToProcess.await();
            }
            Product product = products[beginPointer];
            count--;
            products[beginPointer]=null;
            beginPointer++;
            if (beginPointer == length) beginPointer = 0;
            return product;

        }
        finally {
            notFull.signal();
            lock.unlock();
        }

    }

}
