
/**
 * Created by Marisha on 04/03/2018.
 */
public class MainConcurrency {
    private static int count;
    private static final Object lock1 = "lock1";
    private static final Object lock2 = "lock2";
    public static void main(String[] args) throws InterruptedException {
        System.out.println(Thread.currentThread().getName());
        Thread thread0 = new Thread("Name"){
            @Override
            public void run() {
                System.out.println(getName()+", "+getState());
            }
        };
        thread0.start();
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName()+", "+Thread.currentThread().getState());
        }, "Name1").start();
        System.out.println(thread0.getState());
        for (int i = 0; i < 10000; i++) {
            new Thread(()->{
                for (int j = 0; j < 100; j++) {
                    inc();
                }
            }).start();
        }
        Thread.sleep(500);
        System.out.println(count);

        //dead-lock
        deadLock("thread1", lock1, lock2);
        deadLock("thread2", lock2, lock1);
    }

    private static void inc(){
        synchronized (lock1) {
            count++;
        }
    }

    private static void deadLock(String name, Object lock1, Object lock2){
        new Thread(()->{
            System.out.println(Thread.currentThread().getName() + " waiting" + lock1);
            synchronized (lock1){
                System.out.println(Thread.currentThread().getName() + " took" + lock1);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " waiting" + lock2);
                synchronized (lock2){
                    System.out.println(Thread.currentThread().getName() + " took" + lock2);
                }
            }
        }, name).start();
    }
}
