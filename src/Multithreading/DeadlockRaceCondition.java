
/**
* в RaceConditionExample потоки пытаются изменять одну общую переменную 
* без какой-либо синхронизации. Чтобы поведение стало предсказуемым, можно 
* завернуть увеличение счётчика в метод, дополненный модификатором synchronized - RaceCondtionExampleFixed
*/


public class RaceConditionExample {

    private static int counter = 0;

    public static void main(String[] args) {
        int numberOfThreads = 10;
        Thread[] threads = new Thread[numberOfThreads];

        for (int i = 0; i < numberOfThreads; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 100000; j++) {
                    counter++;
                }
            });
            threads[i].start();
        }

        for (int i = 0; i < numberOfThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Final counter value: " + counter);
    }
}

public class RaceConditionExampleFixed {

    private static int counter = 0;

    public static void main(String[] args) {
        int numberOfThreads = 10;
        Thread[] threads = new Thread[numberOfThreads];

        for (int i = 0; i < numberOfThreads; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 100000; j++) {
                    incrementCounter();
                }
            });
            threads[i].start();
        }

        for (int i = 0; i < numberOfThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Final counter value: " + counter);
    }

  public static synchronized void incrementCounter() {
     counter++; 
  }
}


/**
* Во втором примере ошибка возникает уже из-за использования модификатора synchronized:
* идея модификатора в том, чтобы предваряемый им код в каждый отдельный момент выполнялся только в одном потоке,
* остальные потоки, вызывающие тот же код, будут прерваны и поставлены в очередь. После выълва обоих потоков, 
* Thread 1 и Thread 2, оба отработают до строчки, в которой вызывают друг друга, после чего программа зависнет:
* первый поток продолжит работать, только когда отработает второй, второй - когда отработает первый.
* Чтобы снять этот логический тупик, в обоих потоках можно вызывать ресурсы в одинаковом порядке.
*/

public class DeadlockExample {

    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            synchronized (lock1) {
                System.out.println("Thread 1 acquired lock1");

                try { Thread.sleep(50); } 
                catch (InterruptedException e) { e.printStackTrace(); }

                synchronized (lock2) {
                    System.out.println("Thread 1 acquired lock2");
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            synchronized (lock2) {
                System.out.println("Thread 2 acquired lock2");

                try { Thread.sleep(50); } 
                catch (InterruptedException e) { e.printStackTrace(); }

                synchronized (lock1) {
                    System.out.println("Thread 2 acquired lock1");
                }
            }
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Finished");
    }
}

public class DeadlockExampleFixed {

    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            synchronized (lock1) {
                System.out.println("Thread 1 acquired lock1");

                try { Thread.sleep(50); } 
                catch (InterruptedException e) { e.printStackTrace(); }

                synchronized (lock2) {
                    System.out.println("Thread 1 acquired lock2");
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            synchronized (lock1) {
                System.out.println("Thread 2 acquired lock2");

                try { Thread.sleep(50); } 
                catch (InterruptedException e) { e.printStackTrace(); }

                synchronized (lock2) {
                    System.out.println("Thread 2 acquired lock1");
                }
            }
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Finished");
    }
}
