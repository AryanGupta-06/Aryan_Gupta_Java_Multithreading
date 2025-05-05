package Java_Multithreading_Worksheet_1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class BankAccount {
    private double balance;
    private final Lock lock = new ReentrantLock();
    private final List<String> transactions = new ArrayList<>();

    public BankAccount(double initialBalance) {
        this.balance = initialBalance;
    }

    public void deposit(double amount) {
        lock.lock();
        try {
            balance += amount;
            transactions.add("Deposited " + amount);
        } finally {
            lock.unlock();
        }
    }

    public void withdraw(double amount) {
        lock.lock();
        try {
            if (balance >= amount) {
                balance -= amount;
                transactions.add("Withdrew " + amount);
            } else {
                transactions.add("Failed to withdraw " + amount + " due to insufficient funds");
            }
        } finally {
            lock.unlock();
        }
    }

    public double getBalance() {
        lock.lock();
        try {
            return balance;
        } finally {
            lock.unlock();
        }
    }

    public List<String> getTransactions() {
        lock.lock();
        try {
            return new ArrayList<>(transactions);
        } finally {
            lock.unlock();
        }
    }
}

class TransactionTask implements Runnable {
    private final BankAccount account;
    private final List<Transaction> transactions;

    public TransactionTask(BankAccount account, List<Transaction> transactions) {
        this.account = account;
        this.transactions = transactions;
    }

    @Override
    public void run() {
        for (Transaction transaction : transactions) {
            if (transaction.getType() == TransactionType.DEPOSIT) {
                account.deposit(transaction.getAmount());
            } else if (transaction.getType() == TransactionType.WITHDRAW) {
                account.withdraw(transaction.getAmount());
            }
        }
    }
}

enum TransactionType {
    DEPOSIT, WITHDRAW
}

class Transaction {
    private final TransactionType type;
    private final double amount;

    public Transaction(TransactionType type, double amount) {
        this.type = type;
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }
}

public class Q2_Bank_Account_Synchronization {
    public static void main(String[] args) {
        BankAccount account = new BankAccount(1000);

        List<Transaction> transactions1 = List.of(
                new Transaction(TransactionType.DEPOSIT, 200),
                new Transaction(TransactionType.WITHDRAW, 100)
        );

        List<Transaction> transactions2 = List.of(
                new Transaction(TransactionType.WITHDRAW, 300),
                new Transaction(TransactionType.DEPOSIT, 400)
        );

        List<Transaction> transactions3 = List.of(
                new Transaction(TransactionType.DEPOSIT, 500),
                new Transaction(TransactionType.WITHDRAW, 600)
        );

        Thread thread1 = new Thread(new TransactionTask(account, transactions1));
        Thread thread2 = new Thread(new TransactionTask(account, transactions2));
        Thread thread3 = new Thread(new TransactionTask(account, transactions3));

        thread1.start();
        thread2.start();
        thread3.start();

        try {
            thread1.join();
            thread2.join();
            thread3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Final balance: " + account.getBalance());
        System.out.println("Transaction log:");
        for (String transaction : account.getTransactions()) {
            System.out.println(transaction);
        }
    }
}
