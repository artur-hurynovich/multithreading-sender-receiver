package multithreadingSupplierConsumer;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
class DataLock {
    private volatile boolean available = false;
    private int data;
    public void setData(int data) {
        this.data = data;
    }
    public int getData() {
        return data;
    }
    public void setAvailable(boolean available) {
        this.available = available;
    }
    public boolean isAvailable() {
        return available;
    }
}
class SupplierLock implements Runnable {
    private final DataLock data;
    private ReentrantLock lock;
    private Condition condition;
    private Random random;
    public SupplierLock(DataLock data, ReentrantLock lock, Condition condition) {
        this.data = data;
        this.lock = lock;
        this.condition = condition;
        random = new Random();
    }
    @Override
    public void run() {
        int temp;
        for (int i = 0; i < 10; i++) {
            lock.lock();
            try {
                while (data.isAvailable()) {
                    condition.await();
                }
                temp = random.nextInt(100) + 1;
                data.setData(temp);
                System.out.println("Supplier - " + temp);
                data.setAvailable(true);
                condition.signal();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            finally {
                lock.unlock();
            }
        }
    }
}
class ConsumerLock implements Runnable {
    private final DataLock data;
    private ReentrantLock lock;
    private Condition condition;
    public ConsumerLock(DataLock data, ReentrantLock lock, Condition condition) {
        this.data = data;
        this.lock = lock;
        this.condition = condition;
    }
    @Override
    public void run() {
        int temp;
        for (int i = 0; i < 10; i++) {
            lock.lock();
            try {
                while (!data.isAvailable()) {
                    condition.await();
                }
                temp = data.getData();
                System.out.println("Consumer - " + temp);
                data.setAvailable(false);
                condition.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
}
public class SupplierConsumerLock {
    public static void main(String[] args) {
        DataLock data = new DataLock();
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.execute(new SupplierLock(data, lock, condition));
        service.execute(new ConsumerLock(data, lock, condition));
        service.shutdown();
    }
}
