package Task2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SimpleBuffer implements IBuffer {
    private List<Integer> buffer = new ArrayList<>();
    private Lock lock = new ReentrantLock();
    private Condition notAvailable = lock.newCondition();
    private int M;

    public SimpleBuffer() {
    }

    SimpleBuffer(int m) {
        M = 2*m;
    }
    @Override
    public void setM(int m){
        this.M = 2*m;
    }
    public void put(int[] elements){
        lock.lock();
        try {
            while (M - buffer.size() < elements.length) {
                notAvailable.await();
            }

            for (int e : elements) {
                buffer.add(e);
            }
            notAvailable.signalAll();
        }
        catch (InterruptedException e){
            //e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
    }

    public void get(int count){
        lock.lock();
        try {
            while (buffer.size() < count) {
                notAvailable.await();
            }

            for (int i = 0; i < count; ++i) {
                buffer.remove(0);

            }
            notAvailable.signalAll();

        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
    }
}
