package MultithreadingSolutions;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;


public class FileWriterMock {
    private BufferedWriter writer;
    private static final Random random = new Random();

    public void open(String fileName) throws IOException {
        System.out.println(Thread.currentThread().getName() + " is opening the file: " + fileName);
        someDelay(100, 300);
        writer = new BufferedWriter(new FileWriter(fileName, true)); // Open file in append mode
        System.out.println(Thread.currentThread().getName() + " opened the file.");
    }

    public void write(String content) throws IOException {
        if (writer == null) {
            throw new IllegalStateException("File wasnt opened.");
        }
        System.out.println(Thread.currentThread().getName() + " is writing to file: " + content);
        someDelay(200, 500); // Simulate time taken to write to the file
        writer.write(content);
        writer.newLine();
        System.out.println(Thread.currentThread().getName() + " finished writing.");
    }

    public void close() throws IOException {
        if (writer != null) {
            System.out.println(Thread.currentThread().getName() + " is closing the file...");
            someDelay(100, 200); // Simulate time taken to close the file
            writer.close();
            writer = null;
            System.out.println(Thread.currentThread().getName() + " closed the file.");
        }
    }

    private void someDelay(int minMillis, int maxMillis) {
        try {
            Thread.sleep(random.nextInt(maxMillis - minMillis) + minMillis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
}
