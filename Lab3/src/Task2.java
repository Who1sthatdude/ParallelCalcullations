import java.util.concurrent.Semaphore;

public class Task2 {
    static Semaphore readLock = new Semaphore(1);
    static Semaphore writeLock = new Semaphore(1);
    static int readCount = 0;

    static class Read implements Runnable {
        @Override
        public void run() {
            try {
                readLock.acquire();
                readCount++;
                if (readCount == 1) {
                    writeLock.acquire();
                }
                readLock.release();

                System.out.println("Reader is reading");
                Thread.sleep(1000);
                System.out.println("Reader has finished reading");

                readLock.acquire();
                readCount--;
                if(readCount == 0) {
                    writeLock.release();
                }
                readLock.release();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    static class Write implements Runnable {
        @Override
        public void run() {
            try {
                writeLock.acquire();
                System.out.println("Writer is writing");
                Thread.sleep(1000);
                System.out.println("Writer has finished writing");
                writeLock.release();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Read read = new Read();
        Write write = new Write();
        Thread t1 = new Thread(read);
        Thread t2 = new Thread(read);
        Thread t3 = new Thread(write);
        Thread t4 = new Thread(read);
        Thread t5 = new Thread(write);
        t3.start();
        t1.start();
        t5.start();
        t2.start();
        t4.start();
    }

}
