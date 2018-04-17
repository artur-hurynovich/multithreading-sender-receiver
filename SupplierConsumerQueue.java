package multithreadingSupplierConsumer;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
class SupplierQueue implements Runnable {
    private ArrayBlockingQueue<Integer> queue;
    private Random random;
    public SupplierQueue(ArrayBlockingQueue<Integer> queue) {
        this.queue = queue;
        random = new Random();
    }
    @Override
    public void run() {
        int temp;
        for (int i = 0; i < 10; i ++) {
            try {
                temp = random.nextInt(100) + 1;
                queue.put(temp);
                System.out.println("Supplier - " + temp);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
class ConsumerQueue implements Runnable {
    private ArrayBlockingQueue<Integer> queue;
    public ConsumerQueue(ArrayBlockingQueue<Integer> queue) {
        this.queue = queue;
    }
    @Override
    public void run() {
        for (int i = 0; i < 10; i ++) {
            try {
                System.out.println("Consumer - " + queue.take());
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
public class SupplierConsumerQueue {
    public static void main(String[] args) {
        ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<>(1);
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.execute(new SupplierQueue(queue));
        service.execute(new ConsumerQueue(queue));
        service.shutdown();
    }
}
