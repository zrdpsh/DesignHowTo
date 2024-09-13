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
    private int RANDOM_BOUND = 1000;

    @BeforeEach
    public void setUp() {
        aCalculator = new AverageCalculator();
    }

    @Test
    public void OneNumberTest() {
        int[] anArray = new int[] {1};

        double testResult = aCalculator.calculateAverage(anArray);
        assertEquals(testResult, getReferenceResult(anArray));
    }


    @Test
    public void testOneRandomArray() {
        randomNumbersTest(1);
    }

    @Test
    public void testRandomArraysRandomTimes() {
        Random randomNumbers = new Random();
        int numberOfTests = Math.abs(randomNumbers.nextInt());
        randomNumbersTest(numberOfTests);
        System.out.println("Functions had been tested " + numberOfTests + " times");
    }


    private void testWithRandomArray() {
        Random randomNumbers = new Random();
        int aLength = randomNumbers.nextInt(RANDOM_BOUND);

        int[] aRandomArray = new int[aLength];

        for (int i = 0; i < aRandomArray.length; i++) {
            aRandomArray[i] = randomNumbers.nextInt() * RANDOM_BOUND;
        }

        System.out.println("The function of " + aLength + " random values");


        double testResult = aCalculator.calculateAverage(aRandomArray);

        assertEquals(testResult, getReferenceResult(aRandomArray));
    }

    private void randomNumbersTest(int howManyTimes) {
        for (int i = 0; i < howManyTimes; i++) {
            System.out.println("Test No. " + (i+1) + " started");
            testWithRandomArray();
        }
    }


    private static double getReferenceResult(int[] anArray) {
        List<Integer> aList  = Arrays
                .stream(anArray)
                .boxed()
                .collect(Collectors.toList());

        IntSummaryStatistics iss = aList
                .stream()
                .mapToInt((a) -> a)
                .summaryStatistics();

        return iss.getAverage();
    }
}
