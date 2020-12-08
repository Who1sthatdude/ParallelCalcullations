import java.util.concurrent.atomic.AtomicBoolean;

public class CPU extends Thread{
    private int processingTime;
    private volatile CPUProcess process;
    private CPUQueue[] queues;

    public final AtomicBoolean running = new AtomicBoolean(false);

    public CPU(int min, int max, CPUQueue[] queues, String name) {
        super(name);
        processingTime = (int) (Math.random() * (max - min) + min);
        process = null;
        this.queues = queues;
    }

    public void setProcess(CPUProcess process) {
        this.process = process;
    }
    public CPUProcess getProcess() {
        return this.process;
    }

    public void addProcess(CPUProcess process) {
        if (this.process == null) {
            setProcess(process);
            return;
        }

//      priority number matches the queue index
        int index = process.getCustomPriority();
        try {
            if (queues[index].isFull()) {
                throw new IllegalArgumentException();
            }
            queues[index].add(process);
        } catch (IllegalArgumentException e) {
            System.out.println("Queue is full");
        }

    }

    public CPUProcess getProcessFromQueue() {
        CPUProcess process;
        for (int i = 0; i < this.queues.length; i++) {
            process = this.queues[i].poll();
            if (process != null) {
                return process;
            }
        }
        return null;
    }

    @Override
    public void run() {
        running.set(true);
        while (running.get()) {
            if (process != null) {
                System.out.println("CPU " + this + " processing " + this.process + " for " + this.processingTime + " milliseconds");
                try {
                    sleep(this.processingTime);
                } catch(InterruptedException e) {
                    System.out.println("Thread " + this + " was interrupted");
                }
                System.out.println("CPU " + this + " finished processing of " + this.process);
                setProcess(null);
            } else {
                setProcess(getProcessFromQueue());
            }
        }
    }

    public void stopExecuting() {
        running.set(false);
    }
}
