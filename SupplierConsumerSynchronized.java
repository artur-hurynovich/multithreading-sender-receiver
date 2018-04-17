package multithreadingSupplierConsumer;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
class DataSynchronized {
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
class Supplier implements Runnable {
    private final DataSynchronized data;
    private Random random;
    public Supplier(DataSynchronized data) {
        this.data = data;
        random = new Random();
    }
    @Override
    public void run() {
        int temp;
        for (int i = 0; i < 10; i++) {
            try {
                synchronized (data) {
                    while (data.isAvailable()) {
                        data.wait();
                    }
                    temp = random.nextInt(100) + 1;
                    data.setData(temp);
                    System.out.println("Supplier - " + temp);
                    data.setAvailable(true);
                    data.notify();
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
class Consumer implements Runnable {
    private final DataSynchronized data;
    public Consumer(DataSynchronized data) {
        this.data = data;
    }
    @Override
    public void run() {
        int temp;
        for (int i = 0; i < 10; i++) {
            try {
                synchronized (data) {
                    while (!data.isAvailable()) {
                        data.wait();
                    }
                    temp = data.getData();
                    System.out.println("Consumer - " + temp);
                    data.setAvailable(false);
                    data.notify();
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
public class SupplierConsumerSynchronized {
    public static void main(String[] args) {
        DataSynchronized data = new DataSynchronized();
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.execute(new Supplier(data));
        service.execute(new Consumer(data));
        service.shutdown();
    }

}
