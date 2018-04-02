package Task2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class OptimizedBuffer implements IBuffer {
    private List<Integer> buffer = new ArrayList<>();
    private int M;
    private ReentrantLock lock = new ReentrantLock();
    private Condition firstProducer = lock.newCondition(), anotherProducers = lock.newCondition();
    private Condition firstConsumer = lock.newCondition(), anotherConsumers=lock.newCondition();
    private boolean firstProducerFlag = false;
    private boolean firstConsumerFlag = false;

    public OptimizedBuffer() {
    }

    public OptimizedBuffer(int m){
        this.M = 2*m;
    }
    @Override
    public void setM(int m) {
        this.M = 2*m;
    }

    @Override
    public void put(int[] elements) {
        lock.lock();
        try{
            while(firstProducerFlag){
                anotherProducers.await();
            }
            firstProducerFlag=true;
            while (M - buffer.size() < elements.length) {
                firstProducer.await();
            }
            for (int e : elements) {
                buffer.add(e);
            }
            firstProducerFlag = false;
            anotherProducers.signal();
            firstConsumer.signal();

        }
        catch(InterruptedException e){
            //e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public void get(int elemCount) {
        lock.lock();
        try{
            while(firstConsumerFlag){
                anotherConsumers.await();
            }
            firstConsumerFlag = true;

            while (buffer.size() < elemCount) {
                firstConsumer.await();
            }
            for (int i = 0; i < elemCount; ++i) {
                buffer.remove(0);
            }
            firstConsumerFlag = false;
            anotherConsumers.signal();
            firstProducer.signal();
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
    }
}
