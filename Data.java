package multithreadingSenderReceiver;
public class Data {
    private int data;
    private boolean produced;
    public void setData(int data) {
        this.data = data;
    }
    public int getData() {
        return data;
    }
    public boolean isProduced() {
        return produced;
    }
    public void setProduced(boolean produced) {
        this.produced = produced;
    }
}
