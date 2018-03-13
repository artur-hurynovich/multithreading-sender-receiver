package multithreadingSenderReceiver;
public class Receiver implements Runnable{
    private final Data data;
    public Receiver(Data data){
        this.data = data;
    }
    @Override
    public void run() {
        while (data.getData() != -1){
            synchronized (data){
                while (!data.isProduced()){
                    try {
                        data.wait();
                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
                System.out.println("Received " + data.getData());
                data.setProduced(false);
                data.notifyAll();
            }
        }
    }
}
