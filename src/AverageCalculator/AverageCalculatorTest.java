package AverageCalculator;

import BankAccount.BankAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class AverageCalculatorTest {

    private AverageCalculator aCalculator;

    @BeforeEach
    public void setUp() {
        aCalculator = new AverageCalculator();
    }

    @Test
    public void testAverageOfPositiveNumbers() {
        int[] numbers = {2, 4, 6, 8, 10};
        double result = aCalculator.calculateAverage(numbers);
        assertEquals(6.0, result, 0.001);
    }

    @Test
    public void testAverageOfNegativeNumbers() {
        int[] numbers = {-2, -4, -6, -8, -10};
        double result = aCalculator.calculateAverage(numbers);
        assertEquals(-6.0, result, 0.001);
    }

    @Test
    public void testAverageOfMixedNumbers() {
        int[] numbers = {-2, 4, -6, 8, -10};
        double result = aCalculator.calculateAverage(numbers);
        assertEquals(-1.2, result, 0.001);
    }

    @Test
    public void testAverageOfSingleElement() {
        int[] numbers = {5};
        double result = aCalculator.calculateAverage(numbers);
        assertEquals(5.0, result, 0.001);
    }

    @Test
    public void testAverageOfEmptyArray() {
        int[] numbers = {};
        double result = aCalculator.calculateAverage(numbers);
        assertTrue(Double.isNaN(result));
    }

    @Test
    public void testAverageOfZeros() {
        int[] numbers = {0, 0, 0, 0};
        double result = aCalculator.calculateAverage(numbers);
        assertEquals(0.0, result, 0.001);
    }

    @Test
    public void testMaximumPossibleSizes() {
        int[] numbers = {Integer.MAX_VALUE};
        Arrays.fill(numbers, Integer.MAX_VALUE);
        double result = aCalculator.calculateAverage(numbers);
        assertEquals(Integer.MAX_VALUE, result, 0.001);
    }

    @Test
    public void testZerosAndMaxValuesMixed() {
        int[] numbers = {Integer.MAX_VALUE, Integer.MAX_VALUE, 0, 0};
        double result = aCalculator.calculateAverage(numbers);
        assertEquals(((double)Integer.MAX_VALUE * 2) / 4, result, 0.001);
    }

}
