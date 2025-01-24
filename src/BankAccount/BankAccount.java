package BankAccount;

public class BankAccount {

    
    private double balance = 0;

    public BankAccount(double initialBalance) {
        this.balance = initialBalance;
    }

    public void deposit(double plusMoney) {
        if (plusMoney >= 0) {
            balance += plusMoney;
        }
    }

    public void withdraw(double minusMoney) {
        if (minusMoney >= 0 && minusMoney <= this.balance) {
            balance -= minusMoney;
        }
    }

    public double getBalance() {
        return this.balance;
    }
}
