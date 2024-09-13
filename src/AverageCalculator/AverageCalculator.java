package AverageCalculator;



public class AverageCalculator {
    public double calculateAverage(int[] numbers) {
        double sum = 0;
        for (int i = 0; i < numbers.length; i++) {
            sum +=  numbers[i];
        }
        return sum / numbers.length;
    }
}


