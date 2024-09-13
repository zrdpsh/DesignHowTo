package AverageCalculator;

public class Main {
    public static void main(String[] args) {

        System.out.println("Without tests:");

        System.out.println("1. Method will divide by zero without any complain");
        int[] aEmptyArray = new int[0];
        AverageCalculator aCalculator = new AverageCalculator();
        for (int i = 0; i < aEmptyArray.length; i++) System.out.println(aEmptyArray[i]);
        System.out.println(aCalculator.calculateAverage(aEmptyArray));
    }
}
