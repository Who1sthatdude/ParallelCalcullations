import java.util.ArrayList;

public class Task3 {

    public static final int PHILOSOPHERS_NUMBER = 5;
    public static final int FORKS = 5;

    public static class Fork {

        private final int id;
        private Thread holder;

        public Fork(int id) {
            this.id = id;
            holder = null;
        }

        public int getId() {
            return id;
        }

        public synchronized void acquire() throws InterruptedException {
            if (holder != null) {
                wait();
            }
            holder = Thread.currentThread();
        }

        public synchronized void release() {
            if (holder == Thread.currentThread())
                holder = null;
            notify();
        }

    }

    public static class Philosopher implements Runnable {

        private final int id;
        private final Fork firstFork;
        private final Fork secondFork;

        public Philosopher(int id, Fork left, Fork right) {
            this.id = id;
            if (left.getId() < right.getId()) {
                firstFork = left;
                secondFork = right;
            } else {
                firstFork = right;
                secondFork = left;
            }
        }


        private void pickUpForks() {
            System.out.println("Philosopher " + id + " picked up forks.");
            try {
                firstFork.acquire();
                secondFork.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        private void releaseForks() {
            System.out.println("Philosopher " + id + " released forks.");
            firstFork.release();
            secondFork.release();
        }

        private void eat() throws InterruptedException {
            System.out.println("Philosopher " + id + " is eating.");
            Thread.sleep(1000);
        }


        @Override
        public void run() {
            try {
                pickUpForks();
                eat();
                releaseForks();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

        ArrayList<Fork> forks = new ArrayList<>();
        ArrayList<Philosopher> philosophers = new ArrayList<>();

        for (int i = 0; i < FORKS; i++) {
            forks.add(new Fork(i));
        }

        for (int i = 0; i < PHILOSOPHERS_NUMBER; i++) {
            int next = i + 1;
            if (i == PHILOSOPHERS_NUMBER - 1) {
                next = 0;
            }
            philosophers.add(new Philosopher(i, forks.get(i), forks.get(next)));
        }

        for (Philosopher p : philosophers) {
            new Thread(p).start();
        }

    }

}
