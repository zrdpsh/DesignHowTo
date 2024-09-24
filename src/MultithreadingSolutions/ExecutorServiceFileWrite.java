package MultithreadingSolutions;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
