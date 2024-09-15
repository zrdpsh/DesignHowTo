package AverageCalculator;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class Main {
    public static void main(String[] args) {

        System.out.println("The floating-point arithmetic doesn't give accurate results");

        AverageCalculator aCalculator = new AverageCalculator();
        int[] numbers = {1, 1, 1, 1000000};
        double result = aCalculator.calculateAverage(numbers);
        assertEquals(250_000.0, result, 1);
        assertNotEquals(250_000.0, result);
    }
}
