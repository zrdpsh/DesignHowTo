package MultithreadingSolutions;

import java.io.IOException;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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
