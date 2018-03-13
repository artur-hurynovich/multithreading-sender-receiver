package multithreadingSenderReceiver;
public class Sender implements Runnable{
    private final Data data;
    private int [] number;
    private int index;
    public Sender(Data data){
        this.data = data;
        number = new int[6];
        for (int i = 0; i < number.length - 1; i++) number[i] = i;
        number[number.length - 1] = -1;
    }
    private void produce(){
        data.setData(number[index++]);
        data.setProduced(true);
    }
    @Override
    public void run() {
        while (data.getData() != -1){
            synchronized (data){
                while (data.isProduced()){
                    try {
                        data.wait();
                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
                produce();
                System.out.println("Sent " + data.getData());
                data.notifyAll();
            }
        }
    }
}
