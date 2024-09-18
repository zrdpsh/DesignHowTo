package GradeCalculator;

import AverageCalculator.AverageCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class GradeCalculatorTest {

    @Test
    public void testNullGradesList() {
        GradeCalculator calculator = new GradeCalculator();
        List<Integer> grades = null;
        double result = calculator.calculateAverage(grades);
        assertEquals(0.0, result, 0.01);
    }

    @Test
    public void testEmptyGradesList() {
        GradeCalculator calculator = new GradeCalculator();
        List<Integer> grades = List.of();
        double result = calculator.calculateAverage(grades);
        assertEquals(0.0, result, 0.01);
    }

    @Test
    public void testAllNullGradesList() {
        GradeCalculator calculator = new GradeCalculator();
        List<Integer> grades = Arrays.asList(null, null, null);
        double result = calculator.calculateAverage(grades);
        assertEquals(0.0, result, 0.01);
    }

    @Test
    public void testMixedNullAndNonNullGrades() {
        GradeCalculator calculator = new GradeCalculator();
        List<Integer> grades = Arrays.asList(90, null, 80, null, 70);
        double result = calculator.calculateAverage(grades);
        assertEquals(80.0, result, 0.01);
    }

    @Test
    public void testSingleGrade() {
        GradeCalculator calculator = new GradeCalculator();
        List<Integer> grades = List.of(85);
        double result = calculator.calculateAverage(grades);
        assertEquals(85.0, result, 0.01);
    }
}
