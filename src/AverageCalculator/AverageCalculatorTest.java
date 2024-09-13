package AverageCalculator;

import BankAccount.BankAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class AverageCalculatorTest {

    @Test
    public void OneNumberTest() {
        Random classRandom = new Random();
        double numberRandom = classRandom.nextDouble() * 10000;
        assertDoesNotThrow(() -> new BankAccount(numberRandom));

    }


    @Test
    public void randomNumbersTest() {
        Random classRandom = new Random();
        double numberRandom = classRandom.nextDouble() * 10000;
        assertDoesNotThrow(() -> new BankAccount(numberRandom));

    }
}
