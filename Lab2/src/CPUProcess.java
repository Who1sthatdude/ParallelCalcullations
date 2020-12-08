

public class CPUProcess extends Thread {
    private final long interval;
    private final int priority;

    public CPUProcess(int min, int max, int priority) {
        interval = (int) (Math.random() * (max - min) + min);
        this.priority = priority;
    }

    public int getCustomPriority() {
        return priority;
    }

    @Override
    public void run() {
        try {
            System.out.println("Process " + this + " began to be generated within " + this.interval + " milliseconds");
            sleep(this.interval);
            System.out.println("Process " + this + " generated");
        } catch (InterruptedException e) {
            System.out.println("Generation of the process " + this + " is interrupted");
        }
    }
}
