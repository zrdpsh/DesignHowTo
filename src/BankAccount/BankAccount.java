package BankAccount;

public class BankAccount {

    private double balance = 0;

    public BankAccount(double initialBalance) {
        this.balance = initialBalance;
    }

    public void deposit(double plusMoney) {
        balance += plusMoney;
    }

    public void withdraw(double minusMoney) {
        balance -= minusMoney;
    }

    public double getBalance() {
        return this.balance;
    }
}
