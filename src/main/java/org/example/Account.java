package org.example;

class Account {
    private double balance;

    public Account(double initialBalance) {
        this.balance = initialBalance;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        balance += amount;
        System.out.println("Deposited $" + String.format("%.2f", amount) + ". New balance: $" + String.format("%.2f", balance));
    }

    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        if (amount > balance) {
            throw new IllegalArgumentException("Insufficient funds. Available: $" + balance);
        }
        balance -= amount;
        System.out.println("Withdrew $" + String.format("%.2f", amount) + ". Remaining balance: $" + String.format("%.2f", balance));
    }
}
