package MultithreadingSolutions;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

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
