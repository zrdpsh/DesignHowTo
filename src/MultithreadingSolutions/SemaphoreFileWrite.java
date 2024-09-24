package MultithreadingSolutions;

import java.io.IOException;
import java.util.concurrent.Semaphore;

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
