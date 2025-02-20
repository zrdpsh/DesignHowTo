package GradeCalculator;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class GradeCalculatorTest {

   
    @Test
    public void testNullGrades() {
        GradeCalculator calculator = new GradeCalculator();
        List<Integer> grades = null;
        double result = calculator.calculateAverage(grades);
        assertEquals(0.0, result, 0.01);
    }

    @Test
    public void testEmptyGrades() {
        GradeCalculator calculator = new GradeCalculator();
        List<Integer> grades = List.of();
        double result = calculator.calculateAverage(grades);
        assertEquals(0.0, result, 0.01);
    }

    @Test
    public void testAllNullGrades() {
        GradeCalculator calculator = new GradeCalculator();
        List<Integer> grades = Arrays.asList(null, null, null);
        double result = calculator.calculateAverage(grades);
        assertEquals(0.0, result, 0.01);
    }

   
    @Test
    public void testMixedNullAndNonNull() {
        GradeCalculator calculator = new GradeCalculator();
        List<Integer> grades = Arrays.asList(190, null, 180, null, 170);
        double result = calculator.calculateAverage(grades);
        assertEquals(180.0, result, 0.01);
    }

    @Test
    public void testSingleGrade() {
        GradeCalculator calculator = new GradeCalculator();
        List<Integer> grades = List.of(85);
        double result = calculator.calculateAverage(grades);
        assertEquals(85.0, result, 0.01);
    }
   
    @Test
    public void testMultipleGrades() {
        
    }
}
}
