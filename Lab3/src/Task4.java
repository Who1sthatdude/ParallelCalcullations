import java.util.concurrent.Semaphore;

public class Task4 extends Thread {

    public static final int MAX_CUSTOMERS = 6;
    public static final int CHAIRS = 1;

    public static Semaphore customers = new Semaphore(0);
    public static Semaphore barber = new Semaphore(0);
    public static Semaphore accessSeats = new Semaphore(1);
    public static int numberOfFreeSeats = CHAIRS;
    public static int workingTime = MAX_CUSTOMERS - CHAIRS-1;

    public static void main(String[] args) {
        Task4 barberShop = new Task4();
        barberShop.start();
    }

    public void run() {
        Barber barber = new Barber();
        barber.start();

        for (int i = 1; i <= MAX_CUSTOMERS; i++) {
            Customer customer = new Customer(i);
            customer.start();
            try {
                sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    class Barber extends Thread {
        public void run() {
            while (workingTime>0) {
                try {
                    customers.acquire(); //якщо клієнта немає -лягає спати
                    accessSeats.release();
                    numberOfFreeSeats++; //одне місце стає вільним
                    barber.release(); //перукар готовий стригти
                    accessSeats.release();
                    cutHair();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }finally {
                    workingTime--;
                }
            }
        }

        public void cutHair() {
            System.out.println("Barber is working");
            try {
                sleep(2000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }


    class Customer extends Thread {

        int iD; //customer id
        boolean notCut = true;

        public Customer(int i) {
            iD = i;
        }

        public void run() {
            while (notCut) {
                try {
                    accessSeats.acquire();  //спробувати отримати доступ до стільців
                    if (numberOfFreeSeats > 0) {  //якщо є вільне місце
                        System.out.println("Customer " + this.iD + " took a seat");
                        numberOfFreeSeats--;
                        customers.release();  //повідомити що є клієнт
                        accessSeats.release();
                        try {
                            barber.acquire();  //зачекати якщо перукар працює
                            notCut = false;  //відвідувач залишає перукарню після завершення стрижки
                            this.haircut();
                        } catch (InterruptedException ex) {
                        }
                    } else {  //немає вільних місць
                        System.out.println("All seats are occupied " + this.iD + " has left");
                        accessSeats.release();
                        notCut = false; // відвідувач залишить перукарню через брак вільних місць
                    }
                } catch (InterruptedException ex) {
                }
            }
        }

        public void haircut() {
            System.out.println("Customer " + this.iD + " is getting a haircut");
            try {
                sleep(2000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

    }

}
