package MultithreadingSolutions;

import java.io.IOException;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Solutions {
    public static void main(String[]args){
        //goto
    }
}

public class SemaphoreFileWrite {
    private static final Semaphore semaphore = new Semaphore(3);
    private static final FileWriterMock fileWriter = new FileWriterMock();
    private static final String fileName = "output.txt";

    public static void writeToFile(String content) {
        try {
            semaphore.acquire();
            fileWriter.open(fileName);
            fileWriter.write(content);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            semaphore.release();
        }
    }
}

public class ReadWriteLockFileWrite {
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private static final ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
    private static final FileWriterMock fileWriter = new FileWriterMock();
    private static final String fileName = "output.txt";

    public static void writeToFile(String content) {
        writeLock.lock();
        try {
            fileWriter.open(fileName);
            fileWriter.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            writeLock.unlock();
        }
    }
}


public class CyclicBarrierFileWrite {
    private static final CyclicBarrier barrier = new CyclicBarrier(3, () -> {
        System.out.println("All threads reached the barrier, writing to file...");
    });
    private static final FileWriterMock fileWriter = new FileWriterMock();
    private static final String fileName = "output.txt";

    public static void writeToFile(String content) {
        try {
            barrier.await();
            fileWriter.open(fileName);
            fileWriter.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

public class CompletableFutureFileWrite {
    private static final FileWriterMock fileWriter = new FileWriterMock();
    private static final String fileName = "output.txt";

    public static CompletableFuture<Void> writeToFileAsync(String content) {
        return CompletableFuture.runAsync(() -> {
            try {
                fileWriter.open(fileName);
                fileWriter.write(content);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void writeToFile(String content) {
        CompletableFuture<Void> future = writeToFileAsync(content);
        future.thenRun(() -> System.out.println("Write completed by: " + Thread.currentThread().getName()));
    }
}


public class ExecutorServiceFileWrite {
    private static final ExecutorService executor = Executors.newFixedThreadPool(3);
    private static final FileWriterMock fileWriter = new FileWriterMock();
    private static final String fileName = "output.txt";

    public static void writeToFile(String content) {
        executor.submit(() -> {
            try {
                fileWriter.open(fileName);
                fileWriter.write(content);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void shutdown() {
        executor.shutdown();
    }
}