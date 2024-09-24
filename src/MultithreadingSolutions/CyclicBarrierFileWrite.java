package MultithreadingSolutions;

import java.io.IOException;
import java.util.concurrent.CyclicBarrier;

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
