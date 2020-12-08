public class Main {
    final static int LOWER_BOUND = 100;
    final static int UPPER_BOUND = 500;

    final static int PROCESSES_NUMBER = 30;

    final static int QUEUES_NUMBER = 2;
    final static int QUEUE_MAX_SIZE = 30;

    public static boolean isQueuesEmpty(CPUQueue [] queues) {
        boolean result = true;
        for (int i = 0; i < queues.length; i++) {
            result &= queues[i].isEmpty();
        }
        return result;
    }

    public static void main(String[] args) throws InterruptedException {
        CPUProcess[] processes = new CPUProcess[QUEUES_NUMBER * PROCESSES_NUMBER];
        CPUQueue[] queues = new CPUQueue[QUEUES_NUMBER];

        for (int j = 0; j < QUEUES_NUMBER; j++) {
            queues[j] = new CPUQueue(QUEUE_MAX_SIZE);
        }

        CPU cpu = new CPU(LOWER_BOUND, UPPER_BOUND, queues, "CPU");
        cpu.start();

//        number of priority coincides with queue index
        int priority = 0; // the highest priority for the first queue
        for (int j = 0; j < PROCESSES_NUMBER * QUEUES_NUMBER; j++) {
            if (j >= PROCESSES_NUMBER * (priority + 1)) { priority++; }

            processes[j] = new CPUProcess(LOWER_BOUND, UPPER_BOUND, priority);
            processes[j].start();
            processes[j].join();

            cpu.addProcess(processes[j]);
        }

        while (cpu.isAlive()) {
//            the CPU has finished its work
            if (isQueuesEmpty(queues) && cpu.getProcess() == null) {
                cpu.stopExecuting();
            }
        }
    }
}
