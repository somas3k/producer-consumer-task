import Task1.Task1;
import Task2.Task2;
import Task2.SimpleBuffer;
import Task2.OptimizedBuffer;

public class Main {

    public static void main(String[] args) {
        //Task1.run();
        Task2 task = new Task2();
        task.runTask2(new SimpleBuffer(),100, 10, 10, 1000);
        //task.runTask2(new OptimizedBuffer(), 10000, 1000, 1000, 1000);
    }
}
