package BankAccount;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class BankAccountTest {

    private static final double SMALLER = 10;
    private static final double BIGGER = 10000;
    private BankAccount simpleBankAccount;
    private BankAccount smallerAccount;
    private BankAccount biggerAccount;


    @BeforeEach
    public void setUp() {
        smallerAccount = new BankAccount(SMALLER);
        biggerAccount = new BankAccount(BIGGER);
    }

    @Test
    public void testRandom() {
        Random classRandom = new Random();
        double numberRandom = classRandom.nextDouble() * 10000;
        assertDoesNotThrow(() -> new BankAccount(numberRandom));
    }

    @Test
    public void testMakeSmallerDeposit() {
        assertDoesNotThrow(() -> new BankAccount(SMALLER));
    }

    @Test
    public void testGettingSmallerDeposit() {
        assertDoesNotThrow( () -> smallerAccount.getBalance());
        assertEquals(smallerAccount.getBalance(), SMALLER);
    }

    @Test
    public void testPutIntoSmallerDeposit() {
        assertDoesNotThrow( () -> smallerAccount.deposit(10));
        assertEquals(smallerAccount.getBalance(), 20);
    }

    @Test
    public void testTakeFromSmallerDeposit() {
        assertDoesNotThrow( () -> smallerAccount.withdraw(10));
        assertEquals(smallerAccount.getBalance(), 0);
    }

    @Test
    public void testMakeBiggerDeposit() {
        assertDoesNotThrow(() -> new BankAccount(BIGGER));
    }
    
    @Test
    public void testGettingBiggerDeposit() {
        assertDoesNotThrow( () -> biggerAccount.getBalance());
        assertEquals(biggerAccount.getBalance(), BIGGER);
    }

    @Test
    public void testPutIntoBiggerDeposit() {
        assertDoesNotThrow( () -> biggerAccount.deposit(10));
        assertEquals(biggerAccount.getBalance(), 10010);
    }

    @Test
    public void testTakeFromBiggerDeposit() {
        assertDoesNotThrow( () -> biggerAccount.withdraw(10));
        assertEquals(biggerAccount.getBalance(), 9990);
    }
}