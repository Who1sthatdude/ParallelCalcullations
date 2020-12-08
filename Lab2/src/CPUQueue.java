import java.util.LinkedList;
import java.util.Queue;

public class CPUQueue {
    private Queue<CPUProcess> queue;
    private final int maxSize;
    public CPUQueue(int maxSize) {
        queue = new LinkedList<>();
        this.maxSize = maxSize;
    }

    public void add(CPUProcess process) {
        queue.add(process);
    }

    public CPUProcess poll() {
        return queue.poll();
    }

    public boolean isFull() {
        return queue.size() >= maxSize;
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
