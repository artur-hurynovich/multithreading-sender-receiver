package multithreadingSenderReceiver;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class Runner {
    public static void main(String[] args) {
        Data data = new Data();
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.execute(new Sender(data));
        service.execute(new Receiver(data));
        service.shutdown();
    }
}
